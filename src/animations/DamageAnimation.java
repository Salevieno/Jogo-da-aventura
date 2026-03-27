package animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import graphics.Align;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.Log;
import utilities.Util;

public class DamageAnimation extends Animation
{
	private static final Font font = new Font(Game.MainFontName, Font.BOLD, 15) ;
	private static final Color phyAtkColor  = Game.palette[6] ;
	private static final Color magAtkColor = Game.palette[5] ;
	private static final int speed = 20 ;

    private Point initialPos ;
    private final String text ;
    private final AtkResults atkResults ;
    private final int style ;
    private final Color color ;

	private DamageAnimation(Point initialPos, AtkResults atkResults, int style, Color color)
    {
		super(1.2) ;
        this.initialPos = initialPos ;
		this.text = determineText(atkResults) ;
        this.atkResults = atkResults ;
        this.style = style ;
        this.color = color ;
	}

	private static String determineText(AtkResults atkResults)
	{
		String damage = String.valueOf(Util.round(atkResults.getDamage(), 1)) ;
		return switch (atkResults.getEffect())
		{
			case miss -> "Miss" ;
			case hit -> damage ;
			case crit -> damage + "!" ;
			case block -> "Block" ;
			default -> "" ;
		} ;
	}

    public static void start(Point initialPos, AtkResults atkResults, int style, Color color)
    {
		Color textColor = color != null ? color : (atkResults.getAtkType().equals(AtkTypes.magical) ? magAtkColor : phyAtkColor) ;
		DamageAnimation ani = new DamageAnimation(initialPos, atkResults, style, textColor) ;
        ani.start() ;
    }

    protected void play()
    {
		if (AtkEffects.none.equals(atkResults.getEffect())) { Log.warn("Damage animation with effect = none, will not display") ; return ;}
		double opacity = 1.0 - timer.rate() ;
		double rate = Math.pow(timer.rate(), 0.6) ;
		Point trajectory = switch (style)
		{
			case 0 -> new Point(0, (int) (-speed - 3 * speed * rate  + speed * rate * rate)) ;
			case 1 -> new Point((int) (Math.pow(8 * rate, 2)), (int) (-speed - 2 * speed * rate)) ;
			default -> new Point(0, 0) ;
        } ;

		Point pos = Util.translate(initialPos, trajectory) ;
		GamePanel.DP.drawBufferedText(pos, Align.center, Draw.stdAngle, text, font, color, Game.palette[2], 1, opacity) ;
    }
}