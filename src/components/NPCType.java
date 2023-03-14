package components;

import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;

public class NPCType
{
	private String name ;	// npc name
	private NPCJobs job ;	// npc job
	private String info ;	// info about the npc
	private Color color ;	// npc text color
	private Image image ;	// npc image
	private String[] speech ;	// npc speech
	private String[][] options ;	// options given by the NPC
	
	public NPCType(String name, NPCJobs job, String info, Color color, Image image, String[] speech, String[][] options)
	{
		this.name = name ;
		this.job = job ;
		this.info = info ;
		this.color = color ;
		this.image = image ;
		this.speech = speech ;
		this.options = options ;
	}
	

	public String getName() {return name ;}
	public NPCJobs getJob() {return job ;}
	public String getInfo() {return info ;}
	public Color getColor() {return color ;}
	public Image getImage() {return image ;}
	public String[] getSpeech() {return speech ;}
	public String[][] getOptions() {return options ;}


	@Override
	public String toString()
	{
		return "NPCType [name=" + name + ", job=" + job + ", info=" + info + ", color=" + color + ", image=" + image
				+ ", speech=" + Arrays.toString(speech) + ", options=" + Arrays.toString(options) + "]";
	}
	
}
