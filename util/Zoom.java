package util;

import characters.Hero;
import world.*;

import java.awt.*;

//me is dumb with basic math and spent forever on this :// 
public class Zoom {
  private static final int X = 0;
  private static final int Y = 1;
  private static final int WIDTH = 2;
  private static final int HEIGHT = 3;

  private static final int minWidth = Util.scaleX(700);
  private static final int minHeight = 
    minWidth*GameWindow.height/GameWindow.width;

  /**
   * Get the section of the map for displaying
   * @param players the players to focus on when zooming
   * @return int[] the dimensions of the display section
   */
  private static int[] getDisplaySection(Hero[] players) {
    int minX = World.mapWidth;
    int maxX = 0;
    int minY = World.mapHeight;
    int maxY = 0;
    for (int i = 0; i < players.length; i++) {
      Hero curPlayer = players[i];
      minX = Math.min(curPlayer.getX(), minX);
      maxX = Math.max(curPlayer.getX(), maxX);
      minY = Math.min(curPlayer.getY(), minY);
      maxY = Math.max(curPlayer.getY(), maxY);
    }

    //add the margin
    minX -= World.screenMarginX;
    maxX += World.screenMarginX; 
    minY -= World.screenMarginY; 
    maxY += World.screenMarginY;

    //scale it to match the frame ratio
    int width = maxX - minX;
    int height = maxY - minY;
    
    // expanding the width/height to match the height ratio
    // whichever one is not negative gets added
    int addValX = GameWindow.width*height/GameWindow.height - width;
    int addValY = GameWindow.height*width/GameWindow.width - height;
    if (addValX > 0) {
      minX -= addValX/2;
      width += addValX;
    } else {
      minY -= addValY/2;
      height += addValY;
    }

    //max zoom
    if (width < minWidth) {
      minX -= (minWidth-width)/2;
      minY -= (minHeight-height)/2;
      width = minWidth;
      height = minHeight;
    }

    //prevent it from going out the map
    if (minX < 0) {
      minX = 0;
    }
    if (minY < 0) {
      minY = 0;
    }
    if (maxX > World.mapWidth) {
      minX -= maxX-World.mapWidth;
    }
    if (maxY > World.mapHeight) {
      minY -= maxY-World.mapHeight;
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

    double magX = GameWindow.width/(double)width;
    double magY = GameWindow.height/(double)height;

    //magX*x is the translate ratio
    newPos[X] = (int)(pos[X]*magX-magX*x);  
    newPos[Y] = (int)(pos[Y]*magY-magY*y);
    newPos[WIDTH] = (int)(pos[WIDTH]*magX);
    newPos[HEIGHT] = (int)(pos[HEIGHT]*magY);

    return newPos;
  }
}