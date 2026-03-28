package animations;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import utilities.Util;

public class PterodactileAnimation extends Animation
{
	private static final Font stdFont = DrawPrimitives.stdFont ;
    private static final Dimension screenSize = Game.getScreen().getSize() ;
  	private static final Image image = ImageLoader.loadImage(Path.NPC_IMG + "Pterodactile.png") ;
	private static final int imageWidth = image.getWidth(null) ;

    private Image pterodactile ;
    private Image speakingBubble ;
    private String[] message ;

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
		Point pos = new Point(-imageWidth / 2, (int)(0.25*screenSize.height)) ;
		
		if (timer.rate() <= 0.25)
		{
			pos.x += 4 * timer.rate() * (screenSize.width / 2 + imageWidth / 2) ;
			pos.y += screenSize.height * timer.rate() ;
		}
		else if (timer.rate() <= 0.5)
		{
			pos.x += screenSize.width / 2 + imageWidth / 2 ;
			pos.y += 0.25 * screenSize.height ;
			Draw.speech(Util.translate(pos, 0, -10), message[0], stdFont, speakingBubble, Palette.colors[19]) ;
		}
		else if (timer.rate() <= 0.75)
		{
			pos.x +=screenSize.width / 2 + imageWidth / 2 ;
			pos.y += 0.25 * screenSize.height ;
			Draw.speech(Util.translate(pos, 0, -10), message[1], stdFont, speakingBubble, Palette.colors[19]) ;
		}
		else
		{
			pos.x += (screenSize.width / 2 + imageWidth / 2) + 2 * (screenSize.width + imageWidth) * (timer.rate() - 0.75) ;
			pos.y += 0.25 * screenSize.height - 1 * screenSize.height * (timer.rate() - 0.75) ;
		}
		GamePanel.DP.drawImage(pterodactile, pos, Align.center) ;
    }
}