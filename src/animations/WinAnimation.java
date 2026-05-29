package animations;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Draw;
import items.Item;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;

public class WinAnimation extends Animation
{
    private List<Item> items ;
	private final Point topLeftPos ;
	private final Font stdFont ;
	private final Font smallFont ;
	private final Point textPos ;

	private static final Image WIN_OBTAINED_ITEMS_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "Win.png") ;

	private WinAnimation(List<Item> items)
    {
		super(8) ;
        this.items = items ;
		this.topLeftPos = Screen.getMe().pos(0.35, 0.2) ;
		this.textPos = Util.translate(topLeftPos, 80, 11) ;
		this.stdFont = DrawPrimitives.stdFont ;
		this.smallFont = new Font(DrawPrimitives.stdFont.getName(), Font.BOLD, 11) ;
	}

    public static void start(List<Item> items)
    {
        WinAnimation ani = new WinAnimation(items) ;
        ani.start() ;
    }

    protected void play()
    {
		GamePanel.getDP().drawImage(WIN_OBTAINED_ITEMS_IMG, topLeftPos, Scale.unit, Align.topLeft) ;
		
		if ( timer.rate() <= 0.1 ) { return ;}
		
		GamePanel.getDP().drawText(textPos, Align.center, Draw.stdAngle, "Você obteve!", stdFont, Palette.colors[3]) ;
		
		if ( timer.rate() <= 0.3 ) { return ;}
		
		Point itemTextPos = Util.translate(topLeftPos, 15, 40) ;
		for (int i = 0 ; i <= items.size() - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / items.size() <= timer.rate() )
			{
				GamePanel.getDP().drawText(itemTextPos, Align.bottomLeft, Draw.stdAngle, items.get(i).getName(), smallFont, Palette.colors[3]) ;
				itemTextPos.y += 15 ;
			}
		}
    }
}
