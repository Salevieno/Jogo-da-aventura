package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import UI.GameButton;
import UI.GameTextButton;
import graphics.Align;
import liveBeings.Player;
import liveBeings.PlayerJobs;
import utilities.Util;

public class LoadingSlot
{
    private final String name ;
    private final Point topLeftPos ;
    private final Player player ;
    private final GameButton loadButton ;
	private final Point namePos ;
	private final Point jobPos ;
	private final Point levelPos ;
    private final Point attTopLeft ;

    private static final Image IMAGE = ImageLoader.loadImage(Path.OPENING_IMG + "LoadingSlot.png") ;
    private static final Font FONT = new Font(Game.getMainFontName(), Font.BOLD, 16) ;
    private static final Font SMALL_FONT = new Font(Game.getMainFontName(), Font.BOLD, 13) ;

    // TODO pet window


    protected LoadingSlot(String name, Point topLeftPos, Player player)
    {
        this.name = name ;
        this.topLeftPos = topLeftPos ;
        this.player = player ;
        Dimension windowSize = Util.getSize(IMAGE) ;
        this.loadButton = new GameTextButton(Util.translate(topLeftPos, windowSize.width / 2, windowSize.height - 100), Align.center, name, () -> { loadGame(player, 0) ;}) ;
	    this.namePos = Util.translate(topLeftPos, 75, 30) ;
	    this.jobPos = Util.translate(topLeftPos, 10, 45) ;
	    this.levelPos = Util.translate(topLeftPos, 10, 60) ;
        this.attTopLeft = Util.translate(topLeftPos, 16, 112) ;
    }

	private void loadGame(Player player, int slot)
	{
		Game.setPlayer(player) ;
		Game.setSaveSlotInUse(slot) ;
	    Opening.finish() ;
    }

    protected void deactivateButton() { loadButton.deactivate() ;}

    protected void display(Point mousePos)
    {
        GamePanel.getDP().drawImage(IMAGE, topLeftPos, Align.topLeft) ;
		GamePanel.getDP().drawText(namePos, Align.center, player.getName(), SMALL_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(jobPos, Align.centerLeft, PlayerJobs.getJobs()[player.getJob()].toString(), SMALL_FONT, Palette.colors[0]) ;
		GamePanel.getDP().drawText(levelPos, Align.centerLeft, "Nível: " + player.getLevel(), SMALL_FONT, Palette.colors[0]) ;
        player.getPA().display(attTopLeft, new Dimension(80, 16)) ;
        loadButton.display(true, mousePos) ;
    }
}