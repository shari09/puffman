package util;

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

    if (distX <= rect.getWidth()/2 || distY <= rect.getHeight()/2) {
      return true;
    }

    int cornerDistSq = (int)(Math.pow((distX - rect.getWidth())/2, 2)
                       + Math.pow((distY - rect.getHeight())/2, 2));
                       
    return (cornerDistSq <= Math.pow(circle.getRadius(), 2));
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
    return (distSq <= minDist);
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

  //first obj bottom, second obj top
  //these will be fixed later to be more accurate
  public static boolean top(Object first, Object second) {
    int firstX, secondX;
    if (first instanceof CircleCollidable) {
      firstX = ((CircleCollidable)first).getX();
    } else {
      firstX = ((RectCollidable)first).getX();
    }

    if (second instanceof CircleCollidable) {
      secondX = ((CircleCollidable)second).getX();
    } else {
      secondX = ((RectCollidable)second).getX();
    }

    return firstX < secondX;
  }
    
}
