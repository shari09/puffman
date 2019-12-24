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
    } else if (key.equals("S")) {
      this.world.getPlayers()[0].dropDown();
    }

    if (key.equals("Right")) {
      this.world.getPlayers()[1].moveRight();
    } else if (key.equals("Left")) {
      this.world.getPlayers()[1].moveLeft();
    } else if (key.equals("Up")) {
      this.world.getPlayers()[1].jump();
    } else if (key.equals("Down")) {
      this.world.getPlayers()[1].dropDown();
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
    if (key.equals("Left") || key.equals("Right")) {
      // System.out.println(key);
      this.world.getPlayers()[1].resetXMovement();
    }
  }
}