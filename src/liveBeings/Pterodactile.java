package liveBeings;

import java.awt.Image;

import graphics2.Animation;
import graphics2.AnimationTypes;
import main.Game;
import main.TextCategories;
import utilities.UtilS;

public class Pterodactile
{
    public static final Image image = UtilS.loadImage("\\Pterodactile\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilS.loadImage("\\NPCs\\" + "SpeechBubble.png") ;
    public static String[] message = Game.allText.get(TextCategories.pterodactile)  ;
    
    public static void speak()
    {
		Object[] object = new Object[] { image, SpeakingBubbleImage, message } ;
		Animation.start(AnimationTypes.pterodactile, object);
		
    }
    
    public static void setMessage(String[] newMessage) { message = newMessage ;} 
}
