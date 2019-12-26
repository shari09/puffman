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
      Util.urlToImage("assets/images/characters/gray.jpg")
    };

    BufferedImage test = Util.urlToImage("assets/images/characters/pixelGun.png");
    BufferedImage[] attack = {test, test, test};
    BufferedImage test2 = Util.urlToImage("assets/images/characters/gray.jpg");
    BufferedImage[] jumpFromWall = {test2};
    // BufferedImage[] knockedBack = {test2, test2};

    this.addSprite("faceRight", faceRight);
    this.addSprite("onLeftWall", faceRight);
    this.addSprite("onRightWall", faceRight);
    this.addSprite("inAir", faceRight);
    this.addSprite("onGround", faceRight);
    this.addSprite("jumpFromWall", jumpFromWall);
    this.addSprite("lightAttack", faceRight);
    this.addSprite("knockedBack", jumpFromWall);

    this.addSprite("lightAttack", attack);
    this.setState("inAir");
  }
}