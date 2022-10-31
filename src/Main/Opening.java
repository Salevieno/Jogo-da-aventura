package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import GameComponents.Icon;
import Graphics.Animations;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import LiveBeings.Pet;
import LiveBeings.Player;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class Opening
{
	private static Image OpeningBG, OpeningGif ;
    private static int OPSelectedButton ;
    private static Icon[] OPbuttons ;
    private int OpeningStep ;
    private String[] selectedPlayerAttributes ;
	
	public Opening(Animations Ani)
	{
		OpeningBG = new ImageIcon(Game.ImagesPath + "Opening.png").getImage() ;
		OpeningGif = new ImageIcon(Game.ImagesPath + "Opening.gif").getImage() ;
		OPSelectedButton = 2 ;
    	OpeningStep = 0 ;
    	selectedPlayerAttributes = new String[4] ;	// Chosen name, difficult level, sex and class

    	OPbuttons = new Icon[14] ;
		Image ButtonPort = new ImageIcon(Game.ImagesPath + "ButtonPort.png").getImage() ;
		Image ButtonEn = new ImageIcon(Game.ImagesPath + "ButtonEn.png").getImage() ;
		Image ButtonPortSelected = new ImageIcon(Game.ImagesPath + "ButtonPortSelected.png").getImage() ;
		Image ButtonEnSelected = new ImageIcon(Game.ImagesPath + "ButtonEnSelected.png").getImage() ;
		Image ButtonNewGame = new ImageIcon(Game.ImagesPath + "Button_newGame.png").getImage() ;
		Image ButtonNewGameSelected = new ImageIcon(Game.ImagesPath + "Button_newGameSelected.gif").getImage() ;
		Image ButtonLoadGame = new ImageIcon(Game.ImagesPath + "Button_loadGame.png").getImage() ;
		Image ButtonLoadGameSelected = new ImageIcon(Game.ImagesPath + "Button_loadGameSelected.gif").getImage() ;
    	OPbuttons[0] = new Icon(0, "Port", new Point(Game.getScreen().getSize().x - 50, 30), null, ButtonPort, ButtonPortSelected) ;
    	OPbuttons[1] = new Icon(1, "En", new Point(Game.getScreen().getSize().x - 0, 30), null, ButtonEn, ButtonEnSelected) ;
    	OPbuttons[2] = new Icon(2, "New game", new Point(Game.getScreen().getSize().x / 2 - 80, Game.getScreen().getSize().y / 4), null, ButtonNewGame, ButtonNewGameSelected) ;
    	OPbuttons[3] = new Icon(3, "Load game", new Point(Game.getScreen().getSize().x / 2 + 80, Game.getScreen().getSize().y / 4), null, ButtonLoadGame, ButtonLoadGameSelected) ;
    	OPbuttons[4] = new Icon(4, "Male", new Point(Game.getScreen().getSize().x / 2 - 50, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[5] = new Icon(5, "Female", new Point(Game.getScreen().getSize().x / 2 + 50, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[6] = new Icon(6, "Baixo", new Point(Game.getScreen().getSize().x / 2 - 100, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[7] = new Icon(7, "Médio", new Point(Game.getScreen().getSize().x / 2 + 0, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[8] = new Icon(8, "Alto", new Point(Game.getScreen().getSize().x / 2 + 100, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[9] = new Icon(9, "Cavaleiro", new Point(Game.getScreen().getSize().x / 2 - 200, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[10] = new Icon(10, "Mago", new Point(Game.getScreen().getSize().x / 2 - 100, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[11] = new Icon(11, "Arqueiro", new Point(Game.getScreen().getSize().x / 2 + 0, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[12] = new Icon(12, "Animal", new Point(Game.getScreen().getSize().x / 2 + 100, Game.getScreen().getSize().y / 4), null, null, null) ;
    	OPbuttons[13] = new Icon(13, "Ladrão", new Point(Game.getScreen().getSize().x / 2 + 200, Game.getScreen().getSize().y / 4), null, null, null) ;
    	

		Ani.SetAniVars(20, new Object[] {147, OpeningGif}) ;
		Ani.StartAni(20) ;
	}
	
	public int Run(int[] AllTextCat, String[][] AllText, String action, Point MousePos, Music music, Animations Ani, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 14) ;
		int ScreenW = Game.getScreen().getSize().x, ScreenH = Game.getScreen().getSize().y ;
		float Textangle = DrawPrimitives.OverallAngle ;
		int TextCat = -1 ;
		Color TextColor = Game.ColorPalette[5] ;
		String InitialLanguage = "P" ;
		if (AllTextCat != null)
		{
			TextCat = AllTextCat[2] ;
		}
		if (!Ani.isActive(20))
		{
	    	OPbuttons[0].activate() ;
	    	OPbuttons[1].activate() ;
	    	OPbuttons[2].activate() ;
	    	OPbuttons[3].activate() ;
		}
		DP.DrawImage(OpeningBG, new Point(0, 0), 0, new float[] {1, 1}, new boolean[] {false, false}, "TopLeft", 1) ;
		for (int i = 0 ; i <= OPbuttons.length - 1 ; i += 1)
		{
			if (OPbuttons[i].isActive)
			{
				OPbuttons[i].DrawImage(0, OPSelectedButton, MousePos, DP) ;
			}
		}
		if (action.equals("Enter"))
		{
			action = (String) OPbuttons[OPSelectedButton].startaction() ;
		}
		OPSelectedButton = UtilS.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, OPSelectedButton, OPbuttons.length) ;
		if (OpeningStep == 0)
		{
			if (music.isOn())
			{
				UtilG.PlayMusic(music.getMusicIntro()) ;
			}
			OpeningStep += 1 ;
		}
		if (OpeningStep == 1)
		{
			if (action.equals("N"))
			{
				//TutorialIsOn = true ;
				AllText = UtilG.ReadTextFile(InitialLanguage) ;
				AllTextCat = UtilS.FindAllTextCat(AllText, InitialLanguage) ;
		    	OPbuttons[2].deactivate() ;
		    	OPbuttons[3].deactivate() ;
				OpeningStep += 1 ;
			}	
			if (action.equals("L"))
			{
				//player = new Player("", InitialLanguage, PlayerSex, PlayerJob) ;
				//Items.InitializeStaticVars(ImagesPath) ;
				//pet = new Pet(0) ;
		    	OPbuttons[2].deactivate() ;
		    	OPbuttons[3].deactivate() ;
				
				return 1 ;
			}
		}
		else if (OpeningStep == 2)
		{
			Font Largefont = new Font(Game.MainFontName, Font.BOLD, 13) ;
			DP.DrawText(new Point((int)(0.5*ScreenW) + 20, (int)(0.25*ScreenH)), "Center", Textangle, AllText[TextCat][1], font, TextColor) ;
			if (action.equals("Enter"))
			{
				UtilS.LiveTyping(new Point((int)(0.4*ScreenW), (int)(0.3*ScreenH)), Textangle, action, Largefont, TextColor, DP) ;
		    	OPbuttons[4].activate() ;
		    	OPbuttons[5].activate() ;
				OpeningStep += 1 ;
			}
			else
			{
				selectedPlayerAttributes[0] = UtilS.LiveTyping(new Point((int)(0.4*ScreenW), (int)(0.3*ScreenH)), Textangle, action, Largefont, TextColor, DP) ;
			}
		}
		else if (OpeningStep == 3)
		{
			if (action.equals("M") | action.equals("F"))
			{
		    	OPbuttons[4].deactivate() ;
		    	OPbuttons[5].deactivate() ;
		    	OPbuttons[6].activate() ;
		    	OPbuttons[7].activate() ;
		    	OPbuttons[8].activate() ;
				OpeningStep += 1 ;	
				selectedPlayerAttributes[3] = action ;
			}
		}
		else if (OpeningStep == 4)
		{
			if (action.equals("0") | action.equals("1") | action.equals("2"))
			{
		    	OPbuttons[6].deactivate() ;
		    	OPbuttons[7].deactivate() ;
		    	OPbuttons[8].deactivate() ;
		    	OPbuttons[9].activate() ;
		    	OPbuttons[10].activate() ;
		    	OPbuttons[11].activate() ;
		    	OPbuttons[12].activate() ;
		    	OPbuttons[13].activate() ;
		    	selectedPlayerAttributes[1] = action ;
				OpeningStep += 1 ;
			}
		}
		else if (OpeningStep == 5)
		{
			int TextL = 15 ;
			int sx = (int)(0.21*ScreenW), sy = 20 ;
			Font smallfont = new Font("BoldSansSerif", Font.BOLD, 12) ;
			DP.DrawText(new Point((int)(0.35*ScreenW), (int)(0.1*ScreenH)), "BotLeft", Textangle, AllText[TextCat][5], font, TextColor) ;		
			for (int i = 0 ; i <= 5 - 1 ; i += 1)
			{
				Point Pos = new Point((int) (0.01 * ScreenW + i * sx), (int) (0.28 * ScreenH)) ;
				Size size = new Size(TextL * font.getSize() * 9 / 20, (int) (12.4 * sy)) ;
				DF.DrawMenuWindow(Pos, size, null, 0, Player.ClassColors[i], Game.ColorPalette[7]) ;
				DP.DrawFitText(new Point(Pos.x + 5, Pos.y + 5), sy, "TopLeft", AllText[TextCat][11 + i], smallfont, TextL, TextColor) ;	
			}
			if (action.equals("0") | action.equals("1") | action.equals("2") | action.equals("3") | action.equals("4"))
			{
		    	OPbuttons[9].deactivate() ;
		    	OPbuttons[10].deactivate() ;
		    	OPbuttons[11].deactivate() ;
		    	OPbuttons[12].deactivate() ;
		    	OPbuttons[13].deactivate() ;
				OpeningStep += 1 ;	
				selectedPlayerAttributes[2] = action ;
				OPbuttons[0].deactivate() ;
				OPbuttons[1].deactivate() ;
				
				return 2 ;
			}			
		}
		
		return 0 ;
	}
	
	public String[] getSelectedPlayerAttributes()
	{
		return selectedPlayerAttributes ;
	}
}
