package items;

import java.awt.Graphics2D;
import java.util.HashSet;

import characters.Hero;
import util.Hurtbox;
import util.*;

/**
 * DamagableItemSpawns.java
 * Everyone can get damaged by the item spawns (including the person
 * who spawned/summoned/used the gadget to make these actions).
 * 
 * Jan 17, 2020
 * @author Shari Sun
 * @version 0.0.2
 */

public abstract class DamagableItemSpawns {
  private Hurtbox[] hurtboxes;
  private boolean over;
  private boolean isActive;

  //doesn't respond to all events (in case of when this super class takes
  //away the local timed events/tasks made by its subclasses)
  private final static HashSet<String> RESPONDING_TASKS = new HashSet<String>();
  static {
    RESPONDING_TASKS.add("over");
    RESPONDING_TASKS.add("setActive");
  }


  /**
   * Constructor.
   * @param num the number of hurtboxes the effect/action will have.
   * @param attackTime the attack time of the entire effect/action.
   */
  public DamagableItemSpawns(int num, int attackTime) {
    this.hurtboxes = new Hurtbox[num];
    for (int i = 0; i < num; i++) {
      this.hurtboxes[i] = new Hurtbox();
    }
    TimedEventQueue.addTask(new TimedTask(this, "over", attackTime));
  }

  /**
   * Update necessary/responding timed tasks/events.
   */
  private void updateTimedTasks() {
    if (TimedEventQueue.validTask(this, DamagableItemSpawns.RESPONDING_TASKS)) {
      String action = TimedEventQueue.getTask().getAction();
      if (action.equals("over")) {
        this.over = true;
      } else if (action.equals("setActive")) {
        this.isActive = true;
      }
    }
  }

  /**
   * Updates everything. This includes the timed tasks/events as well
   * as the update method that was meant for the subclass to use.
   */
  public void updateAll() {
    this.updateTimedTasks();
    this.update();
  }

  /**
   * An update method for the subclasses. The update method will be
   * called on a per loop basis in 'world' so if there are any animation,
   * movement, etc, it can be put inside this method.
   */
  public abstract void update(); 

  /**
   * display the hurtboxes of this attack/item action
   * @param g2d the graphics panel
   * @param players the players for scaling purposes
   */
  public void display(Graphics2D g2d, Hero[] players) {
    for (int i = 0; i < this.hurtboxes.length; i++) {
      this.hurtboxes[i].display(g2d, players, this.isActive);
    }
  }

  /**
   * whether or not the spawned damagable things is 
   * @return boolean, whether or not if this action is over
   */
  public boolean isOver() {
    return this.over;
  }

  /**
   * sets the position of the specific hurtboxes
   * @param index the hurtbox num
   * @param x the x-pos
   * @param y the y-pos
   */
  public void setHurtboxPos(int index, int x, int y) {
    this.hurtboxes[index].setPos(x, y);
  }

  /**
   * sets the size of the specific hurtboxes
   * @param index the hurtbox num
   * @param radius the radius of the hurtbox 
   */
  public void setHurtboxSize(int index, int radius) {
    this.hurtboxes[index].setSize(radius);
  }

  /**
   * moves the specific hurtbox
   * @param index the hurtbox number
   * @param moveX moving the x-pos of the hurtbox
   * @param moveY moving the y-pos of the hurtbox
   */
  public void moveHurtbox(int index, int moveX, int moveY) {
    Hurtbox cur = this.hurtboxes[index];
    cur.setPos(cur.getX()+moveX, cur.getY()+moveY);
  }

  /**
   * get the hurtboxes
   * @return hurtboxes Hurtbox[], the hurtboxes of this action
   */
  public Hurtbox[] getHurtboxes() {
    return this.hurtboxes;
  }

  /**
   * sets the state of the hurtboxes to be active or not,
   * determines whether or not the hurtboxes can damage players
   * in the beginning it's set to inactive to give the opponent 
   * a prepping time to dodge the attack
   * @param active sets whether the hurtbox is active or not
   */
  public void setActive(boolean active) {
    this.isActive = active;
  }

  /**
   * whether or not the spawned item is in the "loading stage"
   * or if they can damage players
   * @return boolean, wehter or not the hurtbox is active (can deal damage).
   */
  public boolean isActive() {
    return this.isActive;
  }

  /**
   * Decides what happens if the item spawn hits a player.
   * @param player the player that was hit.
   */
  public abstract void hitPlayer(Hero player);


}