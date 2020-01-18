package util;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * [TimedEventQueue.java]
 * A global static event queue organized by time.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class TimedEventQueue {
  //the event queue that is organized by time of processed events
  private static PriorityQueue<TimedTask> eventQueue
     = new PriorityQueue<TimedTask>();

  /**
   * Add a task to the event queue.
   * @param task the task to add to the queue.
   */
  public static void addTask(TimedTask task) {
    TimedEventQueue.eventQueue.add(task);
  }

  /**
   * Checks if the the task is valid or not.
   * - Time is up.
   * - Task target object matches the passed in object.
   * @param o the Object to check for and see whether or not it
   *          matches the target object of the event.
   */
  public static boolean validTask(Object o) {
    if (TimedEventQueue.eventQueue.size() > 0) {
      return TimedEventQueue.eventQueue.peek().getTargetSource() == o
             && TimedEventQueue.eventQueue.peek().isOver();
    }
    return false;
  }

  /**
   * Checks if the task is valid or not.
   * - Time is up
   * - Task target object matches the passed in object.
   * - Whether or not it's one of the subscribed event.
   * @param o the Object to check for and see whether or not it
   *          matches the target object of the event.
   * @param respondingTasks the tasks that the object listens for.
   * @return boolean, whether or not the task is valid.
   */
  public static boolean validTask(Object o, HashSet<String> respondingTasks) {
    if (TimedEventQueue.eventQueue.size() > 0) {
      return TimedEventQueue.eventQueue.peek().getTargetSource() == o
             && respondingTasks.contains(TimedEventQueue.eventQueue.peek().getAction())
             && TimedEventQueue.eventQueue.peek().isOver();
    }
    return false;
  }

  /**
   * Get and remove the task at the top of the queue.
   * @return TimedTask, the task at the top of the queue.
   */
  public static TimedTask getTask() {
    return TimedEventQueue.eventQueue.poll();
  }

  /**
   * Remove the task from the event queue.
   * @param task the task to remove.
   */
  public static void removeTask(TimedTask task) {
    TimedEventQueue.eventQueue.remove(task);
  }

  /**
   * Resets the global event queue.
   */
  public static void reset() {
    TimedEventQueue.eventQueue.clear();
  }
}