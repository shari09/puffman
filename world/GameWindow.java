package world;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * [GameWindow.java]
 * The frame for where the entire game runs on.
 * 
 * 2020-01-17
 * @version 0.0.3
 * @author Shari Sun
 */
public class GameWindow extends JFrame {
  public static final Dimension screenSize = Toolkit
                                             .getDefaultToolkit()
                                             .getScreenSize();
  public static final int WIDTH = (int)screenSize.getWidth();
  public static final int HEIGHT = (int)screenSize.getHeight();
  // public static int width = 1000;
  // public static int height = 800;
  private JPanel panel;

  /**
   * Constructor.
   * @param panel the panel that the window is initialized with.
   */
  public GameWindow(JPanel panel) {
    super("Puffman");
    // this.setSize(1000, 800);
    
    this.setSize(GameWindow.screenSize);
    this.setUndecorated(true);

    this.setResizable(false);
    this.panel = panel;
    this.getContentPane().add(panel);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    GraphicsEnvironment graphics = GraphicsEnvironment
                                   .getLocalGraphicsEnvironment();
    GraphicsDevice device = graphics.getDefaultScreenDevice();
    device.setFullScreenWindow(this);
  
    this.setVisible(true);
  }

  /**
   * Switch panels - remove old panel and add new panel.
   * @param panel the panel to switch to.
   */
  public void switchPanel(JPanel panel) {
    //if the old panel is a menu panel, reset all its data
    if (this.panel instanceof Menu) {
      ((Menu)(this.panel)).reset();
    }
    this.getContentPane().remove(this.panel);
    this.panel = panel;
    this.getContentPane().add(panel);
    this.panel.requestFocus();
    this.setVisible(true);
  }

  /**
   * Get the current panel of the window.
   * @return JPanel, the current panel of this window.
   */
  public JPanel getCurPanel() {
    return this.panel;
  }

  /**
   * Call the update methods of the panels currently on the frame.
   */
  public void update() throws IOException {
    if (this.panel instanceof World) {
      ((World)(this.panel)).update();
    } else if (this.panel instanceof Menu) {
      ((Menu)(this.panel)).update();
    }
    
    repaint();
  }
}