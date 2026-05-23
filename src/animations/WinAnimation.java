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

	private static final Point POS = Screen.getMe().pos(0.35, 0.2) ;
	private static final Font STD_FONT = DrawPrimitives.stdFont ;
	private static final Font SMALL_FONT = new Font(DrawPrimitives.stdFont.getName(), Font.BOLD, 11) ;
	private static final Point TEXT_POS = Util.translate(POS, 80, 11) ;
	private static final Image WIN_OBTAINED_ITEMS_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "Win.png") ;

	private WinAnimation(List<Item> items)
    {
		super(8) ;
        this.items = items ;
	}

    public static void start(List<Item> items)
    {
        WinAnimation ani = new WinAnimation(items) ;
        ani.start() ;
    }

    protected void play()
    {
		GamePanel.getDP().drawImage(WIN_OBTAINED_ITEMS_IMG, POS, Scale.unit, Align.topLeft) ;
		
		if ( timer.rate() <= 0.1 ) { return ;}
		
		GamePanel.getDP().drawText(TEXT_POS, Align.center, Draw.stdAngle, "Você obteve!", STD_FONT, Palette.colors[3]) ;
		
		if ( timer.rate() <= 0.3 ) { return ;}
		
		Point itemTextPos = Util.translate(POS, 15, 40) ;
		for (int i = 0 ; i <= items.size() - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / items.size() <= timer.rate() )
			{
				GamePanel.getDP().drawText(itemTextPos, Align.bottomLeft, Draw.stdAngle, items.get(i).getName(), SMALL_FONT, Palette.colors[3]) ;
				itemTextPos.y += 15 ;
			}
		}
    }
}
