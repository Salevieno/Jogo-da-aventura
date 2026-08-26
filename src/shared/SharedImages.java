package shared;


import java.awt.Image;

import main.ImageLoader;
import main.Path;

public abstract class SharedImages
{
	private static final Image COIN_IMG = ImageLoader.loadImage(Path.PLAYER_IMG + "CoinIcon.png") ;
    
	public static Image getCoinImg() { return COIN_IMG ;}
}
