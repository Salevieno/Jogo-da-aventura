package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music
{
//	private boolean isOn ;
	private final Clip clip ;
	
	private static Clip currentlyPlayingClip = null ;
	
	
	public Music(Clip clip)
	{
		this.clip = clip ;
//		isOn = false ;
	}
	
//	public boolean isOn() {return isOn ;}
	
	public Clip getClip() {return clip ;}
	
	public static Clip loadMusicFile(String fileName)
	{
		return musicFileToClip(new File(Path.MUSIC + fileName).getAbsoluteFile()) ;
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
 	        System.out.println("Error loading clip.") ;
 	        ex.printStackTrace() ;
 	    }
		
		return MusicClip ;
	}
	
	public static void PlayMusic(Clip MusicFile)
 	{
		// Melhor manter a linha abaixo comentadas para efeitos sonoros
		// if (MusicFile.isRunning() | MusicFile.isActive()) { return ;}
		
 		try 
 		{
 			currentlyPlayingClip = MusicFile ;
			MusicFile.setFramePosition(0) ;
	        MusicFile.start() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
	
	public static void LoopMusic(Clip MusicFile)
 	{
 		try 
 		{
 			currentlyPlayingClip = MusicFile ;
	        MusicFile.loop(999) ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with playing sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void StopMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.close() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with stopping sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void SwitchMusic(Clip newClip)
 	{
 		if (currentlyPlayingClip != null)
 		{
 			StopMusic(currentlyPlayingClip) ;
 		}
		PlayMusic(newClip) ;
 	}
}
