package attributes ;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import utilities.Elements;

public class BattleAttributes
{
	private BasicBattleAttribute phyAtk ;
	private BasicBattleAttribute magAtk ;
	private BasicBattleAttribute phyDef ;
	private BasicBattleAttribute magDef ;
	private BasicBattleAttribute dex ;
	private BasicBattleAttribute agi ;
	private BasicBattleAttribute critAtk ;
	private BasicBattleAttribute critDef ;	
	private BattleSpecialAttribute stun ;
	private BattleSpecialAttribute block ;
	private BattleSpecialAttributeWithDamage blood ;
	private BattleSpecialAttributeWithDamage poison ;
	private BattleSpecialAttribute silence ;
	private BasicBattleAttribute atkSpeed ;
	private Map<Elements, Double> elemResistanceMult ;
	
	public BattleAttributes(BasicBattleAttribute phyAtk, BasicBattleAttribute magAtk, BasicBattleAttribute phyDef, BasicBattleAttribute magDef, BasicBattleAttribute dex, BasicBattleAttribute agi,
			BasicBattleAttribute critAtk, BasicBattleAttribute critDef,
			BattleSpecialAttribute stun, BattleSpecialAttribute block, BattleSpecialAttributeWithDamage blood, BattleSpecialAttributeWithDamage poison, BattleSpecialAttribute silence,
			BasicBattleAttribute atkSpeed)
	{
		this.phyAtk = phyAtk ;
		this.magAtk = magAtk ;
		this.phyDef = phyDef ;
		this.magDef = magDef ;
		this.dex = dex ;
		this.agi = agi ;
		this.critAtk = critAtk ;
		this.critDef = critDef ;
		this.stun = stun ;
		this.block = block ;
		this.blood = blood ;
		this.poison = poison ;
		this.silence = silence ;
		this.atkSpeed = atkSpeed ;
		elemResistanceMult = new HashMap<>() ;
		for (Elements elem : Elements.values())
		{
			elemResistanceMult.put(elem, 1.0) ;
		}
	}
	
	public BattleAttributes(BattleAttributes BA)
	{
		this.phyAtk = new BasicBattleAttribute(BA.getPhyAtk()) ;
		this.magAtk = new BasicBattleAttribute(BA.getMagAtk()) ;
		this.phyDef = new BasicBattleAttribute(BA.getPhyDef()) ;
		this.magDef = new BasicBattleAttribute(BA.getMagDef()) ;
		this.dex = new BasicBattleAttribute(BA.getDex()) ;
		this.agi = new BasicBattleAttribute(BA.getAgi()) ;
		this.critAtk = new BasicBattleAttribute(BA.getCritAtk()) ;
		this.critDef = new BasicBattleAttribute(BA.getCritDef()) ;
		this.stun = new BattleSpecialAttribute(BA.getStun()) ;
		this.block = new BattleSpecialAttribute(BA.getBlock()) ;
		this.blood = new BattleSpecialAttributeWithDamage(BA.getBlood()) ;
		this.poison = new BattleSpecialAttributeWithDamage(BA.getPoison()) ;
		this.silence = new BattleSpecialAttribute(BA.getSilence()) ;
		this.atkSpeed = new BasicBattleAttribute(BA.getAtkSpeed()) ;

		elemResistanceMult = new HashMap<>() ;
		for (Elements elem : Elements.values())
		{
			elemResistanceMult.put(elem, 1.0) ;
		}
	}


	
//	public BattleAttributes(double[] initialAtts)
//	{
//		phyAtk = new BasicBattleAttribute(initialAtts[5], 0, 0) ;
//		magAtk = new BasicBattleAttribute(initialAtts[6], 0, 0) ;
//		phyDef = new BasicBattleAttribute(initialAtts[7], 0, 0) ;
//		magDef = new BasicBattleAttribute(initialAtts[8], 0, 0) ;
//		dex = new BasicBattleAttribute(initialAtts[9], 0, 0) ;
//		agi = new BasicBattleAttribute(initialAtts[10], 0, 0) ;
//		critAtk = new BasicBattleAttribute(initialAtts[11], 0, 0) ;
//		critDef = new BasicBattleAttribute(initialAtts[12], 0, 0) ;
//		stun = new BattleSpecialAttribute(initialAtts[13], 0, initialAtts[14], 0, initialAtts[15]) ;
//		block = new BattleSpecialAttribute(initialAtts[16], 0, initialAtts[17], 0, initialAtts[18]) ;
//		blood = new BattleSpecialAttributeWithDamage(initialAtts[19], 0, initialAtts[20], 0, initialAtts[21], 0, initialAtts[22], 0, initialAtts[23]) ;
//		poison = new BattleSpecialAttributeWithDamage(initialAtts[24], 0, initialAtts[25], 0, initialAtts[26], 0, initialAtts[27], 0, initialAtts[28]) ;
//		silence = new BattleSpecialAttribute(initialAtts[29], 0, initialAtts[30], 0, initialAtts[31]) ;
//		status = new HashMap<>() ;
//		for (Attributes att : Attributes.values())
//		{
//			status.put(att, new LiveBeingStatus(att)) ;
//		}
//	}
	
	public BattleAttributes(String[] initialAtts, double attMult, String atkSpeed)
	{		
		phyAtk = new BasicBattleAttribute(Double.parseDouble(initialAtts[5]) * attMult, 0, 0) ;
		magAtk = new BasicBattleAttribute(Double.parseDouble(initialAtts[6]) * attMult, 0, 0) ;
		phyDef = new BasicBattleAttribute(Double.parseDouble(initialAtts[7]) * attMult, 0, 0) ;
		magDef = new BasicBattleAttribute(Double.parseDouble(initialAtts[8]) * attMult, 0, 0) ;
		dex = new BasicBattleAttribute(Double.parseDouble(initialAtts[9]) * attMult, 0, 0) ;
		agi = new BasicBattleAttribute(Double.parseDouble(initialAtts[10]) * attMult, 0, 0) ;
		critAtk = new BasicBattleAttribute(Double.parseDouble(initialAtts[11]) * attMult, 0, 0) ;
		critDef = new BasicBattleAttribute(Double.parseDouble(initialAtts[12]) * attMult, 0, 0) ;
		stun = new BattleSpecialAttribute(Double.parseDouble(initialAtts[13]) * attMult, 0, Double.parseDouble(initialAtts[14]) * attMult, 0, Double.parseDouble(initialAtts[15]) * attMult) ;
		block = new BattleSpecialAttribute(Double.parseDouble(initialAtts[16]) * attMult, 0, Double.parseDouble(initialAtts[17]) * attMult, 0, Double.parseDouble(initialAtts[18]) * attMult) ;
		blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(initialAtts[19]) * attMult, 0,
													Double.parseDouble(initialAtts[20]) * attMult, 0,
													Double.parseDouble(initialAtts[21]) * attMult, 0,
													Double.parseDouble(initialAtts[22]) * attMult, 0,
													Double.parseDouble(initialAtts[23]) * attMult) ;
		poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(initialAtts[24]) * attMult, 0,
													Double.parseDouble(initialAtts[25]) * attMult, 0,
													Double.parseDouble(initialAtts[26]) * attMult, 0,
													Double.parseDouble(initialAtts[27]) * attMult, 0,
													Double.parseDouble(initialAtts[28]) * attMult) ;
		silence = new BattleSpecialAttribute(Double.parseDouble(initialAtts[29]) * attMult, 0, Double.parseDouble(initialAtts[30]) * attMult, 0, Double.parseDouble(initialAtts[31]) * attMult) ;
		this.atkSpeed = new BasicBattleAttribute(Double.parseDouble(atkSpeed) * attMult, 0, 0) ;
		
		elemResistanceMult = new HashMap<>() ;
		for (Elements elem : Elements.values())
		{
			elemResistanceMult.put(elem, 1.0) ;
		}
	}
	
	public BasicBattleAttribute getPhyAtk() {return phyAtk ;}
	public BasicBattleAttribute getMagAtk() {return magAtk ;}
	public BasicBattleAttribute getPhyDef() {return phyDef ;}
	public BasicBattleAttribute getMagDef() {return magDef ;}
	public BasicBattleAttribute getDex() {return dex ;}
	public BasicBattleAttribute getAgi() {return agi ;}
	public BasicBattleAttribute getCritAtk() {return critAtk ;}
	public BasicBattleAttribute getCritDef() {return critDef ;}
	public BattleSpecialAttribute getStun() {return stun ;}
	public BattleSpecialAttribute getBlock() {return block ;}
	public BattleSpecialAttributeWithDamage getBlood() {return blood ;}
	public BattleSpecialAttributeWithDamage getPoison() {return poison ;}
	public BattleSpecialAttribute getSilence() {return silence ;}
	public BasicBattleAttribute getAtkSpeed() {return atkSpeed ;}
	public Map<Elements, Double> getElemResistanceMult() { return elemResistanceMult ;}

//	public void checkResetStatus() { status.values().stream().filter(st -> !st.isActive()).forEach(st -> st.reset()) ;}
	


	public BasicBattleAttribute mapAttributes(Attributes att)
	{
		switch (att)
		{
			case phyAtk: return phyAtk ;
			case magAtk: return magAtk ;
			case phyDef: return phyDef ;
			case magDef: return magDef ;
			case dex: return dex ;
			case agi: return agi ;
			case critAtk: return critAtk ;
			case critDef: return critDef ;
			case atkSpeed: return atkSpeed ;
			
			default: return null ;
		}
	}
	
	public BattleSpecialAttribute mapSpecialAttributes(Attributes att)
	{
		switch (att)
		{
			case stun: return stun ;
			case block: return block ;
			case blood: return blood ;
			case poison: return poison ;
			case silence: return silence ;
			
			default: return null ;
		}
	}
	
	public void setElemResistance(Elements elem, double amount) { elemResistanceMult.put(elem, amount) ;}
	
	public double TotalPhyAtk()
	{
		return phyAtk.getTotal() ;
	}
	public double TotalMagAtk()
	{
		return magAtk.getTotal() ;
	}
	public double TotalPhyDef()
	{
		return phyDef.getTotal() ;
	}
	public double TotalMagDef()
	{
		return magDef.getTotal() ;
	}
	public double TotalDex()
	{
		return dex.getTotal() ;
	}
	public double TotalAgi()
	{
		return agi.getTotal() ;
	}
	public double TotalCritAtkChance()
	{
		return critAtk.getTotal() ;
	}
	public double TotalCritDefChance()
	{
		return critDef.getTotal() ;
	}
	public double TotalAtkSpeed()
	{
		return atkSpeed.getTotal() ;
	}
	
	public BasicBattleAttribute[] basicAttributes() { return new BasicBattleAttribute[] {getPhyAtk(), getMagAtk(), getPhyDef(), getMagDef(), getDex(), getAgi(), critAtk, critDef};}
	public BattleSpecialAttribute[] specialAttributes() { return new BattleSpecialAttribute[] {stun, block, blood, poison, silence} ;}
//	public double[] getBaseValues()
//	{
//		return new double[] {
//				phyAtk.getBaseValue(), magAtk.getBaseValue(), phyDef.getBaseValue(), magDef.getBaseValue(),
//				dex.getBaseValue(), agi.getBaseValue(), critAtk.getBaseValue(),
//				stun.getBasicAtkChance(),
//				block.getBasicAtkChance(),
//				blood.getBasicAtkChance(), blood.getBasicDefChance(), blood.getBasicAtk(), blood.getBasicDef(),
//				poison.getBasicAtkChance(), poison.getBasicDefChance(), poison.getBasicAtk(),poison.getBasicDef(),
//				silence.getBasicAtkChance()
//				} ;
//	}
	public double[] totalValues() { return new double[] {TotalPhyAtk(), TotalMagAtk(), TotalPhyDef(), TotalMagDef(), TotalDex(), TotalAgi()} ;}
	public double[] baseAtkChances()
	{
		return new double[] {stun.TotalAtkChance(), block.TotalAtkChance(), blood.TotalAtkChance(), poison.TotalAtkChance(), silence.TotalAtkChance()} ;
	}
	public double[] baseDefChances()
	{
		return new double[] {stun.TotalDefChance(), block.TotalDefChance(), blood.TotalDefChance(), poison.TotalDefChance(), silence.TotalDefChance()} ;
	}
	public double[] baseDurations()
	{
		return new double[] {stun.getDuration(), block.getDuration(), blood.getDuration(), poison.getDuration(), silence.getDuration()} ;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        for (Attributes att : Attributes.getBattle())
        {
            content.put(att.toString(), mapAttributes(att).toJsonObject());
        }
        for (Attributes att : Attributes.getSpecial())
        {
            content.put(att.toString(), mapSpecialAttributes(att).toJsonObject());
        }
        
        return content ;
        
	}

	
	public static BattleAttributes fromJson(JSONObject jsonData)
	{

		BasicBattleAttribute phyAtk = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("phyAtk")) ;
		BasicBattleAttribute magAtk = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("magAtk")) ;
		BasicBattleAttribute phyDef = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("phyDef")) ;
		BasicBattleAttribute magDef = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("magDef")) ;
		BasicBattleAttribute dex = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("dex")) ;
		BasicBattleAttribute agi = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("agi")) ;
		BasicBattleAttribute critAtk = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("critAtk")) ;
		BasicBattleAttribute critDef = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("critDef")) ;		
		BattleSpecialAttribute stun = BattleSpecialAttribute.fromJson((JSONObject) jsonData.get("stun")) ;
		BattleSpecialAttribute block = BattleSpecialAttribute.fromJson((JSONObject) jsonData.get("block")) ;
		BattleSpecialAttributeWithDamage blood = BattleSpecialAttributeWithDamage.fromJson((JSONObject) jsonData.get("blood")) ;
		BattleSpecialAttributeWithDamage poison = BattleSpecialAttributeWithDamage.fromJson((JSONObject) jsonData.get("poison")) ;
		BattleSpecialAttribute silence = BattleSpecialAttribute.fromJson((JSONObject) jsonData.get("silence")) ;
		BasicBattleAttribute atkSpeed = BasicBattleAttribute.fromJson((JSONObject) jsonData.get("atkSpeed")) ;
		
		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef,
				stun, block, blood, poison, silence, atkSpeed) ;
	}
	
	@Override
	public String toString()
	{
		return "BattleAttributes [phyAtk=" + phyAtk + "\n magAtk=" + magAtk + "\n phyDef=" + phyDef + "\n magDef=" + magDef
				+ "\n dex=" + dex + "\n agi=" + agi + "\n critAtk=" + critAtk + "\n critDef=" + critDef + "\n stun=" + stun + "\n block="
				+ block + "\n blood=" + blood + "\n poison=" + poison + "\n silence=" + silence + "]";
	}

	
}