package maps;

import java.io.IOException;

/**
 * [MapFactory.java]
 * The global static class for getting different maps based on String names.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class MapFactory {
  public static final String ARENA = "Arena";
  public static final String TREE = "Tree";
  public static final String MIAMI_DOME = "Miami";
  public static final String NIGHT = "Night";
  public static final String BLUE_MAMMOTH = "Blue Mammoth";

  /**
   * Gets the requested map.
   * @param mapName the name of the map.
   * @return Map, the requested map.
   * @throws IOException
   */
  public static Map getMap(String mapName) throws IOException {
    if (mapName.equals(MapFactory.ARENA)) {
      return new ArenaMap();
    } else if (mapName.equals(MapFactory.TREE)) {
      return new TreeMap();
    } else if (mapName.equals(MapFactory.MIAMI_DOME)) {
      return new MiamiDomeMap();
    } else if (mapName.equals(MapFactory.NIGHT)) {
      return new NightMap();
    } else if (mapName.equals(MapFactory.BLUE_MAMMOTH)) {
      return new BlueMammothMap();
    }
    return null;
  }
}