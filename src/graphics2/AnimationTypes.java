package graphics2;

public enum AnimationTypes
{
	damage(1),
	win(6),
	pterodactile(20),
	fishing(5),
	levelUp(9),
	message(4),
	obtainedItem(2),
	bufferedText(4) ;
	
	private double duration ;
	private AnimationTypes(double duration)
	{
		this.duration = duration ;
	}
	
	public double getDuration() { return duration ;}
}
