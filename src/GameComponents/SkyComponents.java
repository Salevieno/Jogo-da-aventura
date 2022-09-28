package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;
import Graphics.DrawPrimitives ;

public class SkyComponents 
{
	private Image image ;
	private String Name ;
	private int[] Pos ;		// [x, y]
	private float[] speed ;
	private int[] size ;
	private Color[] color ;
	private int Counter ;
	
	public SkyComponents (Image image, String Name, int[] Pos, float[] speed, Color[] color)
	{
		this.image = image ;
		this.Name = Name ;
		this.Pos = Pos ;
		this.speed = speed ;
		size = new int[2] ;
		if (image != null)
		{
			size[0] = image.getWidth(null) ;
			size[1] = image.getHeight(null) ;
		}
		this.color = color ;
		Counter = 0 ;
	}

	public Image getImage() {return image ;}
	public String getName() {return Name ;}
	public int[] getPos() {return Pos ;}
	public float[] getSpeed() {return speed ;}
	public int[] getSize() {return size ;}
	public Color[] getColor() {return color ;}
	public int getCounter() {return Counter ;}
	public void setImage(Image I) {image = I ;}
	public void setName(String N) {Name = N ;}
	public void setPos(int[] P) {Pos = P ;}
	public void setSpeed(float[] S) {speed = S ;}
	public void setColor(Color[] C) {color = C ;}
	public void setCounter(int C) {Counter = C ;}
	

	public void IncCounter(int MaxCounter)
	{
		Counter = (Counter + 1) % MaxCounter ;
	}
	public void DrawImage(float angle, DrawPrimitives DP) {DP.DrawImage(image, Pos, angle, new float[] {1, 1}, new boolean[] {false, false}, "TopLeft", 1) ;}
}
