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

    this.addSprite("faceRight", faceRight);
    this.addSprite("onLeftWall", faceRight);
    this.addSprite("onRightWall", faceRight);
    this.addSprite("inAir", faceRight);
    this.addSprite("onGround", faceRight);
    this.setState("inAir");
  }
}