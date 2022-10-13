package GameComponents;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import Graphics.DrawFunctions;
import Items.Alchemy;
import Items.Arrow;
import Items.Equip;
import Items.Fab;
import Items.Food;
import Items.Forge;
import Items.GeneralItem;
import Items.PetItem;
import Items.Potion;
import Items.Quest;
import Main.Game;
import Main.Utg;

public class Bag
{	
	Potion[] pot ;
	Alchemy[] alch ;
	Forge[] forge ;
	PetItem[] petItem ;
	Food[] food ;
	Arrow[] arrow ;
	Equip[] equip ;
	GeneralItem[] genItem ;
	Fab[] fab ;
	Quest[] quest ;
	
	public static Image MenuImage = new ImageIcon(Game.ImagesPath + "BagMenu.png").getImage() ;
    public static Image SlotImage = new ImageIcon(Game.ImagesPath + "BagSlot.png").getImage() ;
	
	public Bag(Potion[] pot, Alchemy[] alch, Forge[] forge, PetItem[] petItem, Food[] food, Arrow[] arrow, Equip[] equip, GeneralItem[] genItem, Fab[] fab, Quest[] quest)
	{
		this.pot = pot ;
		this.alch = alch ;
		this.forge = forge ;
		this.petItem = petItem ;
		this.food = food ;
		this.arrow = arrow ;
		this.equip = equip ;
		this.genItem = genItem ;
		this.fab = fab ;
		this.quest = quest ;
	}
	
	public Potion[] getPotions() {return pot ;}
	public Alchemy[] getAlchemy() {return alch ;}
	public Forge[] getForge() {return forge ;}
	public PetItem[] getPetItem() {return petItem ;}
	public Food[] getFood() {return food ;}
	public Arrow[] getArrow() {return arrow ;}
	public Equip[] getEquip() {return equip ;}
	public GeneralItem[] getGenItem() {return genItem ;}
	public Fab[] getFab() {return fab ;}
	public Quest[] getQuest() {return quest ;}
	

	public void Add(Potion newPot)
	{
		pot = Utg.AddElem(pot, newPot) ;
	}
	
	public void display(int[] WinDim, int[] MousePos, DrawFunctions DF)
	{
		Point pos = new Point((int)(0.35 * WinDim[0]), (int)(0.48 * WinDim[1])) ;
		int[] size = new int[] {(int)(0.52 * WinDim[0]), (int)(0.4 * WinDim[1])} ;
		DF.DrawBag(pos, size, this, MenuImage, SlotImage, 0, 0, 0, 10, 0, MousePos) ;			
	}
	
}
