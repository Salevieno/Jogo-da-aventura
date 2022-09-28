package GameComponents ;

import java.awt.Image ;

import Graphics.DrawPrimitives ;
import Main.Utg ;
import Main.Uts ;

public class Projectiles
{
	private int[] Pos ;		// [x, y]
	private int Type ;		// 0: friendly, 1: hostile
	private int damage ;
	private float[] speed ;	// [vx, vy]
	private int range ;
	private Image image ;
	
	public Projectiles(int[] Pos, int Type, int damage, float[] speed, int range, Image image)
	{
		this.Pos = Pos ;
		this.Type = Type ;
		this.damage = damage ;
		this.speed = speed ;
		this.range = range ;
		this.image = image ;
	}

	public int[] getPos() {return Pos ;}
	public int getType() {return Type ;}
	public int getdamage() {return damage ;}
	public float[] getSpeed() {return speed ;}
	public int getrange() {return range ;}
	public Image getImage() {return image ;}
	public void setPos(int[] P) {Pos = P ;}
	public void setType(int T) {Type = T ;}
	public void setdamage(int D) {damage = D ;}
	public void setSpeed(float[] S) {speed = S ;}
	public void setrange(int R) {range = R ;}
	public void setImage(Image I) {image = I ;}
	
	public void DrawImage(DrawPrimitives DP)
	{
		DP.DrawImage(image, Pos, Utg.getAngle(speed), new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
	}
	public void move()
	{
		Pos[0] += speed[0] ;
		Pos[1] += speed[1] ;
	}
	public int collidedwith(Player player, Creatures[] creature, Pet pet)
	{
		if (Uts.IsInRange(Pos, player.getPos(), range) & Type == 1)
		{
			return -1 ;
		}
		if (Uts.IsInRange(Pos, pet.getPos(), range) & Type == 1)
		{
			return -2 ;
		}
		for (int c = 0 ; c <= creature.length - 1 ; c += 1)
		{
			if (Uts.IsInRange(Pos, creature[c].getPos(), range))
			{
				return c ;
			}
		}
		return -3 ;
	}
	public void go(Player player, Creatures[] creature, Pet pet, DrawPrimitives DP)
	{
		DrawImage(DP) ;
		move() ;
		int hit = collidedwith(player, creature, pet) ;
		System.out.println(hit) ;
		if (hit == -1 & Type == 1)
		{
			player.getLife()[0] += -damage ;
			if (player.getLife()[0] < 0)
			{
				player.Dies() ;
			}
		}
		else if (hit == -2 & Type == 1)
		{
			pet.getLife()[0] += -damage ;
			if (pet.getLife()[0] < 0)
			{
				pet.getLife()[0] = 0 ;
			}
		}
		else if (-1 < hit)
		{
			creature[hit].getLife()[0] += -damage ;
			if (creature[hit].getLife()[0] < 0)
			{
				creature[hit].getLife()[0] = creature[hit].getLife()[1] ;
				creature[hit].getMp()[0] = creature[hit].getMp()[1] ;
				creature[hit].setFollow(false) ;
				//creature[hit].setPos(Utg.RandomPos(screen.getDimensions(), new float[] {0, (float)(0.2)}, new float[] {1, 1 - (float)(SkyHeight)/screen.getDimensions()[1]}, new int[] {1, 1})) ;
			}
		}
	}
}