package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import world.GameWindow;

/**
 * [Util.java]
 * A collection of static utility/helper methods for the program to use.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class Util {
  //development monitor resolution
  //used for scaling
  private static int DEV_WIDTH = 1920;
  private static int DEV_HEIGHT = 1080;

  /**
   * Scales the x value.
   * @param x the x value to scale.
   * @return double, the scaled value. 
   */
  public static double scaleX(double x) {
    return GameWindow.WIDTH*x / DEV_WIDTH;
  }

  /**
   * Scales the x value.
   * @param x the x value to scale.
   * @return int, the scaled value. 
   */
  public static int scaleX(int x) {
    return GameWindow.WIDTH*x / DEV_WIDTH;
  }

  /**
   * Scales the y value.
   * @param y the y value to scale.
   * @return double, the scaled value. 
   */
  public static double scaleY(double y) {
    return GameWindow.HEIGHT*y / DEV_HEIGHT;
  }

  /**
   * Scales the y value.
   * @param y the y value to scale.
   * @return int, the scaled value. 
   */
  public static int scaleY(int y) {
    return GameWindow.HEIGHT*y / DEV_HEIGHT;
  }

  /**
   * Converts the image path to a buffered image.
   * @param url the path url.
   * @return BufferedImage, the image.
   * @throws IOException
   */
  public static BufferedImage urlToImage(String url) throws IOException {
    return ImageIO.read(new File("assets/images/" + url));
  }
}