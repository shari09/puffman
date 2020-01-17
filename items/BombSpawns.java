package items;

import characters.Hero;
import util.Timer;
import util.TimerTasks;
import util.Util;

public class BombSpawns extends DamagableItemSpawns {
  private static final int NUM_EXPLOSIONS = 8;
  private static final int RADIUS = Util.scaleX(150);
  private static final int LOADING_TIME = 200;
  private static final int ATTACK_TIME = 600;

  private static final int DAMAGE = 10;

  private int x;
  private int y;
  private int dir;

  public BombSpawns(int x, int y, int dir) {
    super(1, (BombSpawns.ATTACK_TIME+BombSpawns.LOADING_TIME)
             *BombSpawns.NUM_EXPLOSIONS);
    this.x = x;
    this.y = y;
    this.dir = dir;

    int totalTime = BombSpawns.LOADING_TIME+BombSpawns.ATTACK_TIME;
    this.setHurtboxSize(0, BombSpawns.RADIUS);
    
    //update the hurtboxes
    for (int i = 0; i < BombSpawns.NUM_EXPLOSIONS; i++) {
      //update the position
      TimerTasks.addTask(new Timer(this, "setHurtboxPos", i*totalTime));
      TimerTasks.addTask(new Timer(this, "setActive", 
                         i*totalTime+BombSpawns.LOADING_TIME));
      TimerTasks.addTask(new Timer(this, "setActiveOver", i*totalTime));
    }
  }

  private void updateTimerTasks() {
    if (TimerTasks.validTask(this)) {
      String action = TimerTasks.getTask().getAction();
      if (action.equals("setHurtboxPos")) {
        this.setHurtboxPos(0, 
          this.x+this.dir*Util.scaleX(250+(int)(Math.random()*400)), 
          this.y+this.dir*Util.scaleX((int)(Math.random()*400-200))
        );
      } else if (action.equals("setActive")) {
        this.setActive(true);
      } else if (action.equals("setActiveOver")) {
        this.setActive(false);
      }
    }
  }

  @Override
  public void update() {
    this.updateTimerTasks();
  }

  @Override
  public void hitPlayer(Hero player) {
    player.takeDamage(BombSpawns.DAMAGE);
    player.setSpecialState("knockedBack", player.getDamageTaken()+100);
    player.setYVel(-10);
    player.setxTargetSpeed(20);
    //bombed to the left or right
    if (this.getHurtboxes()[0].getX() < player.getX()) {
      player.setDir(-1);
    } else {
      player.setDir(1);
    }
  }
}