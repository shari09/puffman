package characters;

import java.awt.image.*;
import java.io.IOException;

import util.*;

public class Ash extends Hero {

  public Ash(int x, int y, 
             int width, int height, 
             int hitboxRadius) throws IOException {
    super(x, y, width, height, hitboxRadius);

    BufferedImage[] faceRight = {
      Util.urlToImage("images/characters/gray.jpg")
    };
    this.setState("faceRight");
    this.addSprite("faceRight", faceRight);
  }
}