package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import animations.MessageAnimation;
import graphics.Align;
import graphics.Scale;
import items.Equip;
import items.GeneralItem;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;


public class ElementalWindow extends GameWindow
{	
	private final Point titlePos ;
    private final List<Point> slotsCenter ;
    private final List<Point> namesPos ;
    private final Point navigationButtonsPos ;
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	private List<Equip> equipsOnPage ;
	private Equip selectedEquip ;

	private static final int FIRST_SPHERE_ID = 390 ;
	private static final int QTD_ITEMS_ON_PAGE = 10 ;
    private static final Dimension ITEM_HOVER_AREA = new Dimension(140, 10) ;
	private static final Image IMAGE = ImageLoader.loadImage(Path.WINDOWS_IMG + "Elemental.png") ;
	private static final List<String> MENU_TITLES = Arrays.asList("Selecione o equipamento", "Selecione a esfera") ;

	public ElementalWindow()
	{
		super("Elemental", Screen.getMe().pos(0.35, 0.23), IMAGE, 2, 1, 1, 1) ;
        this.titlePos = Util.translate(topLeftPos, size.width / 2, 2 + 9) ;
		this.spheres = null ;
		this.selectedEquip = null ;
		this.selectedSphere = null ;
		this.equipsForElemChange = new ArrayList<>() ;
        this.equipsOnPage = new ArrayList<>() ;
        this.slotsCenter = new ArrayList<>() ;
        this.namesPos = new ArrayList<>() ;
        for (int i = 0 ; i <= QTD_ITEMS_ON_PAGE - 1 ; i += 1)
        {
			int row = i % ( QTD_ITEMS_ON_PAGE / 1) ;
			int col = i / ( QTD_ITEMS_ON_PAGE / 1) ;
            Point newPos = Util.translate(topLeftPos, BORDER + PADDING + 6 + col * 140, BORDER + PADDING + 22 + row * 21) ;
            this.slotsCenter.add(newPos) ;
            this.namesPos.add(Util.translate(newPos, 20, 0)) ;
        }
        this.navigationButtonsPos = Util.translate(topLeftPos, 0, size.height + 5) ;
	}

    protected void onOpen()
    {
        // get spheres in bag
        this.spheres = new ArrayList<>() ;
		for (int i = FIRST_SPHERE_ID; i <= FIRST_SPHERE_ID + Elements.values().length - 1; i += 1)
		{
			GeneralItem sphere = GeneralItem.getAll()[i] ;
			if (Game.getPlayer().getBag().contains(sphere))
            {
                this.spheres.add(sphere) ;
            }
		}

        this.equipsForElemChange = Game.getPlayer().getBag().getEquips().keySet().stream()
                                                                            .filter(Equip.class::isInstance)
                                                                            .map(Equip.class::cast)
                                                                            .collect(Collectors.toList()) ;

        // get equips on page
		int minIndex = QTD_ITEMS_ON_PAGE * page ;
		int maxIndex = Math.min(QTD_ITEMS_ON_PAGE * (page + 1), equipsForElemChange.size()) ;
		this.equipsOnPage = equipsForElemChange.subList(minIndex, maxIndex) ;
		this.numberItems = menu == 0 ? this.equipsOnPage.size() : (menu == 1 ? spheres.size() : 0) ;
		this.numberPages = menu == 0 ? 1 + equipsForElemChange.size() / QTD_ITEMS_ON_PAGE : (menu == 1 ? spheres.size() : 1) ;
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
    
    protected int itemHoveredID(Point mousePos)
    {
        if (menu == 0)
        {
            for (int i = 0 ; i <= equipsOnPage.size() - 1; i += 1)
            {
                if (itemIsHovered(mousePos, slotsCenter.get(i), Align.center, ITEM_HOVER_AREA)) { return i ;}
            }
        }
        
        if (menu == 1)
        {
            for (int i = 0 ; i <= spheres.size() - 1; i += 1)
            {
                if (itemIsHovered(mousePos, slotsCenter.get(i), Align.center, ITEM_HOVER_AREA)) { return i ;}
            }
        }
        return -1 ;
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
		this.item = 0 ;
		this.numberItems = menu == 0 ? equipsOnPage.size() : (menu == 1 ? spheres.size() : 0) ;
	}
	
	private void selectEquip()
	{
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}
		selectedEquip = equipsForElemChange.get(item) ;
	}
	
	private void selectSphere()
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
	
	private void changeEquipElement(BagWindow bag)
	{
		if (selectedEquip == null) { displayMessage(0) ; return ;}
		if (selectedSphere == null) { displayMessage(1) ; return ;}
		
		Elements sphereElem = Elements.values()[selectedSphere.getId() - FIRST_SPHERE_ID] ;
		
		if (!bag.contains(selectedSphere)) { displayMessage(2) ; return ;}
		
		selectedEquip.setElem(sphereElem) ;
		bag.remove(selectedSphere, 1) ;
		displayMessage(3) ;	
	}
	
	private void displayEquipSelectionMenu(Point mousePos)
	{
		if (equipsForElemChange == null) { return ;}
		if (equipsForElemChange.isEmpty()) { return ;}

		for (int i = 0 ; i <= equipsOnPage.size() - 1; i += 1)
		{
			Color itemColor = this.item == i ? SELECTED_COLOR : STD_COLOR ;
			Equip equip = equipsOnPage.get(i) ;	
            equip.displayInSlot(slotsCenter.get(i)) ;
			GamePanel.getDP().drawText(namesPos.get(i), Align.centerLeft, equip.getName(), STD_FONT, itemColor) ;
		}
	}
	
	private void displaySphereSelectionMenu(Point mousePos)
	{
		for (int i = 0 ; i <= spheres.size() - 1; i += 1)
		{
			Color itemColor = this.item == i ? SELECTED_COLOR : STD_COLOR ;
			GeneralItem sphere = spheres.get(i) ;
            sphere.displayInSlot(slotsCenter.get(i)) ;
			GamePanel.getDP().drawText(namesPos.get(i), Align.centerLeft, sphere.getName(), STD_FONT, itemColor) ;
		}
	}
	
	public void display(Point mousePos)
	{
		GamePanel.getDP().drawImage(image, topLeftPos, Scale.unit, Align.topLeft, stdOpacity) ;
		GamePanel.getDP().drawText(titlePos, Align.center, MENU_TITLES.get(menu), TITLE_FONT, STD_COLOR) ;

		switch (menu)
		{
			case 0: displayEquipSelectionMenu(mousePos) ; break ;
			case 1: displaySphereSelectionMenu(mousePos) ; break ;
			default: break ;
		}

		drawNavigationButtons(navigationButtonsPos, size.width, SUBTITLE_FONT, page, numberPages, stdOpacity) ;
	}

	protected void onClose() { }
}