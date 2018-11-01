package com.newrelic.codingchallenge.metrics;

/**
 * @author uchousu
 * This thread is responsible for printing the server metrics
 * once every tens seconds. 
 */
public class MetricsReporter implements Runnable {

  private static int uniqueCount;
  private static int duplicateCount;
  private static int uniqueTotalCount;

  private boolean run = true;

  @Override
  public void run() {
    while (run) {
      try {
        String fs = String.format("Received %d new unique numbers, %d new duplicates. Application total unique: %d",
            uniqueCount, duplicateCount, uniqueTotalCount);
        System.out.println("\nServer data processing metrics: \n" + fs);
        resetCounters();
        Thread.sleep(10000);
      } catch (InterruptedException e) {
      }
    }
  }

  private void resetCounters() {
    uniqueCount = 0;
    duplicateCount = 0;
  }

  public void terminate() {
    run = false;
  }

  public static void incrementUniqueCount() {
    uniqueCount++;
  }

  public static void incrementDuplicateCount() {
    duplicateCount++;
  }

  public static void incrementUniqueTotalCount() {
    uniqueTotalCount++;
  }

}
