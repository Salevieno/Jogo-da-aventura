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
import main.Game;
import main.GamePanel;
import main.Path;
import utilities.Util;

public class WinAnimation extends Animation
{
	private static final Font stdFont = DrawPrimitives.stdFont ;
	private static final Font smallFont = new Font(DrawPrimitives.stdFont.getName(), Font.BOLD, 11) ;
	private static final Image winObtainedItemsImg = Game.loadImage(Path.PLAYER_IMG + "Win.png") ;
	private static final Point pos = Game.getScreen().pos(0.35, 0.2) ;
	private static final Point textPos = Util.translate(pos, 80, 11) ;

    private List<Item> items ;

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
		GamePanel.DP.drawImage(winObtainedItemsImg, pos, Scale.unit, Align.topLeft) ;
		
		if ( timer.rate() <= 0.1 ) { return ;}
		
		GamePanel.DP.drawText(textPos, Align.center, Draw.stdAngle, "Você obteve!", stdFont, Game.palette[3]) ;
		
		if ( timer.rate() <= 0.3 ) { return ;}
		
		Point itemTextPos = Util.translate(pos, 15, 40) ;
		for (int i = 0 ; i <= items.size() - 1 ; i += 1)
		{
			if ( 0.3 + 0.5 * i / items.size() <= timer.rate() )
			{
				GamePanel.DP.drawText(itemTextPos, Align.bottomLeft, Draw.stdAngle, items.get(i).getName(), smallFont, Game.palette[3]) ;
				itemTextPos.y += 15 ;
			}
		}
    }
}
