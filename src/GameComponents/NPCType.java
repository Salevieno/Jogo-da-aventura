package GameComponents;

import java.awt.Color;
import java.awt.Image;

public class NPCType
{
	private String Name ;
	private String Info ;
	private Color color ;
	private Image image ;
	
	public NPCType(String Name, String Info, Color color, Image image)
	{
		this.Name = Name ;
		this.Info = Info ;
		this.color = color ;
		this.image = image ;
	}
	

	public String getName() {return Name ;}
	public String getInfo() {return Info ;}
	public Color getNColor() {return color ;}
	public Image getImage() {return image ;}
}
