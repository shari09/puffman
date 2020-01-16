package world;

import java.awt.*;
import java.io.IOException;

import javax.swing.JPanel;
import java.awt.image.*;

import util.Button;
import util.ButtonListener;
import util.Util;

public class Menu extends JPanel {
  private Button[] buttons;
  private String action;
  private ButtonListener buttonListener;

  private BufferedImage background;

  public Menu(int numButtons, String imagePath) throws IOException {
    this.setPreferredSize(GameWindow.screenSize);
    this.buttons = new Button[numButtons];
    this.buttonListener = new ButtonListener(this.buttons);
    this.addMouseListener(this.buttonListener);
    this.addMouseMotionListener(this.buttonListener);
    this.background = Util.urlToImage(imagePath);
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

  /**
   * make a new button and set the button properties
   * @param index the index of the button that is trying to be set
   * @param x the xpos
   * @param y the ypos
   * @param width the width
   * @param height the height
   * @param action the action performed by the button 
   *               and text that will show up on the button
   */
  public void setButton(int index, 
                        int x, int y, 
                        int width, int height, 
                        String action) {
    this.buttons[index] = new Button(x, y, width, height, action);
  }

  /**
   * draws the background
   * @param g2d Graphics2D, the graphics control of this component
   */
  private void drawBackground(Graphics2D g2d) {
    g2d.drawImage(this.background, 
                  0, 0,
                  GameWindow.width,
                  GameWindow.height,
                  this);
  }

  /**
   * decides what is painted
   * @param Graphics g, the graphics manager
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    this.drawBackground(g2d);
    for (int i = 0; i < this.buttons.length; i++) {
      this.buttons[i].display(g2d);
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