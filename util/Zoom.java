package util;

import java.awt.Graphics2D;

import characters.Hero;
import world.GameWindow;
import world.World;

/**
 * [Zoom.java]
 * Controls the zooming of the screen based on the player positions.
 * 
 * 2020-01-17
 * @version 0.0.3
 * @author Shari Sun
 */
public class Zoom {
  //index definitions (for arrays)
  private static final int X = 0;
  private static final int Y = 1;
  private static final int WIDTH = 2;
  private static final int HEIGHT = 3;

  private static final int MIN_WIDTH = Util.scaleX(700);
  private static final int MIN_HEIGHT = 
    MIN_WIDTH*GameWindow.HEIGHT/GameWindow.WIDTH;

  /**
   * Get the section of the map for displaying
   * @param players the players to focus on when zooming
   * @return int[] the dimensions of the display section
   */
  private static int[] getDisplaySection(Hero[] players) {
    int minX = World.MAP_WIDTH;
    int maxX = 0;
    int minY = World.MAP_HEIGHT;
    int maxY = 0;
    for (int i = 0; i < players.length; i++) {
      Hero curPlayer = players[i];
      minX = Math.min(curPlayer.getX(), minX);
      maxX = Math.max(curPlayer.getX(), maxX);
      minY = Math.min(curPlayer.getY(), minY);
      maxY = Math.max(curPlayer.getY(), maxY);
    }

    //add the margin
    minX -= World.SCREEN_MARGIN_X;
    maxX += World.SCREEN_MARGIN_X; 
    minY -= World.SCREEN_MARGIN_Y; 
    maxY += World.SCREEN_MARGIN_Y;

    //scale it to match the frame ratio
    int width = maxX - minX;
    int height = maxY - minY;
    
    // expanding the width/height to match the height ratio
    // whichever one is not negative gets added
    int addValX = GameWindow.WIDTH*height/GameWindow.HEIGHT - width;
    int addValY = GameWindow.HEIGHT*width/GameWindow.WIDTH - height;
    if (addValX > 0) {
      minX -= addValX/2;
      width += addValX;
    } else {
      minY -= addValY/2;
      height += addValY;
    }

    //max zoom
    if (width < MIN_WIDTH) {
      minX -= (MIN_WIDTH-width)/2;
      minY -= (MIN_HEIGHT-height)/2;
      width = MIN_WIDTH;
      height = MIN_HEIGHT;
    }

    //prevent it from going out the map
    if (minX < 0) {
      minX = 0;
    }
    if (minY < 0) {
      minY = 0;
    }
    if (maxX > World.MAP_WIDTH) {
      minX -= maxX-World.MAP_WIDTH;
    }
    if (maxY > World.MAP_HEIGHT) {
      minY -= maxY-World.MAP_HEIGHT;
    }

    return new int[]{minX, minY, width, height};
  }


  /**
   * get the new coordinates for displaying the characters with zoom
   * @param g2d the graphics manager
   * @param players the players to focus on when zooming
   * @param pos the original position
   * @return newPos int[], the new display position 
   */
  public static int[] getDisplayPos(Graphics2D g2d, Hero[] players, int[] pos) {
    //x, y, width, height
    int[] newPos = new int[4];

    int[] boundary = getDisplaySection(players);
    int x = boundary[X];
    int y = boundary[Y];
    int width = boundary[WIDTH];
    int height = boundary[HEIGHT];

    // g2d.drawRect(x, y, width, height);

    double magX = GameWindow.WIDTH/(double)width;
    double magY = GameWindow.HEIGHT/(double)height;

    //magX*x is the translate ratio
    newPos[X] = (int)(pos[X]*magX-magX*x);  
    newPos[Y] = (int)(pos[Y]*magY-magY*y);
    newPos[WIDTH] = (int)(pos[WIDTH]*magX);
    newPos[HEIGHT] = (int)(pos[HEIGHT]*magY);

    return newPos;
  }
}