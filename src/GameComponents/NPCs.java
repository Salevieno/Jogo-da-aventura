package GameComponents;

import java.awt.Color;

public class NPCs
{
	private String Name;
	private int[] Pos;
	private Color color;
	
	public NPCs(String Name, int[] Pos, Color color)
	{
		this.Name = Name;
		this.Pos = Pos;
		this.color = color;
	}

	public String getName() {return Name;}
	public int[] getPos() {return Pos;}
	public Color getColor() {return color;}
	public void setName(String N) {Name = N;}
	public void setPos(int[] P) {Pos = P;}
	public void setColor(Color C) {color = C;}
}