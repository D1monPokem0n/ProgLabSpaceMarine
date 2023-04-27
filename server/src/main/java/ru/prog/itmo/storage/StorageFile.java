package ru.prog.itmo.storage;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import ru.prog.itmo.StorageInfo;
import ru.prog.itmo.spacemarine.CreateCancelledException;
import ru.prog.itmo.spacemarine.InvalidSpaceMarineValueException;
import ru.prog.itmo.spacemarine.SpaceMarine;
import ru.prog.itmo.spacemarine.builder.file.SpaceMarineFileCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class StorageFile {
    private Path path;
    private boolean isFileExistBefore;

    public StorageFile() {
        String csvfile = System.getenv("LAB");
        csvfile = csvfile == null ? "" : csvfile;
        try {
            if (csvfile.equals("")) {
                path = Paths.get("SpaceMarine.csv");
                if (Files.exists(path)) {
                    isFileExistBefore = true;
                } else {
                    Files.createFile(path);
                    isFileExistBefore = false;
                }
            } else {
                path = Paths.get(csvfile);
                if (!Files.exists(path)) throw new WrongStorageFileException("Данного файла не существует");
                isFileExistBefore = true;
            }
        } catch (IOException e) {
            throw new WrongStorageFileException("Проблемы при считывании из файла");
        } catch (InvalidPathException e){
            throw new WrongStorageFileException("Указан некорректный путь к файлу");
        }
    }

    public StorageInfo getStorageInfo() throws WrongStorageFileException {
        try {
            FileInputStream inputStream = new FileInputStream(path.toFile());
            InputStreamReader reader = new InputStreamReader(inputStream);
            Scanner scanner = new Scanner(reader);
            if (!scanner.hasNextLine())
                return new StorageInfo();
            String[] values = scanner.nextLine().split(",");
            if (values.length != StorageInfo.getFieldsCount())
                throw new WrongStorageFileException("Некорректное число значений об информации в файле");
            int storageElementsCount = Integer.parseInt(values[0]);
            LocalDateTime storageCreationDate = LocalDateTime.parse(values[1]);
            String storageCollectionType = values[2];
            String storageFileType = values[3];
            String[] valuesByDot = path.toString().split("\\.");
            String fileExtension = valuesByDot[valuesByDot.length - 1];
            if (!storageFileType.equals(fileExtension))
                throw new WrongStorageFileException("Неверное разрешение файла в информации о коллекции.");
            scanner.close();
            reader.close();
            inputStream.close();
            return new StorageInfo(storageElementsCount, storageCreationDate, storageCollectionType, storageFileType);
        } catch (IOException e) {
            throw new WrongStorageFileException("Проблемы, при считывании из файла.");
        } catch (DateTimeParseException e) {
            throw new WrongStorageFileException("Неверный формат даты в информации о коллекции.");
        } catch (NumberFormatException e) {
            throw new WrongStorageFileException("Неверный формат количества элементов в информации о коллекции.");
        }
    }

    public List<SpaceMarine> getMarines() {
        try {
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            Scanner scanner = new Scanner(reader);
            if (!scanner.hasNextLine())
                return new ArrayList<>();
            scanner.nextLine();
            List<SpaceMarine> marines = new ArrayList<>();
            Set<Long> idSet = new HashSet<>();
            SpaceMarineFileCreator creator = new SpaceMarineFileCreator(scanner);
            while (scanner.hasNextLine()){
                creator.setNextValues();
                SpaceMarine marine = creator.create();
                if (!idSet.add(marine.getId()))
                    throw new InvalidSpaceMarineValueException("Замечено повторяющееся id: " + marine.getId());
                marines.add(marine);
            }
            return marines;
        } catch (IOException e) {
            throw new WrongStorageFileException("Беды с файлом у вас.");
        } catch (NullPointerException | NumberFormatException e) {
            throw new WrongStorageFileException("Некорректные данные по космодесантникам в файле.");
        } catch (InvalidSpaceMarineValueException | CreateCancelledException e){
            throw new WrongStorageFileException(e.getMessage());
        }
    }

    public void save(Storage storage) throws NotWritableFileException {
        if (!Files.isWritable(path)) throw new NotWritableFileException("Невозможно записать в файл.");
        String[] storageInfo = {
                String.valueOf(storage.getInfo().getElementsCount()),
                String.valueOf(storage.getInfo().getCreationDate()),
                storage.getInfo().getCollectionType(),
                storage.getInfo().getFileType()
        };
        try {
            FileOutputStream out = new FileOutputStream(path.toFile());
            OutputStreamWriter writer = new OutputStreamWriter(out);
            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .build();
            csvWriter.writeNext(storageInfo);
            for (SpaceMarine marine : storage.getHashSet()) {
                ArrayList<String> chapterValues = marine.getChapter() == null ?
                        new ArrayList<>(List.of("null")) :
                        new ArrayList<>(List.of(
                                marine.getChapter().getName(),
                                marine.getChapter().getParentLegion(),
                                String.valueOf(marine.getChapter().getMarinesCount()),
                                marine.getChapter().getWorld()
                        ));
                List<String> marineValues = new ArrayList<>(List.of(
                        String.valueOf(marine.getId()),
                        marine.getName(),
                        String.valueOf(marine.getCoordinates().getX()),
                        String.valueOf(marine.getCoordinates().getY()),
                        String.valueOf(marine.getCreationDate()),
                        String.valueOf(marine.getHealth()),
                        String.valueOf(marine.getHeartCount()),
                        String.valueOf(marine.getCategory()),
                        String.valueOf(marine.getMeleeWeapon())
                ));
                marineValues.addAll(chapterValues);
                csvWriter.writeNext(marineValues.toArray(new String[0]));
            }
            csvWriter.close();
            writer.close();
        } catch (IOException e) {
            throw new WrongStorageFileException(e.getMessage());
        }
    }


    public boolean isExist() {
        return isFileExistBefore;
    }
}
