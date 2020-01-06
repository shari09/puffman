package characters;

import java.awt.image.*;
import java.io.IOException;

import util.*;
import weapons.*;

public class AshCopy extends Hero {

  public AshCopy(int x, int y, 
             int width, int height, 
             int hitboxRadius,
             String leftKey, String rightKey,
             String jumpKey, String dropKey, 
             String lightAttackKey, String heavyAttackKey,
             String pickupKey) throws IOException {
    super(x, y, width, height, hitboxRadius,
          leftKey, rightKey, jumpKey, dropKey, lightAttackKey,
          heavyAttackKey, pickupKey,
          WeaponFactory.getWeapon("fist"));

    BufferedImage[] faceRight = {
      Util.urlToImage("characters/orange.png")
    };

    BufferedImage test = Util.urlToImage("characters/pixelGun.png");
    BufferedImage[] attack = {test, test, test};
    BufferedImage test2 = Util.urlToImage("characters/orange.png");
    BufferedImage mrorange = Util.urlToImage("characters/mrorange.jpg");
    BufferedImage[] knockback = {mrorange};
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
    this.addSprite("drop", faceRight);
    this.addSprite("jumpFromWall", jumpFromWall);
    this.addSprite("knockedBack", knockback);

    this.addSprite("lightLeft", attack);
    this.addSprite("lightRight", attack);
    this.addSprite("lightNLeft", attack);
    this.addSprite("lightNRight", attack);
    this.addSprite("lightJump", attack);
    this.addSprite("lightDLeft", attack);
    this.addSprite("lightDRight", attack);
    this.addSprite("lightNLair", attack);
    this.addSprite("lightNRair", attack);
    this.addSprite("lightSLair", attack);
    this.addSprite("lightSRair", attack);

    this.addSprite("heavyLeft", attack);
    this.addSprite("heavyRight", attack);
    this.addSprite("heavyNLeft", attack);
    this.addSprite("heavyNRight", attack);
    this.addSprite("heavyJump", attack);
    this.addSprite("heavyDown", attack);
    this.addSprite("heavyNLair", attack);
    this.addSprite("heavyNRair", attack);
    this.addSprite("heavySLair", attack);
    this.addSprite("heavySRair", attack);

    this.setState("faceRight");
  }
}