package animations;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;

public class PterodactileAnimation extends Animation
{
    private Image pterodactile ;
    private Image speakingBubble ;
    private String[] message ;

	private static final Font STD_FONT = DrawPrimitives.stdFont ;
    private static final Dimension SCREEN_SIZE = Screen.getMe().getSize() ;
  	private static final Image IMAGE = ImageLoader.loadImage(Path.NPC_IMG + "Pterodactile.png") ;
	private static final int IMAGE_WIDTH = IMAGE.getWidth(null) ;

	private PterodactileAnimation(Image speakingBubble, String[] message)
    {
		super(20) ;
        this.speakingBubble = speakingBubble ;
        this.message = message ;
	}

    public static void start(Image speakingBubble, String[] message)
    {
        PterodactileAnimation ani = new PterodactileAnimation(speakingBubble, message) ;
        ani.start() ;
    }

    protected void play()
    {
		Point pos = new Point(-IMAGE_WIDTH / 2, (int)(0.25*SCREEN_SIZE.height)) ;
		
		if (timer.rate() <= 0.25)
		{
			pos.x += 4 * timer.rate() * (SCREEN_SIZE.width / 2 + IMAGE_WIDTH / 2) ;
			pos.y += SCREEN_SIZE.height * timer.rate() ;
		}
		else if (timer.rate() <= 0.5)
		{
			pos.x += SCREEN_SIZE.width / 2 + IMAGE_WIDTH / 2 ;
			pos.y += 0.25 * SCREEN_SIZE.height ;
			Draw.speech(Util.translate(pos, 0, -10), message[0], STD_FONT, speakingBubble, Palette.colors[19]) ;
		}
		else if (timer.rate() <= 0.75)
		{
			pos.x +=SCREEN_SIZE.width / 2 + IMAGE_WIDTH / 2 ;
			pos.y += 0.25 * SCREEN_SIZE.height ;
			Draw.speech(Util.translate(pos, 0, -10), message[1], STD_FONT, speakingBubble, Palette.colors[19]) ;
		}
		else
		{
			pos.x += (SCREEN_SIZE.width / 2 + IMAGE_WIDTH / 2) + 2 * (SCREEN_SIZE.width + IMAGE_WIDTH) * (timer.rate() - 0.75) ;
			pos.y += 0.25 * SCREEN_SIZE.height - 1 * SCREEN_SIZE.height * (timer.rate() - 0.75) ;
		}
		GamePanel.getDP().drawImage(pterodactile, pos, Align.center) ;
    }
}