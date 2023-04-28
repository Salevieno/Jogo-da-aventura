package liveBeings;

import java.awt.Image;

import graphics.Animation;
import main.Game;
import utilities.UtilG;

public class Pterodactile
{
    public static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Pterodactile\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "SpeechBubble.png") ;
    public static String[] message = null  ;
    
    public static void speak(Animation ani)
    {
    	// TODO
		Object[] object = new Object[] { image, SpeakingBubbleImage, message } ;
		ani.start(1500, object) ;
		
    }
    
    public static void setMessage(String[] newMessage) { message = newMessage ;} 
}
