package utilities;

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
}
