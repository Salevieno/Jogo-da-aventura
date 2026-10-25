package NPC ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import UI.GameTextButton;
import components.Collider;
import components.Hitbox;
import components.HitboxRectangle;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import liveBeings.Pet;
import liveBeings.Player;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Interactable;
import main.Log;
import main.Palette;
import main.Path;
import screen.Screen;
import utilities.Util;
import windows.GameWindow;

public abstract class NPC implements Interactable
{
	protected final int id ;
	protected String name ;
	protected NPCJobs job ;
	protected Point pos ;
	protected final Image desk ;
	protected final List<NPCMenu> menus ;
    protected final Map<NPCMenu, List<GameTextButton>> menuButtons ;
	protected int currentMenuID ;
	protected int selOption ;
	protected GameWindow window ; // TODO passar window para o  player. Como fazer para a criação de NPCs não depender do player?
	private Hitbox hitbox ;
	private boolean isInteracting ;
	
	protected final List<Collider> colliders ;
	protected static final Image SPEAKING_BUBBLE_IMAGE = ImageLoader.loadImage(Path.NPC_IMG + "SpeechBubble.png") ;
	protected static final Image CHOICES_WINDOW_IMAGE = ImageLoader.loadImage(Path.NPC_IMG + "ChoicesWindow.png") ;
	protected static final Font STD_FONT = new Font(Game.getMainFontName(), Font.BOLD, 12) ;
	protected static final Color STD_COLOR = Palette.colors[0] ;
	protected static final Color STD_SELECTED_COLOR = Palette.colors[18] ;
	protected static final Map<NPCJobs, List<NPCMenu>> JOB_MENUS = new HashMap<>() ;
	private static final List<NPC> ALL = new ArrayList<>() ;
	
	public NPC(NPCJobs job, String name, Point pos, List<NPCMenu> menus, Image desk, GameWindow window)
	{
		this.id = ALL.size() ;
		this.job = job ;
		this.name = name ;
		this.pos = pos ;
		this.selOption = 0 ;
		this.currentMenuID = 0 ;
		this.menus = menus ;
		this.isInteracting = false ;
		this.desk = desk ;
		this.menuButtons = createMenuButtons(this, menus, job) ;
		this.window = window ;
		
		this.hitbox = new HitboxRectangle(Util.translate(pos, 0, -job.getImage().getHeight(null) / 2), Util.getSize(job.getImage()), 0.8) ;
		this.colliders = List.of(new Collider(pos)) ;
		ALL.add(this) ;
	}
	
	public NPC(NPCJobs job, String name, Point pos, List<NPCMenu> menus, GameWindow window)
	{
		this(job, name, pos, menus, null, window) ;
	}
	
	public NPC(NPCJobs job, String name, Point pos, List<NPCMenu> menus)
	{
		this(job, name, pos, menus, null, null) ;
	}

	public void setPos(Point pos)
	{
		Point center = UtilAlignment.getPosAt(pos, Align.bottomCenter, Align.center, Util.getSize(job.getImage())) ;
		this.hitbox.setCenter(center) ;
		this.pos = pos ;
		for (NPCMenu menu : menus)
		{
			for (int i = 0 ; i <= menuButtons.get(menu).size() - 1 ; i += 1)
			{
				GameTextButton button = menuButtons.get(menu).get(i) ;
				button.setTopLeftPos(Util.translate(pos, 20, (button.getSize().height + 6) * i)) ;
			}
		}
	}
	public String getName() { return name ;}
	public NPCJobs getJob() { return job ;}
	public Point getPos() { return pos ;}
	public Image getImage() { return job.getImage() ;}
	public GameWindow getWindow() { return window ;}
	public Hitbox getHitbox() { return hitbox ;}
	public List<NPCMenu> getMenus() { return menus ;}
	public List<Collider> getColliders() { return colliders ;}
	public void setName(String name) { this.name = name ;}
	
	public boolean isInteracting() { return isInteracting ;}

	private void goToMenu(int menuID)
	{
		if (menuID == -1)
		{
			endInteraction() ;
			return ;
		}

		menuButtons.get(menus.get(currentMenuID)).forEach(GameTextButton::deactivate) ;
		currentMenuID = menuID ;
		menuButtons.get(menus.get(currentMenuID)).forEach(GameTextButton::activate) ;
	}
	private void resetMenu() { goToMenu(0) ;}
	public void startInteraction()
	{
		isInteracting = true ;
		resetMenu() ;
	}

	public void endInteraction()
	{
		menuButtons.get(menus.get(currentMenuID)).forEach(GameTextButton::deactivate) ;
		isInteracting = false ;
		if (window != null && window.isOpen())
		{
			Game.getPlayer().switchOpenClose(window) ;
		}

		onEndInteraction() ;
	}
		
	public static boolean actionIsForward(String action) { return action.equals("Enter") | action.equals("LeftClick") ;}
    public static Map<NPCJobs, List<NPCMenu>> getJobMenus() { return JOB_MENUS ;}
	public static List<NPC> getAll() { return ALL ;}
	
	public abstract void act(Player player, Pet pet, String action) ;
	protected abstract void onEndInteraction() ;

	public void navigate(String action)
	{
		if (action == null) { return ;}

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)))
		{
			if (currentMenuID <= 0)
			{
				endInteraction() ;
			}
			else
			{
				resetMenu() ;
				selOption = 0 ;
			}
		}
	}

    private void updateSelectedOption(int index) { this.selOption = index ;}

	public void displaySpeech(Point pos)
	{
		if (menus == null) { Log.warn("Trying to display npc null speech") ; return ;}
		if (menus.size() <= currentMenuID) { Log.warn("Not enough npc speeches to display") ; return ;}
		if (menus.isEmpty()) { Log.warn("NPC speech is empty") ; return ;}
		if (job.getImage() == null) { Log.warn("NPC job image is null") ; return ;}

		String content = menus.get(currentMenuID).getSpeech() ;		
		Point speechPos = Util.translate(pos, 0, 10 - job.getImage().getHeight(null)) ;

		int bubbleL = SPEAKING_BUBBLE_IMAGE.getWidth(null) ;
		int bubbleH = SPEAKING_BUBBLE_IMAGE.getHeight(null) ;
		boolean flipH = Screen.getMe().mapSize().width / 2 <= speechPos.x ;
		Color textColor = STD_COLOR != null ? STD_COLOR : Palette.colors[21] ;
		
		GamePanel.getDP().drawImage(SPEAKING_BUBBLE_IMAGE, speechPos, DrawPrimitives.stdAngle, Scale.unit, flipH, false, Align.bottomCenter, 1) ;
		
		Point padding = new Point(6, 5) ;
		Point textPos = Util.translate(speechPos, padding.x - bubbleL / 2, padding.y - bubbleH) ;
		int maxTextL = SPEAKING_BUBBLE_IMAGE.getWidth(null) - padding.x ;
		int sy = STD_FONT.getSize() + 1 ;
		Draw.fitText(textPos, sy, Align.topLeft, content, STD_FONT, maxTextL, textColor) ;		
	}
	
	public void displaySpeech() { displaySpeech(pos) ;}

	public void displayOptions()
	{
		if (menus == null) { return ;}
		if (menus.size() <= currentMenuID) { return ;}
		if (menus.get(currentMenuID).getOptions() == null) { return ;}
		if (menus.get(currentMenuID).getOptions().size() <= 0) { return ;}

		menuButtons.get(menus.get(currentMenuID)).stream().filter(GameTextButton::isActive).forEach(GameTextButton::display) ;		
	}

	public void display(Hitbox playerHitbox)
	{
		GamePanel.getDP().drawImage(job.getImage(), pos, Scale.unit, Align.bottomCenter) ;
		if (desk != null)
		{
			GamePanel.getDP().drawImage(desk, pos, Align.centerLeft);
		}
		GamePanel.getDP().drawText(Util.translate(pos, 0, -job.getImage().getHeight(null) - 6), Align.bottomCenter, name, Palette.colors[3]);
		
		if (Game.DEBUG_MODE)
		{
			hitbox.display() ;
		}
		
	}

	@SuppressWarnings("unchecked")
	public static void loadJobMenus(String language)
	{
		List<JSONObject> jobMenus = (List<JSONObject>) Util.readJsonArray(Path.DADOS + language + "/NPCJobMenus.json") ;
        for (JSONObject jobMenu : jobMenus)
        {
			int jobId = ((Long) jobMenu.get("job")).intValue();
			List<JSONObject> menusData = (List<JSONObject>) jobMenu.get("menus");
            List<NPCMenu> menus = new ArrayList<>() ;
            for (JSONObject menuData : menusData)
            {
                String speech = (String) menuData.get("fala");
                List<String> options = (List<String>) menuData.getOrDefault("opcoes", new ArrayList<>());
                NPCMenu menu = new NPCMenu(speech, options);
                menus.add(menu);
            }
            JOB_MENUS.put(NPCJobs.getByID(jobId), menus) ;
        }
    }

    private static GameTextButton createMenuButton(NPC npc, NPCMenu menu, int optionIndex, int destinationMenu)
    {
        String text = menu.getOptions().get(optionIndex) ;
        GameTextButton button = new GameTextButton(new Point(0, 0), Align.topLeft, text, text, () -> {
            npc.updateSelectedOption(optionIndex) ;
            npc.act(Game.getPlayer(), Game.getPet(), Game.getPlayer().getCurrentAction());
            npc.goToMenu(destinationMenu) ;
        }) ;
        Dimension textSize = GamePanel.calcTextSize(text, new Font(Game.getMainFontName(), Font.BOLD, 17)) ;
        button.resize(textSize);
        button.deactivate() ;
        return button ;
    }

    private static Map<NPCMenu, List<GameTextButton>> createMenuButtons(NPC npc, List<NPCMenu> menus, NPCJobs job)
    {
        Map<NPCMenu, List<GameTextButton>> menuButtons = new HashMap<>() ;
        for (int i = 0 ; i <= menus.size() - 1 ; i += 1)
        {
            List<Integer> destinationMenus = job.getDestination().get(i) ;
            List<GameTextButton> buttons = new ArrayList<>(menus.get(i).getOptions().size()) ;
            for (int j = 0 ; j <= menus.get(i).getOptions().size() - 1 ; j += 1)
            {
                final int optionIndex = j;
                int destinationMenu = destinationMenus.get(j) ;
                GameTextButton button = createMenuButton(npc, menus.get(i), optionIndex, destinationMenu);
                buttons.add(button) ;
            }
            menuButtons.put(menus.get(i), buttons) ;
        }

        return menuButtons ;
    } 

	@SuppressWarnings("unchecked")
	public static void updateText(String language)
	{
		List<JSONObject> npcList = (List<JSONObject>) Util.readJsonArray(Path.DADOS + language + "/NPCMenus.json");
		for (int i = 0 ; i <= npcList.size() - 1 ; i += 1)
		{
			JSONObject npcData = npcList.get(i) ;
			NPC npc = ALL.get(i) ;
			npc.setName((String) npcData.get("nome")) ;
			List<JSONObject> menusData = (List<JSONObject>) npcData.get("menus");
			for (int m = 0 ; m <= menusData.size() - 1 ; m += 1)
			{
				JSONObject menu = menusData.get(m) ;
				npc.getMenus().get(m).setSpeech((String) menu.get("fala")) ;
				npc.getMenus().get(m).setOptionsText((List<String>) menu.get("opcoes")) ;
			}
		}
	}

	@Override
	public String toString()
	{
		return "NPC [id=" + id + ", job=" + job + "]";
	}
}