package windows;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;

public class CreatureInfoWindow extends GameWindow
{
	private final Font nameFont ;
    private final Font infoFont ;
    private final Point creaturePos ;
    private final Point creatureNametextPos ;
    private final List<Point> creatureInfotextPos ;
	private List<String> selectedCreatureTypeInfo ;
	private String[] creatureInfoText ;
    private CreatureType creatureType ;

	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "CreatureInfoWindow.png") ;

    public CreatureInfoWindow()
    {
		super("Creature info", Screen.getMe().pos(0.48, 0.3), IMAGE, 0, 0, 0, 0) ;
        this.nameFont = new Font(Game.getMainFontName(), Font.BOLD, 15) ;
        this.infoFont = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
        this.creaturePos = Util.translate(topLeftPos, 88, 64) ;
        this.creatureNametextPos = Util.translate(topLeftPos, 88, 16) ;
        this.creatureInfotextPos = new ArrayList<>() ;
        this.selectedCreatureTypeInfo = new ArrayList<>() ;
        this.creatureInfoText = new String[] {"Bestiário",
                                                "Nível",
                                                "Vida",
                                                "Exp",
                                                "Ouro",
                                                "Itens"} ;  // Game.getAllText().get(TextCategories.bestiary) ;
        this.creatureType = null ;
    }
    protected void onOpen()
    {
    }

    protected void setCreatureType(CreatureType creatureType)
    {
        this.creatureType = creatureType ;
        this.selectedCreatureTypeInfo = getSelectedCreatureInfo(creatureType) ;
        this.creatureInfotextPos.clear() ;
        for (int i = 0 ; i <= selectedCreatureTypeInfo.size() - 1 ; i += 1)
		{
			creatureInfotextPos.add(Util.translate(topLeftPos, 20, 80 + (i + 1) * (infoFont.getSize() + 4))) ;
		}
    }

    public void act(Player player, Point mousePos)
    {
    }

    public void navigate(String action)
    {
    }
    protected int itemHoveredID(Point mousePos)
    {
        return -1 ;
    }

    public void updateSelectedItemOnHover(Point mousePos)
    {
    }

    private List<String> getSelectedCreatureInfo(CreatureType creatureType)
    {
		List<String> info = new ArrayList<>() ;
            info.add(creatureInfoText[1] + ": " + creatureType.getLevel()) ;
            info.add(creatureInfoText[2] + ": " + (int)creatureType.getPA().getLife().getCurrentValue()) ;
            info.add(creatureInfoText[3] + ": " + creatureType.getPA().getExp().getCurrentValue()) ;
            info.add(creatureInfoText[4] + ": " + creatureType.getGold()) ;
            info.add(creatureInfoText[5] + ": ") ;

		creatureType.getItems().forEach(item -> info.add(item.getName())) ;

        return info ;
    }

    public void display(Point mousePos)
    {
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, 1.0) ;
		creatureType.display(creaturePos, Scale.unit) ;
 
		GamePanel.getDP().drawText(creatureNametextPos, Align.center, creatureType.getName(), nameFont, Palette.colors[0]) ;
		for (int i = 0 ; i <= selectedCreatureTypeInfo.size() - 1 ; i += 1)
		{
			GamePanel.getDP().drawText(creatureInfotextPos.get(i), Align.topLeft, selectedCreatureTypeInfo.get(i), infoFont, Palette.colors[0]) ;
		}
    }

    protected void onClose()
    {
    }
}
