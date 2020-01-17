package util;

import java.util.*;
public class TimerTasks {
  private static PriorityQueue<Timer> timer = new PriorityQueue<Timer>();

  public static void addTask(Timer task) {
    TimerTasks.timer.add(task);
  }

  public static boolean validTask(Object o) {
    if (TimerTasks.timer.size() > 0) {
      return TimerTasks.timer.peek().getTargetSource() == o
             && TimerTasks.timer.peek().isOver();
    }
    return false;
  }

  public static boolean validTask(Object o, HashSet<String> respondingTasks) {
    if (TimerTasks.timer.size() > 0) {
      return TimerTasks.timer.peek().getTargetSource() == o
             && respondingTasks.contains(TimerTasks.timer.peek().getAction())
             && TimerTasks.timer.peek().isOver();
    }
    return false;
  }

  public static Timer getTask() {
    return TimerTasks.timer.poll();
  }

  public static void removeTask(Timer task) {
    TimerTasks.timer.remove(task);
  }

  public static void reset() {
    TimerTasks.timer.clear();
  }
}