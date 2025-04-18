package graphics2;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import graphics.Align;
import graphics.DrawPrimitives;

public class SpriteAnimation
{
	private BufferedImage[] frames;
    private int currentFrame;
    private int frameCount;
    private int frameDuration;
    private int tick; // TODO converter para time
    private Point pos ;
    private Align align ;
    private boolean active ;
    
    private static final Set<SpriteAnimation> all ;
    
    static
    {
    	all = new HashSet<>() ;
    }

    public SpriteAnimation(String path, Point pos, Align align, Dimension size, int qtdFrames, int frameDuration)
    {
    	Spritesheet sheet = new Spritesheet(path) ;
    	BufferedImage[] sheetFrames = new BufferedImage[qtdFrames] ;
        for (int i = 0; i <= sheetFrames.length - 1 ; i++)
        {
            sheetFrames[i] = sheet.getSprite(i * size.width, 0, size.width, size.height) ;
        }
        this.frames = sheetFrames;
        this.frameDuration = frameDuration;
        this.frameCount = frames.length;
        this.currentFrame = 0;
        this.tick = 0;
        this.pos = pos ;
        this.align = align ;
        this.active = true ;
        all.add(this) ;
    }
    
    public void setPos(Point pos) { this.pos = pos ;}
    
    public void activate() { active = true ;}
    public void deactivate() { active = false ;}    
    
    public static void updateAll()
    {
    	all.stream().filter(sprite -> sprite.active).forEach(sprite -> sprite.update()) ;
    }
    
    public static void displayAll(DrawPrimitives DP)
    {
    	all.stream().filter(sprite -> sprite.active).forEach(sprite -> sprite.display(DP)) ;
    }

    private void update()
    {
        tick++;
        if (frameDuration <= tick)
        {
            tick = 0;
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }
    
    public void display(DrawPrimitives DP)
    {
    	DP.drawImage(getCurrentFrame(), pos, align) ;
    }

    public BufferedImage getCurrentFrame()
    {
        return frames[currentFrame];
    }
}
