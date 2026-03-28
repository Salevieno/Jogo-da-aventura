package main;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

import utilities.Util;

public abstract class ImageLoader
{

	private static final Set<String> loadedImagePaths = new HashSet<>();

	public static Image loadImage(String path)
	{
		if (loadedImagePaths.contains(path)) {Log.warn("Loading image " + path + " multiple times") ;}

		loadedImagePaths.add(path);
		return Util.loadImage(Path.IMAGES + path);
	}
}
