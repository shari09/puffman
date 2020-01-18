package world;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import characters.Hero;

/**
 * [Controls.java]
 * The main key listener in the world.
 * 
 * 2020-01-17
 * @version 0.0.3
 * @author Shari Sun 
 */
public class Controls implements KeyListener {
  private World world;

  //stores all the tapped/held keys for simutaneously using multiple keys at once
  private HashSet<String> tappedKeys = new HashSet<String>();  
  private HashSet<String> heldKeyList = new HashSet<String>();
  
  /**
   * Constructor.
   * @param world the world that's binded to this key listener.
   */
  public Controls(World world) {
    this.world = world;
    for (int i = 0; i < this.world.getPlayers().length; i++) {
      Hero curPlayer = this.world.getPlayers()[i];
      this.heldKeyList.add(curPlayer.getDropKey());
      this.heldKeyList.add(curPlayer.getLeftKey());
      this.heldKeyList.add(curPlayer.getRightKey());
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void keyPressed(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());

    //handle tap keys here
    if (!this.heldKeyList.contains(key)
        && !this.tappedKeys.contains(key)) {
      this.tappedKeys.add(key);

      for (int i = 0; i < this.world.getPlayers().length; i++) {
        Hero curPlayer = this.world.getPlayers()[i];
        
        if (!curPlayer.inSpecialState()) {
          //jump key
          if (key.equals(curPlayer.getJumpKey())) {
            curPlayer.jump();
            //light attack key
          } else if (key.equals(curPlayer.getLightAttackKey())) {
            if (curPlayer.hasGadget()) {
              this.world.useGadget(curPlayer);
            } else {
              curPlayer.lightAttack(this.world.getActiveHeldKeys(), tappedKeys);
            }
            //heavy attack key
          } else if (key.equals(curPlayer.getHeavyAttackKey())) {
            if (!curPlayer.hasGadget()) {
              curPlayer.heavyAttack(this.world.getActiveHeldKeys(), tappedKeys);
            }
            //pick up/throw key
          } else if (key.equals(curPlayer.getPickUpKey())) {
            if (curPlayer.hasItem()) {
              curPlayer.throwItem();
            } else {
              this.world.checkPickUp(curPlayer);
            }
            //dodge key
          } else if (key.equals(curPlayer.getDodgeKey()) 
                     && !curPlayer.inDodgeCoolDown()) {
            curPlayer.dodge();
          }
        }
        
      }
    }
    
    //add held keys for world to handle
    if (this.heldKeyList.contains(key)) {
      this.world.addActiveHeldKey(key);
    }
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keyTyped(KeyEvent e) {
    //not used
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void keyReleased(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());

    this.world.removeActiveHeldKey(key);
    this.tappedKeys.remove(key);
    
  }
}