import java.io.IOException;
// import java.lang.System.*;
// import javax.swing.*;
// import java.awt.event.*;
// import java.awt.*;

public class GameLoop {
  public static void main(String[] args) throws IOException {
    int fps = 60;
    int timePerFrame = 1000/fps;

    World world = new World();
    GameWindow window = new GameWindow(world);


    while (true) {
      window.display();

      //decreases cpu intensity
      try {Thread.sleep(timePerFrame);} catch (Exception e) {}

    }
  }
}