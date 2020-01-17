package weapons;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import characters.Hero;
import util.Hurtbox;

//weapons control the movement of the players

public abstract class Weapon {
  private static final int LOADING_TIME = 0;
  private static final int ACTIVE_TIME = 1;
  private static final int RECOVERY_TIME = 2;
  private static final int POWER = 3; 
  private static final int SIZE = 4;

  private HashMap<String, Double[]> states = new HashMap<String, Double[]>();
  private Hurtbox[] hurtboxes = new Hurtbox[10];
  private int numHurtboxes = 1;


  public Weapon(String filePath) throws IOException {
    this.getStates(filePath);
    for (int i = 0; i < this.hurtboxes.length; i++) {
      this.hurtboxes[i] = new Hurtbox();
    }
  }


  /**
   * get the states data of the weapon
   * @param filePath the file path to the data sheet
   * @throws IOException exception reading the file
   */
  private void getStates(String filePath) throws IOException {
    File file = new File(filePath);

    Scanner fin = new Scanner(file);
    String line;
    String[] data;
    Double[] configNum;
    while (fin.hasNext()) {
      configNum = new Double[5];
      line = fin.nextLine();
      if (line.length() > 0 && Character.isLetter(line.charAt(0))) {
        data = line.split("\\s+");
        for (int i = 0; i < configNum.length; i++) {
          configNum[i] = Double.parseDouble(data[i+1]);
        }
        this.states.put(data[0], configNum); 
      }
    }
    fin.close();
  }

  /**
   * sets the number of hurtboxes during the attack
   * @param num the number of hurtboxes
   */
  public void setNumHurtboxes(int num) {
    this.numHurtboxes = num;
  }

  /**
   * get the number of hurtboxes the current attack has
   * @return numHurtboxes int, the number of hurtboxes
   */
  public int getNumHurtboxes() {
    return this.numHurtboxes;
  }


  /**
   * get the hurtboxes of this weapon
   * @return this.hurtbox Hurtbox[], the hurtbox for this weapon
   */
  public Hurtbox[] getHurtboxes() {
    return this.hurtboxes;
  }

  /**
   * sets the position/dimension of a hurtbox
   * @param num the ordinal number of this hurtbox (first, second, etc)
   * @param x the x-pos of the hurtbox
   * @param y the y-pos of the hurtbox
   * @param size the size of the hurtbox
   */
  public void setHurtboxPos(int num, int x, int y, int size) {
    this.hurtboxes[num-1].setPos(x, y);
    this.hurtboxes[num-1].setSize(size);
  }

  /**
   * overloaded method that sets the position/dimension of a
   * moving hurtbox (has offsets relative to the original position)
   * @param num the ordinal number of this hurtbox (first, second, etc)
   * @param x the x-pos of the hurtbox
   * @param y the y-pos of the hurtbox
   * @param size the size of the hurtbox
   * @param OffsetX the x-offset
   * @param OffsetY the y-offset
   */
  public void setHurtboxPos(int num, int x, int y, int size, 
                            int OffsetX, int OffsetY) {
    this.hurtboxes[num-1].setPos(x, y);
    this.hurtboxes[num-1].setSize(size);
    this.hurtboxes[num-1].setOffset(OffsetX, OffsetY);
  }

  /**
   * get the current x-offset of the hurtbox
   * @param num the ordinal number of the hurtbox
   * @return int, the x-offset
   */
  public int getOffsetX(int num) {
    return this.hurtboxes[num-1].getOffsetX();
  }

  /**
   * get the current y-pos of the hurtbox
   * @param num the ordinal number of the hurtbox
   * @return int, the y-offset
   */
  public int getOffsetY(int num) {
    return this.hurtboxes[num-1].getOffsetY();
  }

  /**
   * get the loading time for this weapon
   * @param state String, the state/type of the weapon
   * @return loadingTime int, the loading time for this weapon
   */
  public int getLoadingTime(String state) {
    return this.states.get(state)[Weapon.LOADING_TIME].intValue();
  }

  /**
   * get the active time of the attack where the weapon is able
   * to deal damage (not loading/recovering)
   * @param state the state/type of the attack
   * @return activeTime int, the time of the active attack
   */
  public int getActiveTime(String state) {
    return this.states.get(state)[Weapon.ACTIVE_TIME].intValue();
  }

  /**
   * get the recovering time,
   * time it takes for the weapon to recover after an attack
   * @param state the state/type of the attack
   * @return recoveryTime int, the time it takes to recover
   */
  public int getRecoveryTime(String state) {
    return this.states.get(state)[Weapon.RECOVERY_TIME].intValue();
  }

  /**
   * get the power of the weapon, can be used in various calculations
   * such as knockback
   * @param state the state/type of the attack
   * @return power int, the power of the weapon in this particular attack
   */
  public int getPower(String state) {
    return this.states.get(state)[Weapon.POWER].intValue();
  }

  /**
   * get the size of the hurtbox for the attack
   * @param state the state/type of the attack
   * @return size, the size of the hurtbox
   */
  public double getSize(String state) {
    return this.states.get(state)[Weapon.SIZE];
  }

  /**
   * reset the offsets of the hurtboxes to zero
   */
  private void resetHurtboxes() {
    for (int i = 0; i < this.hurtboxes.length; i++) {
      this.hurtboxes[i].resetOffset();
    }
  }

  /**
   * The attack manager
   * Determines which attack method to call based on the 
   * passed in state (type of attack)
   * @param curPlayer the player performing the attack
   * @param state the state/type of the attack
   */
  public void attack(Hero curPlayer, String state, int dir) {
    this.resetHurtboxes();
    if (state.equals("lightLeft") || state.equals("lightRight")) {
      this.lightSideAttack(curPlayer, dir, state);
    } else if (state.equals("lightNLeft") || state.equals("lightNRight")) {
      this.lightNeutralAttack(curPlayer, dir, state);
    } else if (state.equals("lightJump")) {
      this.lightJumpAttack(curPlayer);
    } else if (state.equals("lightDLeft") || state.equals("lightDRight")) {
      this.lightDownAttack(curPlayer, dir, state);
    } else if (state.equals("lightNLair") || state.equals("lightNRair")) {
      this.lightNairAttack(curPlayer, dir, state);
    } else if (state.equals("lightSLair") || state.equals("lightSRair")) {
      this.lightSairAttack(curPlayer, dir, state);
    } else if (state.equals("heavyLeft") || state.equals("heavyRight")) {
      this.heavySideAttack(curPlayer, dir, state);
    } else if (state.equals("heavyNLeft") || state.equals("heavyNRight")) {
      this.heavyNeutralAttack(curPlayer, dir, state);
    } else if (state.equals("heavyJump")) {
      this.heavyJumpAttack(curPlayer);
    } else if (state.equals("heavyDown")) {
      this.heavyDownAttack(curPlayer, dir);
    } else if (state.equals("heavyNLair") || state.equals("heavyNRair")) {
      this.heavyNairAttack(curPlayer, dir, state);
    } else if (state.equals("heavySLair") || state.equals("heavySRair")) {
      this.heavySairAttack(curPlayer, dir, state);
    }
  }

  /**
   * The knockback manager
   * Determines which knockback method to call based on the 
   * passed in state (type of attack)
   * @param other the player being knocked back
   * @param state the state/type of the attack
   */
  public void knockBack(Hero other, String state, int dir) {
    if (state.equals("lightLeft") || state.equals("lightRight")) {
      this.lightSideKnockback(other, dir);
    } else if (state.equals("lightNLeft") || state.equals("lightNRight")) {
      this.lightNeutralKnockback(other, dir);
    } else if (state.equals("lightJump")) {
      this.lightJumpKnockback(other);
    } else if (state.equals("lightDLeft") || state.equals("lightDRight")) {
      this.lightDownKnockback(other, dir);
    } else if (state.equals("lightNLair") || state.equals("lightNRair")) {
      this.lightNairKnockback(other, dir);
    } else if (state.equals("lightSLair") || state.equals("lightSRair")) {
      this.lightSairKnockback(other, dir);
    } else if (state.equals("heavyLeft") || state.equals("heavyRight")) {
      this.heavySideKnockback(other, dir);
    } else if (state.equals("heavyNLeft") || state.equals("heavyNRight")) {
      this.heavyNeutralKnockback(other, dir);
    } else if (state.equals("heavyJump")) {
      this.heavyJumpKnockback(other);
    } else if (state.equals("heavyDown")) {
      this.heavyDownKnockback(other);
    } else if (state.equals("heavyNLair") || state.equals("heavyNRair")) {
      this.heavyNairKnockback(other, dir);
    } else if (state.equals("heavySLair") || state.equals("heavySRair")) {
      this.heavySairKnockback(other, dir);
    }
  }

  /**
   * The hurtbox update manager
   * Determines which update hurtback method to call based on the 
   * passed in state (type of attack)
   * @param curPlayer the player performing the attack
   * @param state the state/type of the attack
   */
  public void updateHurtbox(Hero curPlayer, String state, int dir) {
    if (state.equals("lightLeft") || state.equals("lightRight")) {
      this.lightSideHurtbox(curPlayer, dir);
    } else if (state.equals("lightNLeft") || state.equals("lightNRight")) {
      this.lightNeutralHurtbox(curPlayer, dir);
    } else if (state.equals("lightJump")) {
      this.lightJumpHurtbox(curPlayer);
    } else if (state.equals("lightDLeft") || state.equals("lightDRight")) {
      this.lightDownHurtbox(curPlayer, dir);
    } else if (state.equals("lightNLair") || state.equals("lightNRair")) {
      this.lightNairHurtbox(curPlayer, dir);
    } else if (state.equals("lightSLair") || state.equals("lightSRair")) {
      this.lightSairHurtbox(curPlayer, dir);
    } else if (state.equals("heavyLeft") || state.equals("heavyRight")) {
      this.heavySideHurtbox(curPlayer, dir);
    } else if (state.equals("heavyNLeft") || state.equals("heavyNRight")) {
      this.heavyNeutralHurtbox(curPlayer, dir);
    } else if (state.equals("heavyJump")) {
      this.heavyJumpHurtbox(curPlayer);
    } else if (state.equals("heavyDown")) {
      this.heavyDownHurtbox(curPlayer, dir);
    } else if (state.equals("heavyNLair") || state.equals("heavyNRair")) {
      this.heavyNairHurtbox(curPlayer, dir);
    } else if (state.equals("heavySLair") || state.equals("heavySRair")) {
      this.heavySairHurtbox(curPlayer, dir);
    }
  }

  /**
   * sets the movement of the player during a side light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightSideAttack(Hero curPlayer, int dir, 
                                       String originalState);

  /**
   * sets the movement of the player of the player that got hit
   * during a side light attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightSideKnockback(Hero other, int dir);

  /**
   * sets the hurtbox movement for the player during a side light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightSideHurtbox(Hero curPlayer, int dir);



  /**
   * sets the movement of the player during a side neutral attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightNeutralAttack(Hero curPlayer, int dir,
                                          String originalState);

  /**
   * sets the movement of the player of the player that got hit
   * during a neutral attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightNeutralKnockback(Hero other, int dir);

  /**
   * sets the hurtbox movement for the player during a neutral light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightNeutralHurtbox(Hero curPlayer, int dir);


  /**
   * sets the movement of the player during a jump light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightJumpAttack(Hero curPlayer);

  /**
   * sets the movement of the player of the player that got hit
   * during a jump light attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightJumpKnockback(Hero other);

  /**
   * sets the hurtbox movement for the player during a jump light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightJumpHurtbox(Hero curPlayer);

  
  /**
   * sets the movement of the player during a down light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightDownAttack(Hero curPlayer,  int dir,
                                       String originalState);

  /**
   * sets the movement of the player of the player that got hit
   * during a down light attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightDownKnockback(Hero other, int dir);

  /**
   * sets the hurtbox movement for the player during a down light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightDownHurtbox(Hero curPlayer, int dir);


  /**
   * sets the movement of the player during a neutral air light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightNairAttack(Hero curPlayer,  int dir,
                                       String originalState);

  /**
   * sets the movement of the player of the player that got hit
   * during a neutral air light attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightNairKnockback(Hero other, int dir);

  /**
   * sets the hurtbox movement for the player during a neutral air light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightNairHurtbox(Hero curPlayer, int dir);
  
  
  /**
   * sets the movement of the player during a side air light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction the attacking player is facing
   * @param originalState the original state of the player,
   * whether it's left/right side 
   */
  public abstract void lightSairAttack(Hero curPlayer,  int dir,
                                       String originalState);

  /**
   * sets the movement of the player of the player that got hit
   * during a side air light attack
   * @param other the player that was hit by the attack
   * @param dir the direction the attacking player is facing
   */
  public abstract void lightSairKnockback(Hero other, int dir);

  /**
   * sets the hurtbox movement for the player during a side air light attack
   * @param curPlayer the player performing the attack
   * @param dir the direction of the player attacking
   */
  public abstract void lightSairHurtbox(Hero curPlayer, int dir);


  //////////////

  public abstract void heavySideAttack(Hero curPlayer, int dir,
                                       String originalState);
  public abstract void heavySideKnockback(Hero other, int dir);
  public abstract void heavySideHurtbox(Hero curPlayer, int dir);

  public abstract void heavyNeutralAttack(Hero curPlayer, int dir,
                                       String originalState);
  public abstract void heavyNeutralKnockback(Hero other, int dir);
  public abstract void heavyNeutralHurtbox(Hero curPlayer, int dir);

  public abstract void heavyJumpAttack(Hero curPlayer);
  public abstract void heavyJumpKnockback(Hero other);
  public abstract void heavyJumpHurtbox(Hero curPlayer);

  public abstract void heavyNairAttack(Hero curPlayer, int dir,
                                       String originalState);
  public abstract void heavyNairKnockback(Hero other, int dir);
  public abstract void heavyNairHurtbox(Hero curPlayer, int dir);

  public abstract void heavySairAttack(Hero curPlayer, int dir,
                                       String originalState);
  public abstract void heavySairKnockback(Hero other, int dir);
  public abstract void heavySairHurtbox(Hero curPlayer, int dir);

  public abstract void heavyDownAttack(Hero curPlayer, int dir);
  public abstract void heavyDownKnockback(Hero other);
  public abstract void heavyDownHurtbox(Hero curPlayer, int dir);
}