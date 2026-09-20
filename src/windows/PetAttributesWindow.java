package windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import attributes.BasicBattleAttribute;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import liveBeings.Pet;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import main.TextCategories;
import screen.Screen;
import utilities.Util;


public class PetAttributesWindow extends AttributesWindow
{
	private final Point windowPos ;
	private Pet pet ;
	private final Point userPos ;
	private final Font namefont ;
	private final Font font ;
	private final Point namePos ;
	private final Point levelPos ;
    private final Point slotCenter ;
    private final Dimension slotSize ;
    private final Point elemPos ;
	private final Point lifePos ;
	private final Point mpPos ;
	private final Point initialAttPos ;
    private final List<Point> attPos ;
	private final Point critPos ;
	
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "PetAttWindow.png") ;
	
	public PetAttributesWindow()
	{
		super(IMAGE, 1) ;
		this.windowPos = Screen.getMe().pos(0.52, 0.14) ;
	    this.userPos = Util.translate(windowPos, size.width / 2, 73) ;
	    this.namefont = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
	    this.font = new Font(Game.getMainFontName(), Font.BOLD, 11) ;
	    this.namePos = Util.translate(windowPos, size.width / 2, 18) ;
	    this.levelPos = Util.translate(windowPos, size.width / 2, 38) ;
        this.slotCenter = Util.translate(windowPos, 222, 72) ;
        this.slotSize = new Dimension(51, 51) ;
        this.elemPos = Util.translate(slotCenter, slotSize.width - 12, slotSize.height / 2) ;
	    this.lifePos = Util.translate(windowPos, 20, BORDER + PADDING + 46) ;
	    this.mpPos = Util.translate(windowPos, 20, BORDER + PADDING + 46 + 27) ;
	    this.initialAttPos = Util.translate(windowPos, BORDER + PADDING + 26, 136) ;
        this.attPos = new ArrayList<>(ATT_ICONS.length) ;
        for (int i = 0; i <= ATT_ICONS.length - 1; i += 1)
        {
            this.attPos.add(Util.translate(initialAttPos, 134 * (i / 3), (i % 3) * 22)) ;
        }
	    this.critPos = Util.translate(initialAttPos, 0, 71) ;
	}

    protected void onOpen()
    {
        this.pet = Game.getPet() ;
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
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(IMAGE, windowPos, Align.topLeft, stdOpacity) ;

		SpriteAnimation userImage = pet.getMovingAni().spriteIdle ;
		userImage.display(GamePanel.getDP(), userPos, Align.center) ;
		
		String[] attText = Game.getAllText().get(TextCategories.attributes) ;
		GamePanel.getDP().drawText(namePos, Align.center, pet.getName(), namefont, Palette.colors[0]) ;		
		GamePanel.getDP().drawText(levelPos, Align.center, attText[0] + ": " + pet.getLevel(), font, Palette.colors[7]) ;
		
		//	Equips
		if (pet.getEquip() != null)
		{
			GamePanel.getDP().drawImage(pet.getEquip().fullSizeImage(), slotCenter, Align.center) ;
			Elements eqElem = pet.getAtkElem() ;
			if (eqElem != null)
			{
				GamePanel.getDP().drawImage(eqElem.image, elemPos, new Scale(0.12, 0.12), Align.center) ;
			}
		}

		// attributes
		String lifeText = attText[1] + ": " + Util.round(pet.getPA().getLife().getCurrentValue(), 1) ;
		String mpText = attText[2] + ": " + Util.round(pet.getPA().getMp().getCurrentValue(), 1) ;
		GamePanel.getDP().drawText(lifePos, Align.centerLeft, lifeText, font, Palette.colors[7]) ;
		GamePanel.getDP().drawText(mpPos, Align.centerLeft, mpText, font, Palette.colors[20]) ;
				
		BasicBattleAttribute[] attributes = pet.getBA().basicAttributes() ;
		for (int i = 0; i <= ATT_ICONS.length - 1; i += 1)
		{
			String attValue = Util.round(attributes[i].getBaseValue(), 1) + " + " + Util.round(attributes[i].getBonus(), 1) + " + " + Util.round(attributes[i].getTrain(), 1) ;
			
			GamePanel.getDP().drawImage(ATT_ICONS[i], Util.translate(attPos.get(i), -15, 0), Scale.unit, Align.center) ;
			GamePanel.getDP().drawText(attPos.get(i), Align.centerLeft, attValue, font, Palette.colors[0]) ;
		}
		String critValue = attText[9] + ": " + Util.round(100 * pet.getBA().TotalCritAtkChance(), 1) + "%" ;
		GamePanel.getDP().drawImage(CRIT_ICON, Util.translate(initialAttPos, -15, 72), Scale.unit, Align.center) ;
		GamePanel.getDP().drawText(critPos, Align.centerLeft, critValue, font, Palette.colors[7]) ;
	}

	protected void onClose() { }
}
