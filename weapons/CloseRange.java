package weapons;

import java.io.IOException;

import characters.*;
import util.*;


public class CloseRange extends Weapon {

  public CloseRange(String dataPath) throws IOException {
    super(dataPath);
  }

  /////

  @Override
  public void lightSideAttack(Hero curPlayer, int dir, 
                              String originalState) {
    String state = "lightSide";
    
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
    
    curPlayer.setxTargetSpeed(curPlayer.getxMaxSpeed()/2);
    
  }


  @Override
  public void lightSideHurtbox(Hero curPlayer, int dir) {
    this.getHurtbox().setPos(curPlayer.getX() + Util.scaleX(40)*dir,
                             curPlayer.getY());
    this.getHurtbox().setSize(
      (int)(curPlayer.getRadius()*this.getSize("lightSide")));
  }


  @Override
  public void lightSideKnockback(Hero other, int dir) {
    int power = this.getPower("lightSide");
    other.setSpecialState("knockedBack", power*70);
    other.takeDamage(power);
    other.setDir(dir);
    other.setxTargetSpeed(Util.scaleX(1.0)*power+(other.getDamageTaken()/25));
    other.setYVel(Util.scaleY(-1.0)*power-(other.getDamageTaken()/20));
  }


  /////

  @Override
  public void lightNeutralAttack(Hero curPlayer, int dir,
                                 String originalState) {
    String state = "lightNeutral";
    
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));

  }

  @Override
  public void lightNeutralKnockback(Hero other, int dir) {
    int power = this.getPower("lightNeutral");
    other.setSpecialState("knockedBack", power*50);
    other.takeDamage(power);
    other.setDir(dir);
    other.setxTargetSpeed(Util.scaleX(1.0)*power+(other.getDamageTaken()/20));
    other.setYVel(Util.scaleY(-1.0)*power-(other.getDamageTaken()/30));
  }

  @Override
  public void lightNeutralHurtbox(Hero curPlayer, int dir) {
    this.getHurtbox().setPos(curPlayer.getX() + Util.scaleX(25)*dir,
      curPlayer.getY());
    this.getHurtbox().setSize(
      (int)(curPlayer.getRadius()*this.getSize("lightNeutral")));
  }


  @Override
  public void lightJumpAttack(Hero curPlayer) {

  }

  @Override
  public void lightJumpKnockback(Hero other) {

  }

  @Override
  public void lightJumpHurtbox(Hero curPlayer) {

  }

}