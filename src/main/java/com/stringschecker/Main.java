package com.stringschecker;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    final static String outputFileName = "output.txt";
    final static String defaultInputFileName = "lng.txt";

    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        FileManipulator fileManipulator;

        try {
            if (args.length >= 1) {
                fileManipulator = new FileManipulator(new File(args[0]));
            } else {
                fileManipulator = new FileManipulator(new File(defaultInputFileName));
            }
            fileManipulator.read();
            fileManipulator.findGroups();
            if (args.length == 2) {
                fileManipulator.write(args[1]);
                System.out.println("Результат группировки сохранен в файле " + args[1] + ".");
            } else {
                fileManipulator.write(outputFileName);
                System.out.println("Результат группировки сохранен в файле " + outputFileName + ".");

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        final double duration = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println("Программа исполнялась " + duration + " секунд.");
    }
}