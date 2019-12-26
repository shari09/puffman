package util;

import java.util.HashMap;

public class Collision {

  /**
   * checks whether if a circle and a rectangle intersects
   * @param circle CircleCollidable, the circular hitbox
   * @param rect, the rectangular hitbox
   * @return boolean, whether they intersect or not
   */
  public static boolean isCollided(CircleCollidable circle, 
                                   RectCollidable rect) {
    int distX = Math.abs(circle.getX() - rect.getX());
    int distY = Math.abs(circle.getY() - rect.getY());

    if (distX > rect.getWidth()/2 + circle.getRadius()
        || distY > rect.getHeight()/2 + circle.getRadius()) {
      return false;
    }

    if (distX < rect.getWidth()/2 || distY < rect.getHeight()/2) {
      return true;
    }

    int cornerDistSq = (int)(Math.pow((distX - rect.getWidth())/2, 2)
                       + Math.pow((distY - rect.getHeight())/2, 2));
                       
    return (cornerDistSq < Math.pow(circle.getRadius(), 2));
  } 

  /**
   * checks whether if a circle and another circle intersects
   * @param circle CircleCollidable, the first circular hitbox
   * @param circle2 CircleCollidable, the second circular hitbox
   * @return boolean, whether they intersect or not
   */
  public static boolean isCollided(CircleCollidable circle,
                                   CircleCollidable circle2) {

    int distSq = (int)(Math.pow((circle2.getX() - circle.getX())
                 + (circle2.getY() - circle.getY()), 2));

    int minDist = (int)(Math.pow(
                  (circle.getRadius() + circle2.getRadius()),
                  2));
    return (distSq < minDist);
  }


  /**
   * checks whether two rectangles intersect
   * @param rect RectCollidable, the first rectangular hitbox
   * @param rect2 RectCollidable, the second rectangular hitbox
   * @return boolean, whether they intersect or not
   */
  public static boolean isCollided(RectCollidable rect,
                                   RectCollidable rect2) {

      int distX = Math.abs(rect.getX() - rect2.getX());
      int distY = Math.abs(rect.getY() - rect2.getY());
      if (distX < rect.getWidth()/2 + rect2.getWidth()/2
          && distY < rect.getHeight()/2 + rect2.getHeight()/2) {
        return true;
      }
      return false;
  }

  /**
   * Get the bounding box of a RectCollidable object
   * @param obj the object (ex. player, weapon)
   * @return HashMap<String, Integer>, the position
   */
  private static HashMap<String, Integer> getSides(RectCollidable obj) {
    HashMap<String, Integer> position = new HashMap<>();

    position.put("x", obj.getX() - obj.getWidth()/2);
    position.put("x2", obj.getX() + obj.getWidth()/2);
    position.put("y", obj.getY() - obj.getHeight()/2);
    position.put("y2", obj.getY() + obj.getHeight()/2);

    return position;
  }

  /**
   * checks whether two rectangles are touching
   * @param rect RectCollidable, the first rectangular hitbox
   * @param rect2 RectCollidable, the second rectangular hitbox
   * @return boolean, whether they intersect or not
   */
  private static boolean isTouching(RectCollidable rect,
                                   RectCollidable rect2) {

      int distX = Math.abs(rect.getX() - rect2.getX());
      int distY = Math.abs(rect.getY() - rect2.getY());
      if (distX <= rect.getWidth()/2 + rect2.getWidth()/2
          && distY <= rect.getHeight()/2 + rect2.getHeight()/2) {
        return true;
      }
      return false;
  }

  // find where the collision is
  // used after moving the player back
  public static String getTouchingSide(RectCollidable first, 
                                       RectCollidable second) {
    if (isTouching(first, second)) {
      HashMap<String, Integer> firstPos = getSides(first);
      HashMap<String, Integer> secondPos = getSides(second); 
      if (firstPos.get("x").equals(secondPos.get("x2"))) {
        return "left";
      } else if (firstPos.get("x2").equals(secondPos.get("x"))) {
        return "right";
      } else if (firstPos.get("y").equals(secondPos.get("y2"))) {
        return "top";
      } else if (firstPos.get("y2").equals(secondPos.get("y"))) {
        return "bottom";
      }
    }
    return "none";
  }
    
}
