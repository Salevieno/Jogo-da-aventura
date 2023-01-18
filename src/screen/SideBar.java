package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import components.GameIcon;
import components.QuestSkills;
import graphics.DrawingOnPanel;
import liveBeings.HotKeysBar;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.Spell;
import liveBeings.SpellType;
import liveBeings.SpellsBar;
import main.Game;
import utilities.Align;
import utilities.Directions;
import utilities.Scale;
import utilities.UtilG;

public class SideBar
{
	private Set<GameIcon> icons ;
	
	
	public SideBar(Image playerImage, Image petImage)
	{
    	String path = Game.ImagesPath + "\\Icons\\";
		Image Options = UtilG.loadImage(path + "Icon_settings.png") ;
		Image Bag = UtilG.loadImage(path + "Icon1_Bag.png") ;
		Image Quest = UtilG.loadImage(path + "Icon2_Quest.png") ;
		Image Map = UtilG.loadImage(path + "Icon3_Map.png") ;
		Image Book = UtilG.loadImage(path + "Icon4_Book.png") ;
    	Image Tent = UtilG.loadImage(path + "Icon5_Tent.png") ;
    	//Image PlayerImage = Player ;
    	//Image PetImage = UtilG.loadImage(path + "PetType" + 0 + ".png") ;
		Image SkillsTree = UtilG.loadImage(path + "Icon8_SkillsTree.png") ;
		Image SelectedOptions = UtilG.loadImage(path + "Icon_settingsSelected.png") ;
		Image SelectedBag = UtilG.loadImage(path + "Icon1_BagSelected.png") ;
		Image SelectedQuest = UtilG.loadImage(path + "Icon2_QuestSelected.png") ;
		Image SelectedMap = UtilG.loadImage(path + "Icon3_MapSelected.png") ;
		Image SelectedBook = UtilG.loadImage(path + "Icon4_BookSelected.png") ;
    	Image SelectedTent = UtilG.loadImage(path + "Icon5_TentSelected.png") ;
    	//Image PlayerSelectedImage = UtilG.loadImage(path + "Player.png") ;
    	//Image PetSelectedImage = Pet.g ;
		Image SelectedSkillsTree = UtilG.loadImage(path + "Icon8_SelectedSkillsTree.png") ;
		Image[] SideBarIconsImages = new Image[] {Options, Bag, Quest, Map, Book, Tent, playerImage, petImage, SkillsTree} ;
		Image[] SideBarIconsSelectedImages = new Image[] {SelectedOptions, SelectedBag, SelectedQuest, SelectedMap, SelectedBook, SelectedTent, playerImage, petImage, SelectedSkillsTree} ;

		Point botLeftPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height - 250) ;
		icons = new HashSet<>() ;
		icons.add(new GameIcon(0, "Settings", UtilG.Translate(botLeftPos, 0, -10), "description", UtilG.loadImage(path + "Icon_settings.png"), UtilG.loadImage(path + "Icon_settingsSelected.png"))) ;
		icons.add(new GameIcon(1, "Bag", UtilG.Translate(botLeftPos, 0, -80), "description", UtilG.loadImage(path + "Icon1_Bag.png"), UtilG.loadImage(path + "Icon1_BagSelected.png"))) ;
		
		icons.forEach(GameIcon::activate);
//		String[] SBname = new String[] {"Options", "Bag", "Quest", "Map", "Book", "Tent", "Player", "Pet"} ;
//		Point[] SBpos = new Point[SBname.length] ;
//		int sy = 20 ;
//		SBpos[0] = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height - 250) ;
//    	for (int i = 1 ; i <= 6 - 1 ; i += 1)
//    	{
//    		SBpos[i] = new Point(SBpos[0].x, SBpos[i - 1].y - SideBarIconsImages[i - 1].getHeight(null) / 2 - sy) ;
//    	}
//    	SBpos[6] = new Point(SBpos[0].x, SBpos[5].y - SideBarIconsImages[5].getHeight(null)/2 - sy) ;
//    	SBpos[7] = new Point(SBpos[0].x, SBpos[6].y - (int)playerImage.getHeight(null) - SideBarIconsImages[4].getHeight(null)/2 - 20/2 - sy) ;
//     	for (int i = 0 ; i <= icons.length - 1 ; i += 1)
//    	{
//     		GameIcon newIcon = new GameIcon(i, SBname[i], SBpos[i], null, SideBarIconsImages[i], SideBarIconsSelectedImages[i]);
//     		icons[i] = newIcon ;
//     		GameIcon.addToAllIconsList(newIcon) ;
//    	}
	}
	
	/*private void DrawSpellsBar(Player player, Image CooldownImage, Image SlotImage, Point MousePos, DrawingOnPanel DP)
	{
		Font Titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		Font font = new Font("SansSerif", Font.BOLD, 9) ;
		Screen screen = Game.getScreen() ;
		Color[] colorPalette = Game.ColorPalette ;
		double OverallAngle = DrawingOnPanel.stdAngle ;
		Point Pos = new Point(screen.getSize().width + 1, (int) (0.99 * screen.getSize().height) - 70) ;
		List<Spell> ActiveSpells = player.GetActiveSpells() ;
		Dimension size = new Dimension(36, 130) ;
		int slotW = SlotImage.getWidth(null), slotH = SlotImage.getHeight(null) ;	// Bar size
		int Ncols = Math.max(ActiveSpells.size() / 11 + 1, 1) ;
		int Nrows = ActiveSpells.size() / Ncols + 1 ;
		int Sx = (int) UtilG.spacing(size.width, Ncols, slotW, 3), Sy = (int) UtilG.spacing(size.height, Nrows, slotH, 5) ;		
		String[] Key = Player.SpellKeys ;
		Color BGcolor = Player.ClassColors[player.getJob()] ;
		Color TextColor = player.getColor() ;
		
		DP.DrawRoundRect(Pos, Align.bottomLeft, size, 1, colorPalette[7], BGcolor, true) ;
		DP.DrawText(new Point(Pos.x + size.width / 2, Pos.y - size.height + 3), Align.topCenter, OverallAngle, player.allText.get("* Barra de habilidades *")[1], Titlefont, colorPalette[5]) ;
		for (int i = 0 ; i <= ActiveSpells.size() - 1 ; ++i)
		{
			Spell spell = player.getSpell().get(i) ;
			if (0 < spell.getLevel())
			{
				Point slotCenter = new Point(Pos.x + slotW / 2 + (i / Nrows) * Sx + 3, Pos.y - size.height + slotH / 2 + (i % Nrows) * Sy + Titlefont.getSize() + 5) ;
				if (player.getMp().getCurrentValue() < spell.getMpCost())
				{
					DP.DrawImage(SlotImage, slotCenter, Align.center) ;
				}
				else
				{
					DP.DrawImage(SlotImage, slotCenter, Align.center) ;
				}
				DP.DrawText(slotCenter, Align.center, OverallAngle, Key[i], font, TextColor) ;
				Dimension imgSize = new Dimension(CooldownImage.getWidth(null), CooldownImage.getHeight(null)) ;
				if (spell.getCooldownCounter() < spell.getCooldown())
				{
					Scale Imscale = new Scale(1, 1 - (double) spell.getCooldownCounter() / spell.getCooldown()) ;
					DP.DrawImage(CooldownImage, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2), OverallAngle, Imscale, Align.bottomLeft) ;
				}
				if (UtilG.isInside(MousePos, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y - imgSize.height / 2), imgSize))
				{
					DP.DrawText(new Point(slotCenter.x - imgSize.width - 10, slotCenter.y), Align.centerRight, OverallAngle, spell.getName(), Titlefont, TextColor) ;
				}
			}
		}
	}*/


	public Set<GameIcon> getIcons()
	{
		return icons;
	}

	public void setIcons(Set<GameIcon> icons)
	{
		this.icons = icons;
	}
	
	
	public void display(Player player, Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		// icons: 0: Options 1: Bag 2: Quest 3: Map 4: Book, 5: player, 6: pet
		Point barPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height);
		Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
		Color[] colorPalette = Game.ColorPalette ;
		double stdAngle = DrawingOnPanel.stdAngle ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String[] IconKey = new String[] {null, Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7], null, null, null, null} ;
		Color TextColor = colorPalette[7] ;
		
		DP.DrawRect(barPos, Align.bottomLeft, size, 1, colorPalette[9], null) ;
		
//		for (GameIcon icon : icons)
//		{
//			if (icon != null)
//			{
//				icon.display(stdAngle, Align.topLeft, mousePos, DP);
//			}
//		}
//		icons[0].display(stdAngle, Align.topLeft, mousePos, DP) ;		// settings
//		
//		for (String key : IconKey)
//		{
//			DP.DrawText(icon.getPos(), Align.bottomLeft, stdAngle, IconKey[0], font, TextColor) ;
//		}
		
		icons.forEach(icon -> icon.display(stdAngle, Align.topLeft, mousePos, DP));	// TODO desenhar as icon keys
		/*for (int i = 0 ; i <= icons.length - 1; i += 1)
		{
			if (IconKey[i] != null)
			{
				DP.DrawText(icons[i].getPos(), Align.bottomLeft, stdAngle, IconKey[i], font, TextColor) ;
			}
		}*/
		
		SpellsBar.display(player.getMp().getCurrentValue(), player.getSpell(),
				player.getSpell(), player.allText.get("* Barra de habilidades *"),
				mousePos, TextColor, TextColor, DP);
//		DrawSpellsBar(player, SpellType.cooldownImage, SpellType.slotImage, mousePos, DP) ;
//		
//		icons[1].display(stdAngle, Align.topLeft, mousePos, DP) ;		// bag
//		DP.DrawText(icons[1].getPos(), Align.bottomLeft, stdAngle, IconKey[0], font, TextColor) ;
//		icons[2].display(stdAngle, Align.topLeft, mousePos, DP) ;		// quest
//		DP.DrawText(icons[2].getPos(), Align.bottomLeft, stdAngle, IconKey[1], font, TextColor) ;
//		
//		if (player.getQuestSkills().get(QuestSkills.getContinentMap(player.getMap().getContinentName(player).name())))	// map
//		{
//			icons[3].display(stdAngle, Align.topLeft, mousePos, DP) ;
//			DP.DrawText(icons[3].getPos(), Align.bottomLeft, stdAngle, IconKey[2], font, TextColor) ;
//		}
//		
//		if (player.getQuestSkills().get(QuestSkills.craftWindow))										// book
//		{
//			icons[4].display(stdAngle, Align.topLeft, mousePos, DP) ;
//		}
//		
//		// player
//		player.display(UtilG.Translate(icons[5].getPos(), icons[5].getImage().getWidth(null) / 2, 0), new Scale(1, 1), Directions.up, false, DP) ;
//		DP.DrawText(icons[5].getPos(), Align.bottomLeft, stdAngle, Player.ActionKeys[5], font, TextColor) ;
//		if (0 < player.getAttPoints())
//		{
//			DP.DrawImage(icons[5].getImage(), UtilG.Translate(icons[5].getPos(), icons[5].getImage().getWidth(null) / 2, 0), stdAngle, new Scale(1, 1), Align.bottomLeft) ;
//		}
//		
//		// pet
//		if (pet != null)
//		{
//			pet.display(UtilG.Translate(icons[6].getPos(), icons[6].getImage().getWidth(null) / 2, 0), new Scale(1, 1), DP) ;
//			DP.DrawText(icons[6].getPos(), Align.bottomLeft, stdAngle, Player.ActionKeys[8], font, TextColor) ;
//		}
//		
//		// Hotkeys
		HotKeysBar.display(mousePos, DP) ;
//		DP.DrawRoundRect(new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70), Align.topLeft, new Dimension(36, 60), 1, colorPalette[7], colorPalette[19], true) ;
//		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
//		{
//			Point slotCenter = new Point(Game.getScreen().getSize().width + 10, Game.getScreen().getSize().height - 60 + 20 * i) ;
//			Dimension slotSize = new Dimension(SpellType.slotImage.getWidth(null), SpellType.slotImage.getHeight(null)) ;
//			DP.DrawImage(SpellType.slotImage, slotCenter, Align.center) ;
//			DP.DrawText(new Point(slotCenter.x + slotSize.width / 2 + 5, slotCenter.y + slotSize.height / 2), Align.bottomLeft, stdAngle, Player.HotKeys[i], font, TextColor) ;
//			if (Player.HotKeys[i] != null)
//			{
//				DP.DrawImage(SpellType.slotImage, slotCenter, Align.center) ;
//				if (UtilG.isInside(mousePos, slotCenter, slotSize))
//				{
//					DP.DrawText(new Point(slotCenter.x - slotSize.width - 10, slotCenter.y), Align.centerRight, stdAngle, Player.HotKeys[i], font, TextColor) ;
//				}
//			}
//		}
	}

	
}
