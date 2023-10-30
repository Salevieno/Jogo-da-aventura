package attributes ;

import org.json.simple.JSONObject;

import liveBeings.LiveBeingStatus;

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
	
	private LiveBeingStatus status ;
	
	public BattleAttributes(BasicBattleAttribute phyAtk, BasicBattleAttribute magAtk, BasicBattleAttribute phyDef, BasicBattleAttribute magDef, BasicBattleAttribute dex, BasicBattleAttribute agi,
			BasicBattleAttribute critAtk, BasicBattleAttribute critDef,
			BattleSpecialAttribute stun, BattleSpecialAttribute block, BattleSpecialAttributeWithDamage blood, BattleSpecialAttributeWithDamage poison, BattleSpecialAttribute silence,
			LiveBeingStatus status)
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
	public LiveBeingStatus getStatus() {return status ;}
	
	public void resetStatus() { status = new LiveBeingStatus() ;}

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

	
	public boolean isStun()
	{
		return 0 < status.getStun() ;
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
        content.put("status", status.toJsonObject());
        
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
		
		LiveBeingStatus status = LiveBeingStatus.fromJson((JSONObject) jsonData.get("status")) ;
		
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