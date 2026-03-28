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
import main.Path;
import main.TextCategories;
import utilities.Util;

public class Sign
{
	private final Point pos ;
	private final String message ;

	private static final Image image = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Sign.png") ;
	private static final Image boardImg = ImageLoader.loadImage(Path.BUILDINGS_IMG + "SignBoard.png") ;
	private static final Font msgFont = new Font(Game.MainFontName, Font.BOLD, 13) ;

	public Sign(Point pos, int cityID)
	{
		this.pos = pos ;
        this.message = Game.allText.get(TextCategories.signMessages)[cityID] ;
	}

	private boolean isInside(Point pos)
	{
		Point topLeftPos = UtilAlignment.getPosAt(this.pos, Align.center, Align.topLeft, Util.getSize(image)) ;
		return Util.isInside(pos, topLeftPos, Util.getSize(image)) ;
	}

	private void displaySignMessage()
	{
		Point boardPos = Util.translate(pos, image.getWidth(null), 0) ;
		Point messagePos = Util.translate(boardPos, 12, 5) ;
		GamePanel.DP.drawImage(boardImg, boardPos, Align.topLeft, 0.85) ;
		Draw.fitText(messagePos, msgFont.getSize() + 2, Align.topLeft, message, msgFont, 435, Game.palette[0]) ;	
	}
    
	public void display(Point playerPos)
	{
		GamePanel.DP.drawImage(image, pos, Draw.stdAngle, Scale.unit, Align.center) ;
		if (isInside(playerPos))
        {
			displaySignMessage() ;
		}
    }
}
