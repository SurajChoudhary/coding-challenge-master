package com.newrelic.codingchallenge.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author uchousu
 * this class handles the creation,  
 * deletion (when application starts)
 * and continuously appending the file when 
 * application is running.
 */
public class FileHandler {

  private static FileHandler instance = null;

  private final String filename = "logs/numbers.log";

  private FileHandler() {
    deleteFile();
  }

  public static FileHandler getInstance() {
    if (instance == null) {
      instance = new FileHandler();
    }
    return instance;
  }

  private void deleteFile() {
    File file = new File(filename);
    file.delete();
//    if (file.delete()) {
//      System.out.println("File deleted successfully");
//    } else {
//      System.out.println("Failed to delete the file");
//    }
  }

  // TODO removed sync for this method
  public void appendToFile(String str) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
      writer.append(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
