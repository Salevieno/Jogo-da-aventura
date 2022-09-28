package Graphics ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Image ;
import java.util.Arrays ;

import GameComponents.Items ;
import GameComponents.Maps;
import GameComponents.NPCs ;
import GameComponents.Pet ;
import GameComponents.Player ;
import GameComponents.Skills ;
import Main.Uts ;
import Main.Utg ;

public class Animations 
{
	private int[] Anicounter ;
	private int[] Aniduration ;
	private boolean[] AniIsActive ;
	private int ScreenL, ScreenH ;
	private String[][] AllText ;
	private int[] AllTextCat ;
	private Object[][] AniVars ;
	
	public Animations(int[] ScreenDim, int[] AllTextCat, String[][] AllText)
	{
		int NumberOfAni = 21 ;
		Anicounter = new int[NumberOfAni] ;
		Aniduration = new int[NumberOfAni] ;
		Arrays.fill(Aniduration, 100) ;
		AniIsActive = new boolean[NumberOfAni] ;
		AniVars = new Object[NumberOfAni][] ;
		this.ScreenL = ScreenDim[0] ;
		this.ScreenH = ScreenDim[1] ;
		this.AllTextCat = AllTextCat ;
		this.AllText = AllText ;
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
	
	public void RunAnimation(DrawFunctions DF)
	{
		for (int ani = 0 ; ani <= Anicounter.length - 1 ; ani += 1)
		{
			if (AniIsActive[ani])
			{
				if (Anicounter[ani] < Aniduration[ani])
				{
					if (ani == 0)
					{
						GainItemAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 1)
					{
						PlayerDamageAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 2)
					{
						PlayerPhyAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 3)
					{
						PlayerMagAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 4)
					{
						PlayerArrowAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 5)
					{
						PetDamageAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 6)
					{
						PetPhyAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 7)
					{
						PetMagAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 8)
					{
						CreatureDamageAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 9)
					{
						CreaturePhyAtkAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 10)
					{
						CollectAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 11)
					{
						TentAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 12)
					{
						WinAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 13)
					{
						LevelUpAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 14)
					{
						PetLevelUpAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 15)
					{
						FishingAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 16)
					{
						PterodactileAnimation(AniVars[ani], DF) ;
					}
					else if (ani == 20)
					{
						OpeningAnimation(AniVars[ani], DF) ;
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
	
	public void GainItemAnimation(Object[] AniVars0, DrawFunctions DF)
	{
		Items[] items = (Items[]) AniVars0[1] ;
		int[] ItemIDs = (int[]) AniVars0[2] ;
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Font font = new Font("SansSerif", Font.BOLD, 14) ;
		int TextCat = AllTextCat[10] ;
		int[] Pos = new int[] {(int) (0.5*ScreenL), (int) (0.25*ScreenH)} ;
		int[] ValidItemIDs = Utg.ArrayWithValuesGreaterThan(ItemIDs, -1) ;
		int L = (int) (1.2*Utg.TextL(Uts.GetLongestItemName(), font, DF.getGraphs())), H = (int) (10 + (ValidItemIDs.length + 1)*Utg.TextH(font.getSize())) ;
		int sy = (int) (1.2*Utg.TextH(font.getSize())) ;
		int[] WindowPos = new int[] {Pos[0] - L/2, Pos[1] + H/2} ;
		int[] TextPos = new int[] {WindowPos[0] + L/2, Pos[1] - H/2 + 5 + Utg.TextH(font.getSize())/2} ;
		DF.DrawMenuWindow(WindowPos, L, H, null, 0, Color.white, Color.gray) ;
		DP.DrawText(TextPos, "Center", 0, AllText[TextCat][3], font, Color.blue) ;
		for (int i = 0 ; i <= ValidItemIDs.length - 1 ; i += 1)
		{
			DP.DrawText(new int[] {TextPos[0], TextPos[1] + (i + 1)*sy}, "Center", 0, items[ValidItemIDs[i]].getName(), font, Color.blue) ;
		}
	}
	
	public void PlayerDamageAnimation(Object[] AniVars1, DrawFunctions DF)
	{
		int[] TargetPos = (int[]) AniVars1[1] ;
		int[] TargetSize = (int[]) AniVars1[2] ;
		int[] AtkResult = (int[]) AniVars1[3] ;
		int[] AnimationStyle = (int[]) AniVars1[4] ;
		int[] Pos = new int[] {TargetPos[0], TargetPos[1] - TargetSize[1] - 25} ;
		DF.DrawDamageAnimation(Pos, AtkResult[0], AtkResult[1], Anicounter[1], Aniduration[1], AnimationStyle, Color.red) ;
	}
	
	public void PlayerPhyAtkAnimation(Object[] AniVars2, DrawFunctions DF)
	{
		int[] AtkPos = (int[]) AniVars2[1] ;
		int[] TargetPos = (int[]) AniVars2[2] ;
		int[] TargetSize = (int[]) AniVars2[3] ;
		DF.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[2], Aniduration[2]) ;
	}
	
	public void PlayerMagAtkAnimation(Object[] AniVars3, DrawFunctions DF)
	{
		boolean ShowSpellName = (boolean) AniVars3[1] ;
		int[] AttackerPos = (int[]) AniVars3[2] ;
		int[] TargetPos = (int[]) AniVars3[3] ;
		int[] AttackerSize = (int[]) AniVars3[4] ;
		int[] TargetSize = (int[]) AniVars3[5] ;
		Skills skills = (Skills) AniVars3[6] ;
		if (ShowSpellName)	// Spell name animation
		{
			int[] Pos = new int[] {AttackerPos[0], AttackerPos[1] - AttackerSize[1] / 2 - 50} ;
			DF.DrawSkillNameAnimation(Pos, skills.getName(), Color.blue) ;	
		}
		DF.AttackAnimation(AttackerPos, TargetPos, TargetSize, 1, skills.getElem(), Anicounter[3], Aniduration[3]) ;
	}
	
	public void PlayerArrowAtkAnimation(Object[] AniVars4, DrawFunctions DF)
	{
		int[] AtkPos = (int[]) AniVars4[1] ;
		int[] DefPos = (int[]) AniVars4[2] ;
		int[] DefSize = (int[]) AniVars4[3] ;
		DF.AttackAnimation(AtkPos, DefPos, DefSize, 2, null, Anicounter[4], Aniduration[4]) ;
	}

	public void PetDamageAnimation(Object[] AniVars5, DrawFunctions DF)
	{
		int[] TargetPos = (int[]) AniVars5[1] ;
		int[] TargetSize = (int[]) AniVars5[2] ;
		int[] AtkResult = (int[]) AniVars5[3] ;
		int[] AnimationStyle = (int[]) AniVars5[4] ;
		int[] Pos = new int[] {TargetPos[0], TargetPos[1] - TargetSize[1] - 50} ;
		DF.DrawDamageAnimation(Pos, AtkResult[0], AtkResult[1], Anicounter[5], Aniduration[5], AnimationStyle, Color.red) ;
	}
	
	public void PetPhyAtkAnimation(Object[] AniVars6, DrawFunctions DF)
	{
		int[] AtkPos = (int[]) AniVars6[1] ;
		int[] TargetPos = (int[]) AniVars6[2] ;
		int[] TargetSize = (int[]) AniVars6[3] ;
		DF.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[6], Aniduration[6]) ;
	}
	
	public void PetMagAtkAnimation(Object[] AniVars7, DrawFunctions DF)
	{
		boolean ShowSpellName = (boolean) AniVars7[1] ;
		int[] AttackerPos = (int[]) AniVars7[2] ;
		int[] TargetPos = (int[]) AniVars7[3] ;
		int[] AttackerSize = (int[]) AniVars7[4] ;
		int[] TargetSize = (int[]) AniVars7[5] ;
		Skills skills = (Skills) AniVars7[6] ;
		if (ShowSpellName)	// Skill name animation
		{
			int[] Pos = new int[] {AttackerPos[0], AttackerPos[1] - AttackerSize[1] - 50} ;
			DF.DrawSkillNameAnimation(Pos, skills.getName(), Color.blue) ;	
		}
		DF.AttackAnimation(AttackerPos, TargetPos, TargetSize, 1, skills.getElem(), Anicounter[7], Aniduration[7]) ;
	}

	public void CreatureDamageAnimation(Object[] AniVars8, DrawFunctions DF)
	{
		int[] TargetPos = (int[]) AniVars8[1] ;
		int[] TargetSize = (int[]) AniVars8[2] ;
		int[] AtkResult = (int[]) AniVars8[3] ;
		int[] AnimationStyle = (int[]) AniVars8[4] ;
		int[] Pos = new int[] {TargetPos[0], TargetPos[1] - TargetSize[1] - 50} ;
		DF.DrawDamageAnimation(Pos, AtkResult[0], AtkResult[1], Anicounter[8], Aniduration[8], AnimationStyle, Color.red) ;
	}
	
	public void CreaturePhyAtkAnimation(Object[] AniVars9, DrawFunctions DF)
	{
		int[] AtkPos = (int[]) AniVars9[1] ;
		int[] TargetPos = (int[]) AniVars9[2] ;
		int[] TargetSize = (int[]) AniVars9[3] ;
		DF.AttackAnimation(AtkPos, TargetPos, TargetSize, 0, null, Anicounter[9], Aniduration[9]) ;
	}
	
	public void CollectAnimation(Object[] AniVars10, DrawFunctions DF)
	{
		int[] Pos = (int[]) AniVars10[1] ;
		int MessageTime = (int) AniVars10[2] ;
		int CollectibleType = (int) AniVars10[3] ;
		String Message = (String) AniVars10[4] ;
		DF.CollectingAnimation(Pos, Anicounter[10], Aniduration[10], MessageTime, CollectibleType, Message) ;
	}

	public void TentAnimation(Object[] AniVars11, DrawFunctions DF)
	{
		int[] Pos = (int[]) AniVars11[1] ;
		Image TentImage = (Image) AniVars11[2] ;
		DF.TentAnimation(Pos, Anicounter[11], Aniduration[11], TentImage) ;
	}
	
	public void WinAnimation(Object[] AniVars12, DrawFunctions DF)
	{
		String[] ItemsObtained = (String[]) AniVars12[1] ;
		Color textColor = (Color) AniVars12[2] ;
		DF.WinAnimation(Anicounter[12], Aniduration[12], ItemsObtained, textColor) ;
	}
	
	public void LevelUpAnimation(Object[] AniVars13, DrawFunctions DF)
	{
		float[] AttributesIncrease = (float[]) AniVars13[1] ;
		int playerLevel = (int) AniVars13[2] ;
		Color textColor = (Color) AniVars13[3] ;
		DF.PlayerLevelUpAnimation(Anicounter[13], Aniduration[13], AttributesIncrease, playerLevel, textColor) ;
	}
	
	public void PetLevelUpAnimation(Object[] AniVars14, DrawFunctions DF)
	{
		Pet pet = (Pet) AniVars14[1] ;
		float[] AttributesIncrease = (float[]) AniVars14[2] ;
		DF.PetLevelUpAnimation(pet, Anicounter[14], Aniduration[14], AttributesIncrease) ;
	}
	
	public void FishingAnimation(Object[] AniVars15, DrawFunctions DF)
	{
		int[] playerPos = (int[]) AniVars15[1] ;
		Image FishingGif = (Image) AniVars15[2] ;
		String WaterPos = (String) AniVars15[3] ;
		DF.FishingAnimation(playerPos, FishingGif, WaterPos) ;
	}
	
	public void PterodactileAnimation(Object[] AniVars16, DrawFunctions DF)
	{
		Image PterodactileImage = (Image) AniVars16[1] ;
		Image SpeakingBubbleImage = (Image) AniVars16[2] ;
		DF.PterodactileAnimation(Anicounter[16], Aniduration[16], PterodactileImage, SpeakingBubbleImage) ;
	}

	public void CrazyArrowAnimation(Object[] AniVars17, DrawFunctions DF)
	{
		int map = (int) AniVars17[1] ;
		Image CrazyArrowImage = (Image) AniVars17[2] ;
		DF.CrazyArrowAnimation(map, Anicounter[17], Aniduration[17], CrazyArrowImage) ;
	}
	
	public void ChestRewardAnimation(Object[] AniVars18, DrawFunctions DF)
	{
		Items[] items = (Items[]) AniVars18[1] ;
		int[] ItemRewards = (int[]) AniVars18[2] ;
		int[] GoldRewards = (int[]) AniVars18[3] ;
		Color TextColor = (Color) AniVars18[4] ;
		Image CoinIcon = (Image) AniVars18[5] ;
		DF.ChestRewardsAnimation(items, Anicounter[18], Aniduration[18], ItemRewards, GoldRewards, TextColor, CoinIcon) ;
	}

	public void SailingAnimation(Object[] AniVars19, DrawFunctions DF)
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
				player.setMap(64, maps) ;
			}
			DF.SailingAnimation(player, npc[86], maps, "Forest", Anicounter[19], Aniduration[19], BoatImage) ;
		} else if (player.getContinent() == 0)
		{
			if (Anicounter[19] == 0)
			{
				/*if (MusicIsOn)
				{
					UtilGeral.SwitchMusic(Music[MusicInMap[player.getMap()]], Music[11]) ;
				}*/
				player.setMap(61, maps) ;
			}
			DF.SailingAnimation(player, npc[88], maps, "Island", Anicounter[19], Aniduration[19], BoatImage) ;
		}
	}

	public void OpeningAnimation(Object[] AniVars20, DrawFunctions DF)
	{
		Image OpeningGif = (Image) AniVars20[1] ;
		DF.DrawOpeningScreen(OpeningGif) ;
	}
}
