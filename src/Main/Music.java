package Main;

import java.io.File;

import javax.sound.sampled.Clip;

import Utilities.UtilG;

public class Music
{
	private boolean isOn ;
	private Clip[] musicClip, soundEffect ;
	
	private Clip musicIntro ;
	
	public Music()
	{
		Clip MusicKnightCity = UtilG.MusicFileToClip(new File(Game.MusicPath + "2-Knight city.wav").getAbsoluteFile()) ;
    	Clip MusicMageCity = UtilG.MusicFileToClip(new File(Game.MusicPath + "3-Mage city.wav").getAbsoluteFile()) ;
    	Clip MusicArcherCity = UtilG.MusicFileToClip(new File(Game.MusicPath + "4-Archer city.wav").getAbsoluteFile()) ;
    	Clip MusicAnimalCity = UtilG.MusicFileToClip(new File(Game.MusicPath + "5-Animal city.wav").getAbsoluteFile()) ;
    	Clip MusicAssassinCity = UtilG.MusicFileToClip(new File(Game.MusicPath + "6-Assassin city.wav").getAbsoluteFile()) ;
    	Clip MusicForest = UtilG.MusicFileToClip(new File(Game.MusicPath + "7-Forest.wav").getAbsoluteFile()) ;
    	Clip MusicCave = UtilG.MusicFileToClip(new File(Game.MusicPath + "8-Cave.wav").getAbsoluteFile()) ;
    	Clip MusicIsland = UtilG.MusicFileToClip(new File(Game.MusicPath + "9-Island.wav").getAbsoluteFile()) ;
    	Clip MusicVolcano = UtilG.MusicFileToClip(new File(Game.MusicPath + "10-Volcano.wav").getAbsoluteFile()) ;
    	Clip MusicSnowland = UtilG.MusicFileToClip(new File(Game.MusicPath + "11-Snowland.wav").getAbsoluteFile()) ;
    	Clip MusicSpecial = UtilG.MusicFileToClip(new File(Game.MusicPath + "12-Special.wav").getAbsoluteFile()) ;
    	Clip MusicSailing = UtilG.MusicFileToClip(new File(Game.MusicPath + "13-Sailing.wav").getAbsoluteFile()) ;
    	Clip MusicPlayerEvolution = UtilG.MusicFileToClip(new File(Game.MusicPath + "14-Player evolution.wav").getAbsoluteFile()) ;
    	Clip MusicDrumRoll = UtilG.MusicFileToClip(new File(Game.MusicPath + "15-Drumroll.wav").getAbsoluteFile()) ;
    	musicClip = new Clip[] {MusicKnightCity, MusicMageCity, MusicArcherCity, MusicAnimalCity, MusicAssassinCity, MusicForest, MusicCave, MusicIsland, MusicVolcano, MusicSnowland, MusicSpecial, MusicSailing, MusicPlayerEvolution, MusicDrumRoll} ;
	
    	Clip SoundEffectSwordHit = UtilG.MusicFileToClip(new File(Game.MusicPath + "16-Hit.wav").getAbsoluteFile()) ;
    	soundEffect = new Clip[] {SoundEffectSwordHit} ;
    	
    	musicIntro = UtilG.MusicFileToClip(new File(Game.MusicPath + "1-Intro.wav").getAbsoluteFile()) ;
	}
	
	public boolean isOn()
	{
		return isOn ;
	}
	public Clip[] getMusicClip() { return musicClip ; }
	public Clip[] getSoundEffect() { return soundEffect ; }
	public Clip getMusicIntro() { return musicIntro ; }
	
	public void ResetMusic(Clip MusicFile)
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
	
	public void PlayMusic(Clip MusicFile)
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
 	
 	public void StopMusic(Clip MusicFile)
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
 	
 	public void SwitchMusic(Clip MusicFile1, Clip MusicFile2)
 	{
 		ResetMusic(MusicFile1) ;
 		StopMusic(MusicFile1) ;
		PlayMusic(MusicFile2) ;
 	}
}
