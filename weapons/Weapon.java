package weapons;

import java.util.*;
import java.io.*;

import characters.*;
import util.*;

public abstract class Weapon {
  private static final int LOADING_TIME = 0;
  private static final int ACTIVE_TIME = 1;
  private static final int RECOVERY_TIME = 2;
  private static final int POWER = 3;
  private static final int SIZE = 4;

  private HashMap<String, Double[]> states = new HashMap<>();
  private Hurtbox hurtbox = new Hurtbox();

  public Weapon(String filePath) throws IOException {
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
   * get the hurtbox of this weapon
   * @return this.hurtbox Hurtbox, the hurtbox for this weapon
   */
  public Hurtbox getHurtbox() {
    return this.hurtbox;
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

  // private void displayAllData() {
  //   Iterator<HashMap.Entry<String, Double[]>> itr = this
  //                                                   .states
  //                                                   .entrySet()
  //                                                   .iterator();
  //   while (itr.hasNext()) {
  //     HashMap.Entry<String, Double[]> pair = itr.next();
  //     System.out.printf("%-20s", pair.getKey());
  //     for (int i = 0; i < pair.getValue().length; i++) {
  //       System.out.printf("%10.2f", pair.getValue()[i]);
  //     }
  //     System.out.println();
  //   }
  // }


  /**
   * The attack manager
   * Determines which attack method to call based on the 
   * passed in state (type of attack)
   * @param curPlayer the player performing the attack
   * @param state the state/type of the attack
   */
  public void attack(Hero curPlayer, String state) {
    if (state.equals("lightLeft")) {
      this.lightSideAttack(curPlayer, -1, "lightLeft");
    } else if (state.equals("lightRight")) {
      this.lightSideAttack(curPlayer, 1, "lightRight");
    } else if (state.equals("lightNLeft")) {
      this.lightNeutralAttack(curPlayer, -1, "lightNLeft");
    } else if (state.equals("lightNRight")) {
      this.lightNeutralAttack(curPlayer, 1, "lightNRight");
    }
  }

  /**
   * The knockback manager
   * Determines which knockback method to call based on the 
   * passed in state (type of attack)
   * @param other the player being knocked back
   * @param state the state/type of the attack
   */
  public void knockBack(Hero other, String state) {
    if (state.equals("lightLeft")) {
      this.lightSideKnockback(other, -1);
    } else if (state.equals("lightRight")) {
      this.lightSideKnockback(other, 1);
    } else if (state.equals("lightNLeft")) {
      this.lightNeutralKnockback(other, -1);
    } else if (state.equals("lightNRight")) {
      this.lightNeutralKnockback(other, 1);
    }
  }

  /**
   * The hurtbox update manager
   * Determines which update hurtback method to call based on the 
   * passed in state (type of attack)
   * @param curPlayer the player performing the attack
   * @param state the state/type of the attack
   */
  public void updateHurtbox(Hero curPlayer, String state) {
    if (state.equals("lightLeft")) {
      this.lightSideHurtbox(curPlayer, -1);
    } else if (state.equals("lightRight")) {
      this.lightSideHurtbox(curPlayer, 1);
    } else if (state.equals("lightNLeft")) {
      this.lightNeutralHurtbox(curPlayer, -1);
    } else if (state.equals("lightNRight")) {
      this.lightNeutralHurtbox(curPlayer, 1);
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



  public abstract void lightNeutralAttack(Hero curPlayer, int dir,
                                          String originalState);
  public abstract void lightNeutralKnockback(Hero other, int dir);
  public abstract void lightNeutralHurtbox(Hero curPlayer, int dir);

  public abstract void lightJumpAttack(Hero curPlayer);
  public abstract void lightJumpKnockback(Hero other);
  public abstract void lightJumpHurtbox(Hero curPlayer);


}