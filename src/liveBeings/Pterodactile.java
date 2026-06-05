package liveBeings;

import animations.PterodactileAnimation;
import main.Game;
import main.TextCategories;


public class Pterodactile
{
  private static final String[] MESSAGE = Game.getAllText().get(TextCategories.pterodactile)  ;
  
  public static void speak()
  {
    PterodactileAnimation.start(MESSAGE);
  }
}
