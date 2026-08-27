package music;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.Game;
import main.Log;
import main.Path;

public class GameSound
{
	private final Clip clip ;
    private double volume ;
    private FloatControl soundControl ;

    private static final double MIN_VOL = 0.0001 ;
    private static final double MAX_VOL = 1.0 ;
	
	
	public GameSound(String fileName)
	{
		this.clip = MusicManager.fileToClip(new File(Path.SOUND_EFFECTS + fileName).getAbsoluteFile()) ;
        this.volume = 1.0 ;
        this.soundControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN) ;
	}

    public boolean isPlaying() { return clip.isRunning() ;}

    public void setVolume(double volume)
    {
        float decibels = 20.0f * (float) Math.log10(volume) ;
        decibels = Math.max(soundControl.getMinimum(), Math.min(soundControl.getMaximum(), decibels)) ;

        soundControl.setValue(decibels) ;
    }

    public void incVolume(double amount)
    {
        volume = Math.min(MAX_VOL, volume + amount) ;
        setVolume(volume + amount) ;
    }

    public void decVolume(double amount)
    {
        volume = Math.max(MIN_VOL, volume + amount) ;
        setVolume(volume - amount) ;
    }
    
	public void play()
 	{
        if (!Game.getSettings().getSoundEffectsAreOn()) { return ;}

 		try 
 		{
			clip.setFramePosition(0) ;
	        clip.start() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}

	public void playAtVolume(double volume)
 	{
        setVolume(volume) ;
        play() ;
    }
}
