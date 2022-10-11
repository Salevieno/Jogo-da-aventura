package GameComponents;

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
}
