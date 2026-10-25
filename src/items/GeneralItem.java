package items;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import attributes.Attributes;
import graphics.Align;
import liveBeings.AttackModifiers;
import liveBeings.LiveBeing;
import liveBeings.Player;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import maps.GroundType;
import utilities.Util;


public class GeneralItem extends Item
{
	private final double power ;
	private final AttackModifiers atkMod ;
	private final Elements elem ;	
	
	private static final Image GENERAL_ITEM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconGenItem.png") ;
	private static final GeneralItem[] ALL = new GeneralItem[400];
	
	public GeneralItem(int id, int price, double dropChance, int power, Elements elem)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.power = power ;
		this.atkMod = calcAtkMod(id) ;
		this.elem = elem ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/GeneralItemText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}
	
	private static AttackModifiers calcAtkMod(int id)
	{
		return switch (id)
		{
			case 64 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {0.5, 0, 0}, new double[3]) ;
			case 65 -> new AttackModifiers(new double[3], new double[3], new double[] {1, 0, 0}, new double[3], new double[3]) ;
			case 66 -> new AttackModifiers(new double[3], new double[] {0.5, 0, 0}, new double[3], new double[3], new double[3]) ;
			case 67 -> new AttackModifiers(new double[] {0.5, 0, 0}, new double[3], new double[3], new double[3], new double[3]) ;
			case 69 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[3], new double[] {1, 0, 0}) ;
			case 71 -> new AttackModifiers(new double[] {1, 0, 0}, new double[3], new double[3], new double[3], new double[3]) ;
			case 73 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {0.8, 0, 0}, new double[3]) ;
			case 75 -> new AttackModifiers(new double[3], new double[] {1, 0, 0}, new double[3], new double[3], new double[3]) ;
			case 78 -> new AttackModifiers(new double[3], new double[3], new double[3], new double[] {1, 0, 0}, new double[3]) ;
			default -> new AttackModifiers() ;
		};
	}
	
	public static List<Item> throwableItems() { return Arrays.asList(ALL).stream().filter(item -> item.isThrowable()).collect(Collectors.toList()) ;}
	
	public boolean isThrowable() { return 0 < power ;}

	public static GeneralItem[] getAll() {return ALL ;}
	
	public double getPower() { return power ;}
	public Elements getElem() { return elem ;}
	public AttackModifiers getAtkMod() { return atkMod ;}
	
	public static Image imageFromID(int id)
	{		
		return GENERAL_ITEM_ICON ;		
	}
	
	public void use(LiveBeing target)
	{		
		switch (id)
		{
			case 21:
				if (!target.isPlayerAlly()) { return ;}
				
				target.getHpCounter().restart() ;
                Game.getPlayer().getBag().remove(this, 1) ;				
				return ;
				
			case 22:
				if (!target.isPlayerAlly()) { return ;}
				
				target.getStatus().get(Attributes.poison).reset() ;
                Game.getPlayer().getBag().remove(this, 1) ;				
				return ;
				
			case 23:
				if (!target.isPlayerAlly()) { return ;}
                if (!target.canIncreatePersonalAttribute(target.getPA().getLife(), 20)) { return ;}

                target.incPersonalAttribute(target.getPA().getLife(), 20, true) ;
                Game.getPlayer().getBag().remove(this, 1) ;
				return ;
				
			case 26, 27, 28: 
				if (!(target instanceof Player) | !target.isTouching(GroundType.water)) { return ;}
				
				((Player) target).getBag().add(ALL[id + 3], 1) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
			case 29:
                target.incPersonalAttribute(target.getPA().getThirst(), 30, true) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
            case 30:
                target.incPersonalAttribute(target.getPA().getThirst(), 60, true) ; 
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
            case 31:
                target.incPersonalAttribute(target.getPA().getThirst(), 100, true) ; 
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
            case 74:
                target.getsDrunk(20) ; 
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;

			case 79:
				target.getBA().getAtkSpeed().incBonus(-target.getStatus().get(Attributes.atkSpeed).getIntensity()) ;
				target.getBA().getAtkSpeed().incBonus(-0.2) ;
				target.getBattleActionCounter().setDuration(target.getBA().TotalAtkSpeed()) ;
				target.getStatus().get(Attributes.atkSpeed).inflictStatus(-0.2, 5) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 81, 84:
                target.getStatus().get(Attributes.blood).reset() ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
			case 82:
				target.getStatus().get(Attributes.blood).reset() ;
				target.getBA().getAtkSpeed().incBonus(-target.getStatus().get(Attributes.atkSpeed).getIntensity()) ;
				target.getBA().getAtkSpeed().incBonus(0.2) ;
				target.getBattleActionCounter().setDuration(target.getBA().TotalAtkSpeed()) ;
				target.getStatus().get(Attributes.atkSpeed).inflictStatus(0.2, 5) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 83:
				target.getBA().getAtkSpeed().incBonus(-target.getStatus().get(Attributes.atkSpeed).getIntensity()) ;
				target.getBA().getAtkSpeed().incBonus(-0.3) ;
				target.getBattleActionCounter().setDuration(target.getBA().TotalAtkSpeed()) ;
				target.getStatus().get(Attributes.atkSpeed).inflictStatus(-0.3, 5) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;

			case 86:
				target.getBA().getDex().incBonus(-target.getStatus().get(Attributes.dex).getIntensity()) ;
				target.getBA().getDex().incBonus(10) ;
				target.getStatus().get(Attributes.dex).inflictStatus(10, 100) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 87:
				target.getBA().getDex().incBonus(-target.getStatus().get(Attributes.dex).getIntensity()) ;
				target.getBA().getDex().incBonus(20) ;
				target.getStatus().get(Attributes.dex).inflictStatus(20, 100) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 89:
				target.getBA().getMagAtk().incBonus(-target.getStatus().get(Attributes.magAtk).getIntensity()) ;
				target.getBA().getMagDef().incBonus(-target.getStatus().get(Attributes.magDef).getIntensity()) ;
				target.getBA().getPhyDef().incBonus(-target.getStatus().get(Attributes.phyDef).getIntensity()) ;
				target.getBA().getMagAtk().incBonus(5) ;
				target.getBA().getMagDef().incBonus(5) ;
				target.getBA().getPhyDef().incBonus(5) ;
				target.getStatus().get(Attributes.magAtk).inflictStatus(5, 100) ;
				target.getStatus().get(Attributes.magDef).inflictStatus(5, 100) ;
				target.getStatus().get(Attributes.phyDef).inflictStatus(5, 100) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 105:
                target.getsDrunk(50) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;

			case 106:
                target.getsDrunk(150) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
			
			case 109:
				target.getBA().getAgi().incBonus(-target.getStatus().get(Attributes.agi).getIntensity()) ;
				target.getBA().getAgi().incBonus(-10) ;
				target.getStatus().get(Attributes.agi).inflictStatus(-10, 100) ;
                Game.getPlayer().getBag().remove(this, 1) ;
                return ;
				
			case 111: 
				if (!target.isPlayerAlly()) { return ;}
				
				target.getStatus().get(Attributes.silence).reset() ;
                Game.getPlayer().getBag().remove(this, 1) ;			
				return ;
		}
	}

	public void displayInfo(Point pos, Align align)
	{
		drawMenu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
		Font font = new Font(Game.getMainFontName(), Font.BOLD, 9) ;
		Point textPos = Util.translate(pos, 5 - Util.getSize(INFO_MENU_IMAGE).width, 10) ;		
		GamePanel.getDP().drawText(textPos, Align.centerLeft, description, font, Palette.colors[0]) ;
	}
	
	@Override
	public String toString()
	{
		return name ;
	}
}
