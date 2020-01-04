package world;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

import util.*;

public class StartScreen extends JPanel {
  private Button[] buttons = new Button[2];
  private String action = null;
  private ButtonListener buttonListener;

  public StartScreen() {
    this.setPreferredSize(GameWindow.screenSize);
    this.buttons[0] = new Button(
      Util.scaleX(650), Util.scaleY(600),
      Util.scaleX(500), Util.scaleY(80), "Start");
    this.buttons[1] = new Button(
      Util.scaleX(650), Util.scaleY(750),
      Util.scaleX(500), Util.scaleY(80), "Quit");

    this.setBackground(Color.BLACK);
    this.buttonListener = new ButtonListener(this.buttons);
    this.addMouseListener(this.buttonListener);
    this.addMouseMotionListener(this.buttonListener);
  }

  /**
   * get the actions of this menu
   * null - nothing
   * rematch - rematch button clicked
   * quit - quit button clicked
   * @return action String, the current action of the menu
   */
  public String getAction() {
    return this.action;
  }

  // public void reset() {
  //   this.action = null;
  // }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < this.buttons.length; i++) {
      this.buttons[i].display((Graphics2D)g);
    }
  }

  /**
   * update the button onClick checking
   */
  public void update() {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].isClicked()
          && this.action == null) {
        this.action = this.buttons[i].getAction();
      }
    }
  }

}