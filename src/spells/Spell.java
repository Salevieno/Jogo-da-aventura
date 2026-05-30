package spells ;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import attributes.Attributes;
import liveBeings.AttackModifiers;
import liveBeings.LiveBeing;
import main.Elements;
import main.GameTimer;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Spell 
{	
	private final int id ;
	private String name ;
	private String description ;
	private String effect ;
	private int level ;
	private int mpCost ;
	private boolean isActive ;
	private GameTimer cooldownCounter ;
	private GameTimer effectCounter ;

	private final Image image ;
	private final int maxLevel ;
	private final SpellTypes type ;
	private final Map<Integer, Integer> preRequisites ;
	private final int buffID;
	private final int nerfID;
	private final AttackModifiers atkMod ;
	private final Map<Attributes, AttMod> attMods ;
	// private final Map<Attributes, Double> attModPerc ;
	// private final Map<Attributes, Double> attModValue ;
	private final Elements elem ;
	
	private static final List<Spell> ALL = new ArrayList<>() ;

	
	
	protected Spell(int id, String name, String info, String description, SpellTypes type, int cooldown, int duration,
			Elements elem, String imagePath, int maxLevel, int mpCost, Map<Integer, Integer> preRequisites,
			int buffID, int nerfID, Map<Attributes, AttMod> attMods)
	{
		this.id = id ;
		this.name = name;
		this.image = ImageLoader.loadImage(imagePath);
		this.level = 0;
		this.maxLevel = maxLevel;
		this.mpCost = mpCost;
		this.type = type;
		this.preRequisites = preRequisites;
		this.buffID = buffID;
		this.nerfID = nerfID;
		this.attMods = attMods ;
		// this.attModPerc = attModPerc ;
		// this.attModValue = attModValue ;
		this.atkMod = new AttackModifiers() ;
		// this.atkMod = new AttackModifiers(atkMod, defMod, dexMod, agiMod, atkCritMod, defCritMod, stunMod, blockMod, bloodMod, poisonMod, silenceMod) ;
		this.isActive = false;
		this.cooldownCounter = new GameTimer(cooldown / 80.0) ;
		this.effectCounter = new GameTimer(duration / 1.0) ;
		this.elem = elem;
		this.effect = info;
		this.description = description;
		ALL.add(this);
	}

	public Spell(Spell spell)
	{		
		this.id = spell.id ;
		this.name = spell.name;
		this.image = spell.image;
		this.level = spell.level;
		this.maxLevel = spell.maxLevel;
		this.mpCost = spell.mpCost;
		this.type = spell.type;
		this.preRequisites = spell.preRequisites;
		this.buffID = spell.buffID;
		this.nerfID = spell.nerfID;
		this.attMods = spell.getAttMods() ;
		this.atkMod = new AttackModifiers(spell.atkMod.getAtkMod(), spell.atkMod.getDefMod(), spell.atkMod.getDexMod(), spell.atkMod.getAgiMod(), spell.atkMod.getAtkCritMod(), spell.atkMod.getDefCritMod(), spell.atkMod.getStunMod(), spell.atkMod.getBlockMod(), spell.atkMod.getBloodMod(), spell.atkMod.getPoisonMod(), spell.atkMod.getSilenceMod()) ;
		this.isActive = false;
		this.cooldownCounter = new GameTimer(spell.cooldownCounter.getDuration() / 80.0) ;
		this.effectCounter = new GameTimer(spell.effectCounter.getDuration() / 1.0) ;
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
	public Map<Attributes, AttMod> getAttMods() { return attMods ;}
	public AttMod getAttMod(Attributes att)
	{
		if (!attMods.containsKey(att)) { return new AttMod(0, 0) ;}

		return new AttMod(attMods.get(att).getPercentualIncrease() * level, attMods.get(att).getValueIncrease() * level ) ;
	}
	public double[] getSpecialAtt() { return new double[] {
		getAttMod(Attributes.stun).getValueIncrease() * level,
		getAttMod(Attributes.block).getValueIncrease() * level,
		getAttMod(Attributes.blood).getValueIncrease() * level,
		getAttMod(Attributes.poison).getValueIncrease() * level,
		getAttMod(Attributes.silence).getValueIncrease() * level
	} ;}
	public Elements getElem() {return elem ;}
	public GameTimer getCooldownCounter() {return cooldownCounter ;}
	public GameTimer getDurationCounter() {return effectCounter ;}
	public String getEffect() {return effect ;}
	public String getDescription() {return description ;}

	private void setName(String name) {this.name = name ;}
	private void setDescription(String description) {this.description = description ;}
	private void setEffect(String effect) {this.effect = effect ;}

	public boolean isReady() { return cooldownCounter.hasFinished() ;}
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
			if (!preRequisites.keySet().contains(spell.getId())) { continue ;}
			if (spell.getLevel() < preRequisites.get(spell.getId())) { return false ;}
		}
		
		return true ;
		
	}
	
	public void applyBuffs(boolean activate, LiveBeing receiver)
	{
		if (buffID < 0 || Buff.getAllBuffs().size() <= buffID) { return ;}
		
		int mult = activate ? 1 : -1 ;
		Buff.getAllBuffs().get(buffID).apply(mult, level, receiver) ;
	}

	public void applyNerfs(boolean activate, LiveBeing receiver)
	{
		if (buffID < 0 || Buff.getAllNerfs().size() <= buffID) { return ;}

		int mult = activate ? -1 : 1 ;		
		Buff.getAllNerfs().get(nerfID).apply(mult, level, receiver) ;
	}

	public static void create(String language)
	{
		SpellData.createSpells() ;
		updateText(language) ;
	}

	public static void updateText(String language)
	{
		List<String[]> inputs = Util.readcsvFile(Path.DADOS + language + "/SpellsInfo.csv") ;
		for (int i = 0 ; i <= inputs.size() - 1 ; i += 1)
		{
			Spell spell = ALL.get(i) ;
			spell.setName(inputs.get(i)[1]) ;
			spell.setDescription(inputs.get(i)[2]) ;
			spell.setEffect(inputs.get(i)[3]) ;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject content = new JSONObject();
		content.put("id", id);
		content.put("level", level);
		content.put("mpCost", mpCost);
		content.put("isActive", isActive);
		content.put("cooldownCounter", cooldownCounter.toJson());
		content.put("effectCounter", effectCounter.toJson());
		return content;
	}

	public static Spell fromJson(JSONObject jsonData)
	{
		int id = ((Long) jsonData.get("id")).intValue();
		int level = ((Long) jsonData.get("level")).intValue();
		int mpCost = ((Long) jsonData.get("mpCost")).intValue();
		boolean isActive = jsonData.get("isActive") != null && (Boolean) jsonData.get("isActive");
		JSONObject cooldownData = (JSONObject) jsonData.get("cooldownCounter");
		JSONObject effectData = (JSONObject) jsonData.get("effectCounter");
		GameTimer cooldownCounter = GameTimer.fromJson(cooldownData);
		GameTimer effectCounter = GameTimer.fromJson(effectData);

		Spell spell = ALL.get(id) ;
		spell.level = level;
		spell.isActive = isActive;
		spell.mpCost = mpCost ;
		spell.cooldownCounter = cooldownCounter;
		spell.effectCounter = effectCounter;
		return spell;
	}

	public static List<Spell> getAll() { return ALL;}


	@Override
	public String toString()
	{
		return "spell " + id + ": " + name + " level " + level ;
	}

}
