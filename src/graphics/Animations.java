package graphics ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays ;

import components.Items;
import components.NPCs;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import maps.Maps;
import utilities.AttackEffects;

public class Animations 
{
	private int[] Anicounter ;
	private int[] Aniduration ;
	private boolean[] AniIsActive ;
	private Object[][] AniVars ;
	
	public Animations()
	{
		int NumberOfAni = 21 ;
		Anicounter = new int[NumberOfAni] ;
		Aniduration = new int[NumberOfAni] ;
		Arrays.fill(Aniduration, 100) ;
		AniIsActive = new boolean[NumberOfAni] ;
		AniVars = new Object[NumberOfAni][] ;
	}
	
	public void StartAni(int AniID)
	{
		AniIsActive[AniID] = true ;
	}
	
	public void IncAnimationCounter(int AniID)
	{
		Anicounter[AniID] = (Anicounter[AniID] + 1) % (Aniduration[AniID] + 1) ;
	}
	
	public boolean isActive(int AniID)
	{
		return AniIsActive[AniID] ;
	}
	 	
	public boolean SomeAnimationIsActive()
	{
		for (int ani = 0 ; ani <= Anicounter.length - 1 ; ani += 1)
		{
			if (isActive(ani))
			{
				return true ;
			}
		}
		return false ;
	}
	
	public void RunAnimation(DrawingOnPanel DP)
	{
		for (int ani = 0 ; ani <= Anicounter.length - 1 ; ani += 1)
		{
			if (AniIsActive[ani])
			{
				if (Anicounter[ani] < Aniduration[ani])
				{
					if (ani == 0)
					{
						GainItemAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 1)
					{
						PlayerDamageAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 2)
					{
						PlayerPhyAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 3)
					{
						PlayerMagAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 4)
					{
						PlayerArrowAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 5)
					{
						PetDamageAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 6)
					{
						PetPhyAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 7)
					{
						PetMagAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 8)
					{
						CreatureDamageAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 9)
					{
						CreaturePhyAtkAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 10)
					{
						CollectAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 11)
					{
						TentAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 12)
					{
						WinAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 13)
					{
						LevelUpAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 14)
					{
						PetLevelUpAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 15)
					{
						FishingAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 16)
					{
						PterodactileAnimation(AniVars[ani], DP) ;
					}
					else if (ani == 20)
					{
						OpeningAnimation(AniVars[ani], DP) ;
					}
					IncAnimationCounter(ani) ;
				}
				else
				{
					 EndAnimation(ani) ;
				}
			}
		}
	}
	
	public void EndAnimation(int AniID)
	{
		Anicounter[AniID] = 0 ;
		AniIsActive[AniID] = false ;
	}
	
	public void SetAniVars(int ani, Object[] AniVars)
	{
		this.AniVars[ani] = AniVars ;
		Aniduration[ani] = (int) AniVars[0] ;
	}
	
	public void GainItemAnimation(Object[] AniVars0, DrawingOnPanel DP)
	{
		Items[] items = (Items[]) AniVars0[1] ;
		int[] ItemIDs = (int[]) AniVars0[2] ;
		//DrawPrimitives DP = DP.getDrawPrimitives() ;
		/*Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int TextCat = allText ;
		int[] Pos = new int[] {(int) (0.5*Game.getScreen().getSize().x), (int) (0.25*Game.getScreen().getSize().y)} ;
		int[] ValidItemIDs = UtilG.ArrayWithValuesGreaterThan(ItemIDs, -1) ;
		Size size = new Size((int) (1.2*UtilG.TextL(Items.LongestName, font, DP.getGraphs())), (int) (10 + (ValidItemIDs.length + 1)*UtilG.TextH(font.getSize()))) ;
		int sy = (int) (1.2*UtilG.TextH(font.getSize())) ;
		Point WindowPos = new Point(Pos[0] - size.x / 2, Pos[1] + size.y / 2) ;
		Point TextPos = new Point(WindowPos.x + size.x / 2, Pos[1] - size.y / 2 + 5 + UtilG.TextH(font.getSize())/2) ;
		DP.DrawMenuWindow(WindowPos, size, null, 0, Color.white, Color.gray) ;
		DP.DrawText(TextPos, "Center", 0, AllText[TextCat][3], font, Color.blue) ;
		for (int i = 0 ; i <= ValidItemIDs.length - 1 ; i += 1)
		{
			DP.DrawText(new Point(TextPos.x, TextPos.y + (i + 1)*sy), "Center", 0, items[ValidItemIDs[i]].getName(), font, Color.blue) ;
		}*/
	}
	
	public void PlayerDamageAnimation(Object[] AniVars1, DrawingOnPanel DP)
	{
		Point TargetPos = (Point) AniVars1[1] ;
		Dimension TargetSize = (Dimension) AniVars1[2] ;
		int damage = (int)((Object[]) AniVars1[3])[0] ;
		AttackEffects effect = (AttackEffects)((Object[]) AniVars1[3])[1] ;
		int AnimationStyle = (int) AniVars1[4] ;
		Point Pos = new Point(TargetPos.x, TargetPos.y - TargetSize.height - 25) ;
		DP.DrawDamageAnimation(Pos, damage, effect, Anicounter[1], Aniduration[1], AnimationStyle, Color.red) ;
	}
	
	public void PlayerPhyAtkAnimation(Object[] AniVars2, DrawingOnPanel DP)
	{
		Point AtkPos = (Point) AniVars2[1] ;
		Point TargetPos = (Point) AniVars2[2] ;
		Dimension TargetSize = (Dimension) AniVars2[3] ;
		DP.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[2], Aniduration[2]) ;
	}
	
	public void PlayerMagAtkAnimation(Object[] AniVars3, DrawingOnPanel DP)
	{
		boolean ShowSpellName = (boolean) AniVars3[1] ;
		Point AttackerPos = (Point) AniVars3[2] ;
		Point TargetPos = (Point) AniVars3[3] ;
		Dimension AttackerSize = (Dimension) AniVars3[4] ;
		Dimension TargetSize = (Dimension) AniVars3[5] ;
		Spell skills = (Spell) AniVars3[6] ;
		if (ShowSpellName)	// Spell name animation
		{
			Point Pos = new Point(AttackerPos.x, AttackerPos.y - AttackerSize.height / 2 - 50) ;
			DP.DrawSkillNameAnimation(Pos, skills.getName(), Color.blue) ;	
		}
		DP.AttackAnimation(AttackerPos, TargetPos, TargetSize, 1, skills.getElem(), Anicounter[3], Aniduration[3]) ;
	}
	
	public void PlayerArrowAtkAnimation(Object[] AniVars4, DrawingOnPanel DP)
	{
		Point AtkPos = (Point) AniVars4[1] ;
		Point DefPos = (Point) AniVars4[2] ;
		Dimension DefSize = (Dimension) AniVars4[3] ;
		DP.AttackAnimation(AtkPos, DefPos, DefSize, 2, null, Anicounter[4], Aniduration[4]) ;
	}

	public void PetDamageAnimation(Object[] AniVars5, DrawingOnPanel DP)
	{
		Point TargetPos = (Point) AniVars5[1] ;
		int[] TargetSize = (int[]) AniVars5[2] ;
		int damage = (int) AniVars5[3] ;
		AttackEffects effect = (AttackEffects) AniVars5[4] ;
		int AnimationStyle = (int) AniVars5[5] ;
		Point Pos = new Point(TargetPos.x, TargetPos.y - TargetSize[1] - 50) ;
		DP.DrawDamageAnimation(Pos, damage, effect, Anicounter[5], Aniduration[5], AnimationStyle, Color.red) ;
	}
	
	public void PetPhyAtkAnimation(Object[] AniVars6, DrawingOnPanel DP)
	{
		Point AtkPos = (Point) AniVars6[1] ;
		Point TargetPos = (Point) AniVars6[2] ;
		Dimension TargetSize = (Dimension) AniVars6[3] ;
		DP.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[6], Aniduration[6]) ;
	}
	
	public void PetMagAtkAnimation(Object[] AniVars7, DrawingOnPanel DP)
	{
		boolean ShowSpellName = (boolean) AniVars7[1] ;
		Point AttackerPos = (Point) AniVars7[2] ;
		Point TargetPos = (Point) AniVars7[3] ;
		Dimension AttackerSize = (Dimension) AniVars7[4] ;
		Dimension TargetSize = (Dimension) AniVars7[5] ;
		Spell skills = (Spell) AniVars7[6] ;
		if (ShowSpellName)	// Skill name animation
		{
			Point Pos = new Point(AttackerPos.x, AttackerPos.y - AttackerSize.height - 50) ;
			DP.DrawSkillNameAnimation(Pos, skills.getName(), Color.blue) ;	
		}
		DP.AttackAnimation(AttackerPos, TargetPos, TargetSize, 1, skills.getElem(), Anicounter[7], Aniduration[7]) ;
	}

	public void CreatureDamageAnimation(Object[] AniVars8, DrawingOnPanel DP)
	{
		Point TargetPos = (Point) AniVars8[1] ;
		int[] TargetSize = (int[]) AniVars8[2] ;
		int damage = (int) AniVars8[3] ;
		AttackEffects effect = (AttackEffects) AniVars8[4] ;
		int AnimationStyle = (int) AniVars8[5] ;
		Point Pos = new Point(TargetPos.x, TargetPos.y - TargetSize[1] - 50) ;
		DP.DrawDamageAnimation(Pos, damage, effect, Anicounter[8], Aniduration[8], AnimationStyle, Color.red) ;
	}
	
	public void CreaturePhyAtkAnimation(Object[] AniVars9, DrawingOnPanel DP)
	{
		Point AtkPos = (Point) AniVars9[1] ;
		Point TargetPos = (Point) AniVars9[2] ;
		Dimension TargetSize = (Dimension) AniVars9[3] ;
		DP.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[9], Aniduration[9]) ;
	}
	
	public void CollectAnimation(Object[] AniVars10, DrawingOnPanel DP)
	{
		Point Pos = (Point) AniVars10[1] ;
		int MessageTime = (int) AniVars10[2] ;
		int CollectibleType = (int) AniVars10[3] ;
		String Message = (String) AniVars10[4] ;
		DP.CollectingAnimation(Pos, Anicounter[10], Aniduration[10], MessageTime, CollectibleType, Message) ;
	}

	public void TentAnimation(Object[] AniVars11, DrawingOnPanel DP)
	{
		Point Pos = (Point) AniVars11[1] ;
		Image TentImage = (Image) AniVars11[2] ;
		DP.TentAnimation(Pos, Anicounter[11], Aniduration[11], TentImage) ;
	}
	
	public void WinAnimation(Object[] AniVars12, DrawingOnPanel DP)
	{
		String[] ItemsObtained = (String[]) AniVars12[1] ;
		Color textColor = (Color) AniVars12[2] ;
		DP.WinAnimation(Anicounter[12], Aniduration[12], ItemsObtained, textColor) ;
	}
	
	public void LevelUpAnimation(Object[] AniVars13, DrawingOnPanel DP)
	{
		float[] AttributesIncrease = (float[]) AniVars13[1] ;
		int playerLevel = (int) AniVars13[2] ;
		Color textColor = (Color) AniVars13[3] ;
		DP.PlayerLevelUpAnimation(Anicounter[13], Aniduration[13], AttributesIncrease, playerLevel, textColor) ;
	}
	
	public void PetLevelUpAnimation(Object[] AniVars14, DrawingOnPanel DP)
	{
		Pet pet = (Pet) AniVars14[1] ;
		float[] AttributesIncrease = (float[]) AniVars14[2] ;
		DP.PetLevelUpAnimation(pet, Anicounter[14], Aniduration[14], AttributesIncrease) ;
	}
	
	public void FishingAnimation(Object[] AniVars15, DrawingOnPanel DP)
	{
		Point playerPos = (Point) AniVars15[1] ;
		Image FishingGif = (Image) AniVars15[2] ;
		String WaterPos = (String) AniVars15[3] ;
		DP.FishingAnimation(playerPos, FishingGif, WaterPos) ;
	}
	
	public void PterodactileAnimation(Object[] AniVars16, DrawingOnPanel DP)
	{
		Image PterodactileImage = (Image) AniVars16[1] ;
		Image SpeakingBubbleImage = (Image) AniVars16[2] ;
		DP.PterodactileAnimation(Anicounter[16], Aniduration[16], PterodactileImage, SpeakingBubbleImage) ;
	}

	public void CrazyArrowAnimation(Object[] AniVars17, DrawingOnPanel DP)
	{
		int map = (int) AniVars17[1] ;
		Image CrazyArrowImage = (Image) AniVars17[2] ;
		//DP.CrazyArrowAnimation(map, Anicounter[17], Aniduration[17], CrazyArrowImage) ;
	}
	
	public void ChestRewardAnimation(Object[] AniVars18, DrawingOnPanel DP)
	{
		Items[] items = (Items[]) AniVars18[1] ;
		int[] ItemRewards = (int[]) AniVars18[2] ;
		int[] GoldRewards = (int[]) AniVars18[3] ;
		Color TextColor = (Color) AniVars18[4] ;
		Image CoinIcon = (Image) AniVars18[5] ;
		DP.ChestRewardsAnimation(items, Anicounter[18], Aniduration[18], ItemRewards, GoldRewards, TextColor, CoinIcon) ;
	}

	public void SailingAnimation(Object[] AniVars19, DrawingOnPanel DP)
	{
		Player player = (Player) AniVars19[1] ;
		NPCs[] npc = (NPCs[]) AniVars19[2] ;
		Image BoatImage = (Image) AniVars19[3] ;
		Maps[] maps = (Maps[]) AniVars19[4] ;
		/*if (0 < pet.getLife()[0])
		{
			pet.setPos(player.getPos()) ;
		}*/
		if (player.getContinent() == 2)
		{
			if (Anicounter[19] == 0)
			{
				/*if (MusicIsOn)
				{
					UtilGeral.SwitchMusic(Music[MusicInMap[player.getMap()]], Music[11]) ;
				}*/
				player.setMap(maps[64]) ;
			}
			DP.SailingAnimation(player, npc[86], maps, "Forest", Anicounter[19], Aniduration[19], BoatImage) ;
		} else if (player.getContinent() == 0)
		{
			if (Anicounter[19] == 0)
			{
				/*if (MusicIsOn)
				{
					UtilGeral.SwitchMusic(Music[MusicInMap[player.getMap()]], Music[11]) ;
				}*/
				player.setMap(maps[61]) ;
			}
			DP.SailingAnimation(player, npc[88], maps, "Island", Anicounter[19], Aniduration[19], BoatImage) ;
		}
	}

	public void OpeningAnimation(Object[] AniVars20, DrawingOnPanel DP)
	{
		Image OpeningGif = (Image) AniVars20[1] ;
		//DP.DrawOpeningScreen(OpeningGif) ;
	}
}
