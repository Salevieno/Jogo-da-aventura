package UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import main.GamePanel;
import main.Log;
import main.Palette;
import utilities.Util;

public class GameIconButton extends GameButton
{

    public GameIconButton(Point pos, Align alignment, Image image, Image selectedImage, ButtonFunction action)
    {
        super(pos, alignment, "", image, selectedImage, action, null) ;
        if (image == null || selectedImage == null)
        {
            Log.error("Tentando criar GameIconButton com image ou selectedImage nula") ;
            return ;
        }
		this.size = Util.getSize(image) ;
		this.topLeft = UtilAlignment.getTopLeft(pos, alignment, size) ;
    }

	public void display(double angle, boolean displayText, Point mousePos, Color textColor, double opacity)
	{		
		if (!isActive) { return ;}
		
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
            GamePanel.getDP().drawRoundRect(topLeft, Align.topLeft, size, 5, Palette.colors[3], Palette.colors[0], true) ;
            GamePanel.getDP().drawText(getCenter(), Align.center, 0, name, font, selectedTextColor) ;
			return ;
		}
		
		GamePanel.getDP().drawImage(imageDisplayed, topLeft, angle, Scale.unit, Align.topLeft, opacity) ;
	}
    
}
