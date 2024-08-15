package attributes ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import liveBeings.LiveBeingStatus;
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
	private Map<Attributes, LiveBeingStatus> status ;
	private Map<Elements, Double> elemResistanceMult ;
	
	public BattleAttributes(BasicBattleAttribute phyAtk, BasicBattleAttribute magAtk, BasicBattleAttribute phyDef, BasicBattleAttribute magDef, BasicBattleAttribute dex, BasicBattleAttribute agi,
			BasicBattleAttribute critAtk, BasicBattleAttribute critDef,
			BattleSpecialAttribute stun, BattleSpecialAttribute block, BattleSpecialAttributeWithDamage blood, BattleSpecialAttributeWithDamage poison, BattleSpecialAttribute silence,
			Map<Attributes, LiveBeingStatus> status)
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
		this.status = status ;
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

		this.status = new HashMap<>() ;
		for (Attributes att : Attributes.values())
		{
			this.status.put(att, new LiveBeingStatus(att)) ;
		}
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
	public Map<Attributes, LiveBeingStatus> getStatus() {return status ;}
	public Map<Elements, Double> getElemResistanceMult() { return elemResistanceMult ;}

	public void checkResetStatus() { status.values().stream().filter(st -> !st.isActive()).forEach(st -> st.reset()) ;}
	
	public void resetStatus()
	{
		for (Attributes att : Attributes.values())
		{
			status.get(att).reset() ;
		}
	}

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
	
	public void inflictStatus(Attributes att, double intensity, int duration)
	{
		if (Attributes.phyAtk.equals(att))
		{
			phyAtk.incBonus(2) ;
		}
		status.get(att).inflictStatus(intensity, duration);
	}
	
	public BasicBattleAttribute[] basicAttributes() { return new BasicBattleAttribute[] {getPhyAtk(), getMagAtk(), getPhyDef(), getMagDef(), getDex(), getAgi(), critAtk, critDef};}
	public BattleSpecialAttribute[] specialAttributes() { return new BattleSpecialAttribute[] {stun, block, blood, poison, silence} ;}
	public double[] getBaseValues()
	{
		return new double[] {
				phyAtk.getBaseValue(), magAtk.getBaseValue(), phyDef.getBaseValue(), magDef.getBaseValue(),
				dex.getBaseValue(), agi.getBaseValue(), critAtk.getBaseValue(),
				stun.getBasicAtkChance(),
				block.getBasicAtkChance(),
				blood.getBasicAtkChance(), blood.getBasicDefChance(), blood.getBasicAtk(), blood.getBasicDef(),
				poison.getBasicAtkChance(), poison.getBasicDefChance(), poison.getBasicAtk(),poison.getBasicDef(),
				silence.getBasicAtkChance()
				} ;
	}
	public double[] totalValues() { return new double[] {TotalPhyAtk(), TotalMagAtk(), TotalPhyDef(), TotalMagDef(), TotalDex(), TotalAgi()} ;}
	public double[] baseAtkChances()
	{
		return new double[] {stun.TotalAtkChance(), block.TotalAtkChance(), blood.TotalAtkChance(), poison.TotalAtkChance(), silence.TotalAtkChance()} ;
	}
	public double[] baseDefChances()
	{
		return new double[] {stun.TotalDefChance(), block.TotalDefChance(), blood.TotalDefChance(), poison.TotalDefChance(), silence.TotalDefChance()} ;
	}
	public int[] baseDurations()
	{
		return new int[] {stun.getDuration(), block.getDuration(), blood.getDuration(), poison.getDuration(), silence.getDuration()} ;
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
        for (Attributes att : Attributes.getSpecial())
        {
            content.put("status", status.get(att).toJsonObject());
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
		
		Map<Attributes, LiveBeingStatus> status = new HashMap<>() ; 
		// LiveBeingStatus.fromJson((JSONObject) jsonData.get("status")) ;
		
		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef,
				stun, block, blood, poison, silence,
				status) ;
	}
	
	@Override
	public String toString()
	{
		return "BattleAttributes [phyAtk=" + phyAtk + ", magAtk=" + magAtk + ", phyDef=" + phyDef + ", magDef=" + magDef
				+ ", dex=" + dex + ", agi=" + agi + ", critAtk=" + critAtk + ", critDef=" + critDef + ", stun=" + stun + ", block="
				+ block + ", blood=" + blood + ", poison=" + poison + ", silence=" + silence + ", status=" + status
				+ "]";
	}

	
}