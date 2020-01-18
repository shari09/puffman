package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * [ButtonListener.java]
 * A custom button listener that listens to clicks/hover of the button
 * and respond accordingly.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class ButtonListener extends MouseAdapter {
  private Button[] buttons;

  /**
   * Constructor.
   * @param buttons the buttons this button listener is listening for.
   */
  public ButtonListener(Button[] buttons) {
    this.buttons = buttons;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    for (int i = 0; i < this.buttons.length; i++) {
      if (this.buttons[i].inButton(e.getX(), e.getY())) {
        this.buttons[i].click();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
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