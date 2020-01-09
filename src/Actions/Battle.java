package Actions;

import java.util.Scanner;

import GameComponents.Creatures;
import GameComponents.Player;

public class Battle 
{
	private static float random = (float)0.2;

	public static int IndexOf(String[] Vector, String Elem)
	{
		int ID = -1;
		for (int i = 0; i <= Vector.length - 1; ++i)
		{
			if (Vector[i] == Elem)
			{
				ID = i;
			}
		}
		return ID;
	}
	
	public static float BasicElemMult(String Atk, String Def)
	{
		float mult = 1;
		String[] ID = new String[] {"n", "w", "f", "p", "e", "a", "b", "l", "d", "s"};
		float[][] ElemVec = new float[][] {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, (float)1.5, (float)0.75, (float)0.85, (float)0.9, (float)0.5, 1, 1, (float)1.1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
		mult = ElemVec[IndexOf(ID, Atk)][IndexOf(ID, Def)];
		return mult;
	}
	
	public static float ElemMult(String Atk, String Weapon, String Armor, String Shield, String SuperElem)
	{
		float mult = 1;
		mult = BasicElemMult(Atk, Armor)*mult;
		mult = BasicElemMult(Atk, Shield)*mult;
		mult = BasicElemMult(Weapon, Armor)*mult;
		mult = BasicElemMult(Weapon, Shield)*mult;
		mult = BasicElemMult(SuperElem, Armor)*mult;
		mult = BasicElemMult(SuperElem, Shield)*mult;
		return mult;
	}

	public static float RandomMult()
	{
		return (float)(1 - random + 2*random*Math.random());
	}
	
	public static boolean CriticalAtk(Player player, Creatures creature)
	{
		boolean Crit = false;
		
		if (Math.random() <= player.getCrit()[0] + player.getCrit()[1])
		{
			Crit = true;
		}
		
		return Crit;
	}
	
	public static void Skill(Player player, Creatures creature, int skill)
	{
		float damage = 0;
		int level = player.getSkill()[skill];
		
		if (player.getJob() == 1)
		{
			if (skill == 0)
			{
				damage = (float)(1 + 0.02*level)*(player.getPhyAtk()[0] + player.getPhyAtk()[1] + player.getPhyAtk()[2]) + 1*level - creature.getPhyDef()[0];	
			}
			if (skill == 3)
			{
				player.setLifeMax((float)(1 + 0.2*level)*player.getLifeMax());
				//player.setStatus[0](true); // I want to set only the hp stats
				if (Math.random() < 0.02*level)
				{
					//player.setStatus[8](true); // I want to set only the stun stats
				}
			}
		}
		if (player.getJob() == 2)
		{
			if (skill == 1)
			{
				damage = player.getMagAtk()[0] + player.getMagAtk()[1] + player.getMagAtk()[2] - creature.getMagDef()[0];					
			}
		}
		if (player.getJob() == 3)
		{
			
		}
		if (player.getJob() == 4)
		{
			
		}
		if (player.getJob() == 5)
		{
			
		}
		damage = Math.max(0, damage);
		creature.setLife(creature.getLife() - RandomMult()*damage);
	}
	
	public static void PlayerAtk(Player player, Creatures creature)
	{
		Scanner in = new Scanner(System.in);
		String PlayerMove;
		float damage = 0;
		int skill = 0;
		float elemMult = ElemMult(player.getElem()[0], player.getElem()[1], creature.getElem()[2], creature.getElem()[3], player.getElem()[4]);
		PlayerMove = in.nextLine();
		if (PlayerMove.equals("a"))
		{
			System.out.println("Player atacks!");
			damage = Math.max(0, player.getPhyAtk()[0] + player.getPhyAtk()[1] + player.getPhyAtk()[2] - creature.getPhyDef()[0]);
			if (CriticalAtk(player, creature))
			{
				System.out.println("Critical atack!");
				damage += creature.getPhyDef()[0];
			}
			creature.setLife(creature.getLife() - RandomMult()*elemMult*damage);
		}
		if (PlayerMove.equals("s"))
		{
			System.out.println("Magical atack!");
			skill = in.nextInt();
			Skill(player, creature, skill - 1);
		}
		System.out.println("Creature life = " + creature.getLife());
	}
	
	public static void CreatureAtk(Player player, Creatures creature)
	{
		int CreatureMove = 0;
		float damage = 0;
		
		CreatureMove = (int)(0*Math.random());
		if (CreatureMove == 0)
		{
			System.out.println("Creature atacks!");
			damage = Math.max(0, creature.getPhyAtk()[0] + creature.getPhyAtk()[1] - player.getPhyDef()[0]);
			player.setLife(player.getLife() - RandomMult()*damage);
		}
		System.out.println("Player life = " + player.getLife());
	}

}
