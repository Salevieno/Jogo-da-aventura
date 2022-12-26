package components;

import java.awt.Color;
import java.awt.Image;

public class NPCType
{
	private String name ;	// npc name
	private String info ;	// info about the npc
	private Color color ;	// npc text color
	private Image image ;	// npc image
	
	public NPCType(String name, String info, Color color, Image image)
	{
		this.name = name ;
		this.info = info ;
		this.color = color ;
		this.image = image ;
	}
	

	public String getName() {return name ;}
	public String getInfo() {return info ;}
	public Color getNColor() {return color ;}
	public Image getImage() {return image ;}
}
