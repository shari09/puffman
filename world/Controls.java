package world;

import characters.Hero;
import java.awt.event.*;
import java.util.HashSet;

public class Controls implements KeyListener {
  private World world;
  private HashSet<String> tappedKeys = new HashSet<>();  

  private HashSet<String> heldKeyList = new HashSet<>();
  
  public Controls(World world) {
    this.world = world;
    for (int i = 0; i < this.world.getPlayers().length; i++) {
      Hero curPlayer = this.world.getPlayers()[i];
      this.heldKeyList.add(curPlayer.getDropKey());
      this.heldKeyList.add(curPlayer.getLeftKey());
      this.heldKeyList.add(curPlayer.getRightKey());
    }
  }

  //all keys are hardcoded rn but it'll be an easy change
  //if I wanna change them

  //key listeners
  @Override
  public void keyPressed(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());

    //handle tap keys here
    if (!this.heldKeyList.contains(key)
        && !this.tappedKeys.contains(key)) {
      this.tappedKeys.add(key);

      for (int i = 0; i < this.world.getPlayers().length; i++) {
        Hero curPlayer = this.world.getPlayers()[i];
        if (key.equals(curPlayer.getJumpKey())) {
          curPlayer.jump();
        }
        
        else if (key.equals(curPlayer.getLightAttackKey())) {
          curPlayer.lightAttack(this.world.getActiveHeldKeys(), tappedKeys);
        } else if (key.equals(curPlayer.getHeavyAttackKey())) {
          curPlayer.heavyAttack(this.world.getActiveHeldKeys(), tappedKeys);
        } else if (key.equals(curPlayer.getPickUpKey())) {
          if (curPlayer.hasItem()) {
            curPlayer.throwItem();
          } else {
            this.world.checkPickUp(curPlayer);
          }
          
        }
      }
    }
    
    //add held keys for world to handle
    if (this.heldKeyList.contains(key)) {
      this.world.addActiveHeldKey(key);
    }
    
  }

  @Override
  public void keyTyped(KeyEvent e) {
    //not used
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());

    this.world.removeActiveHeldKey(key);
    this.tappedKeys.remove(key);
    
  }
}