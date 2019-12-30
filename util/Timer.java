package util;

import java.util.HashMap;
import java.util.Iterator;

//super sketchy wayyyyyyy
//casting function to runnable
//using runnable as a key hmmmm
//well I might change it later if I feel like it heheh
public class Timer {
  private static HashMap<Runnable, Integer> tasks = new HashMap<>();
  private static long previousTime = System.currentTimeMillis();

  /**
   * Adds a task to the timer
   * @param runnable the runnable task/function
   * @param delay the delay in milliseconds
   */
  public static void setTimeout(Runnable runnable, int delay) {
    tasks.put(runnable, delay);
  }

  /**
   * clear all tasks
   */
  public static void reset() {
    tasks.clear();
  }

  /**
   * Updates the timer each millisecond
   */
  public static void update() {
    int elapasedTime = (int)(System.currentTimeMillis() - previousTime); 
    if (elapasedTime > 0) {
      Iterator<HashMap.Entry<Runnable, Integer>> itr = tasks
                                                       .entrySet()
                                                       .iterator();

      while (itr.hasNext()) {
        HashMap.Entry<Runnable, Integer> pair = itr.next();
        tasks.put(pair.getKey(), pair.getValue()-elapasedTime);
        if (pair.getValue() <= 0) {
          pair.getKey().run();
          itr.remove();
        }
      }
    }

    previousTime = System.currentTimeMillis();
  }
}