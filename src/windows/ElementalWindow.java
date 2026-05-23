package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Equip;
import items.GeneralItem;
import liveBeings.Player;
import main.Elements;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class ElementalWindow extends GameWindow
{
	
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	private Equip selectedEquip ;

	private static final Point WINDOW_POS = Screen.getMe().pos(0.35, 0.23) ;
	private static final List<String> MENU_TITLES = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;
	private static final Image WINDOW_IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Elemental.png") ;
	private static final int FIRST_SPHERE_ID = 390 ;
	private static final int QTD_ITEMS_ON_WINDOW = 10 ;

	public ElementalWindow()
	{
		super("Elemental", WINDOW_POS, WINDOW_IMAGE, 2, 1, 1, 1) ;
		spheres = null ;
		selectedEquip = null ;
		selectedSphere = null ;
		equipsForElemChange = new ArrayList<>() ;
	}

	public static List<GeneralItem> spheresInBag(BagWindow bag)
	{
		
		List<GeneralItem> spheres = new ArrayList<>() ;
		for (int i = FIRST_SPHERE_ID; i <= FIRST_SPHERE_ID + Elements.values().length - 1; i += 1)
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
		numberWindows = menu == 0 ? 1 + equipsForElemChange.size() / QTD_ITEMS_ON_WINDOW : (menu == 1 ? spheres.size() : 1) ;
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
		MessageAnimation.start(Screen.getMe().pos(0.5, 0.2), message, Palette.colors[0]) ;
	}
	
	public void changeEquipElement(BagWindow bag)
	{
		if (selectedEquip == null) { displayMessage(0) ; return ;}
		if (selectedSphere == null) { displayMessage(1) ; return ;}
		
		Elements sphereElem = Elements.values()[selectedSphere.getId() - FIRST_SPHERE_ID] ;
		
		if (!bag.contains(selectedSphere)) { displayMessage(2) ; return ;}
		
		selectedEquip.setElem(sphereElem) ;
		bag.remove(selectedSphere, 1) ;
		displayMessage(3) ;
		
	}

	private List<Equip> getEquipsOnWindow()
	{
		if (equipsForElemChange.size() <= QTD_ITEMS_ON_WINDOW) { return equipsForElemChange ;}
		
		int minIndex = QTD_ITEMS_ON_WINDOW * window ;
		int maxIndex = Math.min(QTD_ITEMS_ON_WINDOW * (window + 1), equipsForElemChange.size()) ;
		return equipsForElemChange.subList(minIndex, maxIndex) ;
	}
	
	private void displayEquipSelectionMenu(Point mousePos)
	{
		
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}
		
		int slotW = BagWindow.SLOT_IMAGE.getWidth(null) ;
		int slotH = BagWindow.SLOT_IMAGE.getHeight(null) ;
		
		List<Equip> equipsOnWindow = getEquipsOnWindow() ;
		for (int i = 0 ; i <= equipsOnWindow.size() - 1; i += 1)
		{
			int row = i % ( QTD_ITEMS_ON_WINDOW / 1) ;
			int col = i / ( QTD_ITEMS_ON_WINDOW / 1) ;
			Equip equip = equipsOnWindow.get(i) ;
			Point slotCenter = Util.translate(WINDOW_POS,
					BORDER + PADDING + 6 + slotW / 2 + col * (140 + slotW),
					BORDER + PADDING + 22 + slotH / 2 + row * 21) ;
			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = i == item ? BagWindow.SELECTED_SLOT_IMAGE : BagWindow.SLOT_IMAGE ;
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			
			GamePanel.getDP().drawImage(slotImage, slotCenter, Align.center) ;
			GamePanel.getDP().drawImage(equip.getImage(), slotCenter, Align.center) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, equip.getName(), STD_FONT, textColor) ;
		}
	}
	
	private void displaySphereSelectionMenu(Point mousePos)
	{
		int slotW = BagWindow.SLOT_IMAGE.getWidth(null) ;
		int slotH = BagWindow.SLOT_IMAGE.getHeight(null) ;

		for (int i = 0 ; i <= spheres.size() - 1; i += 1)
		{
			int row = i % QTD_ITEMS_ON_WINDOW ;
			int col = i / QTD_ITEMS_ON_WINDOW ;
			GeneralItem sphere = spheres.get(i) ;
			Point slotCenter = Util.translate(WINDOW_POS,
					BORDER + PADDING + 6 + slotW / 2 + col * (140 + slotW),
					BORDER + PADDING + 22 + slotH / 2 + row * 21) ;

			Point textPos = new Point(slotCenter.x + slotW / 2 + 5, slotCenter.y) ;
			Image slotImage = item == i ? BagWindow.SELECTED_SLOT_IMAGE : BagWindow.SLOT_IMAGE ;
			GamePanel.getDP().drawImage(slotImage, slotCenter, Align.center) ;
			GamePanel.getDP().drawImage(sphere.getImage(), slotCenter, Align.center) ;
			checkMouseSelection(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
			Color textColor = getTextColor(i == item) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, Draw.stdAngle, sphere.getName(), STD_FONT, textColor) ;
		}
	}
	
	public void display(Point mousePos)
	{
		
		Point titlePos = Util.translate(WINDOW_POS, size.width / 2, 2 + 9) ;
		
		GamePanel.getDP().drawImage(image, WINDOW_POS, Draw.stdAngle, Scale.unit, Align.topLeft, stdOpacity) ;
		GamePanel.getDP().drawText(titlePos, Align.center, Draw.stdAngle, MENU_TITLES.get(menu), TITLE_FONT, STD_COLOR) ;
		
		switch (menu)
		{
			case 0: displayEquipSelectionMenu(mousePos) ; break ;
			case 1: displaySphereSelectionMenu(mousePos) ; break ;
			default: break ;
		}
		
		
		Draw.windowArrows(Util.translate(WINDOW_POS, 0, size.height + 5), size.width, window, numberWindows, stdOpacity) ;
		
	}

}
