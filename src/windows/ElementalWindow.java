package windows;

import java.awt.Point;
import java.util.List;

import graphics.DrawingOnPanel;
import items.Equip;
import items.GeneralItem;
import main.Game;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class ElementalWindow extends GameWindow
{
	private List<GeneralItem> spheres ;

	public ElementalWindow(BagWindow bag)
	{
		super("Elemental", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Elemental.png"), 1, 1, 1, 1) ;
		spheres = null ;
		for (int i = 390; i <= 400 - 1; i += 1)
		{
			GeneralItem sphere = GeneralItem.getAll()[i] ;
			if (bag.contains(sphere)) { spheres.add(sphere) ;}
		}
	}

	@Override
	public void navigate(String action)
	{

	}
	
	public void changeEquipElement(BagWindow bag, Equip equip)
	{
		
		Elements sphereElem = Elements.values()[item] ;
		
		if (!bag.contains(spheres.get(item))) { return ;}
		
		equip.setElem(sphereElem) ;
		bag.Remove(spheres.get(item), 1) ;
		
	}

	public void display(DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
	}

}
