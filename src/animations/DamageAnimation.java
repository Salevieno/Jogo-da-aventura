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
import main.Palette;
import utilities.Util;

public class DamageAnimation extends Animation
{
    private final Point initialPos ;
    private final String text ;
    private final AtkResults atkResults ;
    private final int style ;
    private final Color color ;

	private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 15) ;
	private static final Color PHY_ATK_COLOR  = Palette.colors[6] ;
	private static final Color MAG_ATK_COLOR = Palette.colors[5] ;
	private static final int SPEED = 20 ;

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
		Color textColor = color != null ? color : (atkResults.getAtkType().equals(AtkTypes.magical) ? MAG_ATK_COLOR : PHY_ATK_COLOR) ;
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
			case 0 -> new Point(0, (int) (-SPEED - 3 * SPEED * rate  + SPEED * rate * rate)) ;
			case 1 -> new Point((int) (Math.pow(8 * rate, 2)), (int) (-SPEED - 2 * SPEED * rate)) ;
			default -> new Point(0, 0) ;
        } ;

		Point pos = Util.translate(initialPos, trajectory) ;
		GamePanel.getDP().drawBufferedText(pos, Align.center, Draw.stdAngle, text, FONT, color, Palette.colors[2], 1, opacity) ;
    }
}