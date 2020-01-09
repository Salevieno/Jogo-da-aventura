package Graphics;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class DrawFunctions extends JFrame
{
	private int width, height;
	
	private final String UP_KEY_PRESSED = "UP pressed";
	private final String UP_KEY_RELEASED = "UP released";
	private final String LEFT_KEY_PRESSED = "LEFT pressed";
	private final String LEFT_KEY_RELEASED = "LEFT released";
	private final String DOWN_KEY_PRESSED = "DOWN pressed";
	private final String DOWN_KEY_RELEASED = "DOWN released";
	private final String RIGHT_KEY_PRESSED = "RIGHT pressed";
	private final String RIGHT_KEY_RELEASED = "RIGHT released";
	private final String Letter_KEY_PRESSED = "letter pressed";
	private final String Letter_KEY_RELEASED = "letter released";
	
	private final int KEY_TIMER_DELAY = 50; // If the game starts to lag, increase this
	private Timer upTimer, LeftTimer, DownTimer, RightTimer, letterTimer;
	private String PlayerChoice;
	
	public DrawFunctions(int width, int height)
	{
		this.width = width;
		this.height = height;		

		setTitle("Game");
		setLocation(500, 0);
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//setPreferredSize(new Dimension(400, 300));

		int condition = 2; // WHEN IN FOCUSED WINDOW
		InputMap inputMap = getRootPane().getInputMap(condition);
		ActionMap actionMap = getRootPane().getActionMap();

		KeyStroke upKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
		KeyStroke upKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true);
		KeyStroke leftKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
		KeyStroke leftKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true);
		KeyStroke downKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
		KeyStroke downKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true);
		KeyStroke rightKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
		KeyStroke rightKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true);
		KeyStroke AKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false);
		KeyStroke AKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true);
		KeyStroke BKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false);
		KeyStroke BKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, true);
		KeyStroke CKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false);
		KeyStroke CKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true);
		KeyStroke DKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false);
		KeyStroke DKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true);
		KeyStroke EKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false);
		KeyStroke EKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true);
		KeyStroke FKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false);
		KeyStroke FKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, true);
		KeyStroke GKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, false);
		KeyStroke GKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, true);
		KeyStroke HKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false);
		KeyStroke HKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true);
		KeyStroke IKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, false);
		KeyStroke IKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, true);
		KeyStroke JKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false);
		KeyStroke JKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true);
		KeyStroke KKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false);
		KeyStroke KKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, true);
		KeyStroke LKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false);
		KeyStroke LKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, true);
		KeyStroke MKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, false);
		KeyStroke MKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, true);
		KeyStroke NKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false);
		KeyStroke NKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true);
		KeyStroke OKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, false);
		KeyStroke OKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, true);
		KeyStroke PKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false);
		KeyStroke PKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, true);
		KeyStroke QKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false);
		KeyStroke QKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true);
		KeyStroke RKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false);
		KeyStroke RKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, true);
		KeyStroke SKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false);
		KeyStroke SKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true);
		KeyStroke TKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false);
		KeyStroke TKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, true);
		KeyStroke UKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, false);
		KeyStroke UKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true);
		KeyStroke VKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false);
		KeyStroke VKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, true);
		KeyStroke WKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false);
		KeyStroke WKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true);
		KeyStroke XKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false);
		KeyStroke XKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, true);
		KeyStroke YKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false);
		KeyStroke YKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, true);
		KeyStroke ZKeyPressed = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false);
		KeyStroke ZKeyReleased = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true);
		inputMap.put(upKeyPressed, UP_KEY_PRESSED);
		inputMap.put(upKeyReleased, UP_KEY_RELEASED);
		inputMap.put(leftKeyPressed, LEFT_KEY_PRESSED);
		inputMap.put(leftKeyReleased, LEFT_KEY_RELEASED);
		inputMap.put(downKeyPressed, DOWN_KEY_PRESSED);
		inputMap.put(downKeyReleased, DOWN_KEY_RELEASED);
		inputMap.put(rightKeyPressed, RIGHT_KEY_PRESSED);
		inputMap.put(rightKeyReleased, RIGHT_KEY_RELEASED);
		inputMap.put(AKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(AKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(BKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(BKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(CKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(CKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(DKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(DKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(EKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(EKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(FKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(FKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(GKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(GKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(HKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(HKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(IKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(IKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(JKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(JKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(KKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(KKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(LKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(LKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(MKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(MKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(NKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(NKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(OKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(OKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(PKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(PKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(QKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(QKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(RKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(RKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(SKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(SKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(TKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(TKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(UKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(UKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(VKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(VKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(WKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(WKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(XKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(XKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(YKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(YKeyReleased, Letter_KEY_RELEASED);
		inputMap.put(ZKeyPressed, Letter_KEY_PRESSED);
		inputMap.put(ZKeyReleased, Letter_KEY_RELEASED);
		actionMap.put(UP_KEY_PRESSED, new UpAction(false));
		actionMap.put(UP_KEY_RELEASED, new UpAction(true));
		actionMap.put(LEFT_KEY_PRESSED, new LeftAction(false));
		actionMap.put(LEFT_KEY_RELEASED, new LeftAction(true));
		actionMap.put(DOWN_KEY_PRESSED, new DownAction(false));
		actionMap.put(DOWN_KEY_RELEASED, new DownAction(true));
		actionMap.put(RIGHT_KEY_PRESSED, new RightAction(false));
		actionMap.put(RIGHT_KEY_RELEASED, new RightAction(true));
		actionMap.put(Letter_KEY_PRESSED, new LetterAction(false));
		actionMap.put(Letter_KEY_RELEASED, new LetterAction(true));
	}

	private class UpAction extends AbstractAction 
	{
		private boolean onKeyRelease;
		public UpAction(boolean onKeyRelease) 
		{
			this.onKeyRelease = onKeyRelease;
		}
		@Override
		public void actionPerformed(ActionEvent evt) 
		{
			if (!onKeyRelease) 
			{
				if (upTimer != null && upTimer.isRunning()) 
				{
					return;
				}
				// What to do when UP == pressed
				//System.out.println("UP pressed");

				upTimer = new Timer(KEY_TIMER_DELAY, new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						// WHat to do every delay
					}
				});
				upTimer.start();
			} 
			else 
			{
				//System.out.println("UP released");
				if (upTimer != null && upTimer.isRunning()) 
				{
					upTimer.stop();
					upTimer = null;
				}
			}
		}
	}

	private class LeftAction extends AbstractAction 
	{
		private boolean onKeyRelease;
		public LeftAction(boolean onKeyRelease) 
		{
			this.onKeyRelease = onKeyRelease;
		}
		@Override
		public void actionPerformed(ActionEvent evt) 
		{
			if (!onKeyRelease) 
			{
				if (LeftTimer != null && LeftTimer.isRunning()) 
				{
					return;
				}
				PlayerChoice = "n";

				LeftTimer = new Timer(KEY_TIMER_DELAY, new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						// WHat to do every delay
					}
				});
				LeftTimer.start();
			} 
			else 
			{
				if (LeftTimer != null && LeftTimer.isRunning()) 
				{
					LeftTimer.stop();
					LeftTimer = null;
				}
			}
		}
	}
	private class DownAction extends AbstractAction 
	{
		private boolean onKeyRelease;
		public DownAction(boolean onKeyRelease) 
		{
			this.onKeyRelease = onKeyRelease;
		}
		@Override
		public void actionPerformed(ActionEvent evt) 
		{
			if (!onKeyRelease) 
			{
				if (DownTimer != null && DownTimer.isRunning()) 
				{
					return;
				}
				//PlayerChoice = "n";

				DownTimer = new Timer(KEY_TIMER_DELAY, new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						// WHat to do every delay
					}
				});
				DownTimer.start();
			} 
			else 
			{
				if (DownTimer != null && DownTimer.isRunning()) 
				{
					DownTimer.stop();
					DownTimer = null;
				}
			}
		}
	}
	private class RightAction extends AbstractAction 
	{
		private boolean onKeyRelease;
		public RightAction(boolean onKeyRelease) 
		{
			this.onKeyRelease = onKeyRelease;
		}
		@Override
		public void actionPerformed(ActionEvent evt) 
		{
			if (!onKeyRelease) 
			{
				if (RightTimer != null && RightTimer.isRunning()) 
				{
					return;
				}
				//PlayerChoice = "n";

				RightTimer = new Timer(KEY_TIMER_DELAY, new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						// WHat to do every delay
					}
				});
				RightTimer.start();
			} 
			else 
			{
				if (RightTimer != null && RightTimer.isRunning()) 
				{
					RightTimer.stop();
					RightTimer = null;
				}
			}
		}
	}
	private class LetterAction extends AbstractAction 
	{
		private boolean onKeyRelease;
		public LetterAction(boolean onKeyRelease) 
		{
			this.onKeyRelease = onKeyRelease;
		}
		@Override
		public void actionPerformed(ActionEvent evt) 
		{
			if (!onKeyRelease) 
			{
				if (letterTimer != null && letterTimer.isRunning()) 
				{
					return;
				}
				PlayerChoice = evt.getActionCommand().toString().toUpperCase();
				System.out.println(PlayerChoice);
				
				letterTimer = new Timer(KEY_TIMER_DELAY, new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						PlayerChoice = evt.getActionCommand().toString().toUpperCase();
						System.out.println(PlayerChoice);
					}
				});
				letterTimer.start();
			} 
			else 
			{
				if (letterTimer != null && letterTimer.isRunning()) 
				{
					letterTimer.stop();
					letterTimer = null;
				}
			}
		}
	}
	public String ReturnPlayerChoice()
	{
		return PlayerChoice;
	}
	public void paint(Graphics g) 
	{ 
		
	}
	public void refresh(Graphics g)
	{
		
	}
	public void ClearScreen()
	{
		Graphics g = getGraphics();
		g.clearRect(0, 0, width, height);
	}
	public void DrawText(int[] Pos, Font TextFont, String Text, int size, Color color)
	{
		Graphics g = getGraphics();
		g.setColor(color);
		g.setFont(TextFont);
		g.drawString(Text, Pos[0], Pos[1]);
	}
	public void DrawLine(int[] Start, int[] End, Color color)
	{
		Graphics g = getGraphics();
		g.setColor(color);
		g.drawLine(Start[0], Start[1], End[0], End[1]);
	}
	public void DrawRect(int[] LeftBot, int l, int h, Color color)
	{
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillRect(LeftBot[0], height - LeftBot[1] - h, l, h);
	}
	public void DrawCircle(int[] Center, int size, Color color)
	{
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillOval(Center[0] - size/2, Center[1] - size/2, size, size);
	}
	public void DrawPolyLine(int[] x, int[] y, int n, Color color)
	{
		Graphics g = getGraphics();
		g.setColor(color);
		g.drawPolyline(x, y, n);
	}
	public void DrawTree(int[] Pos, int size, Color color)
	{
		int NumberOfTrunkCoords = 8, NumberOfLeavesCoords = 61, NumberOfNodeCoords = 7;
		int[][] TrunkCoords = new int[3][NumberOfTrunkCoords], LeavesCoords = new int[3][NumberOfLeavesCoords], NodeCoords = new int[3][NumberOfNodeCoords];
		int NumberOfCircles = 8;
		int[][] CirclesCoords = new int[3][NumberOfCircles];
		int[] CirclesRadius = new int[NumberOfCircles];
		String fileName = "Drawings.txt";
		try
		{	
			FileReader fileReader = new FileReader (fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader); 					
			String Line = bufferedReader.readLine();

			while (!Line.equals("* Tree 1 *"))
			{
				Line = bufferedReader.readLine();
			}
			for (int i = 0; i <= NumberOfTrunkCoords - 1; ++i)
			{
				Line = bufferedReader.readLine();
				TrunkCoords[0][i] = Pos[0] + Integer.parseInt(Line.split("\\s+")[0])*size;
				TrunkCoords[1][i] = Pos[1] - Integer.parseInt(Line.split("\\s+")[1])*size;
				TrunkCoords[2][i] = Pos[2] + Integer.parseInt(Line.split("\\s+")[2])*size;
			}
			for (int i = 0; i <= NumberOfLeavesCoords - 1; ++i)
			{
				Line = bufferedReader.readLine();
				LeavesCoords[0][i] = Pos[0] + Integer.parseInt(Line.split("\\s+")[0])*size;
				LeavesCoords[1][i] = Pos[1] - Integer.parseInt(Line.split("\\s+")[1])*size;
				LeavesCoords[2][i] = Pos[2] + Integer.parseInt(Line.split("\\s+")[2])*size;
			}
			for (int i = 0; i <= NumberOfNodeCoords - 1; ++i)
			{
				Line = bufferedReader.readLine();
				NodeCoords[0][i] = Pos[0] + Integer.parseInt(Line.split("\\s+")[0])*size;
				NodeCoords[1][i] = Pos[1] - Integer.parseInt(Line.split("\\s+")[1])*size;
				NodeCoords[2][i] = Pos[2] + Integer.parseInt(Line.split("\\s+")[2])*size;
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) 
		{
			System.out.println("Unable to find file '" + fileName + "'");                
		}		
		catch(IOException ex) 
		{
			System.out.println("Error reading file '" + fileName + "'");                  
		}

		Graphics g = getGraphics();
		g.setColor(Color.orange);
		//DrawRect(x, y, (int)(0.2*size), size, Color.orange);
		g.fillPolygon(TrunkCoords[0], TrunkCoords[1], NumberOfTrunkCoords);
		g.setColor(Color.black);
		g.drawPolyline(TrunkCoords[0], TrunkCoords[1], NumberOfTrunkCoords);
		/*g.setColor(Color.blue);
    	g.fillPolygon(LeavesCoords[0], LeavesCoords[1], NumberOfLeavesCoords);
    	g.setColor(Color.black);
    	g.drawPolyline(LeavesCoords[0], LeavesCoords[1], NumberOfLeavesCoords);*/
		CirclesCoords[0][0] = 26;
		CirclesCoords[1][0] = 65;
		CirclesRadius[0] = 30;
		CirclesCoords[0][1] = 49;
		CirclesCoords[1][1] = 59;
		CirclesRadius[1] = 35;
		CirclesCoords[0][2] = 68;
		CirclesCoords[1][2] = 59;
		CirclesRadius[2] = 35;
		CirclesCoords[0][3] = 88;
		CirclesCoords[1][3] = 69;
		CirclesRadius[3] = 27;
		CirclesCoords[0][4] = 87;
		CirclesCoords[1][4] = 82;
		CirclesRadius[4] = 30;
		CirclesCoords[0][5] = 64;
		CirclesCoords[1][5] = 92;
		CirclesRadius[5] = 40;
		CirclesCoords[0][6] = 35;
		CirclesCoords[1][6] = 85;
		CirclesRadius[6] = 44;
		CirclesCoords[0][7] = 12;
		CirclesCoords[1][7] = 82;
		CirclesRadius[7] = 29;
		for (int i = 0; i <= NumberOfCircles - 1; ++i)
		{
			DrawCircle(new int[] {Pos[0] + CirclesCoords[0][i]*size, Pos[1] - CirclesCoords[1][i]*size}, CirclesRadius[i]*size, color);
		}
		g.drawPolyline(NodeCoords[0], NodeCoords[1], NumberOfNodeCoords);
	}
	public void DrawCloud(int[] Pos, int size, Color color)
	{
		int NumberOfCircles = 13;
		int[][] CirclesCoords = new int[3][NumberOfCircles];
		int[] CirclesRadius = new int[NumberOfCircles];

		CirclesCoords[0] = new int[] {7, 17, 29, 44, 61, 80, 92, 93, 82, 68, 50, 26, 7};
		//CirclesCoords[1] = new int[] {34, 23, 18, 23, 20, 29, 22, 40, 61, 70, 77, 78, 56};
		CirclesCoords[1] = new int[] {34, 23, 18, 23, 20, 29, 22, 40, 41, 50, 57, 58, 36};
		//CirclesRadius = new int[] {13, 17, 14, 24, 16, 29, 10, 8, 13, 21, 16, 25, 13};
		CirclesRadius = new int[] {33, 37, 34, 44, 36, 49, 30, 28, 33, 41, 36, 45, 33};

		Graphics g = getGraphics();
		g.setColor(color);
		for (int i = 0; i <= NumberOfCircles - 1; ++i)
		{
			DrawCircle(new int[] {Pos[0] + CirclesCoords[0][i]*size, Pos[1] - CirclesCoords[1][i]*size}, CirclesRadius[i]*size, color);
		}
	}
	public void DrawThunder(int[] Pos, int size, String dir, Color color)
	{
		int NumberOfPoints = 15;
		int[][] Points = new int[3][NumberOfPoints];

		Points[0] = new int[] {0, 18, 27, 42, 55, 70, 77, 100, 90, 87, 72, 68, 50, 47, 39};
		Points[1] = new int[] {100, 73, 73, 49, 49, 29, 29, 0, 31, 31, 55, 55, 85, 85, 100};
		if (dir == "Left")
		{
			for (int i = 0; i <= NumberOfPoints - 1; ++i)
			{
				Points[0][i] = Arrays.stream(Points[0]).max().getAsInt() - Points[0][i];
			}
		}   	
		for (int i = 0; i <= NumberOfPoints - 1; ++i)
		{
			Points[0][i] = Pos[0] + Points[0][i]*size;
			Points[1][i] = Pos[1] - Points[1][i]*size;
			Points[2][i] = Pos[2] + Points[2][i]*size;
		}

		Graphics g = getGraphics();
		g.setColor(color);
		g.fillPolygon(Points[0], Points[1], NumberOfPoints);
	}   
	public void OpeningScreen()
	{
		Color SkyColor = Color.cyan;
		Color GrassColor = Color.green;
		Color SoilColor = new Color(100, 50, 0);		
		Color ThunderColor = Color.yellow;
		Color TreeColor = new Color(0, 100, 0);
		Color CloudColor = Color.white;
		
		DrawRect(new int[] {0, (int)(0.7*height)}, width, (int)(0.3*height), SkyColor);				// Sky
		DrawRect(new int[] {0, (int)(0.3*height)}, width, (int)(0.4*height), GrassColor);			// Grass
		DrawRect(new int[] {0, 0}, width, (int)(0.3*height), SoilColor);							// Soil
		DrawThunder(new int[] {(int)(0.02*width), (int)(0.6*height), 0},  2, "Right", ThunderColor);// Thunder
		DrawThunder(new int[] {(int)(0.7*width), (int)(0.6*height), 0},  2, "Left", ThunderColor);	// Thunder
		DrawTree(new int[] {(int)(0.2*width), (int)(0.6*height), 0}, 1, TreeColor);					// Tree
		DrawTree(new int[] {(int)(0.5*width), (int)(0.5*height), 0}, 1, TreeColor);					// Tree
		DrawTree(new int[] {(int)(0.8*width), (int)(0.6*height), 0}, 1, TreeColor);					// Tree
		DrawCloud(new int[] {(int)(0.1*width), (int)(0.25*height), 0}, 1, CloudColor);				// Cloud
		DrawCloud(new int[] {(int)(0.4*width), (int)(0.15*height), 0}, 1, CloudColor);				// Cloud
		DrawCloud(new int[] {(int)(0.7*width), (int)(0.25*height), 0}, 1, CloudColor);				// Cloud
	}
}
