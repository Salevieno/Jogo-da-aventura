package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Draw;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import utilities.Util;

public class MessageAnimation extends Animation
{
    private Point initialPos ;
    private final String text ;
    private final Color color ;

	private static final Font SMALL_FONT = DrawPrimitives.stdFont ;
	private static final Image MESSAGE_BOX_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "messageBox.png") ;
    private static final int MESSAGE_BOX_WIDTH = Util.getSize(MESSAGE_BOX_IMG).width ;
    private static final double SPEED = 60 ;

	private MessageAnimation(Point pos, String text, Color color)
    {
		super(3) ;
		this.initialPos = pos ;
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
		Point pos = Util.translate(initialPos, 0, (int) (-SPEED * timer.rate())) ;
		GamePanel.getDP().drawImage(MESSAGE_BOX_IMG, pos, Scale.unit, Align.topCenter, 0.9) ;
		Draw.fitText(Util.translate(pos, 5 - MESSAGE_BOX_IMG.getWidth(null) / 2, 10), Align.centerLeft, text, SMALL_FONT, MESSAGE_BOX_WIDTH - 10, color) ;
    }
}