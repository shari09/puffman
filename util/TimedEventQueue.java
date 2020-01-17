package util;

import java.util.*;
public class TimedEventQueue {
  private static PriorityQueue<TimedTask> timer = new PriorityQueue<TimedTask>();

  public static void addTask(TimedTask task) {
    TimedEventQueue.timer.add(task);
  }

  public static boolean validTask(Object o) {
    if (TimedEventQueue.timer.size() > 0) {
      return TimedEventQueue.timer.peek().getTargetSource() == o
             && TimedEventQueue.timer.peek().isOver();
    }
    return false;
  }

  public static boolean validTask(Object o, HashSet<String> respondingTasks) {
    if (TimedEventQueue.timer.size() > 0) {
      return TimedEventQueue.timer.peek().getTargetSource() == o
             && respondingTasks.contains(TimedEventQueue.timer.peek().getAction())
             && TimedEventQueue.timer.peek().isOver();
    }
    return false;
  }

  public static TimedTask getTask() {
    return TimedEventQueue.timer.poll();
  }

  public static void removeTask(TimedTask task) {
    TimedEventQueue.timer.remove(task);
  }

  public static void reset() {
    TimedEventQueue.timer.clear();
  }
}