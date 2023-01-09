package liveBeings;

import java.awt.Image;

import javax.swing.ImageIcon;

import graphics.Animations;
import main.Game;
import utilities.UtilG;

public class Pterodactile
{
    public final static Image image = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Pterodactile.png") ;
    public static final Image SpeakingBubbleImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "SpeakingBubble.png") ;
    
    public static void speak(Animations ani)
    {
    	ani.SetAniVars(16, new Object[] {500, image, SpeakingBubbleImage}) ;
		ani.StartAni(16) ;
    }
}
