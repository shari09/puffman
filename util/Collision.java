package util;

/**
 * [Collision.java]
 * Has static methods for collision related methods.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class Collision {

  private static final int X = 0;
  private static final int X2 = 1;
  private static final int Y = 2;
  private static final int Y2 = 3;


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

    int distSq = (int)(Math.pow((circle2.getX() - circle.getX()), 2)
                 + Math.pow(circle2.getY() - circle.getY(), 2));

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
   * @return int[], the position
   */
  private static int[] getSides(RectCollidable obj) {
    int[] position = new int[4];

    position[X]  = obj.getX() - obj.getWidth()/2;
    position[X2] = obj.getX() + obj.getWidth()/2;
    position[Y] = obj.getY() - obj.getHeight()/2;
    position[Y2] = obj.getY() + obj.getHeight()/2;

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

  /**
   * Find which side is touching which side,
   * used after the objects are being moved back.
   * @param first the first object to check for.
   * @param second the second object to check for.
   * @return String, where the two objects are touching.
   */
  public static String getTouchingSide(RectCollidable first, 
                                       RectCollidable second) {
    if (isTouching(first, second)) {
      int[] firstPos = getSides(first);
      int[] secondPos = getSides(second); 
      if (firstPos[X] == secondPos[X2]) {
        return "left";
      } else if (firstPos[X2] == secondPos[X]) {
        return "right";
      } else if (firstPos[Y] == secondPos[Y2]) {
        return "top";
      } else if (firstPos[Y2] == secondPos[Y]) {
        return "bottom";
      }
    }
    return "none";
  }
    
}
