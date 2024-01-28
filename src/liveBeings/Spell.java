package liveBeings ;

import java.awt.Image;
import java.util.List;
import java.util.Map;

import components.SpellTypes;
import main.AtkResults;
import main.AtkTypes;
import utilities.AtkEffects;
import utilities.Elements;
import utilities.FrameCounter;
import utilities.TimeCounter;

public class Spell 
{	
	private int id ;
	private String name ;
	private Image image ;
	private int level ;
	private int maxLevel ;
	private int mpCost ;
	private SpellTypes type ;
	private Map<Spell, Integer> preRequisites ;
	private Buff buffs;
	private Buff deBuffs;
	private double[] atkMod ;
	private double[] defMod ;
	private double[] dexMod ;
	private double[] agiMod ;
	private double[] atkCritMod ;
	private double[] defCritMod ;
	private double[] stunMod ;
	private double[] blockMod ;
	private double[] bloodMod ;
	private double[] poisonMod ;
	private double[] silenceMod ;
	private boolean isActive ;
	private FrameCounter cooldownCounter ;
	private TimeCounter effectCounter ;
	private Elements elem ;
	private String[] info ;	// Effect and description

	
	
	public Spell(int id, String name, Image image, int maxLevel, int mpCost, SpellTypes type, Map<Spell, Integer> preRequisites,
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
		this.atkMod = atkMod;
		this.defMod = defMod;
		this.dexMod = dexMod;
		this.agiMod = agiMod;
		this.atkCritMod = atkCritMod;
		this.defCritMod = defCritMod;
		this.stunMod = stunMod;
		this.blockMod = blockMod;
		this.bloodMod = bloodMod;
		this.poisonMod = poisonMod;
		this.silenceMod = silenceMod;
		isActive = false;
		cooldownCounter = new FrameCounter(0, cooldown) ;
		effectCounter = new TimeCounter(duration) ;
		this.elem = elem;
		this.info = info;
	}


	public int getId() {return id ;}
	public String getName() {return name ;}
	public Image getImage() {return image ;}
	public int getLevel() {return level ;}
	public int getMaxLevel() {return maxLevel ;}
	public int getMpCost() {return mpCost ;}
	public SpellTypes getType() {return type ;}
	public Map<Spell, Integer> getPreRequisites() {return preRequisites ;}
	public int getCooldown() {return cooldownCounter.getDuration() ;}
	public Buff getBuffs() {return buffs ;}
//	public List<Buff> getNerfs() {return nerfs ;}
	public double[] getAtkMod() {return atkMod ;}
	public double[] getDefMod() {return defMod ;}
	public double[] getDexMod() {return dexMod ;}
	public double[] getAgiMod() {return agiMod ;}
	public double[] getAtkCritMod() {return atkCritMod ;}
	public double[] getDefCritMod() {return defCritMod ;}
	public double[] getStunMod() {return stunMod ;}
	public double[] getBlockMod() {return blockMod ;}
	public double[] getBloodMod() {return bloodMod ;}
	public double[] getPoisonMod() {return poisonMod ;}
	public double[] getSilenceMod() {return silenceMod ;}
	public Elements getElem() {return elem ;}
	public FrameCounter getCooldownCounter() {return cooldownCounter ;}
	public TimeCounter getDurationCounter() {return effectCounter ;}
	public String[] getInfo() {return info ;}

	public boolean isReady() { return cooldownCounter.finished() ;}
	public boolean isActive() { return isActive ;}
	public boolean isMaxed() { return level == maxLevel ;}
	public void activate() {isActive = true ; effectCounter.start() ;}
	public void deactivate() {isActive = false ; effectCounter.stop() ; effectCounter.reset() ;}

//	private static void saveImages()
//	{
//		BufferedImage img = UtilG.toBufferedImage(UtilS.loadImage("genericSpell.png")) ;
//		for (int i = 0 ; i <= 172 - 1; i += 1)
//		{
//			PlayerJobs job = PlayerJobs.jobFromSpellID(i) ;
////			System.out.println("spell" + job + i + ".png");
//			try
//			{
//				UtilG.saveImage(img, Game.ImagesPath + "\\Spells\\" + "spell" + job + i) ;
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void incLevel(int increment)
	{
		if (level + increment <= maxLevel)
		{
			level += increment ;
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
//		System.out.println("buff mult " + mult);
//		System.out.println("BA = " + receiver.getBA());
		buffs.apply(mult, level, receiver) ;
//		System.out.println("new BA = " + receiver.getBA());
	}

	public void applyNerfs(boolean activate, LiveBeing receiver)
	{
		
		if (deBuffs == null) { return ;}
		int mult = activate ? 1 : -1 ;
		
		deBuffs.apply(mult, level, receiver) ;
	}
	
	@Override
	public String toString()
	{
		return "spell " + id + ": " + name + " level " + level ;
//		return "Spell [name=" + name + ", image=" + image + ", level=" + level + ", maxLevel=" + maxLevel + ", mpCost=" + mpCost + ", type="
//				+ type + ", preRequisites=" + preRequisites + ", buffs=" + buffs + ", AtkMod=" + Arrays.toString(atkMod) + ", DefMod="
//				+ Arrays.toString(defMod) + ", DexMod=" + Arrays.toString(dexMod) + ", AgiMod="
//				+ Arrays.toString(agiMod) + ", AtkCritMod=" + Arrays.toString(atkCritMod) + ", DefCritMod="
//				+ Arrays.toString(defCritMod) + ", StunMod=" + Arrays.toString(stunMod) + ", BlockMod="
//				+ Arrays.toString(blockMod) + ", BloodMod=" + Arrays.toString(bloodMod) + ", PoisonMod="
//				+ Arrays.toString(poisonMod) + ", SilenceMod=" + Arrays.toString(silenceMod) + ", isActive=" + isActive
//				+ ", cooldownCounter=" + cooldownCounter + ", effectCounter=" + effectCounter + ", elem=" + elem
//				+ ", info=" + Arrays.toString(info) + "]";
	}

}
