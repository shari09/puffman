package world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import util.Button;
import util.ButtonListener;
import util.Util;

public class GameOverScreen extends JPanel {
  private Button[] buttons = new Button[2];
  private String action = null;
  private ButtonListener buttonListener;

  private BufferedImage background;

  public GameOverScreen() throws IOException {
    this.setPreferredSize(GameWindow.screenSize);
    this.buttons[0] = new Button(
      Util.scaleX(400), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Rematch");
    this.buttons[1] = new Button(
      Util.scaleX(1200), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Quit");

    this.background = Util.urlToImage("background/gameOver.jpg");
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

  @Override
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