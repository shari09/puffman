package items;

import characters.Hero;
import util.Timer;
import util.Util;

public class HornSpawns extends DamagableItemSpawns {
  private static final int RADIUS = Util.scaleX(40);
  private static final int LOADING_TIME = 200;
  private static final int ATTACK_TIME = 4000;

  private static final int DAMAGE = 20;

  private int dir;
  public HornSpawns(int x, int y, int dir) {
    super(1, HornSpawns.ATTACK_TIME+HornSpawns.LOADING_TIME);
    Timer.setTimeout(() -> this.setActive(true),
                     HornSpawns.LOADING_TIME);
    this.setHurtboxPos(0, x, y-HornSpawns.RADIUS*4);
    this.setHurtboxSize(0, HornSpawns.RADIUS);
    this.dir = dir;
    
  }

  @Override
  public void update() {
    this.moveHurtbox(0, 3*this.dir, 0);
  }

  @Override
  public void hitPlayer(Hero player) {
    player.takeDamage(HornSpawns.DAMAGE);
    player.setSpecialState("knockedBack", player.getDamageTaken()+150);
    player.setxTargetSpeed(Util.scaleX(20));
    player.setDir(this.dir);
  }
}