package GameComponents;

public class Maps 
{
	private String Name;
	private String Continent;
	private boolean[][] Walkable;
	
	public Maps(String Name, String Continent, boolean[][] Walkable)
	{
		this.Name = Name;
		this.Continent = Continent;
		this.Walkable = Walkable;
	}

	public String getName() {return Name;}
	public String getContinent() {return Continent;}
	public boolean[][] getWalkable() {return Walkable;}
	public void setName(String N) {Name = N;}
	public void setContinent(String C) {Continent = C;}
	public void setWalkable(boolean[][] W) {Walkable = W;}
}
