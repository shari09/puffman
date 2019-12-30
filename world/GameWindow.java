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
  private World world;

  public GameWindow(World w) {
    super("Puffman");
    this.setSize(1000, 800);
    
    // this.setSize(GameWindow.screenSize);
    // this.setUndecorated(true);

    this.setResizable(false);
    this.world = w;
    this.getContentPane().add((JPanel)w);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    // GraphicsEnvironment graphics = 
    //   GraphicsEnvironment.getLocalGraphicsEnvironment();
    // GraphicsDevice device = graphics.getDefaultScreenDevice();
    // device.setFullScreenWindow(this);
    this.setVisible(true);
  }

  // public void newWorld(JPanel world) {
  //   this.remove(this.world);
  //   this.world = (World)world;
  //   this.getContentPane().add((JPanel)(this.world));
  //   this.setVisible(true);
  // }

  public void update() {
    this.world.update();
    repaint();
  }
}