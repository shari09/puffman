package items;

import util.Util;
import util.Timer;

public class SpikeSpawns extends DamagableItemSpawns {
  private static final int NUM_SPIKES = 10;
  private static final int RADIUS = Util.scaleX(20);
  private static final int LOADING_TIME = 500;
  private static final int ATTACK_TIME = 2000;


  public SpikeSpawns(int x, int y, int dir) {
    super(SpikeSpawns.NUM_SPIKES, SpikeSpawns.ATTACK_TIME);
    Timer.setTimeout(() -> this.setActive(true),
                     SpikeSpawns.LOADING_TIME);
    for (int i = 0; i < SpikeSpawns.NUM_SPIKES; i++) {
      this.setHurtboxPos(i, x+dir*i*SpikeSpawns.RADIUS*2, y);
      this.setHurtboxSize(i, SpikeSpawns.RADIUS);
    }
  }

  @Override
  public void update() {

  }
}