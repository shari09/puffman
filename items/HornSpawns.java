package items;

import characters.Hero;
import util.TimedTask;
import util.*;

/**
 * [HornSpawns.java]
 * Creates the action of using a horn.
 * The fireball on top of the player.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class HornSpawns extends DamagableItemSpawns {
  private static final int RADIUS = Util.scaleX(40);
  private static final int LOADING_TIME = 200;
  private static final int ATTACK_TIME = 4000;

  private static final int DAMAGE = 20;

  private int dir;

  /**
   * Constructor.
   * @param x the x position of the player used for deciding where to spawn the horn.
   * @param y the y position of the player used for deciding where to spawn the horn.
   * @param dir the direction the player is facing,
   *            used for deciding where to spawn the horn.
   */
  public HornSpawns(int x, int y, int dir) {
    super(1, HornSpawns.ATTACK_TIME+HornSpawns.LOADING_TIME);
    TimedEventQueue.addTask(new TimedTask(this, "setActive", 
                                 HornSpawns.LOADING_TIME));
    this.setHurtboxPos(0, x, y-HornSpawns.RADIUS*4);
    this.setHurtboxSize(0, HornSpawns.RADIUS);
    this.dir = dir;
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update() {
    this.moveHurtbox(0, 3*this.dir, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void hitPlayer(Hero player) {
    player.takeDamage(HornSpawns.DAMAGE);
    player.setSpecialState("knockedBack", player.getDamageTaken()+150);
    player.setxTargetSpeed(Util.scaleX(20));
    player.setDir(this.dir);
  }
}