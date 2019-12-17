import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

import items.*;
import characters.*;
import blocks.*;

public class World extends JPanel implements KeyListener {
  public static final long serialVersionUID = 43L;
  
  private int gravity = 8;
  private int margin = 30;

  public World() {
    setPreferredSize(new Dimension(1000,800));
    addKeyListener(this);
    setFocusable(true);
    requestFocusInWindow();
  };

  private ArrayList<Item> allItems;
  private PlayableCharacter[] players = new PlayableCharacter[2];
  private Block[] blocks = new Block[7];


  /**
   * gets the gravity constant of this world
   * @return int, the world's gravity
   */
  public int getGravity() {
    return this.gravity;
  }

  //key listeners
  public void keyPressed(KeyEvent e) {
    char key = KeyEvent.getKeyText(e.getKeyCode()).charAt(0);
    System.out.println(key);
  }

  public void keyTyped(KeyEvent e) {

  }

  public void keyReleased(KeyEvent e) {
    
  }


  public void updateGame() {

  }

  private int test = 50;
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawRect(this.test, 100, 100, 100);
    repaint();
  }

}