package items;


import java.io.IOException;

import blocks.RectBlock;
import weapons.Hammer;

public class ItemFactory {
  private static final int BOMB = 0;
  private static final int HORN = 1;
  private static final int SPIKE_BALL = 2;
  private static final int HAMMER = 3;
  private RectBlock[] blocks;
  

  public ItemFactory(RectBlock[] blocks) {
    this.blocks = blocks;
  }

  /**
   * determines a random available position for item to spawn on
   * given the location of the blocks in the world
   * @param width the width of the item
   * @param height the height of the item
   * @return pos int[], random [x, y] pos
   */
  private int[] randomPos(int width, int height) {
    int blockNum = (int)(Math.random()*this.blocks.length);
    int randX = (int)(Math.random()*
                      (this.blocks[blockNum].getWidth()
                      -width)
                      +this.blocks[blockNum].getX()
                      -this.blocks[blockNum].getWidth()/2);
    int[] pos = {randX, this.blocks[blockNum].getY()
                        -height
                        -this.blocks[blockNum].getHeight()/2};
    return pos;
  }

  /**
   * gets an item to spawn at a random position
   * @param num the item id
   * @return Item, the item generated
   * @throws IOException
   */
  public Item getItem(int num) throws IOException {
    if (num == ItemFactory.BOMB) {
      return new Bomb(this.randomPos(Bomb.WIDTH, Bomb.HEIGHT));
    } else if (num == ItemFactory.HORN) {
      return new Horn(this.randomPos(Horn.WIDTH, Horn.HEIGHT));
    // } else if (num == ItemFactory.MINE) {
    //   return new Mine();
    } else if (num == ItemFactory.SPIKE_BALL) {
      return new SpikeBall(this.randomPos(SpikeBall.WIDTH, 
                                          SpikeBall.HEIGHT));
    } else if (num == ItemFactory.HAMMER) {
      return new Hammer(this.randomPos(Hammer.WIDTH, Hammer.HEIGHT));
    }
    return null;
  }

  /**
   * get a random item
   * @return Item, the new random item
   * @throws IOException
   */
  public Item getRandomItem() throws IOException {
    return this.getItem((int)(Math.random()*2)+2);
  }
}