import javax.swing.*;
// import java.awt.event.*;
// import java.awt.*;

public class GameWindow extends JFrame {

  public GameWindow(World w) {
    this.setSize(1000, 800);
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().add(w);
  }

  
}