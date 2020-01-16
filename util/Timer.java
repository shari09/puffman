package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;



public class Timer {
  private static ConcurrentHashMap<String, Integer> 
    timer = new ConcurrentHashMap<>();
  private static HashMap<String, Runnable> 
    tasks = new HashMap<>();
  private static long previousTime = System.currentTimeMillis();

  /**
   * Generate a random id for each timeout event
   * add the time and tasks to the Timer
   * @param runnable the runnable task/function
   * @param delay the delay in milliseconds
   * @return id String, the unique id
   */
  public static String setTimeout(Runnable runnable, int delay) {
    String id = UUID.randomUUID().toString(); 
    tasks.put(id, runnable);
    timer.put(id, delay);
    return id;
  }

  /**
   * cancel the timeout event
   * @param id
   */
  public static void clearTimeout(String id) {
    tasks.remove(id);
    timer.remove(id);
  }

  /**
   * clear all tasks
   */
  public static void reset() {
    tasks.clear();
    timer.clear();
  }

  /**
   * Updates the timer each millisecond
   */
  public static void update() {
    int elapasedTime = (int)(System.currentTimeMillis() - previousTime); 
    if (elapasedTime > 0) {
      Iterator<ConcurrentHashMap.Entry<String, Integer>> itr = timer
                                                       .entrySet()
                                                       .iterator();

      while (itr.hasNext()) {
        ConcurrentHashMap.Entry<String, Integer> pair = itr.next();
        timer.put(pair.getKey(), pair.getValue()-elapasedTime);
        if (pair.getValue() <= 0) {
          tasks.get(pair.getKey()).run();
          tasks.remove(pair.getKey());
          itr.remove();
        }
      }
    }

    previousTime = System.currentTimeMillis();
  }
}