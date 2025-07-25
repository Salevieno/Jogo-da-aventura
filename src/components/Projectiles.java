package components ;

import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import graphics.Align;
import graphics.Scale;
import liveBeings.Creature;
import liveBeings.Pet;
import liveBeings.Player;
import main.GamePanel;
import maps.FieldMap;
import utilities.Util;

public class Projectiles
{
	private Point pos ;
	private int type ;		// 0: friendly, 1: hostile
	private int damage ;
	private double[] speed ;	// [vx, vy]
	private int range ;
	private Image image ;
	
	private static List<Projectiles> all ;
	
	public Projectiles(Point Pos, int Type, int damage, double[] speed, int range, Image image)
	{
		this.pos = Pos ;
		this.type = Type ;
		this.damage = damage ;
		this.speed = speed ;
		this.range = range ;
		this.image = image ;
	}

	public Point getPos() {return pos ;}
	public int getType() {return type ;}
	public int getdamage() {return damage ;}
	public double[] getSpeed() {return speed ;}
	public int getrange() {return range ;}
	public Image getImage() {return image ;}
	public void setPos(Point P) {pos = P ;}
	public void setType(int T) {type = T ;}
	public void setdamage(int D) {damage = D ;}
	public void setSpeed(double[] S) {speed = S ;}
	public void setrange(int R) {range = R ;}
	public void setImage(Image I) {image = I ;}
	
	public void DrawImage()
	{
		GamePanel.DP.drawImage(image, pos, Util.getAngle(speed), Scale.unit, Align.center) ;
	}
	public void move()
	{
		pos.x += speed[0] ;
		pos.y += speed[1] ;
	}
	public boolean isInRange(Point target) {return pos.distance(target) <= range ;}
	public int collidedwith(Player player, List<Creature> creature, Pet pet)
	{
		// Type 0 is friendly (shot by the player or the pet)
		// Type 1 is hostile (shot by the creature)
		if (isInRange(player.getPos()) & type == 1)
		{
			return -1 ;	// if a hostile projectile hits the player
		}
		if (isInRange(pet.getPos()) & type == 1)
		{
			return -2 ;	// if a hostile projectile hits the pet
		}
		for (int c = 0 ; c <= creature.size() - 1 ; c += 1)
		{
			if (isInRange(creature.get(c).getPos()) & type == 0)
			{
				return c ;	// if a friendly projectile hits the creature
			}
		}
		return -3 ;	// if the projectile has not hit anything
	}
	
	public void go(Player player, List<Creature> creature, Pet pet)
	{
		DrawImage() ;
		move() ;
		int hit = collidedwith(player, creature, pet) ;
		if (hit == -1 & type == 1)
		{
			player.getLife().decTotalValue(damage); ;
			if (!player.isAlive())
			{
				player.dies() ;
			}
		}
		else if (hit == -2 & type == 1)
		{
			pet.getLife().decTotalValue(damage); ;
			if (pet.getLife().getCurrentValue() < 0)
			{
				pet.dies() ;
			}
		}
		else if (-1 < hit & type == 0)
		{
			Creature creatureHit = creature.get(hit) ;
			creatureHit.getLife().decTotalValue(damage); ;
			if (creatureHit.getLife().getCurrentValue() < 0)
			{
				creatureHit.dies() ;
			}
		}
	}

	public static void updateAll(Player player, Pet pet)
	{
		if (all == null || all.isEmpty()) { return ;}

		List<Creature> creaturesInMap = new ArrayList<>() ;
		if (!player.getMap().isCity())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			creaturesInMap = fm.getCreatures() ;
		}
		for (Projectiles proj : all)
		{
			proj.go(player, creaturesInMap, pet) ;
			if (proj.collidedwith(player, creaturesInMap, pet) != -3) // if the projectile has hit some live being
			{
				all.remove(proj) ;
			}
		}
	}

}