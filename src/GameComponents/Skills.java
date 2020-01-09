package GameComponents;

public class Skills 
{
	private String Name;
	private int Level;
	private int Duration;
	private String Elem;
	
	public Skills(String Name, int Level, int Duration, String Elem)
	{
		this.Name = Name;
		this.Level = Level;
		this.Duration = Duration;
		this.Elem = Elem;
	}
	
	public String getName() {return Name;}
	public int getLevel() {return Level;}
	public int getDuration() {return Duration;}
	public String getElem() {return Elem;}
	public void setName(String N) {Name = N;}
	public void setLevel(int L) {Level = L;}
	public void setDuration(int D) {Duration = D;}
	public void setElem(String E) {Elem = E;}
}
