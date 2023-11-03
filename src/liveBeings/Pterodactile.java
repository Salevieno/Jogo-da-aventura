package liveBeings;

import java.awt.Image;

import main.Game;
import utilities.UtilS;

public class Pterodactile
{
    public static final Image image = UtilS.loadImage("\\Pterodactile\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilS.loadImage("\\NPCs\\" + "SpeechBubble.png") ;
    public static String[] message = null  ;
    
    public static void speak()
    {
    	// TODO pterodactile speak
		Object[] object = new Object[] { image, SpeakingBubbleImage, message } ;
		Game.getAnimations().get(8).start(1500, object) ;
		
    }
    
    public static void setMessage(String[] newMessage) { message = newMessage ;} 
}
