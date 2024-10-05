package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Animation;
import graphics.AnimationTypes;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Equip;
import items.GeneralItem;
import libUtil.Align;
import libUtil.Util;
import liveBeings.Player;
import main.Game;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilS;

public class ElementalWindow extends GameWindow
{
	
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	private Equip selectedEquip ;

	private static final Point windowPos = Game.getScreen().pos(0.35, 0.21) ;
	private static final List<String> menuTitles = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;
	private static final Image windowImage = UtilS.loadImage("\\Windows\\" + "Elemental.png") ;
	private static final int firstSphereID = 390 ;
	private static final int numberItemsOnWindow = 10 ;

	public ElementalWindow()
	{
		super("Elemental", windowPos, windowImage, 2, 1, 1, 1) ;
		spheres = null ;
		selectedEquip = null ;
		selectedSphere = null ;
		equipsForElemChange = new ArrayList<>() ;
	}

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

	public void setItems(List<Equip> equipsForElemChange, List<GeneralItem> spheres)
	{
		this.equipsForElemChange = equipsForElemChange ;
		this.spheres = spheres ;
		numberItems = menu == 0 ? getEquipsOnWindow().size() : (menu == 1 ? spheres.size() : 0) ;
		numberWindows = menu == 0 ? 1 + equipsForElemChange.size() / numberItemsOnWindow : (menu == 1 ? spheres.size() : 1) ;
	}

	public void navigate(String action)
	{
		if (action.equals(stdMenuDown))
		{
			itemUp() ;
		}
		if (action.equals(stdMenuUp))
		{
			itemDown() ;
		}
		if (action.equals(stdWindowUp))
		{
			windowUp() ;
			updateWindow() ;
		}
		if (action.equals(stdWindowDown))
		{
			windowDown() ;
			updateWindow() ;
		}
	}

	private void updateWindow()
	{
		item = 0 ;
		numberItems = menu == 0 ? getEquipsOnWindow().size() : (menu == 1 ? spheres.size() : 0) ;
	}
	
	public void act(BagWindow bag, String action, Player player)
	{
		if (!actionIsForward(action)) { return ;}
		
		switch (menu)
		{
			case 0:
				if (equipsForElemChange == null || equipsForElemChange.isEmpty()) { return ;}
				
				selectEquip() ;
				menu += 1 ;
				updateWindow() ;
				player.resetAction() ;
				return ;
				
			case 1:
				selectSphere() ;
				changeEquipElement(bag) ;
				player.switchOpenClose(this) ;
				player.resetAction() ;
				return ;
			
			default: return ;
		}
		
	}
	
	public void selectEquip()
	{
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}
		selectedEquip = equipsForElemChange.get(item) ;
	}
	
	public void selectSphere()
	{
		if (spheres == null) { return ;}
		if (spheres.isEmpty()) { return ;}
		selectedSphere = spheres.get(item) ;
	}
	
	private void displayMessage(int i)
	{
		String message = switch(i)
		{
			case 0 -> "Nenhum equipamento selecionado" ;
			case 1 -> "Nenhuma esfera selecionada" ;
			case 2 -> "Você não possui esta esfera" ; 
			case 3 -> "Elemento mudado com sucesso!" ;
			default -> "" ;
		};
		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.1, 0.1), message, Game.colorPalette[0]}) ;
	}
	
	public void changeEquipElement(BagWindow bag)
	{
		if (selectedEquip == null) { displayMessage(0) ; return ;}
		if (selectedSphere == null) { displayMessage(1) ; return ;}
		
		Elements sphereElem = Elements.values()[selectedSphere.getId() - firstSphereID] ;
		
		if (!bag.contains(selectedSphere)) { displayMessage(2) ; return ;}
		
		selectedEquip.setElem(sphereElem) ;
		bag.remove(selectedSphere, 1) ;
		displayMessage(3) ;
		
	}

	private List<Equip> getEquipsOnWindow()
	{
		if (equipsForElemChange.size() <= numberItemsOnWindow) { return equipsForElemChange ;}
		
		int minIndex = numberItemsOnWindow * window ;
		int maxIndex = Math.min(numberItemsOnWindow * (window + 1), equipsForElemChange.size()) ;
		return equipsForElemChange.subList(minIndex, maxIndex) ;
	}
	
	private void displayEquipSelectionMenu(Point mousePos, DrawPrimitives DP)
	{
		
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}
		
		int slotW = BagWindow.slotImage.getWidth(null) ;
		int slotH = BagWindow.slotImage.getHeight(null) ;
		
		List<Equip> equipsOnWindow = getEquipsOnWindow() ;
		for (int i = 0 ; i <= equipsOnWindow.size() - 1; i += 1)
		{
			int row = i % ( numberItemsOnWindow / 1) ;
			int col = i / ( numberItemsOnWindow / 1) ;
			Equip equip = equipsOnWindow.get(i) ;
			Point slotCenter = Util.Translate(windowPos,
					border + padding + 6 + slotW / 2 + col * (140 + slotW),
					border + padding + 22 + slotH / 2 + row * 21) ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = i == item ? BagWindow.selectedSlotImage : BagWindow.slotImage ;
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			
			DP.drawImage(slotImage, slotCenter, Align.center) ;
			DP.drawImage(equip.getImage(), slotCenter, Align.center) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, equip.getName(), stdFont, textColor) ;
		}
	}
	
	private void displaySphereSelectionMenu(Point mousePos, DrawPrimitives DP)
	{
		int slotW = BagWindow.slotImage.getWidth(null) ;
		int slotH = BagWindow.slotImage.getHeight(null) ;

		for (int i = 0 ; i <= spheres.size() - 1; i += 1)
		{
			int row = (i - window * numberItemsOnWindow) % numberItemsOnWindow ;
			int col = (i - window * numberItemsOnWindow) / numberItemsOnWindow ;
			GeneralItem sphere = spheres.get(i) ;
			Point slotCenter = Util.Translate(windowPos,
					border + padding + 6 + slotW / 2 + col * (140 + slotW),
					border + padding + 22 + slotH / 2 + row * 21) ;

			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = item == i ? BagWindow.selectedSlotImage : BagWindow.slotImage ;
			DP.drawImage(slotImage, slotCenter, Align.center) ;
			DP.drawImage(sphere.getImage(), slotCenter, Align.center) ;
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, sphere.getName(), stdFont, textColor) ;
		}
	}
	
	public void display(Point mousePos, DrawPrimitives DP)
	{
		
		Point titlePos = Util.Translate(windowPos, size.width / 2, 2 + 9) ;
		
		DP.drawImage(image, windowPos, Draw.stdAngle, Scale.unit, Align.topLeft, stdOpacity) ;
		DP.drawText(titlePos, Align.center, Draw.stdAngle, menuTitles.get(menu), titleFont, stdColor) ;
		
		switch (menu)
		{
			case 0: displayEquipSelectionMenu(mousePos, DP) ; break ;
			case 1: displaySphereSelectionMenu(mousePos, DP) ; break ;
			default: break ;
		}
		
		
		Draw.windowArrows(Util.Translate(windowPos, 0, size.height + 5), size.width, window, numberWindows) ;
		
	}

}
