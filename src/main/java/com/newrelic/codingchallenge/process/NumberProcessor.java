package com.newrelic.codingchallenge.process;

import com.newrelic.codingchallenge.cache.Cache;
import com.newrelic.codingchallenge.file.FileHandler;
import com.newrelic.codingchallenge.metrics.MetricsReporter;

/**
 * @author uchousu
 * Number processing implementation of the processor interface. 
 * 
 * this class handle the processing of the message
 * maintains a temporary buffer while is persisted
 * to the file systems periodically
 */
public class NumberProcessor implements Processor {

  private static NumberProcessor instance = null;

  // TODO removed static here
  private StringBuffer buffer;

  private NumberProcessor() {
    FileHandler.getInstance();
    buffer = new StringBuffer();
  }
  
  public static NumberProcessor getInstance() {
    if (instance == null) {
      instance = new NumberProcessor();
    }
    return instance;
  }
    
  @Override
  public void process(String data) {
   
    boolean newData = Cache.getInstance().add(data);
    if(newData) {
//      System.out.println("New Data: " + data);
      MetricsReporter.incrementUniqueCount();
      MetricsReporter.incrementUniqueTotalCount();
      // append data to buffer
      appendToBuffer(data);
    }
    else {
//      System.out.println("Duplicate Data: " + data);
      MetricsReporter.incrementDuplicateCount();
    }
  }
  
  public void appendToBuffer(String str) {
    buffer.append(str);
  }
  
  public StringBuffer getBuffer() {
    return buffer;
  }
  
  public void emptyBuffer() {
    buffer = new StringBuffer();
  }

}
