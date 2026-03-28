package animations;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import main.TextCategories;
import utilities.Util;
import windows.AttributesWindow;
import windows.BagWindow;

public class LevelUpAnimation extends Animation
{
	private static final Font smallFont = DrawPrimitives.stdFont ;
	private static final Image levelUpAttImg = ImageLoader.loadImage(Path.LIVE_BEINGS_IMG + "LevelUp.png") ;
	private static final Image winObtainedItemsImg = ImageLoader.loadImage(Path.PLAYER_IMG + "Win.png") ;
	private static final Point pos = Game.getScreen().pos(0.55, 0.2) ;
	private static final Point offset = new Point(15, 15) ;
	private static final int nRows = 4 ;
	private static final int nCols = 2 ;
	private static final int sy = smallFont.getSize() + 15 ;
	private static final int[] attOrder = new int[] {0, 2, 4, 6, 1, 3, 5, 7} ;
	private static final String[] attText = Game.allText.get(TextCategories.attributes) ;
	private static final String[] attNames = Arrays.copyOfRange(attText, 1, 9) ;

    private double[] attributeInc ;
	private int newLevel ;

	private LevelUpAnimation(double[] attributeInc, int newLevel)
    {
		super(9) ;
        this.attributeInc = attributeInc ;
        this.newLevel = newLevel ;
	}

    public static void start(double[] attributeInc, int newLevel)
    {
		LevelUpAnimation ani = new LevelUpAnimation(attributeInc, newLevel) ;
        ani.start() ;
    }

    protected void play()
    {		
		GamePanel.DP.drawImage(levelUpAttImg, pos, Scale.unit, Align.topLeft) ;
		Point textPos = Util.translate(pos, winObtainedItemsImg.getWidth(null) / 2, offset.y) ;
		
		GamePanel.DP.drawText(textPos, Align.bottomCenter, Draw.stdAngle, attText[0] + " " + newLevel + "!", smallFont, Game.palette[0]) ;

		Point topLeftSlotCenter = Util.translate(pos, 18, 35) ;
		for (int i = 0 ; i <= attOrder.length - 1 ; i += 1)
		{
			Point imagePos = Util.calcGridPos(topLeftSlotCenter, i, nRows, new Point(80, sy)) ;
			GamePanel.DP.drawImage(BagWindow.slotImage, imagePos, Align.center) ;
			GamePanel.DP.drawImage(AttributesWindow.getIcons()[attOrder[i]], imagePos, Align.center) ;
		}
		
		if (timer.rate() <= 0.2) { return ;}		
		
		for (int i = 0 ; i <= attNames.length - 1 ; i += 1)
		{
			if (timer.rate() <= 0.2 + 0.5 * i / (attNames.length - 1)) { continue ;}

			int row = i % nCols ;
			int col = i / nCols ;
			Point attTextPos = Util.translate(pos, 28 + row * 80, 40 + col * sy) ;
			GamePanel.DP.drawText(attTextPos, Align.bottomLeft, Draw.stdAngle, " + " + attributeInc[i], smallFont, Game.palette[0]) ;
		}
    }
}