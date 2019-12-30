package world;

import javax.swing.*;
import util.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class GameOverScreen extends JPanel {
  private Button[] buttons = new Button[2];
  private String action = null;
  private ButtonListener buttonListener;

  public GameOverScreen() {
    this.setPreferredSize(GameWindow.screenSize);
    this.buttons[0] = new Button(
      Util.scaleX(400), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Rematch");
    this.buttons[1] = new Button(
      Util.scaleX(1200), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Quit");

    this.setBackground(Color.BLACK);
    this.buttonListener = new ButtonListener(this.buttons);
    this.addMouseListener(this.buttonListener);
    this.addMouseMotionListener(this.buttonListener);
  }

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

  public void update() {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].isClicked()
          && this.action == null) {
        this.action = this.buttons[i].getAction();
      }
    }
  }

}