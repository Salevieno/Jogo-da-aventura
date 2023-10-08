package windows;

import java.awt.Point;
import java.util.ArrayList;
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
	
	private List<GeneralItem> spheres ;
	private GeneralItem selectedSphere ;
	private List<Equip> equipsForElemChange ;
	
	private static final int firstSphereID = 390 ;

	public ElementalWindow()
	{
		super("Elemental", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Elemental.png"), 1, 1, 1, 2) ;
		spheres = null ;
		selectedSphere = null ;
		equipsForElemChange = null ;
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

	}
	
	public void selectSphere() { if (spheres == null | spheres.size() == 0) { return ;} selectedSphere = spheres.get(item) ; item = 0 ; System.out.println("bag spheres: " + spheres); System.out.println("selected sphere: " + selectedSphere.getName());}
	
	public void changeEquipElement(BagWindow bag)
	{
		if (selectedSphere == null) { return ;}
		
		Elements sphereElem = Elements.values()[selectedSphere.getId() - firstSphereID] ;
		
		if (!bag.contains(spheres.get(item))) { return ;}
		
		System.out.println(equipsForElemChange.get(item).getName() + ": " + equipsForElemChange.get(item).getElem() + " -> " + sphereElem);
		
		equipsForElemChange.get(item).setElem(sphereElem) ;
		bag.Remove(spheres.get(item), 1) ;
		
//		System.out.println("Equip new elem: " + equipsForElemChange.get(item).getElem());
		
	}

	public void display(Point mousePos, DrawingOnPanel DP)
	{
		
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
		
		
	}

}
