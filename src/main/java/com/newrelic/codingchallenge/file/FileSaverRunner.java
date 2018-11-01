package com.newrelic.codingchallenge.file;

import com.newrelic.codingchallenge.process.NumberProcessor;

/**
 * @author uchousu
 * 
 * This thread is responsible for persisting the file to the file system. 
 * File is persisted to the memory once every 1.5 second.
 * The frequency of saving the file can be adjusted to tune
 * performance of the application and achieve higher throughput.
 * 
 * it safely terminates the thread
 */
public class FileSaverRunner implements Runnable {

  private int persistenceFreqInMilliSecs = 1500;
  private boolean run = true;
  
  @Override
  public void run() {
    FileHandler fileHandler = FileHandler.getInstance();
    NumberProcessor processor = NumberProcessor.getInstance();
    try {
      while (run) {
        Thread.sleep(persistenceFreqInMilliSecs);
        StringBuffer buffer = processor.getBuffer();
        if (buffer.length() != 0) {
          fileHandler.appendToFile(buffer.toString());
          processor.emptyBuffer();
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void terminate() {
    run = false;
  }
}
