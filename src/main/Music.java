package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music
{
	private boolean isOn ;
	private Clip[] musicClip, soundEffect ;
	
	private Clip musicIntro ;
	
	public Music()
	{
		Clip MusicKnightCity = musicFileToClip(new File(Game.MusicPath + "2-Knight city.wav").getAbsoluteFile()) ;
    	Clip MusicMageCity = musicFileToClip(new File(Game.MusicPath + "3-Mage city.wav").getAbsoluteFile()) ;
    	Clip MusicArcherCity = musicFileToClip(new File(Game.MusicPath + "4-Archer city.wav").getAbsoluteFile()) ;
    	Clip MusicAnimalCity = musicFileToClip(new File(Game.MusicPath + "5-Animal city.wav").getAbsoluteFile()) ;
    	Clip MusicAssassinCity = musicFileToClip(new File(Game.MusicPath + "6-Assassin city.wav").getAbsoluteFile()) ;
    	Clip MusicForest = musicFileToClip(new File(Game.MusicPath + "7-Forest.wav").getAbsoluteFile()) ;
    	Clip MusicCave = musicFileToClip(new File(Game.MusicPath + "8-Cave.wav").getAbsoluteFile()) ;
    	Clip MusicIsland = musicFileToClip(new File(Game.MusicPath + "9-Island.wav").getAbsoluteFile()) ;
    	Clip MusicVolcano = musicFileToClip(new File(Game.MusicPath + "10-Volcano.wav").getAbsoluteFile()) ;
    	Clip MusicSnowland = musicFileToClip(new File(Game.MusicPath + "11-Snowland.wav").getAbsoluteFile()) ;
    	Clip MusicSpecial = musicFileToClip(new File(Game.MusicPath + "12-Special.wav").getAbsoluteFile()) ;
    	Clip MusicSailing = musicFileToClip(new File(Game.MusicPath + "13-Sailing.wav").getAbsoluteFile()) ;
    	Clip MusicPlayerEvolution = musicFileToClip(new File(Game.MusicPath + "14-Player evolution.wav").getAbsoluteFile()) ;
    	Clip MusicDrumRoll = musicFileToClip(new File(Game.MusicPath + "15-Drumroll.wav").getAbsoluteFile()) ;
    	musicClip = new Clip[] {MusicKnightCity, MusicMageCity, MusicArcherCity, MusicAnimalCity, MusicAssassinCity, MusicForest, MusicCave, MusicIsland, MusicVolcano, MusicSnowland, MusicSpecial, MusicSailing, MusicPlayerEvolution, MusicDrumRoll} ;
	
    	Clip SoundEffectSwordHit = musicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;
    	soundEffect = new Clip[] {SoundEffectSwordHit} ;
    	
    	musicIntro = musicFileToClip(new File(Game.MusicPath + "1-Intro.wav").getAbsoluteFile()) ;
	}
	
	public boolean isOn()
	{
		return isOn ;
	}
	public Clip[] getMusicClip() { return musicClip ; }
	public Clip[] getSoundEffect() { return soundEffect ; }
	public Clip getMusicIntro() { return musicIntro ; }
	
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
	
	public static void ResetMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.setMicrosecondPosition(0) ; ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with starting sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
	
	public static void PlayMusic(Clip MusicFile)
 	{
 		try 
 		{
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
	        MusicFile.stop() ;
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with stopping sound.") ;
 	        ex.printStackTrace() ;
 	    }
 	}
 	
 	public static void SwitchMusic(Clip MusicFile1, Clip MusicFile2)
 	{
 		ResetMusic(MusicFile1) ;
 		StopMusic(MusicFile1) ;
		PlayMusic(MusicFile2) ;
 	}
}
