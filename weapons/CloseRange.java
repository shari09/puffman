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
    this.setNumHurtboxes(2);
    this.setHurtboxPos(
      1, 
      curPlayer.getX() + Util.scaleX((int)(curPlayer.getWidth()*1.4))*dir,
      curPlayer.getY(),
      (int)(curPlayer.getRadius()*this.getSize("lightSide"))
    );
    this.setHurtboxPos(
      2, 
      curPlayer.getX() + Util.scaleX((int)(curPlayer.getWidth()))*dir,
      curPlayer.getY(),
      (int)(curPlayer.getRadius()*this.getSize("lightSide"))
    );
  }


  @Override
  public void lightSideKnockback(Hero other, int dir) {
    int power = this.getPower("lightSide");
    other.setSpecialState("knockedBack", 30+other.getDamageTaken()*10);
    other.takeDamage(power);
    other.setDir(dir);
    other.setxTargetSpeed(Util.scaleX(power)
                          +Util.scaleX(other.getDamageTaken()/15));
    other.setYVel(Util.scaleY(-power-Util.scaleY(other.getDamageTaken()/20)));
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
    other.setxTargetSpeed(Util.scaleX(power)
                          +Util.scaleX(other.getDamageTaken()));
    other.setYVel(Util.scaleY(-power)
                  -Util.scaleY(other.getDamageTaken()/10));
  }

  @Override
  public void lightNeutralHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1,
      curPlayer.getX() + Util.scaleX((int)(curPlayer.getWidth()))*dir,
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
    other.setYVel(Util.scaleY(-power*4)
                  -Util.scaleY(other.getDamageTaken()/7));
  }

  @Override
  public void lightJumpHurtbox(Hero curPlayer) {
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1,
      curPlayer.getX(),
      curPlayer.getY() - Util.scaleY(curPlayer.getHeight()/2),
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
    other.setYVel(Util.scaleY(-power*2.5)
                  -Util.scaleY(other.getDamageTaken()/20));
    other.setxTargetSpeed(Util.scaleX(power*3)
                          +Util.scaleX(other.getDamageTaken()/10));
    other.setDir(dir);
  }

  @Override
  public void lightDownHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(3);
    this.setHurtboxPos(
      1,
      curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/2),
      curPlayer.getY() + (int)(Util.scaleY(curPlayer.getHeight()/1.3)),
      (int)(curPlayer.getRadius()*this.getSize("lightDown"))
    );
    this.setHurtboxPos(
      2,
      curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/4),
      curPlayer.getY() + Util.scaleY(curPlayer.getHeight()/2),
      (int)(curPlayer.getRadius()*this.getSize("lightDown")*2)
    );
    this.setHurtboxPos(
      3,
      curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/8),
      curPlayer.getY() + Util.scaleY(curPlayer.getHeight()/4),
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
    other.setYVel(Util.scaleY(-power*3)
                  -Util.scaleY(other.getDamageTaken()/10));
    other.setxTargetSpeed(Util.scaleX(power*10));
    other.setDir(dir);
  }

  @Override
  public void lightNairHurtbox(Hero curPlayer, int dir) {
    int dist = Util.scaleX((int)(curPlayer.getWidth()/1.6));
    this.setNumHurtboxes(2);

    //goes from down to up in an arc
    int x = curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/2);
    int y = curPlayer.getY()+Util.scaleY((int)(curPlayer.getWidth()/1.3));

    int offsetY = this.getOffsetY(1)-curPlayer.getHeight()/20;
    int offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      1, 
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightNair")),
      offsetX, offsetY
    );

    dist = Util.scaleX(16);
    x = curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/4);
    y = curPlayer.getY()+Util.scaleY(curPlayer.getHeight()/2);
    offsetY = this.getOffsetY(2)-curPlayer.getHeight()/40;
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
    other.setxTargetSpeed(Util.scaleX(power*40));
    other.setDir(dir);
  }

  @Override
  public void lightSairHurtbox(Hero curPlayer, int dir) {
    int dist = Util.scaleX((int)(curPlayer.getWidth()/1.17));
    this.setNumHurtboxes(2);

    //goes from up to down in an arc
    int x = curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/2);
    int y = curPlayer.getY()-Util.scaleY((int)(curPlayer.getHeight()/1.3));
    int offsetY = this.getOffsetY(1)+curPlayer.getHeight()/20;
    int offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      1, 
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightSair")),
      offsetX, offsetY
    );

    dist = Util.scaleX((int)(curPlayer.getWidth()/1.74));
    x = curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/4);
    y = curPlayer.getY()-Util.scaleY(curPlayer.getHeight()/2);
    offsetY = this.getOffsetY(2)+curPlayer.getHeight()/20;
    offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      2,
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("lightNair")*1.5),
      offsetX, offsetY
    );
  }

  ///////////////////////////////////////////////////////////////////////

  @Override
  public void heavySideAttack(Hero curPlayer, int dir,
                              String originalState) {
    String state = "heavySide";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state),
                             this.getActiveTime(state),
                             this.getRecoveryTime(state));
    Timer.setTimeout(() -> curPlayer.setxTargetSpeed(Util.scaleX(25)),
                     this.getLoadingTime(state));
    Timer.setTimeout(() -> curPlayer.resetXMovement(),
                     this.getActiveTime(state) 
                     + this.getLoadingTime(state));

  }

  @Override
  public void heavySideKnockback(Hero other, int dir) {
    int power = this.getPower("heavySide");
    other.setSpecialState("knockedBack", power*40);
    other.takeDamage(power);
    other.setxTargetSpeed(Util.scaleX(other.getDamageTaken()*10)
                          *Util.scaleX(power*40));
    other.setDir(dir);
  }

  @Override
  public void heavySideHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(1);
    this.setHurtboxPos(1,
     curPlayer.getX() + dir*Util.scaleX((int)(curPlayer.getWidth()/0.8)),
     curPlayer.getY() - Util.scaleY((int)(curPlayer.getHeight()/5.7)),
     (int)(curPlayer.getRadius()*this.getSize("heavySide")));
  }

  @Override
  public void heavyNeutralAttack(Hero curPlayer, int dir,
                                 String originalState) {
    String state = "heavyNeutral";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state), 
                             this.getActiveTime(state), 
                             this.getRecoveryTime(state));
    // curPlayer.setxTargetSpeed(Util.scaleX(3));
  }
  
  @Override
  public void heavyNeutralKnockback(Hero other, int dir) {
    int power = this.getPower("heavyNeutral");
    other.setSpecialState("knockedBack", power*40);
    other.takeDamage(power);
    other.setxTargetSpeed(Util.scaleX(other.getDamageTaken()*10)
                          *Util.scaleX(power*40));
    other.setYVel(-Util.scaleY(other.getDamageTaken()));
    other.setDir(dir);
  }
 
  @Override
  public void heavyNeutralHurtbox(Hero curPlayer, int dir) {
    int dist = Util.scaleX(curPlayer.getWidth()/2);

    this.setNumHurtboxes(1);
    int x = curPlayer.getX()+dir*Util.scaleX(curPlayer.getWidth()/2);
    int y = curPlayer.getY()+Util.scaleY((int)(curPlayer.getHeight()/1.3));
    int offsetY = this.getOffsetY(1)-curPlayer.getHeight()/40;
    int offsetX = dir*(int)Math.sqrt(dist*dist-Math.pow(
      y+offsetY-curPlayer.getY(), 2));

    this.setHurtboxPos(
      1, 
      x, y,
      (int)(curPlayer.getRadius()*this.getSize("heavyNeutral")),
      offsetX, offsetY
    );
  }

  @Override
  public void heavyJumpAttack(Hero curPlayer) {
    String state = "heavyJump";
    curPlayer.setAttackState(state, 
                             this.getLoadingTime(state), 
                             this.getActiveTime(state), 
                             this.getRecoveryTime(state));
    curPlayer.setYVel(Util.scaleY(-10));
  }

  @Override
  public void heavyJumpKnockback(Hero other) {
    int power = this.getPower("heavyJump");
    other.setSpecialState("knockedBack", power*20);
    other.takeDamage(power);
    other.setYVel(Util.scaleY(-other.getDamageTaken()*2));
  }

  @Override
  public void heavyJumpHurtbox(Hero curPlayer) {
    this.setNumHurtboxes(1);
    this.setHurtboxPos(
      1,
      curPlayer.getX(),
      curPlayer.getY() - Util.scaleY((int)(curPlayer.getHeight()/1.3)),
      (int)(this.getSize("heavyJump")*curPlayer.getRadius())
    );
  }

  @Override
  public void heavyNairAttack(Hero curPlayer, int dir,
                              String originalState) {
    String state = "heavyNair";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state), 
                             this.getActiveTime(state), 
                             this.getRecoveryTime(state));
    curPlayer.gravityCancel(this.getLoadingTime(state) 
                            +this.getActiveTime(state));                          
  }

  @Override
  public void heavyNairKnockback(Hero other, int dir) {
    int power = this.getPower("heavyNair");
    other.setSpecialState("knockedBack", power*20);
    other.takeDamage(power);
    other.setxTargetSpeed(Util.scaleX(other.getDamageTaken()/5));
    other.setYVel(Util.scaleY(-5-other.getDamageTaken()/15));
    other.setDir(dir);
  }

  @Override
  public void heavyNairHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(2);
    this.setHurtboxPos(
      1,
      curPlayer.getX() + dir*Util.scaleX(curPlayer.getWidth()),
      curPlayer.getY(),
      (int)(this.getSize("heavyNair")*curPlayer.getRadius())
    );
    this.setHurtboxPos(
      2,
      curPlayer.getX() + dir*Util.scaleX(curPlayer.getWidth()/2),
      curPlayer.getY(),
      (int)(this.getSize("heavyNair")*curPlayer.getRadius())
    );
  }


  @Override
  public void heavySairAttack(Hero curPlayer, int dir,
                              String originalState) {
    String state = "heavyNair";
    curPlayer.setAttackState(originalState, 
                             this.getLoadingTime(state), 
                             this.getActiveTime(state), 
                             this.getRecoveryTime(state));
    curPlayer.setxTargetSpeed(Util.scaleX(8));
    curPlayer.setYVel(-5);
  }

  @Override
  public void heavySairKnockback(Hero other, int dir) {
    int power = this.getPower("heavySair");
    other.setSpecialState("knockedBack", other.getDamageTaken()*7);
    other.takeDamage(power);
    other.setxTargetSpeed(Util.scaleX(10));
    other.setYVel(Util.scaleY(10+other.getDamageTaken()/20));
    other.setDir(dir);
  }

  @Override
  public void heavySairHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(2);
    this.setHurtboxPos(
      1,
      curPlayer.getX() + dir*Util.scaleX(curPlayer.getWidth()/4),
      curPlayer.getY() + Util.scaleY(curPlayer.getHeight()/4),
      (int)(this.getSize("heavySair")*curPlayer.getRadius())
    );
    this.setHurtboxPos(
      2,
      curPlayer.getX() + dir*Util.scaleX(curPlayer.getWidth()/2),
      curPlayer.getY(),
      (int)(this.getSize("heavySair")*curPlayer.getRadius())
    );
  }


  @Override
  public void heavyDownAttack(Hero curPlayer, int dir) {
    String state = "heavyDown";
    curPlayer.setAttackState(state, 
                             this.getLoadingTime(state), 
                             this.getActiveTime(state), 
                             this.getRecoveryTime(state));
    curPlayer.setYVel(10);
  }

  @Override
  public void heavyDownKnockback(Hero other) {
    int power = this.getPower("heavyDown");
    other.setSpecialState("knockedBack", other.getDamageTaken()*7);
    other.takeDamage(power);
    other.setYVel(Util.scaleY(10+other.getDamageTaken()/10));
  }

  @Override
  public void heavyDownHurtbox(Hero curPlayer, int dir) {
    this.setNumHurtboxes(2);
    this.setHurtboxPos(
      1,
      curPlayer.getX(),
      curPlayer.getY() + Util.scaleY(curPlayer.getHeight()/4),
      (int)(this.getSize("heavyDown")*curPlayer.getRadius())
    );
    this.setHurtboxPos(
      2,
      curPlayer.getX(),
      curPlayer.getY() + Util.scaleY((int)(curPlayer.getHeight()/1.8)),
      (int)(this.getSize("heavyDown")*curPlayer.getRadius())
    );
  }


}