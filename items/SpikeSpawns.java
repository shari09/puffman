package items;

import characters.Hero;
import util.Timer;
import util.*;

public class SpikeSpawns extends DamagableItemSpawns {
  private static final int NUM_SPIKES = 10;
  private static final int RADIUS = Util.scaleX(20);
  private static final int LOADING_TIME = 500;
  private static final int ATTACK_TIME = 2000;

  private static final int DAMAGE = 10;


  public SpikeSpawns(int x, int y, int dir) {
    super(SpikeSpawns.NUM_SPIKES, 
          SpikeSpawns.ATTACK_TIME+SpikeSpawns.LOADING_TIME);
    TimerTasks.addTask(new Timer(this, "setActive", 
                                 SpikeSpawns.LOADING_TIME));
    for (int i = 0; i < SpikeSpawns.NUM_SPIKES; i++) {
      this.setHurtboxPos(i, 
        (x+SpikeSpawns.RADIUS+i*SpikeSpawns.RADIUS*2)*dir,
        y);
      this.setHurtboxSize(i, SpikeSpawns.RADIUS);
    }
  }

  private void updateTimerTasks() {
    if (TimerTasks.validTask(this)) {
      String action = TimerTasks.getTask().getAction();
      if (action.equals("setActive")) {
        this.setActive(true);
      }
    }
  }

  @Override
  public void update() {
    this.updateTimerTasks();
  }

  @Override
  public void hitPlayer(Hero player) {
    player.takeDamage(SpikeSpawns.DAMAGE);
    player.setSpecialState("knockedBack", player.getDamageTaken()+100);
    player.setYVel(-10);
  }
}