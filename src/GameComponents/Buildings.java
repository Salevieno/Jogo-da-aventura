package GameComponents;

public class Buildings
{
	private String Name;
	private int[] Pos;
	private int Size;
	
	public Buildings(String Name, int[] Pos, int Size)
	{
		this.Name = Name;
		this.Pos = Pos;
		this.Size = Size;
	}

	public String getName() {return Name;}
	public int[] getPos() {return Pos;}
	public int getSize() {return Size;}
	public void setName(String N) {Name = N;}
	public void setPos(int[] P) {Pos = P;}
	public void setSize(int S) {Size = S;}
}