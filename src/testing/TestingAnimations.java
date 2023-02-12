package testing;

import java.awt.Dimension;
import java.awt.Point;

import graphics.Animations;
import liveBeings.Pterodactile;
import main.AtkResults;
import utilities.AttackEffects;

public class TestingAnimations
{
	public static void runTests(Animations ani)
	{    	
		Object[] Object1 = new Object[] { 200, new Point(200, 200), new Dimension(20, 50),
    			new AtkResults("Physical", AttackEffects.hit, 100), 1 } ;
    	ani.SetAniVars(1, Object1);
    	ani.StartAni(1) ;    	

		Object[] Object12 = new Object[] { 200, new String[] {"a", "b", "c"} } ;
    	ani.SetAniVars(12, Object12);
    	ani.StartAni(12) ;

		Object[] Object13 = new Object[] { 200, new double[] {0.2, 0.5, 0.7}, 1 } ;
    	ani.SetAniVars(13, Object13);
		ani.StartAni(13) ;

		Object[] Object14 = new Object[] { 200, new double[] {0.2, 0.5, 0.7} } ;
    	ani.SetAniVars(14, Object14);
		ani.StartAni(14) ;

		Pterodactile.speak(ani) ;
	}
}
