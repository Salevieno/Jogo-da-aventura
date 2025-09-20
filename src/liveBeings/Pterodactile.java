package liveBeings;

import java.awt.Image;

import graphics2.Animation;
import graphics2.AnimationTypes;
import main.Game;
import main.Path;
import main.TextCategories;


public class Pterodactile
{
  private static final Image image = Game.loadImage(Path.NPC_IMG + "Pterodactile.png") ;
  private static final Image SpeakingBubbleImage = Game.loadImage(Path.NPC_IMG + "SpeechBubble.png") ;
  private static final String[] message = Game.allText.get(TextCategories.pterodactile)  ;
  
  public static void speak()
  {
    Object[] object = new Object[] { image, SpeakingBubbleImage, message } ;
    Animation.start(AnimationTypes.pterodactile, object) ;  
  }
}
