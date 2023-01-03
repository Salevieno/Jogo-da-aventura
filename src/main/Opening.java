package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Map;

import javax.swing.ImageIcon;

import components.Icon;
import graphics.Animations;
import graphics.DrawingOnPanel;
import graphics.Gif;
import liveBeings.Player;
import utilities.Align;
import utilities.Scale;
import utilities.Typing;
import utilities.UtilG;
import utilities.UtilS;

public class Opening
{
	private static Image backgroundImage;
	private static Gif openingGif ;
    private static Icon[] buttons ;
    private static String[] text ;
    private int step ;
    private String[] playerInfo ;	// Chosen name, difficult level, sex and class
	
	public Opening()
	{
		String path = Game.ImagesPath  + "\\Opening\\";
		backgroundImage = new ImageIcon(path + "Opening.png").getImage() ;
		openingGif = new Gif(new ImageIcon(path + "Opening.gif").getImage(), 1375, false, true) ;
		Icon.selectedIconID = 2 ;
    	step = 0 ;
    	playerInfo = new String[4] ;

    	buttons = new Icon[14] ;
		Image btPort = new ImageIcon(path + "btPort.png").getImage() ;
		Image btEn = new ImageIcon(path + "btEn.png").getImage() ;
		Image btPortSelected = new ImageIcon(path + "btPortSelected.png").getImage() ;
		Image btEnSelected = new ImageIcon(path + "btEnSelected.png").getImage() ;
		Image btNewGame = new ImageIcon(path + "bt_newGame.png").getImage() ;
		Image btNewGameSelected = new ImageIcon(path + "bt_newGameSelected.gif").getImage() ;
		Image btLoadGame = new ImageIcon(path + "bt_loadGame.png").getImage() ;
		Image btLoadGameSelected = new ImageIcon(path + "bt_loadGameSelected.gif").getImage() ;
		
		Dimension screenSize = Game.getScreen().getSize() ;
		buttons[0] = new Icon(0, "Port", new Point(screenSize.width - 50, 30), null, btPort, btPortSelected) ;
		buttons[1] = new Icon(1, "En", new Point(screenSize.width - 0, 30), null, btEn, btEnSelected) ;
		buttons[2] = new Icon(2, "New game", new Point(screenSize.width / 2 - 80, screenSize.height / 4), null, btNewGame, btNewGameSelected) ;
		buttons[3] = new Icon(3, "Load game", new Point(screenSize.width / 2 + 80, screenSize.height / 4), null, btLoadGame, btLoadGameSelected) ;
		buttons[4] = new Icon(4, "Male", new Point(screenSize.width / 2 - 50, screenSize.height / 4), null, null, null) ;
		buttons[5] = new Icon(5, "Female", new Point(screenSize.width / 2 + 50, screenSize.height / 4), null, null, null) ;
    	buttons[6] = new Icon(6, "Baixo", new Point(screenSize.width / 2 - 100, screenSize.height / 4), null, null, null) ;
    	buttons[7] = new Icon(7, "M�dio", new Point(screenSize.width / 2 + 0, screenSize.height / 4), null, null, null) ;
    	buttons[8] = new Icon(8, "Alto", new Point(screenSize.width / 2 + 100, screenSize.height / 4), null, null, null) ;
    	buttons[9] = new Icon(9, "Cavaleiro", new Point(screenSize.width / 2 - 200, screenSize.height / 4), null, null, null) ;
    	buttons[10] = new Icon(10, "Mago", new Point(screenSize.width / 2 - 100, screenSize.height / 4), null, null, null) ;
    	buttons[11] = new Icon(11, "Arqueiro", new Point(screenSize.width / 2 + 0, screenSize.height / 4), null, null, null) ;
    	buttons[12] = new Icon(12, "Animal", new Point(screenSize.width / 2 + 100, screenSize.height / 4), null, null, null) ;
    	buttons[13] = new Icon(13, "Ladr�o", new Point(screenSize.width / 2 + 200, screenSize.height / 4), null, null, null) ;
    	
    	for (Icon button : buttons)
    	{
     		Icon.addToAllIconsList(button) ;
    	}

    	Map<String, String[]> allText = UtilG.ReadTextFile("P") ;
    	text = allText.get("* Novo jogo *") ;
		//Ani.SetAniVars(20, new Object[] {147, OpeningGif}) ;
		//Ani.StartAni(20) ;
	}

	public Gif getOpeningGif() { return openingGif ;}
	
	/*public void Animation(DrawingOnPanel DP)
	{
		DP.DrawGif(openingGif, new Point(0, 0), AlignmentPoints.topLeft) ;
	}*/
	
	/*public boolean animationIsDonePlaying()
	{
		return openingGif.isDonePlaying();
	}*/
	
	public void Run(String action, Point MousePos, Animations Ani, DrawingOnPanel DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		Dimension screenSize = Game.getScreen().getSize() ;
		double textAngle = DrawingOnPanel.stdAngle ;
		Color textColor = Game.ColorPalette[5] ;
		

		if (!Ani.isActive(20))
		{
	    	buttons[0].activate() ;
	    	buttons[1].activate() ;
	    	buttons[2].activate() ;
	    	buttons[3].activate() ;
		}
		
		// draw background
		DP.DrawImage(backgroundImage, new Point(0, 0), 0, new Scale(1, 1), Align.topLeft) ;		
		for (int i = 0 ; i <= buttons.length - 1 ; i += 1)
		{
			if (buttons[i].getIsActive())
			{
				buttons[i].display(0, Align.center, MousePos, DP) ;
			}
		}
		
		// defining button behavior
		for (int i = 0 ; i <= buttons.length - 1 ; i += 1)
		{
			if (buttons[i].getIsActive() & buttons[i].ishovered(MousePos))
			{
				Icon.selectedIconID = i ;
			}
		}
		
		Icon.selectedIconID = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, Icon.selectedIconID, buttons.length) ;
		if (step == 0)
		{
			Music.PlayMusic(Music.intro) ;
			step += 1 ;
		}
		if (step == 1)
		{		
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				//action = buttons[Icon.selectedIconID].getValue() ;
			}
			if (action.equals("N"))
			{
				//AllText = UtilG.ReadTextFile(InitialLanguage) ;
				//AllTextCat = UtilS.FindAllTextCat(AllText, InitialLanguage) ;
		    	buttons[2].deactivate() ;
		    	buttons[3].deactivate() ;
				step += 1 ;
			}	
			else if (action.equals("L"))
			{
		    	buttons[2].deactivate() ;
		    	buttons[3].deactivate() ;
			}
		}
		else if (step == 2)
		{
			Font Largefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
			DP.DrawText(new Point((int)(0.5*screenSize.width) + 20, (int)(0.25*screenSize.height)), Align.center, textAngle, text[1], font, textColor) ;
			if (action.equals("Enter"))
			{
				Typing.LiveTyping(new Point((int)(0.4*screenSize.width), (int)(0.3*screenSize.height)), textAngle, action, Largefont, textColor, DP) ;
		    	buttons[4].activate() ;
		    	buttons[5].activate() ;
				step += 1 ;
			}
			else
			{
				playerInfo[0] = Typing.LiveTyping(new Point((int)(0.4*screenSize.width), (int)(0.3*screenSize.height)), textAngle, action, Largefont, textColor, DP) ;
			}
		}
		else if (step == 3)
		{
			if (action.equals("M") | action.equals("F"))
			{
		    	buttons[4].deactivate() ;
		    	buttons[5].deactivate() ;
		    	buttons[6].activate() ;
		    	buttons[7].activate() ;
		    	buttons[8].activate() ;
				step += 1 ;	
				playerInfo[3] = action ;
			}
		}
		else if (step == 4)
		{
			if (action.equals("0") | action.equals("1") | action.equals("2"))
			{
		    	buttons[6].deactivate() ;
		    	buttons[7].deactivate() ;
		    	buttons[8].deactivate() ;
		    	buttons[9].activate() ;
		    	buttons[10].activate() ;
		    	buttons[11].activate() ;
		    	buttons[12].activate() ;
		    	buttons[13].activate() ;
		    	playerInfo[1] = action ;
				step += 1 ;
			}
		}
		else if (step == 5)
		{
			int TextL = 15 ;
			int sx = (int)(0.21*screenSize.width), sy = 20 ;
			Font smallfont = new Font("BoldSansSerif", Font.BOLD, 12) ;
			DP.DrawText(new Point((int)(0.35*screenSize.width), (int)(0.1*screenSize.height)), Align.bottomLeft, textAngle, text[5], font, textColor) ;		
			for (int i = 0 ; i <= 5 - 1 ; i += 1)
			{
				Point Pos = new Point((int) (0.01 * screenSize.width + i * sx), (int) (0.28 * screenSize.height)) ;
				DP.DrawFitText(new Point(Pos.x + 5, Pos.y + 5), sy, Align.topLeft, text[11 + i], smallfont, TextL, textColor) ;	
			}
			if (action.equals("0") | action.equals("1") | action.equals("2") | action.equals("3") | action.equals("4"))
			{
		    	buttons[9].deactivate() ;
		    	buttons[10].deactivate() ;
		    	buttons[11].deactivate() ;
		    	buttons[12].deactivate() ;
		    	buttons[13].deactivate() ;
				step += 1 ;	
				playerInfo[2] = action ;
				buttons[0].deactivate() ;
				buttons[1].deactivate() ;
			}			
		}
	}
	
	public String[] getSelectedPlayerAttributes()
	{
		return playerInfo ;
	}

	public boolean isOver()
	{
		return (step == 6) ;
	}
}
