package windows;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import main.Game;
import main.GamePanel;
import utilities.UtilS;

public class PauseWindow extends GameWindow
{

	private static final Point windowPos ;
    private final static Image imageBg ;

    static
    {
        windowPos = new Point(20, 20) ;
	    imageBg = UtilS.loadImage("\\Windows\\" + "SettingsBackground.png") ;
    }

    public PauseWindow()
    {
        super("Opções", windowPos, imageBg, 3, 0, 6, 0) ;
    }

    public void navigate(String action)
    {
    }

    public void display(Point mousePos)
    {
        GamePanel.DP.drawImage(imageBg, windowPos, Align.topLeft, 0.3) ;
    }

}
