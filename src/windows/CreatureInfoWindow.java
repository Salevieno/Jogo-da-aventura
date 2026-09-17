package windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import liveBeings.CreatureType;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.Palette;
import screen.Screen;
import utilities.Util;

public class CreatureInfoWindow extends GameWindow
{
    private final Point windowPos ; // TODO redundante
	private final Font nameFont ;
    private final Font infoFont ;
    private final Dimension windowSize ;
    private final Point creaturePos ;
    private final List<Point> creatureInfotextPos ;
	private List<String> selectedCreatureTypeInfo ;
	private String[] creatureInfoText ;
    private CreatureType creatureType ;

    public CreatureInfoWindow()
    {
		super("Creature info", Screen.getMe().pos(0.1, 0.3), null, 0, 0, 0, 0) ;
        this.nameFont = new Font(Game.getMainFontName(), Font.BOLD, 15) ;
        this.infoFont = new Font(Game.getMainFontName(), Font.BOLD, 13) ;
        this.windowPos = Util.translate(topLeftPos, 384, 0) ;
        this.windowSize = new Dimension(128, 240) ;
        this.creaturePos = Util.translate(windowPos, 40, 12) ;
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
        this.creatureInfotextPos.add(Util.translate(windowPos, 12, 12)) ;
        for (int i = 0 ; i <= selectedCreatureTypeInfo.size() - 1 ; i += 1)
		{
			creatureInfotextPos.add(Util.translate(creatureInfotextPos.get(0), 0, (i + 1) * infoFont.getSize())) ;
		}
    }

    public void act(Player player, Point mousePos)
    {
    }

    public void navigate(String action)
    {
    }

    public void display(Point mousePos)
    {
		GamePanel.getDP().drawGradRoundRect(windowPos, Align.topLeft, windowSize, 3, Palette.colors[5], Palette.colors[14], Palette.colors[0], true) ;		
		creatureType.display(creaturePos, Scale.unit) ;
 
		GamePanel.getDP().drawText(creatureInfotextPos.get(0), Align.topLeft, creatureType.getName(), nameFont, Palette.colors[0]) ;
		for (int i = 0 ; i <= selectedCreatureTypeInfo.size() - 1 ; i += 1)
		{
			GamePanel.getDP().drawText(creatureInfotextPos.get(i + 1), Align.topLeft, selectedCreatureTypeInfo.get(i), infoFont, Palette.colors[0]) ;
		}
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

    protected void onClose()
    {
    }
}
