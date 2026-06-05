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
    private String[] message ;
    private final Dimension screenSize ;
	private final Point pos ;
	private final Font font ;

  	private static final Image IMAGE = ImageLoader.loadImage(Path.NPC_IMG + "Pterodactile.png") ;
	private static final int IMAGE_WIDTH = IMAGE.getWidth(null) ;

	private PterodactileAnimation(String[] message)
    {
		super(20) ;
        this.message = message ;
		this.screenSize = Screen.getMe().getSize() ;
		this.pos = new Point(-IMAGE_WIDTH / 2, (int)(0.25*screenSize.height)) ;
		this.font = DrawPrimitives.stdFont ;
	}

    public static void start(String[] message)
    {
        PterodactileAnimation ani = new PterodactileAnimation(message) ;
        ani.start() ;
    }

    protected void play()
    {
		int padding = 6 ;
		int maxTextL = 300 - padding ; // SPEAKING_BUBBLE_IMAGE.getWidth(null) - padding
		int sy = font.getSize() + 1 ;
		if (timer.rate() <= 0.25)
		{
			pos.x += 4 * timer.rate() * (screenSize.width / 2 + IMAGE_WIDTH / 2) ;
			pos.y += screenSize.height * timer.rate() ;
		}
		else if (timer.rate() <= 0.5)
		{
			pos.x += screenSize.width / 2 + IMAGE_WIDTH / 2 ;
			pos.y += 0.25 * screenSize.height ;
			Draw.fitText(Util.translate(pos, 0, -10), sy, Align.topLeft, message[0], font, maxTextL, Palette.colors[19]) ;
		}
		else if (timer.rate() <= 0.75)
		{
			pos.x +=screenSize.width / 2 + IMAGE_WIDTH / 2 ;
			pos.y += 0.25 * screenSize.height ;
			Draw.fitText(Util.translate(pos, 0, -10), sy, Align.topLeft, message[1], font, maxTextL, Palette.colors[19]) ;
		}
		else
		{
			pos.x += (screenSize.width / 2 + IMAGE_WIDTH / 2) + 2 * (screenSize.width + IMAGE_WIDTH) * (timer.rate() - 0.75) ;
			pos.y += 0.25 * screenSize.height - 1 * screenSize.height * (timer.rate() - 0.75) ;
		}
		GamePanel.getDP().drawImage(pterodactile, pos, Align.center) ;
    }
}