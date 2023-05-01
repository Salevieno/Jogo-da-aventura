package testing;

import java.awt.Dimension;
import java.awt.Point;

import graphics.Animation;
import liveBeings.Pterodactile;
import main.AtkResults;
import main.AtkTypes;
import utilities.AttackEffects;

public class TestingAnimations
{
	public static void runTests(Animation[] ani)
	{    	

		Object[] object0 = new Object[] {new Point(200, 200), new Dimension(100, 100), new AtkResults(AtkTypes.magical, AttackEffects.crit, 200), 1} ;
    	ani[0].start(100, object0) ;
    	
		Object[] object1 = new Object[] {new Point(200, 300), new Dimension(100, 100), new AtkResults(AtkTypes.magical, AttackEffects.crit, 200), 1} ;
    	ani[1].start(100, object1) ;
    	
    	Object[] object2 = new Object[] {new Point(300, 200), new Dimension(100, 100), new AtkResults(AtkTypes.magical, AttackEffects.crit, 300), 1} ;
    	ani[2].start(100, object2) ;
    	   	

		Object[] object3 = new Object[] {new String[] {"a", "b", "c"}} ;
    	ani[3].start(200, object3) ;

		Object[] object4 = new Object[] {new double[] {2, 5, 2, 1, 3, 1}, 1} ;
		ani[4].start(200, object4) ;

		Pterodactile.speak() ;
		
	}
}
