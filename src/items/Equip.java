package items;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import animations.MessageAnimation;
import attributes.AttributeBonus;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.Align;
import graphics.Scale;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Log;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;
import windows.AttributesWindow;

public class Equip extends Item
{
	private final AttributeBonus attBonus ;
	private final Elements originalElem ;
	private int forgeLevel ;
	private Elements elem ;

	private static final double SET_BONUS = 0.2 ;
	private static final int MAX_FORGE_LEVEL = 10 ;
	
	private static final Image SWORD_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSword.png") ;
	private static final Image STAFF_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconStaff.png") ;
	private static final Image BOW_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconBow.png") ;
	private static final Image CLAWS_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconClaws.png") ;
	private static final Image DAGGER_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconDagger.png") ;
	private static final Image SHIELD_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconShield.png") ;
	private static final Image ARMOR_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconArmor.png") ;
	private static final Image EMBLEM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconEmblem.png") ;
	
	private static final Image SWORD_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq0_Sword.png") ;
	private static final Image STAFF_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq1_Staff.png") ;
	private static final Image BOW_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq2_Bow.png") ;
	private static final Image CLAWS_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq3_Claws.png") ;
	private static final Image DAGGER_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq4_Dagger.png") ;
	private static final Image SHIELD_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq5_Shield.png") ;
	private static final Image ARMOR_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq6_Armor.png") ;
	private static final Image EMBLEM_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq8_emblem.png") ;
	
	private static final Image SHINING_SWORD_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq0_ShiningSword.png") ;
	private static final Image SHINING_STAFF_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq1_ShiningStaff.png") ;
	private static final Image SHINING_BOW_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq2_ShiningBow.png") ;
	private static final Image SHINING_CLAWS_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq3_ShiningClaws.png") ;
	private static final Image SHINING_DAGGER_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq4_ShiningDagger.png") ;
	private static final Image SHINING_SHIELD_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq5_ShiningShield.png") ;
	private static final Image SHINING_ARMOR_IMAGE = ImageLoader.loadImage(Path.EQUIPS_IMG + "Eq6_ShiningArmor.png") ;
	private static final Equip[] ALL = new Equip[1000];

	public Equip(int id, int price, double dropChance, AttributeBonus attBonus, Elements elem)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.forgeLevel = 0 ;
		this.attBonus = attBonus ;
		this.elem = elem ;
		originalElem = elem ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/EquipText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
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
		
		Image[] equipImages = new Image[] {SWORD_ICON, STAFF_ICON, BOW_ICON, CLAWS_ICON, DAGGER_ICON, SHIELD_ICON, ARMOR_ICON, EMBLEM_ICON} ;
		return equipImages[Arrays.asList(EquipTypes.values()).indexOf(typeFromID(id))] ;
		
	}

	public Image fullSizeImage()
	{
		switch(typeFromID(id))
		{
			case sword: return forgeLevel == 10 ? SHINING_SWORD_IMAGE : SWORD_IMAGE ;
			case staff: return forgeLevel == 10 ? SHINING_STAFF_IMAGE : STAFF_IMAGE ;
			case bow: return forgeLevel == 10 ? SHINING_BOW_IMAGE : BOW_IMAGE ;
			case claws: return forgeLevel == 10 ? SHINING_CLAWS_IMAGE : CLAWS_IMAGE ;
			case dagger: return forgeLevel == 10 ? SHINING_DAGGER_IMAGE : DAGGER_IMAGE ;
			case shield: return forgeLevel == 10 ? SHINING_SHIELD_IMAGE : SHIELD_IMAGE ;
			case armor: return forgeLevel == 10 ? SHINING_ARMOR_IMAGE : ARMOR_IMAGE ;
			case emblem: return EMBLEM_IMAGE ;
			default: return null ;
		}
	}
	
	public int getForgeLevel() {return forgeLevel ;}
	public Elements getElem() {return elem ;}
	public void setElem(Elements newElem) { elem = newElem ;}
	public AttributeBonus getAttributeBonus() {return attBonus ;}
	public static Equip[] getAll() {return ALL ;}

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
		
		Log.warn("Verificação se o equipamento é uma arma com item que não é equipamento");
		return false ;
	}
	
	public void resetElem() { elem = originalElem ;}
	
	public boolean isAtMaxForgeLevel() { return forgeLevel == MAX_FORGE_LEVEL ;}
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
//		player.getElem()[type + 1] = equip.elem ;
		
		if (Player.setIsFormed(player.getEquips()))
		{
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[0], SET_BONUS) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[1], SET_BONUS) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[2], SET_BONUS) ;
		}			
		applyBonus(player.getPA(), player.getBA(), equip, 1) ;

		// Animation.start(AnimationTypes.message, new Object[] {Screen.getMe().pos(0.4, 0.3), equip.getName() + " equipado!", Palette.colors[0]}) ;
		MessageAnimation.start(Screen.getMe().pos(0.4, 0.3), equip.getName() + " equipado!", Palette.colors[0]) ;
		
		player.updateSuperElem() ;
//		player.getElem()[4] = player.hasSuperElement() ? equip.elem : Elements.neutral ;
		if (player.hasSuperElement())
		{
			player.applySuperElementEffect(player.getSuperElem(), true) ;
		}
	}
	
	private void unequip(Player player, Equip equip)
	{
		int type = numTypeFromID(id) ;
		applyBonus(player.getPA(), player.getBA(), equip, -1) ;
//		player.getElem()[type + 1] = Elements.neutral ;
		
		if (Player.setIsFormed(player.getEquips()))
		{
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[0], -SET_BONUS) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[1], -SET_BONUS) ;
			applyBonus(player.getPA(), player.getBA(), player.getEquips()[2], -SET_BONUS) ;
		}
		
		player.applySuperElementEffect(player.getSuperElem(), false) ;
		player.updateSuperElem() ;
//		player.getElem()[4] = Elements.neutral ;
		
		// Animation.start(AnimationTypes.message, new Object[] {Screen.getMe().pos(0.4, 0.36), equip.getName() + " desequipado!", Palette.colors[0]}) ;
		MessageAnimation.start(Screen.getMe().pos(0.4, 0.36), equip.getName() + " desequipado!", Palette.colors[0]) ;
		
		player.getEquips()[type] = null ;
	}
	
	public void displayInfo(Point pos, Align align)
	{
		GamePanel.getDP().drawImage(INFO_MENU_IMAGE, pos, align) ;
		Font font = new Font(Game.getMainFontName(), Font.BOLD, 9) ;
		int nRows = 4 ;
		Point topLeftSlotCenter = Util.translate(pos, 15 - Util.getSize(INFO_MENU_IMAGE).width, 18) ;
		int[] attOrder = new int[] {0, 2, 4, 6, 1, 3, 5, 7} ;
		for (int i = 0 ; i <= attOrder.length - 1 ; i += 1)
		{
			Point imagePos = Util.calcGridPos(topLeftSlotCenter, i, nRows, new Point(70, 25)) ;
			GamePanel.getDP().drawImage(AttributesWindow.getIcons()[attOrder[i]], imagePos, Align.center) ;
			
			Point textPos = Util.translate(imagePos, 10, 0) ;
			GamePanel.getDP().drawText(textPos, Align.centerLeft, "+ " + attBonus.all()[attOrder[i]], font, Palette.colors[0]) ;
		}
	}
	
	public void display(Point pos, double angle, Scale scale, Align align)
	{
		GamePanel.getDP().drawImage(this.fullSizeImage(), pos, angle, scale, align) ;
	}
	
	@Override
	public String toString()
	{
//		return "Equip [id=" + id + ", name = " + name + ", forgeLevel=" + forgeLevel + ", attBonus=" + attBonus + ", elem=" + elem + "]";
		return "Equip," + id + "," + name;
	}
	
	
}
