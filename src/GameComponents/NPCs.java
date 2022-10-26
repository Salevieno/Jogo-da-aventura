package GameComponents ;

import java.awt.Color ;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;

import Graphics.DrawPrimitives;
import LiveBeings.Player;

public class NPCs
{
	private int id ;
	private String Name ;
	private Point Pos ;
	private Image image ;
	private int Map ;
	private String PosRelToBuilding ;
	private String Info ;
	private Color color ;	
	public boolean Firstcontact ;
	
	public NPCs(int ID, String Name, Point Pos, Image image, int Map, String PosRelToBuilding, String Info, Color color)
	{
		this.id = ID ;
		this.Name = Name ;
		this.Pos = Pos ;
		this.image = image ;
		this.Map = Map ;
		this.PosRelToBuilding = PosRelToBuilding ;
		this.Info = Info ;
		this.color = color ;		
		Firstcontact = true ;
	}

	public int getID() {return id ;}
	public String getName() {return Name ;}
	public Point getPos() {return Pos ;}
	public Image getImage() {return image ;}
	public int getMap() {return Map ;}
	public String getPosRelToBuilding() {return PosRelToBuilding ;}
	public String getInfo() {return Info ;}
	public Color getColor() {return color ;}
	public void setID(int I) {id = I ;}
	public void setName(String N) {Name = N ;}
	public void setPos(Point P) {Pos = P ;}
	public void setImage(Image I) {image = I ;}
	public void setMap(int M) {Map = M ;}
	public void setPosRelToBuilding(String P) {PosRelToBuilding = P ;}
	public void setInfo(String I) {Info = I ;}
	public void setColor(Color C) {color = C ;}
	
	public void display(DrawPrimitives DP)
	{
		//if (id == 11 + 17*player.getMap())	// Master
		//{
		//	player.display(Pos, new float[] {1, 1}, Player.MoveKeys[3], false, DP) ;
		//}
		//else
		//{
		DP.DrawImage(image, Pos, DrawPrimitives.OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "BotCenter", 1) ;
		DP.DrawText(Pos, "BotCenter", DrawPrimitives.OverallAngle, String.valueOf(id), new Font("SansSerif", Font.BOLD, 10), Color.blue) ;				
		//}
	}
	
	@Override
	public String toString() {
		return "NPCs [id=" + id + ", Name=" + Name + ", Pos=" + Pos + ", image=" + image + ", Map=" + Map
				+ ", PosRelToBuilding=" + PosRelToBuilding + ", Info=" + Info + ", color=" + color + ", Firstcontact="
				+ Firstcontact + "]";
	}
}