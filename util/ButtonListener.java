package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonListener extends MouseAdapter {
  private Button[] buttons;

  public ButtonListener(Button[] buttons) {
    this.buttons = buttons;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].inButton(e.getX(), e.getY())) {
        this.buttons[i].click();
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].inButton(e.getX(), e.getY())) {
        this.buttons[i].setHover(true);
      } else {
        this.buttons[i].setHover(false);
      }
    }
  }



}