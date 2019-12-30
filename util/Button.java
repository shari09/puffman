package util;

import java.awt.*;


public class Button {
  public static final Color BLUE = new Color(92, 156, 255);
  
  private String text;
  private int x;
  private int y;
  private int width;
  private int height;

  public Button(int x, int y, int width, int height, String text) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height= height;
    this.text = text;  
  }

  public void display(Graphics2D g2d, boolean hover) {
    if (hover) {
      g2d.setStroke(new BasicStroke(2));
      g2d.setColor(Button.BLUE);
    } else {

    }
    
  }
}