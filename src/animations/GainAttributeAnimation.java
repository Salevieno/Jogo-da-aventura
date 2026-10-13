package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.math.BigDecimal;

import graphics.Align;
import graphics.DrawPrimitives;
import main.GamePanel;
import utilities.Util;

public class GainAttributeAnimation extends Animation
{
    private final Point initialPos ;
    private final String text ;
    private final Color color ;

	private static final Font SMALL_FONT = DrawPrimitives.stdFont ;
    private static final double SPEED = 30 ;

	private GainAttributeAnimation(Point initialPos, double quantity, Color color)
    {
		super(2.0) ;
        this.initialPos = initialPos ;
        this.text = formatQuantity(quantity) ;
        this.color = color ;
	}

    private static String formatQuantity(double quantity)
    {
        String value = BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString() ;
        return quantity > 0 ? "+" + value : value ;
    }

    public static void start(Point initialPos, double quantity, Color color)
    {
		GainAttributeAnimation ani = new GainAttributeAnimation(initialPos, quantity, color) ;
        ani.start() ;
    }

    protected void play()
    {
		Point textPos = Util.translate(initialPos, 0, (int) (-SPEED * timer.rate())) ;
		GamePanel.getDP().drawText(textPos, Align.center, text, SMALL_FONT, color) ;
    }
}
