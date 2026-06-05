package Buildings;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import utilities.Util;

public class Sign
{
	private final Point pos ;
	private final String message ;

	private static final Image IMAGE = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Sign.png") ;
	private static final Image BOARD_IMAGE = ImageLoader.loadImage(Path.BUILDINGS_IMG + "SignBoard.png") ;
	private static final Font MSG_FONT = new Font(Game.getMainFontName(), Font.BOLD, 13) ;

	public Sign(Point pos, int cityID)
	{
		this.pos = pos ;
        this.message = Game.getAllText().get(TextCategories.signMessages)[cityID] ;
	}

	private boolean isInside(Point pos)
	{
		Point topLeftPos = UtilAlignment.getPosAt(this.pos, Align.center, Align.topLeft, Util.getSize(IMAGE)) ;
		return Util.isInside(pos, topLeftPos, Util.getSize(IMAGE)) ;
	}

	private void displaySignMessage()
	{
		Point boardPos = Util.translate(pos, IMAGE.getWidth(null), 0) ;
		int paddingX = 12 ;
		int paddingY = 5 ;
		Point messagePos = Util.translate(boardPos, paddingX, paddingY) ;
		int maxTextLength = BOARD_IMAGE.getWidth(null) - paddingX ;
		GamePanel.getDP().drawImage(BOARD_IMAGE, boardPos, Align.topLeft, 0.85) ;
		Draw.fitText(messagePos, MSG_FONT.getSize() + 2, Align.topLeft, message, MSG_FONT, maxTextLength, Palette.colors[0]) ;	
	}
    
	public void display(Point playerPos)
	{
		GamePanel.getDP().drawImage(IMAGE, pos, Scale.unit, Align.center) ;
		if (isInside(playerPos))
        {
			displaySignMessage() ;
		}
    }
}
