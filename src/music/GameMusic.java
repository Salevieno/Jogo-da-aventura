package music;

import java.io.File;

import javax.sound.sampled.Clip;

import main.Path;

public class GameMusic
{
    public static Clip load(String fileName)
	{
		return MusicManager.fileToClip(new File(Path.MUSIC + fileName).getAbsoluteFile()) ;
	}
}
