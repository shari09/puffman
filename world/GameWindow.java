package world;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

public class GameWindow extends JFrame {
  public static final Dimension screenSize = Toolkit
                                             .getDefaultToolkit()
                                             .getScreenSize();
  // public static final int width = (int)screenSize.getWidth();
  // public static final int height = (int)screenSize.getHeight();
  public static int width = 1000;
  public static int height = 800;
  private JPanel panel;

  public GameWindow(JPanel panel) {
    super("Puffman");
    this.setSize(1000, 800);
    
    // this.setSize(GameWindow.screenSize);
    // this.setUndecorated(true);

    this.setResizable(false);
    this.panel = panel;
    this.getContentPane().add(panel);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    // GraphicsEnvironment graphics =is);
  
    this.setVisible(true);
  }

  /**
   * switch panels - remove old panel and add new panel
   * @param panel the panel to switch to
   */
  public void switchPanel(JPanel panel) {
    if (this.panel instanceof Menu) {
      ((Menu)(this.panel)).reset();
    }
    this.getContentPane().remove(this.panel);
    this.panel = panel;
    this.getContentPane().add(panel);
    this.panel.requestFocus();
    this.setVisible(true);
  }

  public JPanel getCurPanel() {
    return this.panel;
  }

  /**
   * call the update methods of the panels currently on the frame
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