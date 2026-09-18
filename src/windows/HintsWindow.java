package windows;

import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class HintsWindow extends GameWindow
{
    private final Point padding ;
    private final Point textPos ;
    private final Point textPos0 ;
    private final Point textPos1 ;
    private final Point textPos2 ;
    private final Point textPos3 ;
    private final Point textPos4 ;
    private final Point buttonsPos ;
    private final int maxTextLength ;
    private final int sy ;

	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Hints.png") ;

	public HintsWindow()
	{
		super("Dicas", Screen.getMe().pos(0.15, 0.4), IMAGE, 0, 0, 0, 0) ;
        this.padding = new Point(15, 10) ;
        this.textPos = new Point(topLeftPos.x + padding.x, topLeftPos.y + padding.y) ;
        this.textPos0 = Util.translate(topLeftPos, size.width / 2, 20);
        this.textPos1 = Util.translate(textPos, 10, size.height - 35) ;
        this.textPos2 = Util.translate(textPos, (int)(0.9 * size.width), size.height - 35);
        this.textPos3 = Util.translate(textPos, size.width / 2, size.height - 40);
        this.textPos4 = Util.translate(textPos, 0, 30) ;
        this.buttonsPos = Util.translate(topLeftPos, 0, size.height + 10) ;
        this.maxTextLength = image.getHeight(null) - padding.x ;
        this.sy = SUBTITLE_FONT.getSize() + 2 ;
	}

    protected void onOpen()
    {
        
    }

    public void act(Player player, Point mousePos)
    {
        
    }
	
	public void navigate(String action)
	{
		stdNavigation(action);
	}
	
	public void display(Point mousePos)
	{
		String[] text = Game.getAllText().get(TextCategories.hints) ;
		numberPages = text.length - 6 ;
		
		GamePanel.getDP().drawImage(image, topLeftPos, 0, Scale.unit, Align.topLeft, stdOpacity) ;		
		GamePanel.getDP().drawText(textPos0, Align.center, text[0], SUBTITLE_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(textPos1, Align.topLeft, text[1], SUBTITLE_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(textPos2, Align.topRight, text[2], SUBTITLE_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(textPos3, Align.center, text[3], SUBTITLE_FONT, Palette.colors[0]) ;
		Draw.fitText(textPos4, sy, Align.topLeft, text[page + 4], SUBTITLE_FONT, maxTextLength, Palette.colors[0]) ;
		
		drawNavigationButtons(buttonsPos, size.width, SUBTITLE_FONT, page, numberPages - 1, stdOpacity) ;
	}

	protected void onClose() { }
}
