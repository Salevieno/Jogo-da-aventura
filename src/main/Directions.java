package main;

public enum Directions
{
	up,
	down,
	left,
	right;
	
	public static Directions getDir(int id)
	{
		switch (id)
		{
			case 0: return up ;
			case 1: return down ;
			case 2: return left ;
			case 3: return right ;
			default: return null;
		}
	}
	
	public static Directions getDir(String move)
	{
		switch (move)
		{
			case "Acima": return up ;
			case "Abaixo": return down ;
			case "Esquerda": return left ;
			case "Direita": return right ;
			default: return null;
		}
	}
	
	private static Directions getOpposite(Directions dir)
	{
		switch (dir)
		{
			case up: return down ;
			case down: return up ;
			case left: return right ;
			case right: return left ;
			default: return null ;
		}
	}
	
	public static boolean areOpposite(Directions dir1, Directions dir2)
	{
		return dir1.equals(Directions.getOpposite(dir2)) ;
	}
}
