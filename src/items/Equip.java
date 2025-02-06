package items;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import attributes.AttributeBonus;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Game;
import utilities.Elements;
import utilities.Util;
import utilities.UtilS;
import windows.AttributesWindow;

public class Equip extends Item
{
	private int forgeLevel ;
	private AttributeBonus attBonus ;
	private Elements elem ;
	private Elements originalElem ;
	
	private static Equip[] allEquips ;
	private static final double setBonus = 0.2 ;
	public static final int maxForgeLevel = 10 ;
	
	private static final Image swordIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconSword.png") ;
	private static final Image staffIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconStaff.png") ;
	private static final Image bowIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconBow.png") ;
	private static final Image clawsIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconClaws.png") ;
	private static final Image daggerIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconDagger.png") ;
	private static final Image shieldIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconShield.png") ;
	private static final Image armorIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconArmor.png") ;
	private static final Image emblemIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconEmblem.png") ;
	
	public static final Image SwordImage = UtilS.loadImage("\\Equips\\" + "Eq0_Sword.png") ;
	public static final Image StaffImage = UtilS.loadImage("\\Equips\\" + "Eq1_Staff.png") ;
	public static final Image BowImage = UtilS.loadImage("\\Equips\\" + "Eq2_Bow.png") ;
	public static final Image ClawsImage = UtilS.loadImage("\\Equips\\" + "Eq3_Claws.png") ;
	public static final Image DaggerImage = UtilS.loadImage("\\Equips\\" + "Eq4_Dagger.png") ;
	public static final Image ShieldImage = UtilS.loadImage("\\Equips\\" + "Eq5_Shield.png") ;
	public static final Image ArmorImage = UtilS.loadImage("\\Equips\\" + "Eq6_Armor.png") ;
	public static final Image emblemImage = UtilS.loadImage("\\Equips\\" + "Eq8_emblem.png") ;
	
	public static final Image ShiningSwordImage = UtilS.loadImage("\\Equips\\" + "Eq0_ShiningSword.png") ;
	public static final Image ShiningStaffImage = UtilS.loadImage("\\Equips\\" + "Eq1_ShiningStaff.png") ;
	public static final Image ShiningBowImage = UtilS.loadImage("\\Equips\\" + "Eq2_ShiningBow.png") ;
	public static final Image ShiningClawsImage = UtilS.loadImage("\\Equips\\" + "Eq3_ShiningClaws.png") ;
	public static final Image ShiningDaggerImage = UtilS.loadImage("\\Equips\\" + "Eq4_ShiningDagger.png") ;
	public static final Image ShiningShieldImage = UtilS.loadImage("\\Equips\\" + "Eq5_ShiningShield.png") ;
	public static final Image ShiningArmorImage = UtilS.loadImage("\\Equips\\" + "Eq6_ShiningArmor.png") ;

	static
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "Item_Equip.csv") ;
		allEquips = new Equip[input.size()] ;
		for (int p = 0; p <= allEquips.length - 1; p += 1)
		{
			int id = Integer.parseInt(input.get(p)[0]) ;
			String name = input.get(p)[1] ;
			String description = input.get(p)[3] ;
			int price = Integer.parseInt(input.get(p)[5]) ;
			float dropChance = Float.parseFloat(input.get(p)[6]) ;
			int forgeLevel = 0;
			
			int life = Integer.parseInt(input.get(p)[7]) ;
			int MP = Integer.parseInt(input.get(p)[8]) ;
			int phyAtk = Integer.parseInt(input.get(p)[9]) ;
			int magAtk = Integer.parseInt(input.get(p)[10]) ;
			int phyDef = Integer.parseInt(input.get(p)[11]) ;
			int magDef = Integer.parseInt(input.get(p)[12]) ;
			int dex = Integer.parseInt(input.get(p)[13]) ;
			int agi = Integer.parseInt(input.get(p)[14]) ;
			double critAtkChance = Double.parseDouble(input.get(p)[15]) ;
			double critDefChance = Double.parseDouble(input.get(p)[16]) ;
			double stunAtkChance = Double.parseDouble(input.get(p)[17]) ;
			double stunDefChance = Double.parseDouble(input.get(p)[18]) ;
			int stunDuration = Integer.parseInt(input.get(p)[19]) ;
			double blockAtkChance = Double.parseDouble(input.get(p)[20]) ;
			double blockDefChance = Double.parseDouble(input.get(p)[21]) ;
			int blockDuration = Integer.parseInt(input.get(p)[22]) ;
			double bloodAtkChance = Double.parseDouble(input.get(p)[23]) ;
			double bloodDefChance = Double.parseDouble(input.get(p)[24]) ;
			int bloodAtk = Integer.parseInt(input.get(p)[25]) ;
			int bloodDef = Integer.parseInt(input.get(p)[26]) ;
			int bloodDuration = Integer.parseInt(input.get(p)[27]) ;
			double poisonAtkChance = Double.parseDouble(input.get(p)[28]) ;
			double poisonDefChance = Double.parseDouble(input.get(p)[29]) ;
			int poisonAtk = Integer.parseInt(input.get(p)[30]) ;
			int poisonDef = Integer.parseInt(input.get(p)[31]) ;
			int poisonDuration = Integer.parseInt(input.get(p)[32]) ;
			double silenceAtkChance = Double.parseDouble(input.get(p)[33]) ;
			double silenceDefChance = Double.parseDouble(input.get(p)[34]) ;
			int silenceDuration = Integer.parseInt(input.get(p)[35]) ;
	
			AttributeBonus attBonus = new AttributeBonus(life, MP,
					phyAtk,	magAtk, phyDef, magDef,	dex, agi,
					critAtkChance, critDefChance,
					stunAtkChance, stunDefChance, stunDuration,
					blockAtkChance, blockDefChance, blockDuration,
					bloodAtkChance, bloodDefChance, bloodAtk, bloodDef, bloodDuration,
					poisonAtkChance, poisonDefChance, poisonAtk, poisonDef, poisonDuration,
					silenceAtkChance, silenceDefChance, silenceDuration);
			
			Elements elem = Elements.valueOf(input.get(p)[36]) ;
			
			allEquips[p] = new Equip(id, name, description, price, dropChance, forgeLevel, attBonus, elem);																																					// elem
		}
	}
	
	public Equip(int id, String name, String description, int price, float dropChance, int forgeLevel, AttributeBonus attBonus, Elements elem)
	{
		super(id, name, description, imageFromID(id), price, dropChance) ;
		this.forgeLevel = forgeLevel ;
		this.attBonus = attBonus ;
		this.elem = elem ;
		originalElem = elem ;
	}

	
	public static EquipTypes typeFromID(int id)
	{
		
		if ((id + 1) % 100 == 0) { return EquipTypes.emblem ;}

		int job = id / 200 ;
		int idRel = id - 200 * job;
		
		if (idRel <= 98)
		{
			return idRel % 3 == 0 ? EquipTypes.weapons()[job] : idRel % 3 == 1 ? EquipTypes.shield : EquipTypes.armor ;
		}
		
		return idRel % 3 == 1 ? EquipTypes.weapons()[job] : idRel % 3 == 2 ? EquipTypes.shield : EquipTypes.armor ;
		
	}
	
	private static int numTypeFromID(int id)
	{
		EquipTypes type = typeFromID(id) ;
		
		switch(type)
		{
			case emblem: return 3 ;
			case shield: return 1 ;
			case armor: return 2 ;
			default: return 0 ;
		}
	}
	
	public static Image imageFromID(int id)
	{
		
		Image[] equipImages = new Image[] {swordIcon, staffIcon, bowIcon, clawsIcon, daggerIcon, shieldIcon, armorIcon, emblemIcon} ;
		return equipImages[Arrays.asList(EquipTypes.values()).indexOf(typeFromID(id))] ;
		
	}

	public Image fullSizeImage()
	{
		switch(typeFromID(id))
		{
			case sword: return forgeLevel == 10 ? ShiningSwordImage : SwordImage ;
			case staff: return forgeLevel == 10 ? ShiningStaffImage : StaffImage ;
			case bow: return forgeLevel == 10 ? ShiningBowImage : BowImage ;
			case claws: return forgeLevel == 10 ? ShiningClawsImage : ClawsImage ;
			case dagger: return forgeLevel == 10 ? ShiningDaggerImage : DaggerImage ;
			case shield: return forgeLevel == 10 ? ShiningShieldImage : ShieldImage ;
			case armor: return forgeLevel == 10 ? ShiningArmorImage : ArmorImage ;
			case emblem: return emblemImage ;
			default: return null ;
		}
	}
	
	public int getForgeLevel() {return forgeLevel ;}
	public Elements getElem() {return elem ;}
	public void setElem(Elements newElem) { elem = newElem ;}
	public AttributeBonus getAttributeBonus() {return attBonus ;}
	public static Equip[] getAll() {return allEquips ;}

	public boolean isSpecial()
	{
		if (0 <= id & id <= 99) { return false ;}
		if (200 <= id & id <= 299) { return false ;}
		if (400 <= id & id <= 499) { return false ;}
		if (600 <= id & id <= 699) { return false ;}
		if (800 <= id & id <= 899) { return false ;}
		
		return true ;
	}

	public boolean isWeapon()
	{
		if (0 <= id & id <= 99) { return id % 3 == 0 ;}
		if (100 <= id & id <= 199) { return id % 3 == 1 ;}
		if (200 <= id & id <= 299) { return id % 3 == 2 ;}
		if (300 <= id & id <= 399) { return id % 3 == 0 ;}
		if (400 <= id & id <= 499) { return id % 3 == 1 ;}
		if (500 <= id & id <= 599) { return id % 3 == 2 ;}
		if (600 <= id & id <= 699) { return id % 3 == 0 ;}
		if (700 <= id & id <= 799) { return id % 3 == 1 ;}
		if (800 <= id & id <= 899) { return id % 3 == 2 ;}
		if (900 <= id & id <= 999) { return id % 3 == 0 ;}
		
		System.out.println("Verificação se o equipamento é uma arma com item que não é equipamento");
		return false ;
	}
	
	public void resetElem() { elem = originalElem ;}
	
	public void incForgeLevel()
	{
		double forgeBonus = 0.1 ;
		double[] bonuses = attBonus.all() ;
		double[] increment = new double[bonuses.length] ;
		for (int i = 0 ; i <= bonuses.length - 1; i += 1)
		{
			increment[i] = forgeBonus * bonuses[i] ;
		}
		attBonus.inc(increment) ;
		forgeLevel += 1 ;
	}
	
	public void resetForgeLevel()
	{
		double forgeBonus = 0.1 ;
		double[] bonuses = attBonus.all() ;
		double[] initialBonus = Arrays.copyOf(bonuses, bonuses.length) ;
		for (int i = 0 ; i <= bonuses.length - 1; i += 1)
		{
			initialBonus[i] = initialBonus[i] / Math.pow((1 + forgeBonus), forgeLevel) ;
		}
		attBonus.setBasic(DoubleStream.of(initialBonus).boxed().collect(Collectors.toList())) ;
		forgeLevel = 0 ;
	}
	
	private void applyBonus(PersonalAttributes PA, BattleAttributes BA, Equip equip, double mult)
	{
		AttributeBonus attBonus = equip.getAttributeBonus() ;
		PA.getLife().incMaxValue((int) (attBonus.getLife() * mult)) ;
		PA.getMp().incMaxValue((int) (attBonus.getMP() * mult)) ;
		BA.getPhyAtk().incBonus(attBonus.getPhyAtk() * mult) ;
		BA.getMagAtk().incBonus(attBonus.getMagAtk() * mult) ;
		BA.getPhyDef().incBonus(attBonus.getPhyDef() * mult) ;
		BA.getMagDef().incBonus(attBonus.getMagDef() * mult) ;
		BA.getDex().incBonus(attBonus.getDex() * mult) ;
		BA.getAgi().incBonus(attBonus.getAgi() * mult) ;
		BA.getCritAtk().incBonus(attBonus.getCritAtkChance() * mult) ;
		BA.getCritDef().incBonus(attBonus.getCritDefChance() * mult) ;
		BA.getStun().incAtkChanceBonus(attBonus.getStunAtkChance() * mult) ;
		BA.getStun().incDefChanceBonus(attBonus.getStunDefChance() * mult) ;
		BA.getStun().incDuration(attBonus.getStunDuration() * mult) ;
		BA.getBlock().incAtkChanceBonus(attBonus.getBlockAtkChance() * mult) ;
		BA.getBlock().incDefChanceBonus(attBonus.getBlockDefChance() * mult) ;
		BA.getBlock().incDuration(attBonus.getBlockDuration() * mult) ;
		BA.getBlood().incAtkChanceBonus(attBonus.getBloodAtkChance() * mult) ;
		BA.getBlood().incDefChanceBonus(attBonus.getBloodDefChance() * mult) ;
		BA.getBlood().incAtkBonus(attBonus.getBloodAtk() * mult) ;
		BA.getBlood().incDefBonus(attBonus.getBloodDef() * mult) ;
		BA.getBlood().incDuration(attBonus.getBloodDuration() * mult) ;
		BA.getPoison().incAtkChanceBonus(attBonus.getPoisonAtkChance() * mult) ;
		BA.getPoison().incDefChanceBonus(attBonus.getPoisonDefChance() * mult) ;
		BA.getPoison().incAtkBonus(attBonus.getPoisonAtk() * mult) ;
		BA.getPoison().incDefBonus(attBonus.getPoisonDef() * mult) ;
		BA.getPoison().incDuration(attBonus.getPoisonDuration() * mult) ;
		BA.getSilence().incAtkChanceBonus(attBonus.getSilenceAtkChance() * mult) ;
		BA.getSilence().incDefChanceBonus(attBonus.getSilenceDefChance() * mult) ;
		BA.getSilence().incDuration(attBonus.getSilenceDuration() * mult) ;
	}
	
	public void use(LiveBeing user)
	{		
		if (!(user instanceof Player)) { return ;}
		
		Player player = (Player) user ;
		int type = numTypeFromID(id) ;

		boolean isEquippingTheSameEquip = player.getEquips()[type] != null ? player.getEquips()[type].getId() == id : false ;
		if (player.getEquips()[type] != null)
		{
			unequip(player, player.getEquips()[type]) ;
		}
		
		if (!isEquippingTheSameEquip)
		{
			equip(player, Equip.getAll()[id]) ;
		}
		
	}
	
	private void equip(Player player, Equip equip)
	{
		int type = numTypeFromID(id) ;
		player.getEquips()[type] = equip ;
		player.getElem()[type + 1] = equip.elem ;
		
		if (Player.setIsFormed(player.getEquips()))
		{
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[0], setBonus) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[1], setBonus) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[2], setBonus) ;
		}				
		applyBonus(player.getPA(), player.getBA(), equip, 1) ;

		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.3), equip.getName() + " equipado!", Game.palette[0]}) ;
		player.getElem()[4] = player.hasSuperElement() ? player.getElem()[1] : Elements.neutral ;
		if (player.hasSuperElement())
		{
			player.applySuperElementEffect(player.getElem()[4], true) ;
		}
	}
	
	private void unequip(Player player, Equip equip)
	{
		int type = numTypeFromID(id) ;
		applyBonus(player.getPA(), player.getBA(), equip, -1) ;
		player.getElem()[type + 1] = Elements.neutral ;
		
		if (Player.setIsFormed(player.getEquips()))
		{
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[0], -setBonus) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[1], -setBonus) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[2], -setBonus) ;
		}
		
		player.applySuperElementEffect(player.getElem()[4], false) ;
		player.getElem()[4] = Elements.neutral ;
		
		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.36), equip.getName() + " desequipado!", Game.palette[0]}) ;
		player.getEquips()[type] = null ;
	}
	
	public void displayInfo(Point pos, Align align)
	{
		Game.DP.drawImage(infoMenu, pos, align) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 9) ;
		int nRows = 4 ;
		Point topLeftSlotCenter = Util.Translate(pos, 15 - Util.getSize(infoMenu).width, 18) ;
		int[] attOrder = new int[] {0, 2, 4, 6, 1, 3, 5, 7} ;
		for (int i = 0 ; i <= attOrder.length - 1 ; i += 1)
		{
			Point imagePos = Util.calcGridPos(topLeftSlotCenter, i, nRows, new Point(70, 25)) ;
			Game.DP.drawImage(AttributesWindow.getIcons()[attOrder[i]], imagePos, Align.center) ;
			
			Point textPos = Util.Translate(imagePos, 10, 0) ;
			Game.DP.drawText(textPos, Align.centerLeft, Draw.stdAngle, "+ " + attBonus.all()[attOrder[i]], font, Game.palette[0]) ;
		}
	}
	
	public void display(Point pos, double angle, Scale scale, Align align)
	{
		Game.DP.drawImage(this.fullSizeImage(), pos, angle, scale, align) ;
	}
	
	@Override
	public String toString()
	{
//		return "Equip [id=" + id + ", name = " + name + ", forgeLevel=" + forgeLevel + ", attBonus=" + attBonus + ", elem=" + elem + "]";
		return "Equip," + id + "," + name;
	}
	
	
}
