package music;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import main.Log;

public abstract class MusicManager
{
    
	private static Clip currentlyPlayingClip = null ;

	
	public static Clip fileToClip(File file)
	{
		Clip MusicClip = null ;
		try 
 		{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file) ;
	        MusicClip = AudioSystem.getClip() ;
	        MusicClip.open(audioInputStream) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        Log.error("When loading clip " + file.getName()) ;
 	        ex.printStackTrace() ;
 	    }
		
		return MusicClip ;
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
