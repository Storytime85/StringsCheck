package com.stringschecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileManipulator {
    static final String regex = "^(?!;)(;?\"\\d*+\")+$";
    private final FileReader reader;
    private final Set<String> filteredSetOfEntries;
    private List<List<String>> resultList;

    public FileManipulator(File fileName) throws FileNotFoundException {
        reader = new FileReader(fileName);
        filteredSetOfEntries = new LinkedHashSet<>();
    }

    public Set<String> getFilteredSetOfEntries() {
        return filteredSetOfEntries;
    }

    //Уникальных значений для входного файла 998578
    public void read() {
        try (BufferedReader br = new BufferedReader(reader)) {
            String inputLine = br.readLine();
            if (inputLine == null) {
                throw new RuntimeException("File is empty");
            }
            while (inputLine != null) {
                if (inputLine.matches(regex)) {
                    filteredSetOfEntries.add(inputLine);
                }
                inputLine = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String outputFileName) {
        System.out.println("Групп, размером больше 1: " + resultList.stream().filter(s -> s.size() > 1).count());

        Path outputPath = Path.of(outputFileName);
        try {
            Files.createFile(outputPath);
        } catch (IOException e) {
            System.out.println("Ошибка создания файла с результатом. Возможно, файл уже существует.");
            throw new RuntimeException(e);
        }

        try (PrintStream out = new PrintStream(new FileOutputStream(outputPath.toFile()))) {
            int i = 0;
            for (List<String> group : resultList) {
                i++;
                out.println("\nГруппа " + i);
                for (String val : group) {
                    out.println(val);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
            throw new RuntimeException(e);
        }
    }

    public void findGroups() {
        class NewLineEntry {
            private final String lineEntry;
            private final int columnNumber;

            private NewLineEntry(String lineEntry, int columnNumber) {
                this.lineEntry = lineEntry;
                this.columnNumber = columnNumber;
            }
        }
        resultList = new ArrayList<>();
        List<Map<String, Integer>> columns = new ArrayList<>();
        Map<Integer, Integer> unitedGroups = new HashMap<>();

        for (String line : filteredSetOfEntries) {
            TreeSet<Integer> groupsWithSameElems = new TreeSet<>();
            List<NewLineEntry> newElements = new ArrayList<>();
            String[] separatedStrings = line.split(";");

            for (int i = 0; i < separatedStrings.length; i++) {
                if (columns.size() == i)
                    columns.add(new HashMap<>());
                if ("".equals(separatedStrings[i].replaceAll("\"", "").trim()))
                    continue;

                Map<String, Integer> currentColumn = columns.get(i);
                Integer elementNumber = currentColumn.get(separatedStrings[i]);
                if (elementNumber != null) {
                    while (unitedGroups.containsKey(elementNumber)) {
                        elementNumber = unitedGroups.get(elementNumber);
                    }
                    groupsWithSameElems.add(elementNumber);
                } else {
                    newElements.add(new NewLineEntry(separatedStrings[i], i));
                }
            }
            int groupNumber;
            if (groupsWithSameElems.isEmpty()) {
                resultList.add(new ArrayList<>());
                groupNumber = resultList.size() - 1;
            } else {
                groupNumber = groupsWithSameElems.first();
            }
            for (NewLineEntry newLineEntry : newElements) {
                columns.get(newLineEntry.columnNumber).put(newLineEntry.lineEntry, groupNumber);
            }
            for (int matchedGrNum : groupsWithSameElems) {
                if (matchedGrNum != groupNumber) {
                    unitedGroups.put(matchedGrNum, groupNumber);
                    resultList.get(groupNumber).addAll(resultList.get(matchedGrNum));
                    resultList.set(matchedGrNum, null);
                }
            }
            resultList.get(groupNumber).add(line);
        }
        resultList.removeAll(Collections.singleton(null));
        resultList.sort(Comparator.comparingInt(s -> -s.size()));
    }
}
