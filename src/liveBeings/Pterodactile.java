package liveBeings;

import java.awt.Image;

import graphics2.Animation;
import graphics2.AnimationTypes;
import main.Game;
import main.TextCategories;
import utilities.UtilS;

public class Pterodactile
{
  private static final Image image = UtilS.loadImage("\\Pterodactile\\" + "Pterodactile.png") ;
  private static final Image SpeakingBubbleImage = UtilS.loadImage("\\NPCs\\" + "SpeechBubble.png") ;
  private static final String[] message = Game.allText.get(TextCategories.pterodactile)  ;
  
  public static void speak()
  {
    Object[] object = new Object[] { image, SpeakingBubbleImage, message } ;
    Animation.start(AnimationTypes.pterodactile, object) ;  
  }
}
