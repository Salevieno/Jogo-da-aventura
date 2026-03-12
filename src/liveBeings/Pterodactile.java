package liveBeings;

import java.awt.Image;

import animations.PterodactileAnimation;
import main.Game;
import main.Path;
import main.TextCategories;


public class Pterodactile
{
  private static final Image SpeakingBubbleImage = Game.loadImage(Path.NPC_IMG + "SpeechBubble.png") ;
  private static final String[] message = Game.allText.get(TextCategories.pterodactile)  ;
  
  public static void speak()
  {
    PterodactileAnimation.start(SpeakingBubbleImage, message);
  }
}
