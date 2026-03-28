package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import utilities.Util;

public class ObtainedItemAnimation extends Animation
{
	private static final Font smallFont = DrawPrimitives.stdFont ;
	private static final Image obtainedItemImg = ImageLoader.loadImage(Path.PLAYER_IMG + "ObtainedItem.png") ;
    private static final double speed = 60 ;

    private Point initialPos ;
    private String text ;
    private Color color ;

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
		Point pos = Util.translate(initialPos, 0, (int) (-speed * timer.rate())) ;
		GamePanel.DP.drawImage(obtainedItemImg, pos, Align.topCenter) ;
		GamePanel.DP.drawText(Util.translate(pos, 0, 0), Align.topCenter, Draw.stdAngle, "Você obteve", smallFont, color) ;
		GamePanel.DP.drawText(Util.translate(pos, 5 - obtainedItemImg.getWidth(null) / 2, 20), Align.topLeft, Draw.stdAngle, text, smallFont, color) ;
    }
}