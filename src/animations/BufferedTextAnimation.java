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
    private static final double speed = 85 ;

    private Point initPos ;
    private final String text ;
    private final Color color ;

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
		Point textPos = Util.translate(initPos, 0, (int) (-speed * timer.rate())) ;
		GamePanel.DP.drawBufferedText(textPos, Align.center, Draw.stdAngle, text, smallFont, color, Game.palette[3], 2);
    }
}