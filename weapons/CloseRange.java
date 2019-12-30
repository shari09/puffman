package weapons;

import java.io.IOException;

import characters.*;
import util.*;


public class CloseRange extends Weapon {

  public CloseRange(String dataPath) throws IOException {
    super(dataPath);
  }

  /////////////////

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
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1, 
      curPlayer.getX() + Util.scaleX(40)*dir,
      curPlayer.getY(),
      (int)(curPlayer.getRadius()*this.getSize("lightSide"))
    );
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


  ///////////////

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
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1,
      curPlayer.getX() + Util.scaleX(25)*dir,
      curPlayer.getY(),
      (int)(curPlayer.getRadius()*this.getSize("lightNeutral"))
    );
  }


  /////////////////////
  @Override
  public void lightJumpAttack(Hero curPlayer) {
    String state = "lightJump";
    curPlayer.setAttackState(state, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
    curPlayer.setYVel(Util.scaleY(-7));
  }

  @Override
  public void lightJumpKnockback(Hero other) {
    int power = this.getPower("lightNeutral");
    other.setSpecialState("knockedBack", power*30);
    other.takeDamage(power);
    other.setYVel(Util.scaleY(-1.0)-power*2-(other.getDamageTaken()/20));
  }

  @Override
  public void lightJumpHurtbox(Hero curPlayer) {
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1,
      curPlayer.getX(),
      curPlayer.getY() - Util.scaleY(20),
      (int)(curPlayer.getRadius()*this.getSize("lightJump"))
    );
  }


  ///////////////

  @Override
  public void lightDownAttack(Hero curPlayer, int dir, String originalState) {
    String state = "lightDown";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
    curPlayer.setYVel(Util.scaleY(7));
    curPlayer.setDir(dir);
    curPlayer.setxTargetSpeed(Util.scaleY(7));
  }

  @Override
  public void lightDownKnockback(Hero other, int dir) {
    int power = this.getPower("lightDown");
    other.setSpecialState("knockedBack", power*30);
    other.takeDamage(power);
    other.setYVel(-power*1.5-(other.getDamageTaken()/20));
    other.setxTargetSpeed(power*3-(other.getDamageTaken()/20));
    other.setDir(dir);
  }

  @Override
  public void lightDownHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(3);
    this.setHurtboxPos(
      1,
      curPlayer.getX()+dir*Util.scaleX(20),
      curPlayer.getY() + Util.scaleY(30),
      (int)(curPlayer.getRadius()*this.getSize("lightDown"))
    );
    this.setHurtboxPos(
      2,
      curPlayer.getX()+dir*Util.scaleX(10),
      curPlayer.getY() + Util.scaleY(20),
      (int)(curPlayer.getRadius()*this.getSize("lightDown")*2)
    );
    this.setHurtboxPos(
      3,
      curPlayer.getX()+dir*Util.scaleX(5),
      curPlayer.getY() + Util.scaleY(10),
      (int)(curPlayer.getRadius()*this.getSize("lightDown"))
    );
  }

  ///////////////////////

  @Override
  public void lightNairAttack(Hero curPlayer,  int dir,
                              String originalState) {
    String state = "lightNair";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
  }

  @Override
  public void lightNairKnockback(Hero other, int dir) {
    int power = this.getPower("lightNair");
    other.setSpecialState("knockedBack", power*40);
    other.takeDamage(power);
    other.setYVel(-power*2-(other.getDamageTaken()/20));
    other.setxTargetSpeed(power*10);
    other.setDir(dir);
  }

  @Override
  public void lightNairHurtbox(Hero curPlayer, int dir) {
    int dist = 18;
    this.setNumHurtboxes(2);

    //goes from down to up in an arc
    int x = curPlayer.getX()+dir*Util.scaleX(20);
    int y = curPlayer.getY()+Util.scaleY(30);
    int offsetY = this.getOffsetY(1)-2;
    int offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      1, 
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightNair")),
      offsetX, offsetY
    );

    dist = 12;
    x = curPlayer.getX()+dir*Util.scaleX(10);
    y = curPlayer.getY()+Util.scaleY(20);
    offsetY = this.getOffsetY(2)-1;
    offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      2,
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightNair")*1.5),
      offsetX, offsetY
    );
  }

  ////////////////////

  @Override
  public void lightSairAttack(Hero curPlayer,  int dir,
                              String originalState) {
    String state = "lightSair";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
    curPlayer.setxTargetSpeed(Util.scaleX(10));
  }
  

  @Override
  public void lightSairKnockback(Hero other, int dir) {
    int power = this.getPower("lightSair");
    other.setSpecialState("knockedBack", other.getDamageTaken());
    other.takeDamage(power);
    other.setxTargetSpeed(power*20);
    other.setDir(dir);
  }

  @Override
  public void lightSairHurtbox(Hero curPlayer, int dir) {
    int dist = 24;
    this.setNumHurtboxes(2);

    //goes from up to down in an arc
    int x = curPlayer.getX()+dir*Util.scaleX(20);
    int y = curPlayer.getY()-Util.scaleY(30);
    int offsetY = this.getOffsetY(1)+3;
    int offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      1, 
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightSair")),
      offsetX, offsetY
    );

    dist = 17;
    x = curPlayer.getX()+dir*Util.scaleX(10);
    y = curPlayer.getY()-Util.scaleY(20);
    offsetY = this.getOffsetY(2)+2;
    offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      2,
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightNair")*1.5),
      offsetX, offsetY
    );
  }

  ////////////
}