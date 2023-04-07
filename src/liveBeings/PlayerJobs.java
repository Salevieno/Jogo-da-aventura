package liveBeings;

public enum PlayerJobs
{
	knight(13),
	mage(48),
	archer(83),
	animal(117),
	thief(151),
	lord(23),
	shielder(33),
	arquimage(58),
	healer(68),
	ranger(93),
	elementalist(103),
	forestKing(127),
	wild(137),
	mercenary(161),
	assassin(171);
	
	int lastSpellID ;
	
	private PlayerJobs(int lastSpellID)
	{
		this.lastSpellID = lastSpellID ;
	}
	
	public static PlayerJobs[] getJobs() { return new PlayerJobs[] {knight, mage, archer, animal, thief} ;}
	
	public static PlayerJobs jobFromSpellID(int spellID)
	{
		if (0 <= spellID & spellID <= knight.lastSpellID) { return knight ;}
		if (spellID <= lord.lastSpellID) { return lord ;}
		if (spellID <= shielder.lastSpellID) { return shielder ;}
		if (spellID <= mage.lastSpellID) { return mage ;}
		if (spellID <= arquimage.lastSpellID) { return arquimage ;}
		if (spellID <= healer.lastSpellID) { return healer ;}
		if (spellID <= archer.lastSpellID) { return archer ;}
		if (spellID <= ranger.lastSpellID) { return ranger ;}
		if (spellID <= elementalist.lastSpellID) { return elementalist ;}
		if (spellID <= animal.lastSpellID) { return animal ;}
		if (spellID <= forestKing.lastSpellID) { return forestKing ;}
		if (spellID <= wild.lastSpellID) { return wild ;}
		if (spellID <= thief.lastSpellID) { return thief ;}
		if (spellID <= mercenary.lastSpellID) { return mercenary ;}
		if (spellID <= assassin.lastSpellID) { return assassin ;}
		
		return null ;
	}
}
