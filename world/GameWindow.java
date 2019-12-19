package world;

import javax.swing.*;
// import java.awt.event.*;
import java.awt.*;

public class GameWindow extends JFrame {
  public static final long serialVersionUID = 1L;
  public static final Dimension screenSize = Toolkit
                                             .getDefaultToolkit()
                                             .getScreenSize();
  public static final int width = (int)screenSize.getWidth();
  public static final int height = (int)screenSize.getHeight();

  public GameWindow(World w) {
    this.setSize(GameWindow.screenSize);
    // this.setExtendedState(Frame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setVisible(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().add(w);
  }

  public void display() {
    repaint();
  }
}