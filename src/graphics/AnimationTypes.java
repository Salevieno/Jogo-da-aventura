package graphics;

public enum AnimationTypes
{
	damage(1),
	win(6),
	pterodactile(20),
	fishing(5),
	levelUp(12),
	message(2),
	obtainedItem(2) ;
	
	private double duration ;
	private AnimationTypes(double duration)
	{
		this.duration = duration ;
	}
	
	public double getDuration() { return duration ;}
}
