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
        sheet = Util.toBufferedImage(img) ;
    }

    public BufferedImage getSprite(int x, int y, int width, int height)
    {
        return sheet.getSubimage(x, y, width, height);
    }

}
