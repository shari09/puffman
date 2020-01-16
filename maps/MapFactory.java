package maps;

import java.io.IOException;

public class MapFactory {
  private static final String ARENA = "Arena";
  private static final String TREE = "Tree";
  private static final String MIAMI_DOME = "Miami";

  public static Map getMap(String num) throws IOException {
    if (num.equals(MapFactory.ARENA)) {
      return new ArenaMap();
    } else if (num.equals(MapFactory.TREE)) {
      return new TreeMap();
    } else if (num.equals(MapFactory.MIAMI_DOME)) {
      return new MiamiDomeMap();
    }
    return null;
  }
}