package liveBeings;

import java.awt.Image;

import graphics.Animations;
import main.Game;
import utilities.UtilG;

public class Pterodactile
{
    public static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Pterodactile\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "SpeechBubble.png") ;
    public static String[] message = null  ;
    
    public static void speak(Animations ani)
    {

		Object[] object = new Object[] { 1500, image, SpeakingBubbleImage, message } ;
		ani.start(object) ;
    }
    
    public static void setMessage(String[] newMessage) { message = newMessage ;} 
}
