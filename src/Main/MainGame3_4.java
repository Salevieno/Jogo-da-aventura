package Main ;

import java.awt.EventQueue ;

import javax.swing.JFrame ;

public class MainGame3_4 extends JFrame
{		
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L ;

	public MainGame3_4() 
    {
        initUI() ;
    }
    
    private void initUI() 
    {
    	int[] WinDim = new int[] {600 + 40, 480 + 39} ;	// frame dimensions (0, 39) and give an extra space on the right (40, 0)
        setTitle("Star") ;          
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setSize(WinDim[0], WinDim[1]) ;
        setLocation(500, 200) ;
        add(new Game(WinDim)) ;
    }
    
	public static void main(String[] args) 
	{
		//Game g = new Game() ;
		//g.WhereEverythingHappens() ;
		
		//GameTest g = new GameTest() ;
		//g.WhereEverythingHappens() ;
		EventQueue.invokeLater(() -> {MainGame3_4 ex = new MainGame3_4() ; ex.setVisible(true) ;}) ;
	}
}
