package world;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
  public static final long serialVersionUID = 1L;
  public static final Dimension screenSize = Toolkit
                                             .getDefaultToolkit()
                                             .getScreenSize();
  // public static final int width = (int)screenSize.getWidth();
  // public static final int height = (int)screenSize.getHeight();
  public static int width = 1000;
  public static int height = 800;

  public GameWindow(World w) {
    this.setSize(1000, 800);
    
    // this.setSize(GameWindow.screenSize);
    // this.setUndecorated(true);

    this.setVisible(true);
    this.setResizable(false);
    this.getContentPane().add(w);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    // GraphicsEnvironment graphics = 
    //   GraphicsEnvironment.getLocalGraphicsEnvironment();
    // GraphicsDevice device = graphics.getDefaultScreenDevice();
    // device.setFullScreenWindow(this);
  }

  public void update(World world) {
    world.update();
    repaint();
  }
}