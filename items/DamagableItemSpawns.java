package items;

import java.awt.Graphics2D;

import characters.Hero;
import util.Hurtbox;
import util.Timer;

/**
 * everyone can get damaged by the item spawns
 */

public abstract class DamagableItemSpawns {
  private Hurtbox[] hurtboxes;
  private boolean over;
  private boolean isActive;

  public DamagableItemSpawns(int num, int attackTime) {
    this.hurtboxes = new Hurtbox[num];
    for (int i = 0; i < num; i++) {
      this.hurtboxes[i] = new Hurtbox();
    }
    Timer.setTimeout(() -> this.over = true, attackTime);
  }

  //in case if I want to animate the spikes 
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
   * @return
   */
  public boolean isActive() {
    return this.isActive;
  }

  /**
   * decides what happens if the item spawn hits a player
   * @param player
   */
  public abstract void hitPlayer(Hero player);


}