package NPC;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import main.ImageLoader;
import main.Palette;
import main.Path;

public enum NPCJobs
{
	alchemist (0, Palette.colors[4], List.of(List.of(1, 4), List.of(), List.of(), List.of(), List.of(4), List.of(-1))),
	banker (1, Palette.colors[13], List.of(List.of(1, 2, 3, 3, 8), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(8), List.of(8), List.of(8), List.of(-1))),
	citizen (2, Palette.colors[5], List.of(List.of(1), List.of(2), List.of(-1))),
	crafter (3, Palette.colors[21], List.of(List.of(1, 4), List.of(), List.of(), List.of(), List.of(4), List.of(), List.of(-1))),
	doctor (4, Palette.colors[3], List.of(List.of(3, 4), List.of(), List.of(), List.of(), List.of(4), List.of(-1))),
	elemental (5, Palette.colors[3], List.of(List.of(0, 1), List.of(1), List.of(-1))),
	equipsSeller (6, Palette.colors[20], List.of(List.of(1, 2), List.of(), List.of(2), List.of(-1))),
	itemsSeller (7, Palette.colors[12], List.of(List.of(1, 2), List.of(), List.of(2), List.of(-1))),
	smuggleSeller (8, Palette.colors[17], List.of(List.of(1, 2), List.of(), List.of(2), List.of(-1))),
	master (9, Palette.colors[5], List.of(List.of(1, 6), List.of(), List.of(3, 5), List.of(), List.of(6), List.of(1, 6), List.of(6), List.of(-1))),
	woodcrafter (10, Palette.colors[8], List.of(List.of(1, 4), List.of(), List.of(), List.of(), List.of(4), List.of(-1))),
	forger (11, Palette.colors[21], List.of(List.of(0, 1), List.of(1), List.of(-1))),
	saver (12, Palette.colors[19], List.of(List.of(1, 3), List.of(2, 2, 2), List.of(3), List.of(3), List.of(-1))),
	questExp (13, Palette.colors[5], List.of(List.of(1, 4), List.of(2), List.of(2), List.of(4), List.of(4), List.of(-1))),
	questItem (14, Palette.colors[5], List.of(List.of(1, 4), List.of(2), List.of(2), List.of(4), List.of(4), List.of(-1)));
	
	private final int id ;
	private final Image image ;
	private final Color color ;
	private final List<List<Integer>> destination ;
	
	private NPCJobs(int id, Color color, List<List<Integer>> destination)
	{
		this.id = id ;
		this.image = ImageLoader.loadImage(Path.NPC_IMG + "NPC_" + this.toString() + ".png") ;
		this.color = color ;
		this.destination = destination ;
	}

	public static NPCJobs getByID(int id)
	{
		for (NPCJobs job : values())
		{
			if (id == job.id) { return job ;}
		}

		return null ;
	}
	
	public int getID() { return id ;}
	public Image getImage() { return image ;}
	public Color getColor() { return color ;}
	public List<List<Integer>> getDestination() { return destination ;}
}
