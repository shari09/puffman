package items;

import characters.Hero;
import util.TimedEventQueue;
import util.TimedTask;
import util.Util;

/**
 * [BombSpawns.java]
 * This is the effects of using the bomb gadget. A number of explosions
 * will pop up in random places at a certain area/range.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */

public class BombSpawns extends DamagableItemSpawns {
  private static final int NUM_EXPLOSIONS = 8;
  private static final int RADIUS = Util.scaleX(150);
  private static final int LOADING_TIME = 200;
  private static final int ATTACK_TIME = 600;

  private static final int DAMAGE = 10;

  private int x;
  private int y;
  private int dir;

  /**
   * Constructor.
   * @param x the player's x position.
   * @param y the player's y position.
   * @param dir the direction the player is facing.
   */
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
      //spawns explosions at a certain area with random fluctuations
      TimedEventQueue.addTask(new TimedTask(this, "setHurtboxPos", i*totalTime));
      TimedEventQueue.addTask(new TimedTask(this, "setActive", 
                         i*totalTime+BombSpawns.LOADING_TIME));
      //reset it to be inactive so it'll have some loading time again
      TimedEventQueue.addTask(new TimedTask(this, "setActiveOver", i*totalTime));
    }
  }

  /**
   * Update all the related timed tasks/events.
   */
  private void updateTimedTasks() {
    if (TimedEventQueue.validTask(this)) {
      String action = TimedEventQueue.getTask().getAction();
      if (action.equals("setHurtboxPos")) {
        this.setHurtboxPos(0, 
          this.x+this.dir*Util.scaleX(250+(int)(Math.random()*400)), 
          this.y+this.dir*Util.scaleX((int)(Math.random()*400-200))
        );
      } else if (action.equals("setActiveOver")) {
        this.setActive(false);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update() {
    this.updateTimedTasks();
  }

  /**
   * {@inheritDoc}
   */
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