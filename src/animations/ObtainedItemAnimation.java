package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import utilities.Util;

public class ObtainedItemAnimation extends Animation
{
    private Point initialPos ;
    private String text ;
    private Color color ;

	private static final Font SMALL_FONT = DrawPrimitives.stdFont ;
	private static final Image OBTAINED_ITEM_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "ObtainedItem.png") ;
    private static final double SPEED = 60 ;

	private ObtainedItemAnimation(Point pos, String text, Color color)
    {
		super(2) ;
        this.initialPos = pos ;
        this.text = text ;
        this.color = color ;
	}

    public static void start(Point pos, String text, Color color)
    {
		ObtainedItemAnimation ani = new ObtainedItemAnimation(pos, text, color) ;
        ani.start() ;
    }

    protected void play()
    {
		Point pos = Util.translate(initialPos, 0, (int) (-SPEED * timer.rate())) ;
		GamePanel.getDP().drawImage(OBTAINED_ITEM_IMG, pos, Align.topCenter) ;
		GamePanel.getDP().drawText(Util.translate(pos, 0, 0), Align.topCenter, "Você obteve", SMALL_FONT, color) ;
		GamePanel.getDP().drawText(Util.translate(pos, 5 - OBTAINED_ITEM_IMG.getWidth(null) / 2, 20), Align.topLeft, text, SMALL_FONT, color) ;
    }
}