package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import world.GameWindow;

public class Util {
  private static int DEV_WIDTH = 1920;
  private static int DEV_HEIGHT = 1080;


  public static double scaleX(double x) {
    return GameWindow.width*x / DEV_WIDTH;
  }

  public static int scaleX(int x) {
    return GameWindow.width*x / DEV_WIDTH;
  }

  public static double scaleY(double y) {
    return GameWindow.height*y / DEV_HEIGHT;
  }

  public static int scaleY(int y) {
    return GameWindow.height*y / DEV_HEIGHT;
  }

  public static BufferedImage  
  urlToImage(String url) throws IOException {
    return ImageIO.read(new File("assets/images/" + url));
  }
}