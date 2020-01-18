package world;

import java.awt.*;
import java.io.IOException;

import javax.swing.JPanel;
import java.awt.image.*;

import util.Button;
import util.ButtonListener;
import util.Util;

/**
 * [Menu.java]
 * A menu that has buttons.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */
public class Menu extends JPanel {
  private Button[] buttons;
  private String action = "";
  private ButtonListener buttonListener;

  private BufferedImage background;

  /**
   * Constructor.
   * @param numButtons the number of buttons this menu has.
   * @param imagePath the image path for the background of the menu.
   * @throws IOException
   */
  public Menu(int numButtons, String imagePath) throws IOException {
    this.setPreferredSize(GameWindow.screenSize);
    this.buttons = new Button[numButtons];
    this.buttonListener = new ButtonListener(this.buttons);
    this.addMouseListener(this.buttonListener);
    this.addMouseMotionListener(this.buttonListener);
    this.background = Util.urlToImage(imagePath);
  }

  /**
   * Get the actions of this menu.
   * If there's nothing, the default return is "".
   * @return action String, the current action of the menu.
   */
  public String getAction() {
    return this.action;
  }

  /**
   * Make a new button and set the button properties.
   * @param index the index of the button that is trying to be set.
   * @param x the xpos.
   * @param y the ypos.
   * @param width the width.
   * @param height the height.
   * @param action the action performed by the button 
   *               and text that will show up on the button.
   */
  public void setButton(int index, 
                        int x, int y, 
                        int width, int height, 
                        String action) {
    this.buttons[index] = new Button(x, y, width, height, action);
  }

  /**
   * Reset the menu buttons to not clicked.
   */
  public void reset() {
    for (int i = 0; i < this.buttons.length; i++) {
      this.buttons[i].reset();
    }
    this.action = "";
  }

  /**
   * Draws the background.
   * @param g2d Graphics2D, the graphics control of this component.
   */
  private void drawBackground(Graphics2D g2d) {
    g2d.drawImage(this.background, 
                  0, 0,
                  GameWindow.WIDTH,
                  GameWindow.HEIGHT,
                  this);
  }

  /**
   * Decides what is painted.
   * @param Graphics g, the graphics manager.
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    this.drawBackground(g2d);
    for (int i = 0; i < this.buttons.length; i++) {
      this.buttons[i].display(g2d);
    }
  }


  /**
   * Update the button onClick checking.
   */
  public void update() {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].isClicked()
          && this.action.equals("")) {
        this.action = this.buttons[i].getAction();
      }
    }
  }
}