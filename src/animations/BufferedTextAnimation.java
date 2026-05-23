package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import main.GamePanel;
import main.Palette;
import utilities.Util;

public class BufferedTextAnimation extends Animation
{
    private Point initPos ;
    private final String text ;
    private final Color color ;

	private static final Font SMALL_FONT = DrawPrimitives.stdFont ;
    private static final double SPEED = 85 ;

	private BufferedTextAnimation(Point pos, String text, Color color)
    {
		super(0.8) ;
        this.initPos = pos ;
        this.text = text ;
        this.color = color ;
	}

    public static void start(Point pos, String text, Color color)
    {
        BufferedTextAnimation ani = new BufferedTextAnimation(pos, text, color) ;
        ani.start() ;
    }

    public void play()
    {
		Point textPos = Util.translate(initPos, 0, (int) (-SPEED * timer.rate())) ;
		GamePanel.getDP().drawBufferedText(textPos, Align.center, Draw.stdAngle, text, SMALL_FONT, color, Palette.colors[3], 2);
    }
}