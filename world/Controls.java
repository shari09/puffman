package world;

import java.awt.event.*;

public class Controls implements KeyListener {
  private World world;

  public Controls(World world) {
    this.world = world;
  }

  //key listeners
  public void keyPressed(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());
    // System.out.println(key);
    if (key.equals("D")) {
      this.world.getPlayers()[0].moveRight();
    } else if (key.equals("A")) {
      this.world.getPlayers()[0].moveLeft();
    }
    
  }

  public void keyTyped(KeyEvent e) {
    // char key = e.getKeyChar();
    // // System.out.println(key);
    // if (key == 'd') {
    //   this.players[0].moveRight();
    // } else if (key == 'a') {
    //   this.players[0].moveLeft();
    // }
  }

  public void keyReleased(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());
    // System.out.println(key);
    if (key.equals("D") || key.equals("A")) {
      // System.out.println(key);
      this.world.getPlayers()[0].resetXMovement();
    }
  }
}