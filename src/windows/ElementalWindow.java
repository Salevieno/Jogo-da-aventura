package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.DrawingOnPanel;
import items.Equip;
import items.GeneralItem;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class ElementalWindow extends GameWindow
{
	
	private Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	private Equip selectedEquip ;
	
	private static final List<String> menuTitles = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;
	private static final Image windowImage = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Elemental.png") ;
	private static final int firstSphereID = 390 ;
	private static final int numberItemsOnWindow = 10 ;

	public ElementalWindow()
	{
		super("Elemental", windowImage, 2, 1, 1, 2) ;
		spheres = null ;
		selectedEquip = null ;
		selectedSphere = null ; // ArrayList<Item>) ((ArrayList<?>) equipsForElemChange))
		equipsForElemChange = null ;
	}
	// TODO fazer a janela fechar após a seleção da esfera
	public static List<GeneralItem> spheresInBag(BagWindow bag)
	{
		
		List<GeneralItem> spheres = new ArrayList<>() ;
		for (int i = firstSphereID; i <= firstSphereID + Elements.values().length - 1; i += 1)
		{
			GeneralItem sphere = GeneralItem.getAll()[i] ;
			if (bag.contains(sphere)) { spheres.add(sphere) ;}
		}

		return spheres ;
		
	}

	public List<GeneralItem> getSpheres() { return spheres ;}
	public GeneralItem getSelectedSphere() { return selectedSphere ;}
	public List<Equip> getEquipsForElemChange() { return equipsForElemChange ;}
	public Equip getSelectedEquip() { return equipsForElemChange == null | equipsForElemChange.size() == 0 ? null : equipsForElemChange.get(item) ;}

	public void setEquipsForElemChange(List<Equip> equipsForElemChange)
	{
		this.equipsForElemChange = new ArrayList<>() ;
		this.equipsForElemChange = equipsForElemChange ;
		numberItems = this.equipsForElemChange.size() ;
	}

	public void setSpheres(List<GeneralItem> spheres)
	{
		this.spheres = spheres ;
		numberItems = this.spheres.size() ;
	}

	@Override
	public void navigate(String action)
	{
		
		if (action.equals(Player.ActionKeys[2]))
		{
			itemUp() ;
		}
		if (action.equals(Player.ActionKeys[0]))
		{
			itemDown() ;
		}
//		if (action.equals("Escape")) { menu = 0 ; close() ; return ;}

	}
	
	public void act(BagWindow bag, String action)
	{
		if (action == null) { return ;}
		
		
		if (!action.equals("Enter")) { return ;}
		
		System.out.println("menu = " + menu);
		switch (menu)
		{
			case 0:
				selectEquip() ;
				menu += 1 ;
				return ;
				
			case 1:
				selectSphere() ;
				changeEquipElement(bag) ;
				menu = 0 ;
				return ;
			
			default: return ;
		}
		
	}
	
	public void selectEquip()
	{System.out.println("equips available = " + equipsForElemChange) ;System.out.println("selecting " + equipsForElemChange.get(item));
		selectedEquip = equipsForElemChange.get(item) ;
	}
	
	public void selectSphere()
	{
		if (spheres == null | spheres.size() == 0) { return ;}
		selectedSphere = spheres.get(item) ; item = 0 ;
		System.out.println("bag spheres: " + spheres);
		System.out.println("selected sphere: " + selectedSphere.getName());
	}
	
	public void changeEquipElement(BagWindow bag)
	{
		if (selectedEquip == null) { return ;}
		if (selectedSphere == null) { return ;}
		
		Elements sphereElem = Elements.values()[selectedSphere.getId() - firstSphereID] ;
		
		if (!bag.contains(selectedSphere)) { return ;}
		
		System.out.println(selectedEquip.getName() + ": " + selectedEquip.getElem() + " -> " + sphereElem);
		
		selectedEquip.setElem(sphereElem) ;
		bag.remove(selectedSphere, 1) ;
		
		System.out.println("Equip new elem: " + selectedEquip.getElem());
		
	}

	private List<Equip> getEquipsOnWindow()
	{
		return equipsForElemChange.subList(numberItemsOnWindow * window, numberItemsOnWindow * (window + 1)) ;
	}
	
	private void displayEquipSelectionMenu(DrawingOnPanel DP)
	{
		int slotW = BagWindow.SlotImage.getWidth(null) ;
		int slotH = BagWindow.SlotImage.getHeight(null) ;
		List<Equip> equipsOnWindow = getEquipsOnWindow() ;
		for (int i = 0 ; i <= equipsOnWindow.size() - 1; i += 1)
		{
			int row = (i - window * numberItemsOnWindow) % ( numberItemsOnWindow / 1) ;
			int col = (i - window * numberItemsOnWindow) / ( numberItemsOnWindow / 1) ;
			Equip equip = equipsForElemChange.get(i) ;
			Point slotCenter = UtilG.Translate(windowPos,
					border + padding + 6 + slotW / 2 + col * (140 + slotW),
					border + padding + 22 + slotH / 2 + row * 21) ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = i == item ? BagWindow.SelectedSlotImage : BagWindow.SlotImage ;
			Color textColor = getTextColor(i == item) ;
			
			DP.DrawImage(slotImage, slotCenter, Align.center) ;
			DP.DrawImage(equip.getImage(), slotCenter, Align.center) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, equip.getName(), stdFont, textColor) ;
		}
	}
	
	private void displaySphereSelectionMenu(DrawingOnPanel DP)
	{
		int slotW = BagWindow.SlotImage.getWidth(null) ;
		int slotH = BagWindow.SlotImage.getHeight(null) ;

		for (int i = 0 ; i <= spheres.size() - 1; i += 1)
		{
			int row = (i - window * numberItemsOnWindow) % numberItemsOnWindow ;
			int col = (i - window * numberItemsOnWindow) / numberItemsOnWindow ;
			GeneralItem sphere = spheres.get(i) ;
			Point slotCenter = UtilG.Translate(windowPos,
					border + padding + 6 + slotW / 2 + col * (140 + slotW),
					border + padding + 22 + slotH / 2 + row * 21) ;

			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = item == i ? BagWindow.SelectedSlotImage : BagWindow.SlotImage ;
			DP.DrawImage(slotImage, slotCenter, Align.center) ;
			DP.DrawImage(sphere.getImage(), slotCenter, Align.center) ;
			Color textColor = getTextColor(i == item) ;
			DP.DrawText(textPos, Align.centerLeft, DrawingOnPanel.stdAngle, sphere.getName(), stdFont, textColor) ;
		}
	}
	
	public void display(Point mousePos, DrawingOnPanel DP)
	{
		
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 18) ;
		double angle = DrawingOnPanel.stdAngle ;
		
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		DP.DrawText(titlePos, Align.center, angle, menuTitles.get(menu), titleFont, stdColor) ;
		switch (menu)
		{
			case 0: displayEquipSelectionMenu(DP) ; break ;
			case 1: displaySphereSelectionMenu(DP) ; break ;
			default: break ;
		}
		
		
		DP.DrawWindowArrows(UtilG.Translate(windowPos, 0, size.height + 5), size.width, window, numberWindows) ;
		
	}

}
