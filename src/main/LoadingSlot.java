package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import UI.GameButton;
import UI.GameTextButton;
import graphics.Align;
import graphics.Scale;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerJobs;
import utilities.Util;

public class LoadingSlot
{
    private final Point topLeftPos ;
    private final Player player ;
    private final Pet pet ;
    private final GameButton loadButton ;
	private final Point playerPos ;
	private final Point namePos ;
	private final Point jobPos ;
	private final Point levelPos ;
    private final Point attTopLeft ;
	private final Point petPos ;
    private final Point petTopLeftPos ;
	private final Point petNamePos ;
	private final Point petJobPos ;
	private final Point petLevelPos ;
    private final Point petAttTopLeft ;

    private static final Image IMAGE = ImageLoader.loadImage(Path.OPENING_IMG + "LoadingSlot.png") ;
    private static final Image PET_IMAGE = ImageLoader.loadImage(Path.OPENING_IMG + "PetLoadingSlot.png") ;
    private static final Font SMALL_FONT = new Font(Game.getMainFontName(), Font.BOLD, 13) ;

    protected LoadingSlot(String name, Point topLeftPos, Player player, Pet pet)
    {
        this.topLeftPos = topLeftPos ;
        this.player = player ;
        this.pet = pet ;
        Dimension windowSize = Util.getSize(IMAGE) ;
        this.loadButton = new GameTextButton(Util.translate(topLeftPos, windowSize.width / 2, windowSize.height - 100), Align.center, name, () -> { loadGame(player, pet, 0) ;}) ;
	    this.playerPos = Util.translate(topLeftPos, 160, 96) ;
        this.namePos = Util.translate(topLeftPos, 160, 30) ;
	    this.jobPos = Util.translate(topLeftPos, 16, 45) ;
	    this.levelPos = Util.translate(topLeftPos, 16, 60) ;
        this.attTopLeft = Util.translate(topLeftPos, 16, 112) ;
        this.petTopLeftPos = Util.translate(topLeftPos, 320, 416) ;
        this.petPos = Util.translate(petTopLeftPos, 80, 64) ;
        this.petNamePos = Util.translate(petTopLeftPos, 80, 16) ;
        this.petJobPos = Util.translate(petTopLeftPos, 16, 45) ;
        this.petLevelPos = Util.translate(petTopLeftPos, 16, 60) ;
        this.petAttTopLeft = Util.translate(petTopLeftPos, 16, 82) ;
    }

	private void loadGame(Player player, Pet pet, int slot)
	{
		Game.setPlayer(player) ;
        if (pet != null)
        {
            System.out.println("Loading pet: " + pet.getName() + " with job: " + pet.getJob()) ;
            Game.setPet(pet) ;
        }
        Game.setSaveSlotInUse(slot) ;
	    Opening.finish() ;
    }

    protected void deactivateButton() { loadButton.deactivate() ;}

    private void displayPetWindow()
    {
        GamePanel.getDP().drawImage(PET_IMAGE, petTopLeftPos, Align.topLeft) ;
        GamePanel.getDP().drawText(petNamePos, Align.center, pet.getName(), SMALL_FONT, Palette.colors[0]) ;
        GamePanel.getDP().drawText(petJobPos, Align.centerLeft, PlayerJobs.getJobs()[pet.getJob()].toString(), SMALL_FONT, Palette.colors[0]) ;
        GamePanel.getDP().drawText(petLevelPos, Align.centerLeft, "Nível: " + pet.getLevel(), SMALL_FONT, Palette.colors[0]) ;
        pet.getPA().display(petAttTopLeft, new Dimension(80, 16)) ;
        pet.display(petPos, Scale.unit) ;
    }

    protected void display(Point mousePos)
    {
        GamePanel.getDP().drawImage(IMAGE, topLeftPos, Align.topLeft) ;
		GamePanel.getDP().drawText(namePos, Align.center, player.getName(), SMALL_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(jobPos, Align.centerLeft, PlayerJobs.getJobs()[player.getJob()].toString(), SMALL_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(levelPos, Align.centerLeft, "Nível: " + player.getLevel(), SMALL_FONT, Palette.colors[0]) ;
        player.getPA().display(attTopLeft, new Dimension(80, 16)) ;
        player.display(playerPos, Scale.unit, Directions.down, false) ;
        if (pet != null)
        {
            displayPetWindow() ;
        }
        loadButton.display(true, mousePos) ;
    }
}