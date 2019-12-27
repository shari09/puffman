package characters;

import java.awt.image.*;
import java.io.IOException;

import util.*;

public class Ash extends Hero {

  public Ash(int x, int y, 
             int width, int height, 
             int hitboxRadius,
             String leftKey, String rightKey,
             String jumpKey, String dropKey, 
             String lightAttackKey) throws IOException {
    super(x, y, width, height, hitboxRadius,
          leftKey, rightKey, jumpKey, dropKey, lightAttackKey);

    BufferedImage[] faceRight = {
      Util.urlToImage("characters/gray.jpg")
    };

    BufferedImage test = Util.urlToImage("characters/pixelGun.png");
    BufferedImage[] attack = {test, test, test};
    BufferedImage test2 = Util.urlToImage("characters/gray.jpg");
    BufferedImage[] jumpFromWall = {test2};
    BufferedImage[] idk = {test2, test};

    //the faceDir states are broken
    //it's getting overriden with onGround
    //graphic animation will be saved for the end

    this.addSprite("faceRight", idk);
    this.addSprite("onLeftWall", faceRight);
    this.addSprite("onRightWall", faceRight);
    this.addSprite("inAir", faceRight);
    this.addSprite("onGround", faceRight);
    this.addSprite("jumpFromWall", jumpFromWall);
    this.addSprite("knockedBack", jumpFromWall);

    this.addSprite("lightAttack", attack);
    this.setState("faceRight");
  }
}