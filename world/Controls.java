package world;

import java.awt.event.*;

public class Controls implements KeyListener {
  private World world;

  public Controls(World world) {
    this.world = world;
  }

  //key listeners
  @Override
  public void keyPressed(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());
    // System.out.println(key);
    if (key.equals("D")) {
      this.world.getPlayers()[0].moveRight();
    } else if (key.equals("A")) {
      this.world.getPlayers()[0].moveLeft();
    } else if (key.equals("Space")) {
      this.world.getPlayers()[0].jump();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());
    // System.out.println(key);
    if (key.equals("D") || key.equals("A")) {
      // System.out.println(key);
      this.world.getPlayers()[0].resetXMovement();
    }
  }
}