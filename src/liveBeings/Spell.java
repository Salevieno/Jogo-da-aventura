package liveBeings ;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import components.SpellTypes;
import main.Game;
import main.Languages;
import utilities.Elements;
import utilities.GameTimer;
import utilities.Util;
import utilities.UtilS;

public class Spell 
{	
	private int level ;
	private int mpCost ;
	private boolean isActive ;

	private final int id ;
	private final String name ;
	private final Image image ;
	private final int maxLevel ;
	private final SpellTypes type ;
	private final Map<Spell, Integer> preRequisites ;
	private final Buff buffs;
	private final Buff deBuffs;
	private final AttackModifiers atkMod ;
	private final GameTimer cooldownCounter ;
	private final GameTimer effectCounter ;
	private final Elements elem ;
	private final String effect ;
	private final String description ;
	
	public static final List<Spell> all = new ArrayList<>() ;

	
	
	private Spell(int id, String name, Image image, int maxLevel, int mpCost, SpellTypes type, Map<Spell, Integer> preRequisites,
			Buff buffs, Buff deBuffs, double[] atkMod, double[] defMod, double[] dexMod, double[] agiMod,
			double[] atkCritMod, double[] defCritMod, double[] stunMod, double[] blockMod, double[] bloodMod,
			double[] poisonMod, double[] silenceMod, int cooldown, int duration, Elements elem, String[] info)
	{
		this.id = id ;
		this.name = name;
		this.image = image;
		level = 0;
		this.maxLevel = maxLevel;
		this.mpCost = mpCost;
		this.type = type;
		this.preRequisites = preRequisites;
		this.buffs = buffs;
		this.deBuffs = deBuffs;
		this.atkMod = new AttackModifiers(atkMod, defMod, dexMod, agiMod, atkCritMod, defCritMod, stunMod, blockMod, bloodMod, poisonMod, silenceMod) ;
		isActive = false;
		cooldownCounter = new GameTimer(cooldown / 80.0) ;
		effectCounter = new GameTimer(duration / 1.0) ;
		this.elem = elem;
		this.effect = info[0];
		this.description = info[1];
		all.add(this);
	}

	public Spell(Spell spell)
	{		
		this.id = spell.id ;
		this.name = spell.name;
		this.image = spell.image;
		level = spell.level;
		this.maxLevel = spell.maxLevel;
		this.mpCost = spell.mpCost;
		this.type = spell.type;
		this.preRequisites = spell.preRequisites;
		this.buffs = spell.buffs;
		this.deBuffs = spell.deBuffs;
		this.atkMod = new AttackModifiers(spell.atkMod.getAtkMod(), spell.atkMod.getDefMod(), spell.atkMod.getDexMod(), spell.atkMod.getAgiMod(), spell.atkMod.getAtkCritMod(), spell.atkMod.getDefCritMod(), spell.atkMod.getStunMod(), spell.atkMod.getBlockMod(), spell.atkMod.getBloodMod(), spell.atkMod.getPoisonMod(), spell.atkMod.getSilenceMod()) ;
		isActive = false;
		cooldownCounter = new GameTimer(spell.cooldownCounter.getDuration() / 80.0) ;
		effectCounter = new GameTimer(spell.effectCounter.getDuration() / 1.0) ;
		this.elem = spell.elem;
		this.effect = spell.effect;
		this.description = spell.description;
	}

	public int getId() {return id ;}
	public String getName() {return name ;}
	public Image getImage() {return image ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<Spell, Integer> getPreRequisites() {return preRequisites ;}
	public double getCooldown() {return cooldownCounter.getDuration() ;}
	public Buff getBuffs() {return buffs ;}
	public double[] getAtkMod() {return atkMod.getAtkMod() ;}
	public double[] getDefMod() {return atkMod.getDefMod() ;}
	public double[] getDexMod() {return atkMod.getDexMod() ;}
	public double[] getAgiMod() {return atkMod.getAgiMod() ;}
	public double[] getAtkCritMod() {return atkMod.getAtkCritMod() ;}
	public double[] getDefCritMod() {return atkMod.getDefCritMod() ;}
	public double[] getStunMod() {return atkMod.getStunMod() ;}
	public double[] getBlockMod() {return atkMod.getBlockMod() ;}
	public double[] getBloodMod() {return atkMod.getBloodMod() ;}
	public double[] getPoisonMod() {return atkMod.getPoisonMod() ;}
	public double[] getSilenceMod() {return atkMod.getSilenceMod() ;}
	public Elements getElem() {return elem ;}
	public GameTimer getCooldownCounter() {return cooldownCounter ;}
	public GameTimer getDurationCounter() {return effectCounter ;}
	public String getEffect() {return effect ;}
	public String getDescription() {return description ;}

	public boolean isReady() { return cooldownCounter.finished() ;}
	public boolean isActive() { return isActive ;}
	public boolean isMaxed() { return level == maxLevel ;}
	public boolean isUsable() { return !type.equals(SpellTypes.passive) & !type.equals(SpellTypes.auto) & 0 < level ;}
	public void activate() {isActive = true ; effectCounter.start() ;}
	public void deactivate() {isActive = false ; effectCounter.stop() ; effectCounter.reset() ;}
	
	public void incLevel(int increment)
	{
		if (increment <= 0) { return ;}
		int inc = Math.min(increment, maxLevel - level) ;
		level += inc ;
		if (2 <= level)
		{
			for (int i = 0 ; i <= inc - 1 ; i += 1)
			{
				mpCost *= 1.2 ;
			}
		}
	}

	public boolean hasPreRequisitesMet(List<Spell> playerSpells)
	{
		if (preRequisites.isEmpty()) { return true ;}
		
		for (Spell spell : playerSpells)
		{
			if (!preRequisites.keySet().contains(spell)) { continue ;}
			if (spell.getLevel() < preRequisites.get(spell)) { return false ;}
		}
		
		return true ;
		
	}
	
	public void applyBuffs(boolean activate, LiveBeing receiver)
	{
		if (buffs == null) { return ;}
		
		int mult = activate ? 1 : -1 ;
		buffs.apply(mult, level, receiver) ;
	}

	public void applyDebuffs(boolean activate, LiveBeing receiver)
	{
		
		if (deBuffs == null) { return ;}
		int mult = activate ? -1 : 1 ;
		
		deBuffs.apply(mult, level, receiver) ;
	}
	
	public static void load(Languages language, List<Buff> allBuffs, List<Buff> allDebuffs)
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "SpellTypes.csv") ;

		Spell[] allSpells = new Spell[input.size()] ;
		String[][] info = new String[allSpells.length][2] ;

		for (int row = 0 ; row <= allSpells.length - 1 ; row += 1)
		{
			int id = row ;
			String[] col = input.get(row) ;
			info[row] = new String[] { col[44], col[45 + 2 * language.ordinal()] } ;
			String name = col[4] ;
			String job = PlayerJobs.jobFromSpellID(row).toString() ;
			Image image = UtilS.loadImage("\\Spells\\" + "spell" + job + row + ".png") ;
			int maxLevel = Integer.parseInt(col[5]) ;
			int mpCost = Integer.parseInt(col[6]) ;
			SpellTypes type = SpellTypes.valueOf(col[7]) ;
			Map<Spell, Integer> preRequisites = new HashMap<>() ;
			for (int p = 0 ; p <= 6 - 1 ; p += 2)
			{
				if (-1 < Integer.parseInt(col[p + 8]))
				{
					preRequisites.put(allSpells[Integer.parseInt(col[p + 8])], Integer.parseInt(col[p + 9])) ;
				}
			}
			int cooldown = Integer.parseInt(col[14]) ;
			int duration = Integer.parseInt(col[15]) ;
			double[] atkMod = new double[] { Double.parseDouble(col[16]), Double.parseDouble(col[17]) } ;
			double[] defMod = new double[] { Double.parseDouble(col[18]), Double.parseDouble(col[19]) } ;
			double[] dexMod = new double[] { Double.parseDouble(col[20]), Double.parseDouble(col[21]) } ;
			double[] agiMod = new double[] { Double.parseDouble(col[22]), Double.parseDouble(col[23]) } ;
			double[] atkCritMod = new double[] { Double.parseDouble(col[24]) } ;
			double[] defCritMod = new double[] { Double.parseDouble(col[25]) } ;
			double[] stunMod = new double[] { Double.parseDouble(col[26]), Double.parseDouble(col[27]), Double.parseDouble(col[28]) } ;
			double[] blockMod = new double[] { Double.parseDouble(col[29]), Double.parseDouble(col[30]), Double.parseDouble(col[31]) } ;
			double[] bloodMod = new double[] { Double.parseDouble(col[32]), Double.parseDouble(col[33]), Double.parseDouble(col[34]) } ;
			double[] poisonMod = new double[] { Double.parseDouble(col[35]), Double.parseDouble(col[36]), Double.parseDouble(col[37]) } ;
			double[] silenceMod = new double[] { Double.parseDouble(col[38]), Double.parseDouble(col[39]), Double.parseDouble(col[40]) } ;

			int buffId = col[41].equals("-") ? -1 : Integer.parseInt(col[41]) ;
			int debuffId = col[42].equals("-") ? -1 : Integer.parseInt(col[42]) ;
			Buff buffs = buffId == -1 ? null : allBuffs.get(buffId);
			Buff debuffs = debuffId == -1 ? null : allDebuffs.get(debuffId);
			Elements elem = Elements.valueOf(col[43]) ;

			new Spell(id, name, image, maxLevel, mpCost, type, preRequisites, buffs, debuffs, atkMod,
					defMod, dexMod, agiMod, atkCritMod, defCritMod, stunMod, blockMod, bloodMod, poisonMod, silenceMod,
					cooldown, duration, elem, info[row]) ;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject content = new JSONObject();
		content.put("id", id);
		content.put("name", name);
		content.put("level", level);
		content.put("maxLevel", maxLevel);
		content.put("mpCost", mpCost);
		content.put("type", type != null ? type.name() : null);
		content.put("isActive", isActive);
		content.put("elem", elem != null ? elem.name() : null);
//		content.put("info0", info != null && info.length > 0 ? info[0] : null);
//		content.put("info1", info != null && info.length > 1 ? info[1] : null);
		content.put("cooldownCounter", cooldownCounter != null ? cooldownCounter.toJson() : null);
		content.put("effectCounter", effectCounter != null ? effectCounter.toJson() : null);
		// Note: Buffs, deBuffs, atkMod, preRequisites, and image are omitted for simplicity.
		return content;
	}

	public static Spell fromJson(JSONObject jsonData)
	{
		int id = ((Long) jsonData.get("id")).intValue();
		String name = (String) jsonData.get("name");
		int level = ((Long) jsonData.get("level")).intValue();
		int maxLevel = ((Long) jsonData.get("maxLevel")).intValue();
		int mpCost = ((Long) jsonData.get("mpCost")).intValue();
		SpellTypes type = jsonData.get("type") != null ? SpellTypes.valueOf((String) jsonData.get("type")) : null;
		boolean isActive = jsonData.get("isActive") != null && (Boolean) jsonData.get("isActive");
		Elements elem = jsonData.get("elem") != null ? Elements.valueOf((String) jsonData.get("elem")) : null;
		String[] info = new String[] {
			(String) jsonData.get("info0"),
			(String) jsonData.get("info1")
		};
		JSONObject cooldownObj = (JSONObject) jsonData.get("cooldownCounter");
		JSONObject effectObj = (JSONObject) jsonData.get("effectCounter");
		GameTimer cooldownCounter = cooldownObj != null ? GameTimer.fromJson(cooldownObj) : null;
		GameTimer effectCounter = effectObj != null ? GameTimer.fromJson(effectObj) : null;

		// The following fields are set to null/defaults as full deserialization is not implemented here:
		Image image = null;
		Map<Spell, Integer> preRequisites = new HashMap<>();
		Buff buffs = null;
		Buff deBuffs = null;
		double[] atkMod = new double[2];
		double[] defMod = new double[2];
		double[] dexMod = new double[2];
		double[] agiMod = new double[2];
		double[] atkCritMod = new double[1];
		double[] defCritMod = new double[1];
		double[] stunMod = new double[3];
		double[] blockMod = new double[3];
		double[] bloodMod = new double[3];
		double[] poisonMod = new double[3];
		double[] silenceMod = new double[3];
		int cooldown = 0;
		int duration = 0;

		Spell spell = new Spell(id, name, image, maxLevel, mpCost, type, preRequisites, buffs, deBuffs,
			atkMod, defMod, dexMod, agiMod, atkCritMod, defCritMod, stunMod, blockMod, bloodMod, poisonMod, silenceMod,
			cooldown, duration, elem, info);
		spell.level = level;
		spell.isActive = isActive;
//		if (cooldownCounter != null) spell.cooldownCounter = cooldownCounter;
//		if (effectCounter != null) spell.effectCounter = effectCounter;
		return spell;
	}


	@Override
	public String toString()
	{
		return "spell " + id + ": " + name + " level " + level ;
	}

}
