package simulations;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import components.GameButton;
import components.IconFunction;
import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.AtkResults;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public abstract class PlayerEvolutionSimulation
{
	private static Map<String, GameButton> buttons ;
	private static final Image buttonImage = UtilG.loadImage(Game.ImagesPath + "ButtonGeneral.png") ;
	private static final Image buttonSelectedImage = UtilG.loadImage(Game.ImagesPath + "ButtonGeneralSelected.png") ;
	
	static
	{
		IconFunction startLevelAction = () -> {System.out.println("player start at level ");}  ;
		buttons = new HashMap<>() ;
		buttons.put("start at level", new GameButton(new Point(200, 200), buttonImage, buttonSelectedImage, startLevelAction)) ;
	}
	
	public static void startAtLevel(int level, Player player)
	{
		
		player.getExp().incCurrentValue(player.getExp().getMaxValue());

		while (player.getLevel() <= level - 1)
		{
			player.getExp().incCurrentValue(player.getExp().getMaxValue());
			if (player.shouldLevelUP())
			{
				player.levelUp(null) ;
			}
		}
		
	}
	
	public static void train(int amountAtks, AtkResults atkResults, LiveBeing user)
	{
		
		if (user instanceof Creature) { return ;}
			
		for (int i = 0 ; i <= amountAtks - 1 ; i += 1)
		{
			user.train(atkResults) ;
		}
		
	}
	
	public static void displayInterface(Point mousePos, DrawingOnPanel DP)
	{
		DP.DrawRect(new Point(0, 0), Align.topLeft, Game.getScreen().getSize(), 1, Color.black, null) ;
		buttons.values().forEach(button -> {
			button.display(0, Align.center, mousePos, DP) ;
		});
		
	}
}
