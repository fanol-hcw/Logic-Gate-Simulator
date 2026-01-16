package org.example.logicgatesimulator.exporter;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class RecentsWriter {

    public static void addToRecent(String value) throws IOException {
        File confFile = new File("recentGatesFiles.conf");
        LinkedList<String> recentStack= new LinkedList<>();
        if(!confFile.exists()){
            confFile.createNewFile();
        }
        Scanner scanner = null;
        scanner = new Scanner(confFile);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(!recentStack.contains(line)){
                recentStack.push(line);
            }
        }
        if(!recentStack.contains(value)){
            recentStack.push(value);
        }
        FileWriter fileWriter = null;
        fileWriter = new FileWriter("recentGatesFiles.conf");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        int endIndex = recentStack.size() > 9 ? 9 : recentStack.size();
        bufferedWriter.write(String.join("\n", recentStack.subList(0, endIndex)));
        bufferedWriter.close();
    }

}
