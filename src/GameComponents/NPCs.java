package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;

public class NPCs
{
	private int ID ;
	private String Name ;
	private int[] Pos ;
	private Image image ;
	private int Map ;
	private String PosRelToBuilding ;
	private String Info ;
	private Color color ;	
	public boolean Firstcontact ;
	
	public NPCs(int ID, String Name, int[] Pos, Image image, int Map, String PosRelToBuilding, String Info, Color color)
	{
		this.ID = ID ;
		this.Name = Name ;
		this.Pos = Pos ;
		this.image = image ;
		this.Map = Map ;
		this.PosRelToBuilding = PosRelToBuilding ;
		this.Info = Info ;
		this.color = color ;		
		Firstcontact = true ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public int[] getPos() {return Pos ;}
	public Image getImage() {return image ;}
	public int getMap() {return Map ;}
	public String getPosRelToBuilding() {return PosRelToBuilding ;}
	public String getInfo() {return Info ;}
	public Color getColor() {return color ;}
	public void setID(int I) {ID = I ;}
	public void setName(String N) {Name = N ;}
	public void setPos(int[] P) {Pos = P ;}
	public void setImage(Image I) {image = I ;}
	public void setMap(int M) {Map = M ;}
	public void setPosRelToBuilding(String P) {PosRelToBuilding = P ;}
	public void setInfo(String I) {Info = I ;}
	public void setColor(Color C) {color = C ;}
}