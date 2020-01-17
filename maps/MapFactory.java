package maps;

import java.io.IOException;

public class MapFactory {
  public static final String ARENA = "Arena";
  public static final String TREE = "Tree";
  public static final String MIAMI_DOME = "Miami";
  public static final String NIGHT = "Night";
  public static final String BLUE_MAMMOTH = "Blue Mammoth";

  public static Map getMap(String num) throws IOException {
    if (num.equals(MapFactory.ARENA)) {
      return new ArenaMap();
    } else if (num.equals(MapFactory.TREE)) {
      return new TreeMap();
    } else if (num.equals(MapFactory.MIAMI_DOME)) {
      return new MiamiDomeMap();
    } else if (num.equals(MapFactory.NIGHT)) {
      return new NightMap();
    } else if (num.equals(MapFactory.BLUE_MAMMOTH)) {
      return new BlueMammothMap();
    }
    return null;
  }
}