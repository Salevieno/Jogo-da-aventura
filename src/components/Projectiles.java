package components ;

import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;

import graphics.DrawingOnPanel;
import liveBeings.Creature;
import liveBeings.Pet;
import liveBeings.Player;
import utilities.AlignmentPoints;
import utilities.Scale;
import utilities.UtilG;
import utilities.UtilS;

public class Projectiles
{
	private Point Pos ;
	private int Type ;		// 0: friendly, 1: hostile
	private int damage ;
	private double[] speed ;	// [vx, vy]
	private int range ;
	private Image image ;
	
	public Projectiles(Point Pos, int Type, int damage, double[] speed, int range, Image image)
	{
		this.Pos = Pos ;
		this.Type = Type ;
		this.damage = damage ;
		this.speed = speed ;
		this.range = range ;
		this.image = image ;
	}

	public Point getPos() {return Pos ;}
	public int getType() {return Type ;}
	public int getdamage() {return damage ;}
	public double[] getSpeed() {return speed ;}
	public int getrange() {return range ;}
	public Image getImage() {return image ;}
	public void setPos(Point P) {Pos = P ;}
	public void setType(int T) {Type = T ;}
	public void setdamage(int D) {damage = D ;}
	public void setSpeed(double[] S) {speed = S ;}
	public void setrange(int R) {range = R ;}
	public void setImage(Image I) {image = I ;}
	
	public void DrawImage(DrawingOnPanel DP)
	{
		DP.DrawImage(image, Pos, UtilG.getAngle(speed), new Scale(1, 1), AlignmentPoints.center) ;
	}
	public void move()
	{
		Pos.x += speed[0] ;
		Pos.y += speed[1] ;
	}
	public int collidedwith(Player player, ArrayList<Creature> creature, Pet pet)
	{
		// Type 0 is friendly (shot by the player or the pet)
		// Type 1 is hostile (shot by the creature)
		if (UtilS.IsInRange(Pos, player.getPos(), range) & Type == 1)
		{
			return -1 ;	// if a hostile projectile hits the player
		}
		if (UtilS.IsInRange(Pos, pet.getPos(), range) & Type == 1)
		{
			return -2 ;	// if a hostile projectile hits the pet
		}
		for (int c = 0 ; c <= creature.size() - 1 ; c += 1)
		{
			if (UtilS.IsInRange(Pos, creature.get(c).getPos(), range) & Type == 0)
			{
				return c ;	// if a friendly projectile hits the creature
			}
		}
		return -3 ;	// if the projectile has not hit anything
	}
	public void go(Player player, ArrayList<Creature> creature, Pet pet, DrawingOnPanel DP)
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
		else if (-1 < hit & Type == 0)
		{
			creature.get(hit).getLife()[0] += -damage ;
			if (creature.get(hit).getLife()[0] < 0)
			{
				creature.get(hit).getLife()[0] = creature.get(hit).getLife()[1] ;
				creature.get(hit).getMp()[0] = creature.get(hit).getMp()[1] ;
				creature.get(hit).setFollow(false) ;
				//creature[hit].setPos(Utg.RandomPos(screen.getDimensions(), new double[] {0, (double)(0.2)}, new double[] {1, 1 - (double)(SkyHeight)/screen.getDimensions()[1]}, new int[] {1, 1})) ;
			}
		}
	}
}