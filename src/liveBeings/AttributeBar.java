package liveBeings;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;
import utilities.Util;

public class AttributeBar
{
    private Point topLeft ;
    private int style ;
	private Dimension barSize ;
    private List<Point> barPos ;

    private static final int QTD_ATT = 5 ;
	private static final Image ATT_IMAGE = ImageLoader.loadImage(Path.PLAYER_IMG + "Attributes.png") ;

    public AttributeBar(int style, Point topLeft)
    {
        this.style = style ;
        this.topLeft = topLeft ;
        updateBarSize(style) ;
        updateBarsPos() ;
    }

    public int getStyle() { return this.style ;}

    public void updatePos(Point topLeft)
    {
        this.topLeft = topLeft ;
        updateBarsPos() ;
    }

    public void updateStyle(int style)
    {
        this.style = style ;
        updateBarSize(style) ;
        updateBarsPos() ;
    }

    private void updateBarSize(int style) { this.barSize = style == 0 ? new Dimension(32, 4) : new Dimension(5, 60) ;}

    private void updateBarsPos()
    {
        this.barPos = new ArrayList<>() ;
        for (int i = 0 ; i <= QTD_ATT - 1 ; i += 1)
        {
            if (style == 0)
            {
                barPos.add(Util.translate(topLeft, 0, - QTD_ATT * barSize.height - 3 + i * barSize.height)) ;
            }
            else
            {
			    barPos.add(Util.translate(topLeft, 63 + i * (barSize.width + 26), 70)) ;
            }
        }
    }
    
	public void display(Point topLeft, PersonalAttributes PA, boolean showAll)
	{
        if (!this.topLeft.equals(topLeft))
        {
            this.topLeft = topLeft ;
            updateBarsPos() ;
        }

		List<Double> attRate = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attRate.add(PA.getMp().getRate()) ;
		if (showAll)
		{
			attRate.add(PA.getExp().getRate()) ;
			attRate.add(PA.getSatiation().getRate()) ;
			attRate.add(PA.getThirst().getRate()) ;
		}

		if (style == 0)
		{
			for (int i = 0; i <= attRate.size() - 1; i += 1)
			{
				Dimension filledSize = new Dimension((int)(attRate.get(i) * barSize.width), barSize.height) ;
				GamePanel.getDP().drawRect(barPos.get(i), Align.topLeft, filledSize, 1, PersonalAttributes.getColors().get(i), Palette.colors[0], 1.0) ;
			}
		}
		if (style == 1)
		{
			GamePanel.getDP().drawImage(ATT_IMAGE, topLeft, Align.topLeft, 0.85) ;
			for (int i = 0; i <= attRate.size() - 1; i += 1)
			{
				Dimension rateSize = new Dimension(barSize.width, (int) (attRate.get(i) *  barSize.height)) ;
				GamePanel.getDP().drawRect(barPos.get(i), Align.bottomCenter, rateSize, 1, PersonalAttributes.getColors().get(i), null, 1.0) ;
				GamePanel.getDP().drawRect(barPos.get(i), Align.bottomCenter, barSize, 1, null, Palette.colors[0], 1.0) ;
			}
		}
	}
}