package animations;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;
import windows.AttributesWindow;
import windows.BagWindow;

public class LevelUpAnimation extends Animation
{
    private final double[] attributeInc ;
	private final int newLevel ;
	private final Point topLeftPos ;
	private final Font font ;
	private final int spacingY ;

	private static final Image LEVEL_UP_ATT_IMG = ImageLoader.loadImage(Path.LIVE_BEINGS_IMG + "LevelUp.png") ;
	private static final Image WIN_OBTAINED_ITEMS_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "Win.png") ;
	private static final Point OFFSET = new Point(15, 15) ;
	private static final int N_ROWS = 4 ;
	private static final int N_COLS = 2 ;
	private static final int[] ATT_ORDER = new int[] {0, 2, 4, 6, 1, 3, 5, 7} ;
	private static final String[] ATT_TEXT = Game.getAllText().get(TextCategories.attributes) ;
	private static final String[] ATT_NAMES = Arrays.copyOfRange(ATT_TEXT, 1, 9) ;

	private LevelUpAnimation(double[] attributeInc, int newLevel)
    {
		super(9) ;
        this.attributeInc = attributeInc ;
        this.newLevel = newLevel ;
		this.topLeftPos = Screen.getMe().pos(0.55, 0.2) ;
		this.font = DrawPrimitives.stdFont ;
		this.spacingY = font.getSize() + 15 ;
	}

    public static void start(double[] attributeInc, int newLevel)
    {
		LevelUpAnimation ani = new LevelUpAnimation(attributeInc, newLevel) ;
        ani.start() ;
    }

    protected void play()
    {		
		GamePanel.getDP().drawImage(LEVEL_UP_ATT_IMG, topLeftPos, Scale.unit, Align.topLeft) ;
		Point textPos = Util.translate(topLeftPos, WIN_OBTAINED_ITEMS_IMG.getWidth(null) / 2, OFFSET.y) ;
		
		GamePanel.getDP().drawText(textPos, Align.bottomCenter, ATT_TEXT[0] + " " + newLevel + "!", font, Palette.colors[0]) ;

		Point topLeftSlotCenter = Util.translate(topLeftPos, 18, 35) ;
		for (int i = 0 ; i <= ATT_ORDER.length - 1 ; i += 1)
		{
			Point imagePos = Util.calcGridPos(topLeftSlotCenter, i, N_ROWS, new Point(80, spacingY)) ;
			GamePanel.getDP().drawImage(BagWindow.getSlotImage(), imagePos, Align.center) ;
			GamePanel.getDP().drawImage(AttributesWindow.getIcons()[ATT_ORDER[i]], imagePos, Align.center) ;
		}
		
		if (timer.rate() <= 0.2) { return ;}		
		
		for (int i = 0 ; i <= ATT_NAMES.length - 1 ; i += 1)
		{
			if (timer.rate() <= 0.2 + 0.5 * i / (ATT_NAMES.length - 1)) { continue ;}

			int row = i % N_COLS ;
			int col = i / N_COLS ;
			Point attTextPos = Util.translate(topLeftPos, 28 + row * 80, 40 + col * spacingY) ;
			GamePanel.getDP().drawText(attTextPos, Align.bottomLeft, " + " + attributeInc[i], font, Palette.colors[0]) ;
		}
    }
}