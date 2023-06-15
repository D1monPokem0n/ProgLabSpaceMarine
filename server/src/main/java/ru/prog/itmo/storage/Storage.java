package ru.prog.itmo.storage;

import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.connection.InvalidUserException;
import ru.prog.itmo.connection.User;
import ru.prog.itmo.spacemarine.SpaceMarine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class Storage {
    private final HashSet<SpaceMarine> hashSet;
    private final StorageInfo info;
    private final DataBase dataBase;

    public Storage() {
        dataBase = new DataBase();
        hashSet = dataBase.getHashSet();
        info = new StorageInfo(hashSet);
    }

    public boolean checkUser(User user) {
        try {
            dataBase.getUserIdByLogin(user.getLogin());
            return dataBase.checkUserPassword(user);
        } catch (StorageDBException e) {
            throw new InvalidUserException(e.getMessage());
        }
    }

    public void addNewUser(User user, String salt) {
        try {
            String hashedPassword = getHashedPassword(user, salt);
            dataBase.addNewUser(user.getLogin(), hashedPassword, salt);
        } catch (StorageDBException | NoSuchAlgorithmException e) {
            throw new InvalidUserException(e.getMessage());
        }
    }

    public static String getHashedPassword(User user, String salt) throws NoSuchAlgorithmException {
        var messageDigest = MessageDigest.getInstance("SHA-224");
        byte[] hash = messageDigest.digest((user.getToken() + salt).getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : hash)
            builder.append(String.format("%02x", b));
        return builder.toString();
    }

    public void updateRefreshToken(String login, String token) {
        dataBase.updateRefreshToken(login, token);
    }

    public String getRefreshToken(String login) {
        return dataBase.getRefreshToken(login);
    }

    public HashSet<SpaceMarine> getHashSet() {
        return hashSet;
    }

    public synchronized void add(SpaceMarine spaceMarine) {
        dataBase.add(spaceMarine);
        hashSet.add(spaceMarine);
        info.incrementElementsCount();
    }

    public boolean contains(SpaceMarine marine) {
        return hashSet.contains(marine);
    }

    public StorageInfo getInfo() {
        return info;
    }

    public Stream<SpaceMarine> getStream() {
        return hashSet.stream();
    }

    public synchronized SpaceMarine[] sort() {
        SpaceMarine[] array = hashSet.toArray(new SpaceMarine[0]);
        Arrays.sort(array);
        return array;
    }

    public synchronized int removeAll(Collection<SpaceMarine> marinesToDelete, String userName) {
        dataBase.startMassiveDeleting();
        List<SpaceMarine> deletedMarines = new ArrayList<>();
        removeAllFromDB(marinesToDelete, userName, deletedMarines);
        int deletedCount = deletedMarines.size();
        deletedMarines.forEach(hashSet::remove);
        info.reduceElementsCount(deletedCount);
        return deletedCount;
    }

    private void removeAllFromDB(Collection<SpaceMarine> marinesToDelete,
                                 String userName,
                                 List<SpaceMarine> deletedMarines) {
        try {
            removeAllUsersMarines(marinesToDelete, userName, deletedMarines);
        } catch (StorageDBException e) {
            dataBase.cancelMassiveDeleting();
            throw e;
        }
        dataBase.finishMassiveDeleting();
    }

    private void removeAllUsersMarines(Collection<SpaceMarine> marinesToDelete,
                                       String userName,
                                       List<SpaceMarine> deletedMarines) {
        for (SpaceMarine marine : marinesToDelete) {
            if (userName.equals(marine.getOwnerUser())) {
                dataBase.remove(marine);
                deletedMarines.add(marine);
            }
        }
    }

    public synchronized void clear(String userName) {
        List<SpaceMarine> deletedMarines = new ArrayList<>();
        clearFromDB(userName, deletedMarines);
        deletedMarines.forEach(hashSet::remove);
        info.reduceElementsCount(deletedMarines.size());
    }

    private void clearFromDB(String userName, List<SpaceMarine> deletedMarines) {
        dataBase.startMassiveDeleting();
        try {
            clearUsersMarines(userName, deletedMarines);
            dataBase.finishMassiveDeleting();
        } catch (StorageDBException e) {
            dataBase.cancelMassiveDeleting();
            throw e;
        }
    }

    private  void clearUsersMarines(String userName, List<SpaceMarine> deletedMarines) {
        for (SpaceMarine marine : hashSet) {
            if (userName.equals(marine.getOwnerUser())) {
                dataBase.remove(marine);
                deletedMarines.add(marine);
            }
        }
    }


    public synchronized void updateMarine(SpaceMarine oldMarine, SpaceMarine updatedMarine) {
        dataBase.update(oldMarine, updatedMarine);
        hashSet.remove(oldMarine);
        hashSet.add(updatedMarine);
    }

    public synchronized void remove(SpaceMarine marine) {
        dataBase.remove(marine);
        boolean haveDeleted = hashSet.remove(marine);
        if (haveDeleted) info.reduceElementsCount(1);
    }

    public SpaceMarine getById(long id) {
        SpaceMarine searchableMarine = null;
        for (SpaceMarine marine : hashSet) {
            if (marine.getId() == id) {
                searchableMarine = marine;
                break;
            }
        }
        return searchableMarine;
    }

    public void save() {
        try {
            dataBase.save();
        } catch (StorageDBFatalError e) {
            throw new WrongDataBaseException(e.getMessage());
        }
    }

    public void clearAllTokens() {
        try {
            dataBase.clearAllTokens();
        } catch (SQLException e) {
            throw new StorageDBException(e.getMessage());
        }
    }

    public void clearToken(String login) {
        try {
            dataBase.clearToken(login);
        } catch (SQLException | InvalidUserException e) {
            throw new StorageDBException(e.getMessage());
        }
    }

}
