package util;

import java.awt.*;


public class Button {
  private static final Color BLUE = new Color(92, 156, 255);
  private static final Color TAN = new Color(220, 220, 220);

  private String text;
  private int x;
  private int y;
  private int width;
  private int height;
  private boolean hover;
  private boolean clicked;

  public Button(int x, int y, int width, int height, String text) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.text = text;
  }

  public boolean inButton(int x, int y) {
    if (this.x < x
        && x < this.x+this.width
        && this.y < y
        && y < this.y+this.height) {
      return true;
    }
    return false;
  }

  public void setHover(boolean hover) {
    this.hover = hover;
  }

  public void click() {
    this.clicked = true;
  }

  public boolean isClicked() {
    return this.clicked;
  }

  public String getAction() {
    return this.text;
  }

  public void display(Graphics2D g2d) {
    if (this.hover) {
      g2d.setColor(Button.BLUE);
      g2d.drawRect(this.x, this.y, this.width, this.height);
    } else {
      g2d.setColor(Button.TAN);
    }
    Font font = new Font("Helvetica", Font.PLAIN, this.width/6);
    g2d.setFont(font);
    FontMetrics metrics = g2d.getFontMetrics(font);
    int x = this.x+(int)((this.width-metrics.stringWidth(this.text))/2.5);
    int y = this.y+(this.height-metrics.getHeight())/2+metrics.getAscent();
    g2d.drawString(this.text.toUpperCase(), x, y);
  }

}