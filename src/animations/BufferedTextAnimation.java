package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import utilities.Util;

public class BufferedTextAnimation extends Animation
{
	private static final Font smallFont = DrawPrimitives.stdFont ;

    private Point pos ;
    private final String text ;
    private final Color color ;

	private BufferedTextAnimation(Point pos, String text, Color color)
    {
		super(4) ;
        this.pos = pos ;
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
		pos = Util.translate(pos, 0, (int) (-30 * timer.rate())) ;
		GamePanel.DP.drawBufferedText(pos, Align.centerLeft, Draw.stdAngle, text, smallFont, color, Game.palette[3], 2);
    }
}