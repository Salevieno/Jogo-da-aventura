package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import items.Equip;
import items.GeneralItem;
import main.Game;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class ElementalWindow extends GameWindow
{
	
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	private Equip selectedEquip ;

	private static final Point windowPos = Game.getScreen().pos(0.4, 0.2) ;
	private static final List<String> menuTitles = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;
	private static final Image windowImage = UtilS.loadImage("\\Windows\\" + "Elemental.png") ;
	private static final int firstSphereID = 390 ;
	private static final int numberItemsOnWindow = 10 ;

	public ElementalWindow()
	{
		super("Elemental", windowPos, windowImage, 2, 1, 1, 2) ;
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
		
		if (action.equals(stdWindowDown))
		{
			itemUp() ;
		}
		if (action.equals(stdWindowUp))
		{
			itemDown() ;
		}
//		if (action.equals("Escape")) { menu = 0 ; close() ; return ;}

	}
	
	public void act(BagWindow bag, String action)
	{
		if (action == null) { return ;}
		
		
		if (!action.equals("Enter") & !action.equals("LeftClick")) { return ;}
		
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
	
	private void displayEquipSelectionMenu(Point mousePos, DrawPrimitives DP)
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
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			
			DP.drawImage(slotImage, slotCenter, Align.center) ;
			DP.drawImage(equip.getImage(), slotCenter, Align.center) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, equip.getName(), stdFont, textColor) ;
		}
	}
	
	private void displaySphereSelectionMenu(Point mousePos, DrawPrimitives DP)
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
			DP.drawImage(slotImage, slotCenter, Align.center) ;
			DP.drawImage(sphere.getImage(), slotCenter, Align.center) ;
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, sphere.getName(), stdFont, textColor) ;
		}
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		
		Point titlePos = UtilG.Translate(windowPos, size.width / 2, 18) ;
		double angle = Draw.stdAngle ;
		
		DP.drawImage(image, windowPos, angle, Scale.unit, Align.topLeft) ;
		DP.drawText(titlePos, Align.center, angle, menuTitles.get(menu), titleFont, stdColor) ;
		switch (menu)
		{
			case 0: displayEquipSelectionMenu(mousePos, DP) ; break ;
			case 1: displaySphereSelectionMenu(mousePos, DP) ; break ;
			default: break ;
		}
		
		
		Draw.windowArrows(UtilG.Translate(windowPos, 0, size.height + 5), size.width, window, numberWindows) ;
		
	}

}
