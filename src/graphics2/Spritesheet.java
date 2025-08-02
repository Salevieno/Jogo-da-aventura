package graphics2;
import java.awt.Image;
import java.awt.image.BufferedImage;

import utilities.Util;
import utilities.UtilS;

public class Spritesheet
{
    private final BufferedImage sheet;

    public Spritesheet(String path)
    {
    	Image img = UtilS.loadImage(path) ;

        if (img == null)
        {
            System.out.println("Error loading spritesheet: image not found at " + path) ;
            sheet = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB) ;
            return ;
        }

        sheet = Util.toBufferedImage(img) ;
    }

    public BufferedImage getSprite(int x, int y, int width, int height)
    {
        if (sheet.getWidth() < x + width || sheet.getHeight() < y + height)
        {
            System.out.println("Error getting sprite: pos outside of boundaries") ;
            return null ;
        }

        return sheet.getSubimage(x, y, width, height);
    }

}
