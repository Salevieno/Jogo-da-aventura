package liveBeings;

import java.awt.Image;

import javax.swing.ImageIcon;

import graphics.Animations;
import main.Game;

public class Pterodactile
{
    public final static Image image = new ImageIcon(Game.ImagesPath + "\\Player\\" + "Pterodactile.png").getImage() ;
    public static final Image SpeakingBubbleImage = new ImageIcon(Game.ImagesPath + "\\Player\\" + "SpeakingBubble.png").getImage() ;
    
    public static void speak(Animations ani)
    {
    	ani.SetAniVars(16, new Object[] {500, image, SpeakingBubbleImage}) ;
		ani.StartAni(16) ;
    }
}
