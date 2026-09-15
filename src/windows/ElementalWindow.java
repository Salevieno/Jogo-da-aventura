package windows;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
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

	private static final int FIRST_SPHERE_ID = 390 ;
	private static final int QTD_ITEMS_ON_PAGE = 10 ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Elemental.png") ;
	private static final List<String> MENU_TITLES = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;

	public ElementalWindow()
	{
		super("Elemental", Screen.getMe().pos(0.35, 0.23), IMAGE, 2, 1, 1, 1) ;
		spheres = null ;
		selectedEquip = null ;
		selectedSphere = null ;
		equipsForElemChange = new ArrayList<>() ;
	}

    protected void onOpen()
    {
        
    }
	
	public void act(Player player, Point mousePos)
	{
		if (!actionIsForward(player.getCurrentAction())) { return ;}
		
		switch (menu)
		{
			case 0:
				if (equipsForElemChange == null || equipsForElemChange.isEmpty()) { return ;}
				
				selectEquip() ;
				menu += 1 ;
				updatePage() ;
				player.resetAction() ;
				return ;
				
			case 1:
				selectSphere() ;
				changeEquipElement(player.getBag()) ;
				player.switchOpenClose(this) ;
				player.resetAction() ;
				return ;
			
			default: return ;
		}		
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
		numberItems = menu == 0 ? getEquipsOnPage().size() : (menu == 1 ? spheres.size() : 0) ;
		numberPages = menu == 0 ? 1 + equipsForElemChange.size() / QTD_ITEMS_ON_PAGE : (menu == 1 ? spheres.size() : 1) ;
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
		if (action.equals(stdPageUp))
		{
			pageUp() ;
			updatePage() ;
		}
		if (action.equals(stdPageDown))
		{
			pageDown() ;
			updatePage() ;
		}
	}

	private void updatePage()
	{
		item = 0 ;
		numberItems = menu == 0 ? getEquipsOnPage().size() : (menu == 1 ? spheres.size() : 0) ;
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

	private List<Equip> getEquipsOnPage()
	{
		if (equipsForElemChange.size() <= QTD_ITEMS_ON_PAGE) { return equipsForElemChange ;}
		
		int minIndex = QTD_ITEMS_ON_PAGE * page ;
		int maxIndex = Math.min(QTD_ITEMS_ON_PAGE * (page + 1), equipsForElemChange.size()) ;
		return equipsForElemChange.subList(minIndex, maxIndex) ;
	}
	
	private void displayEquipSelectionMenu(Point mousePos)
	{
		
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}
		
		List<Equip> equipsOnWindow = getEquipsOnPage() ;
		for (int i = 0 ; i <= equipsOnWindow.size() - 1; i += 1)
		{
			int row = i % ( QTD_ITEMS_ON_PAGE / 1) ;
			int col = i / ( QTD_ITEMS_ON_PAGE / 1) ;
			Equip equip = equipsOnWindow.get(i) ;
			Point slotCenter = Util.translate(topLeftPos, BORDER + PADDING + 6 + col * 140, BORDER + PADDING + 22 + row * 21) ;
			Point textPos = new Point(slotCenter.x + 5, slotCenter.y) ;
			updateSelectedItemOnHover(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;			
            equip.displayInSlot(slotCenter, true) ;
		}
	}
	
	private void displaySphereSelectionMenu(Point mousePos)
	{
		for (int i = 0 ; i <= spheres.size() - 1; i += 1)
		{
			int row = i % QTD_ITEMS_ON_PAGE ;
			int col = i / QTD_ITEMS_ON_PAGE ;
			GeneralItem sphere = spheres.get(i) ;
			Point slotCenter = Util.translate(topLeftPos,
					BORDER + PADDING + 6  + col * 140,
					BORDER + PADDING + 22 + row * 21) ;

			Point textPos = new Point(slotCenter.x  + 5, slotCenter.y) ;
            sphere.displayInSlot(slotCenter, true) ;
			updateSelectedItemOnHover(mousePos, textPos, Align.centerLeft, new Dimension(140, 10), i) ;
		}
	}
	
	public void display(Point mousePos)
	{
		
		Point titlePos = Util.translate(topLeftPos, size.width / 2, 2 + 9) ;
		
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;
		GamePanel.getDP().drawText(titlePos, Align.center, MENU_TITLES.get(menu), TITLE_FONT, STD_COLOR) ;
		
		switch (menu)
		{
			case 0: displayEquipSelectionMenu(mousePos) ; break ;
			case 1: displaySphereSelectionMenu(mousePos) ; break ;
			default: break ;
		}
		
		
		drawNavigationButtons(Util.translate(topLeftPos, 0, size.height + 5), size.width, SUBTITLE_FONT, page, numberPages, stdOpacity) ;
		
	}

}
