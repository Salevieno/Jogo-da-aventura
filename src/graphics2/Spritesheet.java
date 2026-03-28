package graphics2;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import main.ImageLoader;
import main.Log;
import utilities.Util;


public class Spritesheet
{
    private final BufferedImage sheet;

    public Spritesheet(String path)
    {
    	Image img = ImageLoader.loadImage(path) ;

        if (img == null)
        {
            Log.error("When loading spritesheet: image not found at " + path) ;
            sheet = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB) ;
            return ;
        }

        sheet = Util.toBufferedImage(img) ;
    }

    public BufferedImage getSprite(int x, int y, int width, int height)
    {
        if (sheet.getWidth() < x + width || sheet.getHeight() < y + height)
        {
            Log.error("When getting sprite: pos outside of boundaries") ;
            return null ;
        }

        return sheet.getSubimage(x, y, width, height);
    }

    public Dimension getSize() { return new Dimension(sheet.getWidth(), sheet.getHeight()) ;}

}
