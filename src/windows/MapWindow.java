package windows;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;

import graphics.DrawingOnPanel;
import main.Game;
import maps.GameMap;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class MapWindow extends GameWindow
{
	public MapWindow()
	{
		super(new ImageIcon(Game.ImagesPath + "\\Windows\\" + "MapWindow.png").getImage(), 0, 0, 0, 0) ;
	}
	
	public void display(GameMap playerMap, DrawingOnPanel DP)
	{
		Point windowPos = new Point(60, 60) ;
		Point offset = new Point(30, 15) ;
		Point spacing = new Point (29 + 5, 19 + 5) ;
		
		DP.DrawImage(image, windowPos, Align.topLeft) ;
		for (GameMap map : Game.getMaps())
		{
			int row = 0 ;
			int col = 0 ;
			Scale scale = new Scale(0.05, 0.05) ;
			switch (map.getContinent())
			{
				case 0:
				{
					switch(map.getName())
					{
						case "City of the knights": row = 3 ; col = 0 ; break ;
						case "City of the mages": row = 0 ; col = 2 ; break ;
						case "City of the archers": row = 4 ; col = 4 ; break ;
						case "City of the animals": row = 6 ; col = 4 ; break ;
						case "City of the assassins": row = 0 ; col = 0 ; break ;
						case "Forest 1": row = 0 ; col = 1 ; break ;
						case "Forest 2": row = 1 ; col = 0 ; break ;
						case "Forest 3": row = 1 ; col = 1 ; break ;
						case "Forest 4": row = 1 ; col = 2 ; break ;
						case "Forest 5": row = 1 ; col = 3 ; break ;
						case "Forest 6": row = 2 ; col = 1 ; break ;
						case "Forest 7": row = 2 ; col = 2 ; break ;
						case "Forest 8": row = 2 ; col = 3 ; break ;
						case "Forest 9": row = 2 ; col = 4 ; break ;
						case "Forest 10": row = 3 ; col = 1 ; break ;
						case "Forest 11": row = 3 ; col = 2 ; break ;
						case "Forest 12": row = 3 ; col = 3 ; break ;
						case "Forest 13": row = 3 ; col = 4 ; break ;
						case "Forest 14": row = 4 ; col = 1 ; break ;
						case "Forest 15": row = 4 ; col = 2 ; break ;
						case "Forest 16": row = 4 ; col = 3 ; break ;
						case "Forest 17": row = 5 ; col = 1 ; break ;
						case "Forest 18": row = 5 ; col = 2 ; break ;
						case "Forest 19": row = 5 ; col = 3 ; break ;
						case "Forest 20": row = 5 ; col = 4 ; break ;
						case "Forest 21": row = 6 ; col = 1 ; break ;
						case "Forest 22": row = 6 ; col = 2 ; break ;
						case "Forest 23": row = 6 ; col = 3 ; break ;
						case "Forest 24": row = 7 ; col = 2 ; break ;
						case "Forest 25": row = 7 ; col = 3 ; break ;
					}
					
					Point pos = new Point(windowPos.x + offset.x + spacing.x * col, windowPos.y + size.height - offset.y - spacing.y * row) ;
					map.display(pos, scale, DP) ;
					
					if (map.equals(playerMap))
					{
						DP.DrawCircle(UtilG.Translate(pos, (int) (scale.x * size.width / 2), (int) (- scale.y * size.height / 2)), 5, 0, Game.ColorPalette[6], null) ;
					}
				}
			}
		}
	}
}
