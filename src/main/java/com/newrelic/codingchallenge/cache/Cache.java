package com.newrelic.codingchallenge.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author uchousu
 * Using local server side cache to achieve state-full 
 * processing of the messages, discarding duplicates.
 */
public class Cache {

  private static ConcurrentHashMap.KeySetView<String, Boolean> lookupSet;
  private static Cache instance = null;

  private Cache() {
    lookupSet = ConcurrentHashMap.newKeySet();
  }

  public static Cache getInstance() {
    if (instance == null) {
      instance = new Cache();
    }
    return instance;
  }

  public boolean add(String key) {
    if (key == null || key.isEmpty()) {
      return false;
    }
    return lookupSet.add(key);
  }
}
