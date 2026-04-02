package graphics2;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import graphics.Align;
import graphics.DrawPrimitives;
import main.GameTimer;
import main.Log;

public class SpriteAnimation
{
	private BufferedImage[] frames;
    private int currentFrameID;
    private int frameCount;
    private double frameDuration;
    private Dimension frameSize;
    private GameTimer timer;
    private Point pos ;
    private Align align ;
    private boolean loops ;
    private boolean active ;
    private int layer ;
    
    private static final Set<SpriteAnimation> all ;
    
    static
    {
    	all = new HashSet<>() ;
    }

    public SpriteAnimation(String path, Point pos, Align align, boolean loops, int qtdFrames, double frameDuration, int layer)
    {
    	Spritesheet sheet = new Spritesheet(path) ;
    	BufferedImage[] sheetFrames = new BufferedImage[qtdFrames] ;
        this.frameSize = new Dimension(sheet.getSize().width / qtdFrames, sheet.getSize().height) ;
        for (int i = 0; i <= sheetFrames.length - 1 ; i++)
        {
            sheetFrames[i] = sheet.getSprite(i * frameSize.width, 0, frameSize.width, frameSize.height) ;
        }
        this.frames = sheetFrames;
        this.frameDuration = frameDuration;
        this.frameCount = frames.length;
        this.currentFrameID = 0;
        this.layer = layer ;
        this.timer = new GameTimer(frameDuration * frames.length) ;
        this.pos = pos ;
        this.align = align ;
        this.loops = loops ;
        this.active = false ;
        all.add(this) ;
    }

    public SpriteAnimation(String path, Point pos, Align align, boolean loops, int qtdFrames, double frameDuration)
    {
        this(path, pos, align, loops, qtdFrames, frameDuration, 1) ;
    }

    public SpriteAnimation(String path, Point pos, Align align, int qtdFrames, double frameDuration)
    {
    	this(path, pos, align, true, qtdFrames, frameDuration) ;
    }

    public SpriteAnimation(String path, Align align, int qtdFrames, double frameDuration)
    {
    	this(path, null, align, true, qtdFrames, frameDuration) ;
    }
    
    public Dimension getFrameSize() { return frameSize ;}

    public int getLayer() { return layer ;}

    public BufferedImage getCurrentFrame() { return frames[currentFrameID] ;}
    public double getTotalDuration() { return frameDuration * frameCount ;}
    public void setPos(Point pos) { this.pos = pos ;}
    
    public boolean isActive() { return active ;}
    public void activate()
    {
        if (active) { Log.warn("Trying to activate animation that is already active") ; return ;}

        active = true ;
        timer.restart() ;
    }
    public void deactivate()
    {
        if (!active) { Log.warn("Trying to deactivate animation that is already inactive") ; return ;}

        active = false ;
        timer.stop() ;
    }
    public void activateIfInactive() { if (!active) activate() ;}
    
    public static void updateAll()
    {
    	all.stream().filter(sprite -> sprite.active).forEach(sprite -> sprite.update()) ;
    }
    
    public static void displayAllFromLayer(int layer, DrawPrimitives DP)
    {
    	all.stream().filter(sprite -> sprite.active).filter(sprite -> sprite.layer == layer).forEach(sprite -> sprite.display(DP)) ;
    }
    
    public static void displayAll(DrawPrimitives DP)
    {
    	all.stream().filter(sprite -> sprite.active).forEach(sprite -> sprite.display(DP)) ;
    }

    private void update()
    {
        if (timer.crossedTime(frameDuration) && timer.getCounter() != 0)
        {
            currentFrameID = (currentFrameID + 1) % frameCount;
        }
        if (timer.hasFinished())
        {
            if (loops)
            {
                timer.restart();
            }
            else
            {
                deactivate();
                timer.reset();
                currentFrameID = 0;
            }
        }
    }

    public boolean hasFinished()
    {
        return timer.hasFinished();
    }
    
    public void display(DrawPrimitives DP, Point pos, Align align)
    {
    	DP.drawImage(getCurrentFrame(), pos, align) ;
    }
    
    public void display(DrawPrimitives DP)
    {
    	display(DP, pos, align) ;
    }

    @Override
    public String toString()
    {
        return "SpriteAnimation [currentFrame=" + currentFrameID + " / " + frameCount + ", frameDuration="
                + frameDuration + ", timer=" + timer + ", pos=" + pos + ", align=" + align + ", loops=" + loops
                + ", active=" + active + "]";
    }


}
