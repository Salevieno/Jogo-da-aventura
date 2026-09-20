package windows;

import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class CreatureAttributesWindow extends AttributesWindow
{
	private final Point topLeftPos ;
	private final Point userPos ;
	private final Point namePos ;
	private final Point levelPos ;
	private final Point lifePos ;
	private final Point mpPos ;
	private final Point initialAttPos ;
    private final List<Point> attPos ;
	private final Point critPos ;
	private final Font namefont ;
	private final Font font ;
	public CreatureAttributesWindow()
	{
		super(ImageLoader.loadImage(Path.WINDOWS_IMG + "CreatureAttWindow.png"), 1);
		this.topLeftPos = Screen.getMe().pos(0.4, 0.2) ;
	    this.userPos = Util.translate(topLeftPos, size.width / 2, 60) ;	
	    this.namePos = Util.translate(topLeftPos, size.width / 2, 14) ;
	    this.levelPos = Util.translate(topLeftPos, size.width / 2, 30) ;
	    this.lifePos = Util.translate(topLeftPos, 20, BORDER + PADDING + 37) ;
	    this.mpPos = Util.translate(topLeftPos, 20, BORDER + PADDING + 37 + 26) ;
	    this.initialAttPos = Util.translate(topLeftPos, BORDER + PADDING + 34, 124) ;
        this.attPos = new ArrayList<>() ;
        for (int i = 0 ; i <= ATT_ICONS.length - 1 ; i += 1)
        {
            this.attPos.add(Util.translate(initialAttPos, 117 * (i / 3), (i % 3) * 22)) ;
        }
	    this.critPos = Util.translate(initialAttPos, 0, 71) ;
        this.namefont = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
        this.font = new Font(Game.getMainFontName(), Font.BOLD, 11) ;
	}

    protected void onOpen()
    {
        
    }

    public void act(Player player, Point mousePos)
    {
        
    }
    protected int itemHoveredID(Point mousePos)
    {
        return -1 ;
    }

    public void updateSelectedItemOnHover(Point mousePos)
    {
    }

	public void display(CreatureType creatureType)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Align.topLeft) ;

		SpriteAnimation userImage = creatureType.getMovingAnimations().spriteIdle ;
		userImage.display(GamePanel.getDP(), userPos, Align.center) ;

		String[] attText = Game.getAllText().get(TextCategories.attributes) ;
		GamePanel.getDP().drawText(namePos, Align.center, creatureType.getName(), namefont, Palette.colors[0]) ;		
		GamePanel.getDP().drawText(levelPos, Align.center, attText[0] + ": " + creatureType.getLevel(), font, Palette.colors[6]) ;
		
		// attributes
		String lifeText = attText[1] + ": " + Util.round(creatureType.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(creatureType.getPA().getMp().getCurrentValue(), 1) ;
		GamePanel.getDP().drawText(lifePos, Align.centerLeft, lifeText, font, Palette.colors[6]) ;
		GamePanel.getDP().drawText(mpPos, Align.centerLeft, mpText, font, Palette.colors[5]) ;
				
		BasicBattleAttribute[] attributes = creatureType.getBA().basicAttributes() ;
		for (int i = 0; i <= ATT_ICONS.length - 1; i += 1)
		{
			String attValue = Util.round(attributes[i].getBaseValue(), 1) + " + " + Util.round(attributes[i].getBonus(), 1) + " + " + Util.round(attributes[i].getTrain(), 1) ;
			
			GamePanel.getDP().drawImage(ATT_ICONS[i], Util.translate(attPos.get(i), -15, 0), Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(attPos.get(i), Align.centerLeft, attValue, font, Palette.colors[0]) ;
		}
		String critValue = attText[9] + ": " + Util.round(100 * creatureType.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.getDP().drawImage(CRIT_ICON, Util.translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(critPos, Align.centerLeft, critValue, font, Palette.colors[6]) ;
	}

	protected void onClose() { }
}
