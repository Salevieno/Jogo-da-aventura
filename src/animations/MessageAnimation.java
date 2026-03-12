package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.Path;
import utilities.Util;

public class MessageAnimation extends Animation
{
	private static final Font smallFont = DrawPrimitives.stdFont ;
	private static final Image messageBoxImg = Game.loadImage(Path.PLAYER_IMG + "messageBox.png") ;

    private Point pos ;
    private final String text ;
    private final Color color ;

	private MessageAnimation(Point pos, String text, Color color)
    {
		super(4) ;
		this.pos = pos ;
		this.text = text ;
		this.color = color ;
	}

    public static void start(Point pos, String text, Color color)
    {
        MessageAnimation ani = new MessageAnimation(pos, text, color) ;
        ani.start() ;
    }

    public void play()
    {
		pos = Util.translate(pos, 0, (int) (-30 * timer.rate())) ;
		GamePanel.DP.drawImage(messageBoxImg, pos, Draw.stdAngle, Scale.unit, Align.topCenter, 0.9) ;
		GamePanel.DP.drawText(Util.translate(pos, 5 - messageBoxImg.getWidth(null) / 2, 20), Align.centerLeft, Draw.stdAngle, text, smallFont, color) ;
    }
}