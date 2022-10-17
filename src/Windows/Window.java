package Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import GameComponents.Size;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Main.Game;
import Main.Utg;

public class Window
{
	protected Image image ;
	protected boolean isOpen ;
	protected int menu ;
	protected int numberMenus ;
	protected int tab ;
	protected int numberTabs ;
	protected int item ;
	protected int numberItems ;
	protected int window ;
	protected int numberWindows ;
	
	public Window(Image image, int numberMenus, int numberTabs, int numberItems, int numberWindows)
	{
		this.image = image ;
		this.numberMenus = numberMenus ;
		this.numberTabs = numberTabs ;
		this.numberItems = numberItems ;
		this.numberWindows = numberWindows ;
		isOpen = false ;
		menu = 0 ;
		tab = 0 ;
		item = 0 ;
		window = 0 ;
	}
	public boolean isOpen() {return isOpen ;}
	public int getMenu() {return menu ;}
	public int getTab() {return tab ;}
	public int getWindow() {return window ;}
	public int getItem() {return item ;}
	public void setItem(int newValue) {item = newValue ;}
	
	public void open()
	{
		isOpen = !isOpen ;	
	}
	
	public void menuUp()
	{
		if (menu < numberMenus - 1)
		{
			menu += 1 ;
		}
	}	
	public void menuDown()
	{
		if (0 < menu)
		{
			menu -= 1 ;
		}
	}
	
	public void tabUp()
	{
		if (tab < numberTabs - 1)
		{
			tab += 1 ;
		}
	}	
	public void tabDown()
	{
		if (0 < tab)
		{
			tab -= 1 ;
		}
	}
	
	public void windowUp()
	{
		if (window < numberWindows - 1)
		{
			window += 1 ;
		}
	}	
	public void windowDown()
	{
		if (0 < window)
		{
			window -= 1 ;
		}
	}
	
	public void itemUp()
	{
		if (item < numberItems - 1)
		{
			item += 1 ;
		}
	}	
	public void itemDown()
	{
		if (0 < item)
		{
			item -= 1 ;
		}
	}
	
	public void drawGenericWindow(DrawPrimitives DP)
	{
		Size screenDim = Game.getScreen().getSize() ;
		Point pos = new Point((int) (0.5 * screenDim.x), (int) (0.5 * screenDim.y)) ;
		Font font = new Font("SansSerif", Font.BOLD, screenDim.x * screenDim.y / 3500) ;
		String Title = "Janela gen�rica" ;
		Size size = new Size((int)(0.3*screenDim.x), (int)(3*Utg.TextH(font.getSize()))) ;
		Point windowCenter = new Point((int) (pos.x + 0.5*screenDim.x), (int) (pos.y - screenDim.y - 0.5*3*Utg.TextH(font.getSize()))) ;
		//if (type == 0)
		//{
			DP.DrawRoundRect(pos, "TopLeft", size, 3, Game.ColorPalette[7], Game.ColorPalette[2], true) ;
			//DP.DrawRoundRect(windowCenter, "Center", size[0], size[1], 3, Game.ColorPalette[7], Game.ColorPalette[2], true) ;
			DP.DrawText(windowCenter, "Center", DrawPrimitives.OverallAngle, Title, font, Game.ColorPalette[9]) ;
		//}
		//if (type == 1)
		//{
		//	int ImageW = MenuWindow1.getWidth(null), ImageH = MenuWindow1.getHeight(null) ;
		//	DP.DrawImage(MenuWindow1, pos, new float[] {(float) L / ImageW, (float) H / ImageH}, "TopLeft") ;
		//}
	}
	
	public void display(DrawPrimitives DP)
	{
		if (image != null)
		{
			Size screenDim = Game.getScreen().getSize() ;
			Point pos = new Point((int) (0.5 * screenDim.x), (int) (0.5 * screenDim.y)) ;
			DP.DrawImage(image, pos, "Center") ;
		}
		else
		{
			drawGenericWindow(DP) ;
			//System.out.println("Tentando mostrar uma janela que n�o possui imagem") ;
		}
	}
}