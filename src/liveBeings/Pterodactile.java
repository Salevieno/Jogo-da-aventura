package liveBeings;

import java.awt.Image;

import graphics.Animations;
import main.Game;
import utilities.UtilG;

public class Pterodactile
{
    public static final Image image = UtilG.loadImage(Game.ImagesPath + "\\Pterodactile\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilG.loadImage(Game.ImagesPath + "\\NPCs\\" + "SpeakingBubble.png") ;
    public static String[] message = null  ;
    
    public static void speak(Animations ani)
    {
    	ani.SetAniVars(16, new Object[] {1500, image, SpeakingBubbleImage, message}) ;
		ani.StartAni(16) ;
    }
    
    public static void setMessage(String[] newMessage) { message = newMessage ;} 
}
