package items;

import characters.Hero;
import util.Timer;
import util.Util;

public class BombSpawns extends DamagableItemSpawns {
  private static final int NUM_EXPLOSIONS = 8;
  private static final int RADIUS = Util.scaleX(150);
  private static final int LOADING_TIME = 200;
  private static final int ATTACK_TIME = 600;

  private static final int DAMAGE = 10;


  public BombSpawns(int x, int y, int dir) {
    super(1, (BombSpawns.ATTACK_TIME+BombSpawns.LOADING_TIME)
             *BombSpawns.NUM_EXPLOSIONS);
    int totalTime = BombSpawns.LOADING_TIME+BombSpawns.ATTACK_TIME;
    this.setHurtboxSize(0, BombSpawns.RADIUS);

    //update the hurtboxes
    for (int i = 0; i < BombSpawns.NUM_EXPLOSIONS; i++) {
      //update the position
      Timer.setTimeout(() -> {
        this.setHurtboxPos(0, 
                           x+Util.scaleX(250+(int)(Math.random()*400)), 
                           y+Util.scaleX((int)(Math.random()*400-200)));
      }, i*totalTime);
      Timer.setTimeout(() -> this.setActive(true), 
                       i*totalTime+BombSpawns.LOADING_TIME);
      Timer.setTimeout(() -> this.setActive(false),
                       i*totalTime);
    }
  }

  @Override
  public void update() {

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