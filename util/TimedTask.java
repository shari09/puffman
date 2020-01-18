package util;

/**
 * [TimedTask.java]
 * A timed task object that can be added to the global event queue.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */
public class TimedTask implements Comparable<TimedTask> {
  private long over;
  private String action;
  private Object targetSource;

  /**
   * Constructor.
   * @param targetSource the target object that is supposed to receive the event.
   * @param action the event action (name).
   * @param delay the delay of when the even fires off.
   */
  public TimedTask(Object targetSource, String action, int delay) {
    this.over = System.currentTimeMillis()+delay;
    this.targetSource = targetSource;
    this.action = action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(TimedTask t) {
    return (int)(this.over - t.getOverTime());
  }

  /**
   * Get whether or not the task is ready (time's up).
   * @return whether or not the task is ready (time's up).
   */
  public boolean isOver() {
    return (System.currentTimeMillis() >= this.over);
  }

  /**
   * Get the time for when this task is ready.
   * @return long, the system time in milliseconds for when this is ready.
   */
  public long getOverTime() {
    return this.over;
  }

  /**
   * Get the task action/name. 
   * @return String, the task action.
   */
  public String getAction() {
    return this.action;
  }

  /**
   * Get the target object of the task.
   * @return Object, the target object for the task.
   */
  public Object getTargetSource() {
    return this.targetSource;
  }

}