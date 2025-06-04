package graphics2;
import java.awt.Image;
import java.awt.image.BufferedImage;

import utilities.Util;
import utilities.UtilS;

public class Spritesheet
{
    private BufferedImage sheet;

    public Spritesheet(String path)
    {
    	Image img = UtilS.loadImage(path) ;
        sheet = Util.toBufferedImage(img) ;
    }
    
    // private static BufferedImage load(String path)
    // {
    //     try
    //     {
    //         return ImageIO.read(Spritesheet.class.getResource(path)) ;
    //     }
    //     catch (IOException e)
    //     {
    //         e.printStackTrace() ;
    //         return null ;
    //     }
    // }

    public BufferedImage getSprite(int x, int y, int width, int height)
    {
        return sheet.getSubimage(x, y, width, height);
    }

}
