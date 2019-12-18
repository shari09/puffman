import javax.swing.*;
// import java.awt.event.*;
import java.awt.*;

public class GameWindow extends JFrame {
  public static final long serialVersionUID = 1L;

  public GameWindow(World w) {
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().add(w);
  }

  public void display() {
    repaint();
  }
}