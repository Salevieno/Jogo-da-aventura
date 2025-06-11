package simulations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import attributes.AttributeIncrease;
import attributes.BattleAttributes;
import liveBeings.LiveBeing;
import liveBeings.Pet;
import liveBeings.Player;

public abstract class JobBuild
{
	private static final Player knight ;
	private static final Player mage ;
	private static final Player archer ;
	private static final Player animal ;
	private static final Player thief ;
	
	private static final Player lord ;
	private static final Player shielder ;
	private static final Player archmage ;
	private static final Player healer ;
	private static final Player ranger ;
	private static final Player elementalist ;
	private static final Player forestKing ;
	private static final Player wild ;
	private static final Player assassin ;
	private static final Player mercenary ;

	private static final Pet Tobby ;
	private static final Pet Little ;
	private static final Pet Melly ;
	private static final Pet Yall ;
	
	
	private static final List<Player> players ;
	private static final List<Pet> pets ;
	
	static
	{
		
		knight = new Player("", "", 0) ;
		mage = new Player("", "", 1) ;
		archer = new Player("", "", 2) ;
		animal = new Player("", "", 3) ;
		thief = new Player("", "", 4) ;
		
		lord = new Player("", "", 0) ;
		shielder = new Player("", "", 0) ;
		archmage = new Player("", "", 1) ;
		healer = new Player("", "", 1) ;
		ranger = new Player("", "", 2) ;
		elementalist = new Player("", "", 2) ;
		forestKing = new Player("", "", 3) ;
		wild = new Player("", "", 3) ;
		assassin = new Player("", "", 4) ;
		mercenary = new Player("", "", 4) ;
		
		Tobby = new Pet(0) ;
		Little = new Pet(1) ;
		Melly = new Pet(2) ;
		Yall = new Pet(3) ;
		
		players = List.of(knight, mage, archer, animal, thief, lord, shielder, archmage, healer, ranger, elementalist, forestKing, wild, assassin, mercenary) ;
		for (int i = 5 ; i <= 15 - 1 ; i += 1)
		{
			players.get(i).setProJob(1 + (i + 1) % 2) ;
		}
		players.stream().filter(player -> 1 <= player.getProJob()).forEach(player -> levelUpTo50(player, false)) ;
		players.stream().filter(player -> 1 <= player.getProJob()).forEach(Player::updateAttributeIncrease) ;
		
		pets = List.of(Tobby, Little, Melly, Yall) ;
				
	}
	private static void resetToLevel1(Player player)
	{
		int originalProJob = player.getProJob() ;
		player.setLevel(1) ;
		player.setProJob(0) ;
		player.updateAttributeIncrease() ;
		player.setProJob(originalProJob) ;
		player.setPA(Player.InitializePersonalAttributes(player.getJob())) ;
		player.setBA(new BattleAttributes(Player.InitialAtts.get(player.getJob()), 1, Player.InitialAtts.get(player.getJob())[41])) ; // Player.InitializeBattleAttributes(player.getJob())
	}
	// private static void resetToLevel50(Player player, boolean addChosenPoints)
	// {
	// 	if (player.getProJob() == 0) { return ;}
		
	// 	resetToLevel1(player) ;
	// 	levelUpTo50(player, addChosenPoints) ;
	// }
	
	private static double[] calcChosenIncrease(double[] chanceIncrease)
	{
		double sumChances = 0.0 ;
		double[] chosenIncrease = new double[chanceIncrease.length] ;
		for (int i = 2 ; i <= chanceIncrease.length - 1 ; i += 1)
		{
			sumChances += chanceIncrease[i] ;
		}
		for (int i = 2 ; i <= chanceIncrease.length - 1 ; i += 1)
		{
			chosenIncrease[i] = 2 * chanceIncrease[i] / sumChances ;
		}
		return chosenIncrease ;
	}
	
	private static void levelUp(LiveBeing being, int qtdLevels, boolean addChosenPoints)
	{
		if (being.getLevel() == Player.maxLevel) { return ;}
		
		AttributeIncrease attInc = being instanceof Player ? ((Player) being).getAttInc() : ((Pet) being).getAttInc() ;
		double[] chosenIncrease = addChosenPoints ? calcChosenIncrease(attInc.getChance().basic()) : new double[8] ;
		being.setLevel(being.getLevel() + 1);
		
		double[] totalIncrease = new double[8] ;
		for (int i = 0 ; i <= 8 - 1 ; i += 1)
		{
			totalIncrease[i] = qtdLevels * (attInc.getIncrement().basic()[i] * attInc.getChance().basic()[i] + chosenIncrease[i]) ;
		}

		being.getPA().getLife().incMaxValue((int) (totalIncrease[0])) ;
		being.getPA().getMp().incMaxValue((int) (totalIncrease[1])); ;	
		being.getBA().getPhyAtk().incBaseValue(totalIncrease[2]) ;
		being.getBA().getMagAtk().incBaseValue(totalIncrease[3]) ;
		being.getBA().getPhyDef().incBaseValue(totalIncrease[4]) ;
		being.getBA().getMagDef().incBaseValue(totalIncrease[5]) ;
		being.getBA().getAgi().incBaseValue(totalIncrease[6]) ;
		being.getBA().getDex().incBaseValue(totalIncrease[7]) ;
		being.getPA().getLife().setToMaximum() ;
		being.getPA().getMp().setToMaximum() ;
	}
	
	private static void levelUpTo50(LiveBeing player, boolean addChosenPoints) { levelUp(player, 49, addChosenPoints) ;}
	private static void levelUpTo99(LiveBeing player, boolean addChosenPoints) { levelUp(player, 50, addChosenPoints) ;}
	
	private static String jobName(int job, int proJob)
	{
		return switch (job)
		{
			case 0 -> proJob == 0 ? "knight" : proJob == 1 ? "lord" : "shielder" ;
			case 1 -> proJob == 0 ? "mage" : proJob == 1 ? "archmage" : "healer" ;
			case 2 -> proJob == 0 ? "archer" : proJob == 1 ? "ranger" : "elementalist" ;
			case 3 -> proJob == 0 ? "animal" : proJob == 1 ? "forest's king" : "wild" ;
			case 4 -> proJob == 0 ? "thief" : proJob == 1 ? "assassin" : "mercenary" ;
			default -> "" ;
		} ;
	}

	private static void printBaseBuildsLevel1()
	{
		printBuilds("Base attributes at level 1", Stream.concat(players.stream().filter(player -> player.getProJob() == 0), pets.stream()).collect(Collectors.toList())) ;
	}
	
	private static void printBaseBuildsLevel50()
	{
		players.stream().filter(player -> player.getProJob() == 0).forEach(player -> levelUpTo50(player, false)) ;
		pets.forEach(pet -> levelUpTo50(pet, false));
		printBuilds("Base attributes at level 50", Stream.concat(players.stream(), pets.stream()).collect(Collectors.toList())) ;
	}
	
	private static void printBaseBuildsLevel99()
	{
		players.stream().filter(player -> player.getProJob() == 0).forEach(player -> levelUpTo50(player, false)) ;
		players.stream().filter(player -> 1 <= player.getProJob()).forEach(player -> levelUpTo99(player, false)) ;
		pets.forEach(pet -> levelUpTo99(pet, false));
		printBuilds("Base attributes at level 99", Stream.concat(players.stream(), pets.stream()).collect(Collectors.toList())) ;
	}
	
	private static void printBaseBuildsLevel50WithPoints()
	{
		players.stream().forEach(player -> resetToLevel1(player)) ;
		players.stream().forEach(player -> levelUpTo50(player, true)) ;
		pets.forEach(pet -> levelUpTo50(pet, true));
		
		printBuilds("Base attributes with chosen points at level 50", Stream.concat(players.stream(), pets.stream()).collect(Collectors.toList())) ;
	}
	
	private static void printBaseBuildsLevel99WithPoints()
	{
		players.stream().forEach(player -> resetToLevel1(player)) ;
		players.stream().forEach(player -> levelUpTo50(player, true)) ;
		players.stream().forEach(player -> player.updateAttributeIncrease()) ;
		players.stream().forEach(player -> levelUpTo99(player, true)) ;
		pets.forEach(pet -> levelUpTo99(pet, true));
		
		printBuilds("Base attributes with chosen points at level 99", Stream.concat(players.stream(), pets.stream()).collect(Collectors.toList())) ;
	}
	
	private static void printBuild(LiveBeing liveBeing)
	{
		String jobName = liveBeing instanceof Pet ? liveBeing.getName() : jobName(liveBeing.getJob(), liveBeing.getProJob()) ;
		System.out.print(jobName + "	") ;
		System.out.print(liveBeing.getPA().getLife().getCurrentValue() + "	") ;
		System.out.print(liveBeing.getPA().getMp().getCurrentValue() + "	") ;
		System.out.print((int) liveBeing.getBA().getPhyAtk().getTotal() + "	") ;
		System.out.print((int) liveBeing.getBA().getMagAtk().getTotal() + "	") ;
		System.out.print((int) liveBeing.getBA().getPhyDef().getTotal() + "	") ;
		System.out.print((int) liveBeing.getBA().getMagDef().getTotal() + "	") ;
		System.out.print((int) liveBeing.getBA().getDex().getTotal() + "	") ;
		System.out.println((int) liveBeing.getBA().getAgi().getTotal() + "") ;
	}
	
	private static void printBuilds(String title, List<LiveBeing> liveBeings)
	{
		System.out.println("\n--------------- " + title + " ---------------") ;
		System.out.println("Job	Life	MP 	PhyAtk	MagAtk	PhyDef	MagDef	Dex	Agi") ;
		liveBeings.forEach(liveBeing -> printBuild(liveBeing)) ;
		System.out.println() ;
	}
	
	public static void printAll()
	{
		printBaseBuildsLevel1() ;
		printBaseBuildsLevel50() ;
		printBaseBuildsLevel99() ;
		printBaseBuildsLevel50WithPoints() ;
		printBaseBuildsLevel99WithPoints() ;
	}
}
