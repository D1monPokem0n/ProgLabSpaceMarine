package ru.prog.itmo.storage;

import org.apache.logging.log4j.Level;
import ru.prog.itmo.connection.InvalidUserException;
import ru.prog.itmo.connection.User;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.chapter.Chapter;
import ru.prog.itmo.spacemarine.coordinates.Coordinates;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;

import static ru.prog.itmo.control.ClientListener.LOGGER;

public class DataBase {
    private Connection connection;
    private static final String ENV_CONFIG_FILE = "DB_CFG";
    private static final String DEFAULT_CONFIG_FILE = "db.cfg";


    private static final String CREATE_USERS = """
            CREATE TABLE IF NOT EXISTS USERS(
                ID BIGSERIAL PRIMARY KEY,
                NAME VARCHAR(255) NOT NULL UNIQUE,
                PASSWORD CHAR(56) NOT NULL,
                    CHECK(PASSWORD SIMILAR TO ('[0-9A-Fa-f]{56}')),
                SALT VARCHAR(255) NOT NULL,
                REFRESH_TOKEN VARCHAR
            );
            """;

    private static final String CREATE_SPACE_MARINE = """
            CREATE TABLE IF NOT EXISTS SPACE_MARINE (
                ID BIGSERIAL PRIMARY KEY,
                NAME VARCHAR(255) NOT NULL,
                    CHECK(NAME <> ''),
                CREATION_DATE TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                HEALTH INT NOT NULL,
                    CHECK(HEALTH > 0),
                HEART_COUNT INT NOT NULL,
                    CHECK(HEART_COUNT <= 3 AND HEART_COUNT > 0),
                CATEGORY VARCHAR(10),
                    CHECK(CATEGORY SIMILAR TO ('(SCOUT|LIBRARIAN|CHAPLAIN|APOTHECARY)')),
                WEAPON VARCHAR(13) NOT NULL,
                    CHECK(WEAPON SIMILAR TO ('(MANREAPER|LIGHTING_CLAW|POWER_FIST)')),
                USER_OWNER_ID BIGINT NOT NULL REFERENCES USERS(ID),
                LAST_UPDATING TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
            );
            """;

    private static final String CREATE_CHAPTER = """
            CREATE TABLE IF NOT EXISTS CHAPTER (
                SPACE_MARINE_ID BIGINT REFERENCES SPACE_MARINE ON DELETE CASCADE,
                NAME VARCHAR(255) NOT NULL,
                    CHECK (NAME <> ''),
                PARENT_LEGION VARCHAR(255),
                MARINES_COUNT BIGINT,
                    CHECK(MARINES_COUNT > 0 AND MARINES_COUNT <= 1000),
                WORLD VARCHAR(255) NOT NULL,
                PRIMARY KEY(SPACE_MARINE_ID)
            );
            """;

    private static final String CREATE_COORDINATES = """
            CREATE TABLE IF NOT EXISTS COORDINATES (
                SPACE_MARINE_ID BIGINT REFERENCES SPACE_MARINE ON DELETE CASCADE,
                X REAL NOT NULL,
                Y DOUBLE PRECISION NOT NULL,
                    CHECK(Y <= 431),
                PRIMARY KEY(SPACE_MARINE_ID)
            );
            """;


    private static final String SELECT_ALL = """
            SELECT SPACE_MARINE.ID, SPACE_MARINE.NAME, X, Y, CREATION_DATE,
                        HEALTH, HEART_COUNT, CATEGORY, WEAPON,
                        CHAPTER.NAME, PARENT_LEGION, MARINES_COUNT, WORLD, USERS.NAME AS USER_OWNER FROM SPACE_MARINE
                        JOIN COORDINATES ON SPACE_MARINE.ID = COORDINATES.SPACE_MARINE_ID
                        LEFT JOIN CHAPTER ON CHAPTER.SPACE_MARINE_ID = SPACE_MARINE.ID
                        JOIN USERS ON SPACE_MARINE.USER_OWNER_ID = USERS.ID;
            """;

    private static final String INSERT_USER = """
            INSERT INTO USERS(NAME, PASSWORD, SALT) VALUES(?, ?, ?);
            """;

    private static final String SELECT_SALT = """
            SELECT SALT FROM USERS WHERE NAME = ?;
            """;

    private static final String SELECT_PASSWORD = """
            SELECT PASSWORD FROM USERS WHERE NAME = ?;
            """;

    private static final String INSERT_SPACE_MARINE = """
            INSERT INTO SPACE_MARINE(NAME, CREATION_DATE, HEALTH, HEART_COUNT, CATEGORY, WEAPON, USER_OWNER_ID, LAST_UPDATING)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String INSERT_CHAPTER = """
            INSERT INTO CHAPTER(SPACE_MARINE_ID, NAME, PARENT_LEGION, MARINES_COUNT, WORLD) VALUES(?, ?, ?, ?, ?);
            """;

    private static final String INSERT_COORDINATES = """
            INSERT INTO COORDINATES(SPACE_MARINE_ID, X, Y) VALUES(?, ?, ?);
            """;

    private static final String GET_USER_ID = """
            SELECT ID FROM USERS WHERE NAME = ?;
            """;


    private static final String UPDATE_SPACE_MARINE = """
            UPDATE SPACE_MARINE
            SET NAME = ?, HEALTH = ?, HEART_COUNT = ?, CATEGORY = ?, WEAPON = ?,  LAST_UPDATING = ?
            WHERE ID = ?;
            """;

    private static final String UPDATE_CHAPTER = """
            UPDATE CHAPTER
            SET NAME = ?, PARENT_LEGION = ?, MARINES_COUNT = ?, WORLD = ?
            WHERE SPACE_MARINE_ID = ?;
            """;

    private static final String UPDATE_COORDINATES = """
            UPDATE COORDINATES
            SET X = ?, Y = ?
            WHERE SPACE_MARINE_ID = ?;
            """;

    private static final String DELETE_SPACE_MARINE = """
            DELETE FROM SPACE_MARINE WHERE ID = ?;
            """;

    private static final String DELETE_CHAPTER = """
            DELETE FROM CHAPTER WHERE SPACE_MARINE_ID = ?;
            """;

    private static final String UPDATE_REFRESH_TOKEN = """
            UPDATE USERS
            SET REFRESH_TOKEN = ?
            WHERE NAME = ?
            """;

    private static final String SELECT_REFRESH_TOKEN = """
            SELECT REFRESH_TOKEN FROM USERS
            WHERE NAME = ?
            """;

    private static final String CLEAR_TOKENS = """
            UPDATE USERS SET REFRESH_TOKEN = NULL;
            """;

    private static final String CLEAR_TOKEN_BY_ID = """
            UPDATE USERS SET REFRESH_TOKEN = NULL WHERE ID = ?;
            """;


    public DataBase() {
        try {
            Class.forName("java.sql.Driver");
            initConnection();
            createStructure();
            LOGGER.log(Level.INFO, "Created DB STRUCTURE");
        } catch (IOException e) {
            throw new StorageDBException("Не удалось загрузить данный из конфигурационного файла базы данных");
        } catch (ClassNotFoundException e) {
            throw new StorageDBException("Не удалось загрузить драйвер для СУБД.");
        } catch (SQLException e) {
            throw new StorageDBException("Не удалось подкючиться к СУБД. \"" + e.getMessage() + "\"");
        }
    }

    private void initConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(configFile());
        connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("name"),
                properties.getProperty("password")
        );
    }

    private void createStructure() throws SQLException {
        var statement = connection.createStatement();
        statement.executeUpdate(CREATE_USERS);
        statement.executeUpdate(CREATE_SPACE_MARINE);
        statement.executeUpdate(CREATE_CHAPTER);
        statement.executeUpdate(CREATE_COORDINATES);
    }

    private FileInputStream configFile() throws FileNotFoundException {
        String fileName = System.getenv(ENV_CONFIG_FILE);
        if (fileName != null)
            return new FileInputStream(fileName);
        if (Files.exists(Path.of(DEFAULT_CONFIG_FILE)))
            return new FileInputStream(DEFAULT_CONFIG_FILE);
        throw new StorageDBException(
                """
                        У вас не установлена переменная окружения "DB_CFG".
                        Она должна указывать на конфигурационный файл для подключения к базе данных.
                        Ожидаемое содержимое файла:
                                        
                        url = 'DB_URL'
                        user = 'USER_NAME'
                        password = 'USER_PASSWORD'
                        """
        );
    }

    public HashSet<SpaceMarine> getHashSet() {
        var hashSet = new HashSet<SpaceMarine>();
        try (var statement = connection.createStatement();
             var set = statement.executeQuery(SELECT_ALL)) {
            var creator = new DBSpaceMarineCreator(set);
            while (creator.nextRow())
                hashSet.add(creator.create());
        } catch (SQLException | InvalidSpaceMarineValueException e) {
            throw new StorageDBException("Не удалость считать данные из СУБД");
        }
        return hashSet;
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized void startMassiveDeleting() {
        autoCommitOff();
    }

    public synchronized void finishMassiveDeleting() {
        commit();
        autoCommitOn();
    }

    public synchronized void cancelMassiveDeleting() {
        rollback();
        autoCommitOn();
    }

    private void autoCommitOn() {
        try {
            connection.setAutoCommit(true);
            LOGGER.log(Level.INFO, "auto commit on");
        } catch (SQLException e) {
            throw new StorageDBFatalError("Не удалось включить автоматический commit");
        }
    }

    private void autoCommitOff() {
        try {
            connection.setAutoCommit(false);
            LOGGER.log(Level.INFO, "auto commit off");
        } catch (SQLException e) {
            throw new StorageDBFatalError("Не удалось выключить автоматический commit");
        }
    }

    private void commit() {
        try {
            connection.commit();
            LOGGER.log(Level.INFO, "commit");
        } catch (SQLException e) {
            throw new StorageDBFatalError("Не удалось зафиксировать изменения");
        }
    }

    private void rollback() {
        try {
            connection.rollback();
            LOGGER.log(Level.INFO, "rollback");
        } catch (SQLException e) {
            throw new StorageDBFatalError("Не удалось откатиться к прошлому состоянию");
        }
    }


    public void remove(SpaceMarine marine) {
        try {
            executeSpaceMarineDelete(marine);
        } catch (SQLException e) {
            throw new StorageDBException("Не удалось удалить десантника.");
        }
    }

    private void executeSpaceMarineDelete(SpaceMarine marine) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SPACE_MARINE)) {
            statement.setLong(1, marine.getId());
            statement.executeUpdate();
        }
    }

    public synchronized void update(SpaceMarine oldMarine, SpaceMarine updatedMarine) {
        try {
            autoCommitOff();
            executeSpaceMarineUpdate(oldMarine, updatedMarine);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new StorageDBException("Не удалось обновить космодесантника.");
        } finally {
            autoCommitOn();
        }
    }

    private void executeSpaceMarineUpdate(SpaceMarine oldMarine, SpaceMarine updatedMarine) throws SQLException {
        Chapter oldChapter = oldMarine.getChapter();
        Chapter updatedChapter = updatedMarine.getChapter();
        if (!Objects.isNull(oldChapter) && !Objects.isNull(updatedChapter))
            executeUpdateWithChapterUpdate(oldMarine, updatedMarine);
        if (Objects.isNull(oldChapter) && !Objects.isNull(updatedChapter))
            executeUpdateWithChapterCreate(oldMarine, updatedMarine);
        if (!Objects.isNull(oldChapter) && Objects.isNull(updatedChapter))
            executeUpdateWithChapterDelete(oldMarine, updatedMarine);
        if (Objects.isNull(oldChapter) && Objects.isNull(updatedChapter))
            executeUpdateWithoutChapter(oldMarine, updatedMarine);
    }

    private void executeUpdateWithoutChapter(SpaceMarine oldMarine, SpaceMarine updatedMarine) throws SQLException {
        long marineId = oldMarine.getId();
        updateMarine(marineId, updatedMarine);
        updateCoordinates(marineId, updatedMarine.getCoordinates());
    }

    private void executeUpdateWithChapterDelete(SpaceMarine oldMarine, SpaceMarine updatedMarine) throws SQLException {
        long marineId = oldMarine.getId();
        updateMarine(marineId, updatedMarine);
        deleteChapter(marineId);
        updateCoordinates(marineId, updatedMarine.getCoordinates());
    }

    private void deleteChapter(long marineId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CHAPTER)) {
            statement.setLong(1, marineId);
            statement.executeUpdate();
        }
    }

    private void executeUpdateWithChapterCreate(SpaceMarine oldMarine, SpaceMarine updatedMarine) throws SQLException {
        long marineId = oldMarine.getId();
        updateMarine(marineId, updatedMarine);
        insertChapter(marineId, updatedMarine.getChapter());
        updateCoordinates(marineId, updatedMarine.getCoordinates());
    }

    private void executeUpdateWithChapterUpdate(SpaceMarine oldMarine, SpaceMarine updatedMarine) throws SQLException {
        long marineId = oldMarine.getId();
        updateMarine(marineId, updatedMarine);
        updateChapter(marineId, updatedMarine.getChapter());
        updateCoordinates(marineId, updatedMarine.getCoordinates());
    }

    //SET X = ?, Y = ?
    //WHERE SPACE_MARINE_ID = ?;
    private void updateCoordinates(long marineId, Coordinates coordinates) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_COORDINATES)) {
            statement.setFloat(1, coordinates.getX());
            statement.setDouble(2, coordinates.getY());
            statement.setLong(3, marineId);
            statement.executeUpdate();
        }
    }


    private void updateMarine(long id, SpaceMarine marine) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SPACE_MARINE)) {
            prepareMarineUpdate(statement, id, marine);
            statement.executeUpdate();
        }
    }


    // SET NAME = ?, HEALTH = ?, HEART_COUNT = ?, CATEGORY = ?, WEAPON = ?, LAST_UPDATING = ?
    //            WHERE ID = ?;


    private void prepareMarineUpdate(PreparedStatement statement, long id, SpaceMarine marine) throws SQLException {
        statement.setString(1, marine.getName());
        statement.setInt(2, marine.getHealth());
        statement.setLong(3, marine.getHeartCount());
        statement.setString(4, marine.getCategory().getName());
        statement.setString(5, marine.getMeleeWeapon().getName());
        statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        statement.setLong(7, id);
    }

    private void updateChapter(long id, Chapter chapter) throws SQLException {
        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_CHAPTER)) {
            prepareChapterUpdateStatement(updateStatement, id, chapter);
            updateStatement.executeUpdate();
        }
    }

    private void prepareChapterUpdateStatement(PreparedStatement statement, long id, Chapter chapter) throws SQLException {
        statement.setString(1, chapter.getName());
        statement.setString(2, chapter.getParentLegion());
        statement.setLong(3, chapter.getMarinesCount());
        statement.setString(4, chapter.getWorld());
        statement.setLong(5, id);
    }


    public synchronized void add(SpaceMarine marine) {
        try {
            autoCommitOff();
            insertNewSpaceMarine(marine);
            commit();
        } catch (SQLException e) {
            rollback();
            LOGGER.log(Level.WARN, e);
            throw new StorageDBException("Не удалось добавить космодесантника.");
        } finally {
            autoCommitOn();
        }
    }

    private void insertNewSpaceMarine(SpaceMarine marine) throws SQLException {
        long marineId = insertMarineAndGetKey(marine);
        if (marine.getChapter() != null) insertChapter(marineId, marine.getChapter());
        insertCoordinates(marineId, marine);
        LOGGER.log(Level.INFO, "INSERT NEW SPACE_MARINE");
    }

    private void insertCoordinates(long marineId, SpaceMarine marine) {
        Coordinates coordinates = marine.getCoordinates();
        try (PreparedStatement coordinatesStatement
                     = connection.prepareStatement(INSERT_COORDINATES)) {
            prepareCoordinatesStatement(marineId, coordinates, coordinatesStatement);
            coordinatesStatement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageDBException("Не удалось добавить десантника.");
        }
    }

    private void prepareCoordinatesStatement(long marineId,
                                             Coordinates coordinates,
                                             PreparedStatement coordinatesStatement) throws SQLException {
        float x = coordinates.getX();
        double y = coordinates.getY();
        coordinatesStatement.setLong(1, marineId);
        coordinatesStatement.setFloat(2, x);
        coordinatesStatement.setDouble(3, y);
    }

    private long insertMarineAndGetKey(SpaceMarine marine) throws SQLException {
        try (PreparedStatement marineStatement =
                     connection.prepareStatement(INSERT_SPACE_MARINE, Statement.RETURN_GENERATED_KEYS)) {
            long ownerId = getUserIdByLogin(marine.getOwnerUser());
            prepareSpaceMarineInsertStatement(marine, ownerId, marineStatement);
            if (marineStatement.executeUpdate() > 0) {
                var generatedKeys = marineStatement.getGeneratedKeys();
                generatedKeys.next();
                var marineId = generatedKeys.getLong("ID");
                marine.setId(marineId);
                return marineId;
            } else throw new StorageDBException("Не удалось добавить десантника в коллекцию.");
        }
    }


    private void insertChapter(long marineId, Chapter chapter) throws SQLException {
        try (PreparedStatement chapterStatement =
                     connection.prepareStatement(INSERT_CHAPTER)) {
            prepareChapterInsert(marineId, chapter, chapterStatement);
            chapterStatement.executeUpdate();
        }
    }


    public long getUserIdByLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_ID)) {
            statement.setString(1, login);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong("ID");
                } else {
                    throw new InvalidUserException("Не существует пользователя с таким именем.");
                }
            }
        } catch (SQLException e){
            throw new StorageDBException("Не удалось найти пользователя");
        }
    }

    private void prepareSpaceMarineInsertStatement(SpaceMarine marine,
                                                   long userId,
                                                   PreparedStatement marineStatement) throws SQLException {
        String category = marine.getCategory() == null ? null : marine.getCategory().getName();
        marineStatement.setString(1, marine.getName());
        marineStatement.setTimestamp(2, Timestamp.valueOf(marine.getCreationDate()));
        marineStatement.setInt(3, marine.getHealth());
        marineStatement.setLong(4, marine.getHeartCount());
        marineStatement.setObject(5, category, Types.OTHER);
        marineStatement.setObject(6, marine.getMeleeWeapon().getName(), Types.OTHER);
        marineStatement.setLong(7, userId);
        marineStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
    }

    private void prepareChapterInsert(long marineId, Chapter chapter, PreparedStatement statement) throws SQLException {
        statement.setLong(1, marineId);
        statement.setString(2, chapter.getName());
        statement.setString(3, chapter.getParentLegion());
        statement.setLong(4, chapter.getMarinesCount());
        statement.setString(5, chapter.getWorld());
    }

    public boolean checkUserPassword(User user) {
        try {
            String salt = getSaltByUserLogin(user.getLogin());
            String hashedInputPassword = Storage.getHashedPassword(user, salt);
            String actualPassword = getActualPasswordByLogin(user.getLogin());
            return actualPassword.equals(hashedInputPassword);
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new StorageDBException("Не удалось достать пользователя из СУБД.");
        }
    }

    private String getActualPasswordByLogin(String login) throws SQLException {
        try (PreparedStatement passwordStatement = connection.prepareStatement(SELECT_PASSWORD)) {
            passwordStatement.setString(1, login);
            try (ResultSet passwordSet = passwordStatement.executeQuery()) {
                if (passwordSet.next())
                    return passwordSet.getString("PASSWORD");
                else throw new StorageDBException("Данного пользователя не существует");
            }
        }
    }

    private String getSaltByUserLogin(String login) throws SQLException {
        try (PreparedStatement saltStatement = connection.prepareStatement(SELECT_SALT)) {
            saltStatement.setString(1, login);
            try (ResultSet saltSet = saltStatement.executeQuery()) {
                if (saltSet.next())
                    return saltSet.getString("SALT");
                else
                    throw new StorageDBException("Данного пользователя не существует");
            }
        }
    }

    public void addNewUser(String login, String hash, String salt) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            statement.setString(1, login);
            statement.setString(2, hash);
            statement.setString(3, salt);
            int changedStrings = statement.executeUpdate();
            if (changedStrings == 0)
                throw new StorageDBException("Не удалось добавить пользователя.");
        } catch (SQLException e) {
            throw new StorageDBException("Пользователь с таким именем уже существует.");
        }
    }

    public void updateRefreshToken(String login, String token) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_REFRESH_TOKEN)) {
            statement.setString(1, token);
            statement.setString(2, login);
            int updatedRows = statement.executeUpdate();
            LOGGER.log(Level.INFO, "Выполнен запрос на обновление refresh-token пользователя: "
                                   + login + ". Количество обновлённых строк в БД: " + updatedRows );
        } catch (SQLException e) {
            throw new StorageDBException("Некорректный запрос");
        }
    }

    public String getRefreshToken(String login) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_REFRESH_TOKEN)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultSet.getString(1);
            else throw new StorageDBException("Нет токена");
        } catch (SQLException e) {
            throw new StorageDBException(e.getMessage());
        }
    }

    public synchronized void save() {
        autoCommitOff();
        commit();
        autoCommitOn();
    }

    public void clearAllTokens() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CLEAR_TOKENS)){
            statement.executeUpdate();
        }
    }

    public void clearToken(String login) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CLEAR_TOKEN_BY_ID)) {
            var id = getUserIdByLogin(login);
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
