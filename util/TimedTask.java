package util;

public class TimedTask implements Comparable<TimedTask> {
  private long over;
  private String action;
  private Object targetSource;

  public TimedTask(Object targetSource, String action, int delay) {
    this.over = System.currentTimeMillis()+delay;
    this.targetSource = targetSource;
    this.action = action;
  }

  @Override
  public int compareTo(TimedTask t) {
    return (int)(this.over - t.getOverTime());
  }

  public boolean isOver() {
    return (System.currentTimeMillis() >= this.over);
  }

  public long getOverTime() {
    return this.over;
  }

  public String getAction() {
    return this.action;
  }

  public Object getTargetSource() {
    return this.targetSource;
  }

}