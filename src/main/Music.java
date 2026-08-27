package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music
{
	private final Clip clip ;
    private double volume ;
    private FloatControl soundControl ;
	
	private static Clip currentlyPlayingClip = null ;
    private static final double MIN_VOL = 0.0001 ;
    private static final double MAX_VOL = 1.0 ;
	
	
	public Music(Clip clip)
	{
		this.clip = clip ;
        this.volume = 1.0 ;
        this.soundControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN) ;
	}
	
	public Clip getClip() {return clip ;}

	public static Clip loadMusicFile(String fileName)
	{
		return musicFileToClip(new File(Path.MUSIC + fileName).getAbsoluteFile()) ;
	}

	public static Clip loadSoundEffect(String fileName)
	{
		return musicFileToClip(new File(Path.SOUND_EFFECTS + fileName).getAbsoluteFile()) ;
	}
	
	public static Clip musicFileToClip(File MusicFile)
	{
		Clip MusicClip = null ;
		try 
 		{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(MusicFile) ;
	        MusicClip = AudioSystem.getClip() ;
	        MusicClip.open(audioInputStream) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When loading clip.") ;
 	        ex.printStackTrace() ;
 	    }
		
		return MusicClip ;
	}

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

	public static void playMusic(Clip clip)
 	{		
 		try 
 		{
 			currentlyPlayingClip = clip ;
			clip.setFramePosition(0) ;
	        clip.start() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
    

	public void play(double volume)
 	{
        if (clip.isRunning()) { return ;}

        setVolume(volume) ;
        playMusic(clip) ;
    }
	
	public static void loopMusic(Clip MusicFile)
 	{
 		try 
 		{
 			currentlyPlayingClip = MusicFile ;
	        MusicFile.loop(999) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void stopMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.close() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When with stopping sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void switchMusic(Clip newClip)
 	{
 		if (currentlyPlayingClip != null)
 		{
 			stopMusic(currentlyPlayingClip) ;
 		}
		playMusic(newClip) ;
 	}
}
