package NPC ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import components.Collider;
import components.Hitbox;
import components.HitboxRectangle;
import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import items.Item;
import items.Recipe;
import liveBeings.Pet;
import liveBeings.Player;
import liveBeings.PlayerActions;
import main.Game;
import main.GamePanel;
import main.ImageLoader;
import main.Interactable;
import main.Log;
import main.Palette;
import main.Path;
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
	protected int currentMenuID ;
	protected int selOption ;
	protected GameWindow window ;
	private Hitbox hitbox ;
	private boolean isInteracting ;
	
	protected final List<Collider> colliders ;
	protected static final Image speakingBubble = ImageLoader.loadImage(Path.NPC_IMG + "SpeechBubble.png") ;
	protected static final Image choicesWindow = ImageLoader.loadImage(Path.NPC_IMG + "ChoicesWindow.png") ;
	protected static final Font stdfont = new Font(Game.MainFontName, Font.BOLD, 12) ;
	protected static final Color stdColor = Palette.colors[0] ;
	protected static final Color selColor = Palette.colors[18] ;
	private static final List<NPC> allNPCs = new ArrayList<>() ;

	
	public NPC(NPCJobs job, String name, Point pos, List<NPCMenu> menus, Image desk)
	{
		this.id = allNPCs.size() ;
		this.job = job ;
		this.name = name ;
		this.pos = pos ;
		this.selOption = 0 ;
		this.currentMenuID = 0 ;
		this.menus = menus ;
		this.isInteracting = false ;
		this.desk = desk ;
		
		this.hitbox = new HitboxRectangle(Util.translate(pos, 0, -job.getImage().getHeight(null) / 2), Util.getSize(job.getImage()), 0.8) ;
		this.colliders = List.of(new Collider(pos)) ;
		allNPCs.add(this) ;
	}
	
	public NPC(NPCJobs job, String name, Point pos, List<NPCMenu> menus)
	{
		this(job, name, pos, menus, null) ;
	}

	public void setPos(Point pos)
	{
		Point center = UtilAlignment.getPosAt(pos, Align.bottomCenter, Align.center, Util.getSize(job.getImage())) ;
		this.hitbox.setCenter(center) ;
		this.pos = pos ;
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

	public void resetMenu() { currentMenuID = 0 ;}
	public boolean isInteracting() { return isInteracting ;}
	public void startInteraction() { isInteracting = true ;}
	public void endInteraction()
	{
		isInteracting = false ;
		if (window != null && window.isOpen())
		{
			Game.getPlayer().switchOpenClose(window) ;
		}
	}
		
	public static boolean actionIsForward(String action) { return action.equals("Enter") | action.equals("LeftClick") ;}
	public static List<NPC> getAll() { return allNPCs ;}
	
	public abstract void act(Player player, Pet pet, String action) ;

	public void navigate(String action)
	{
		if (action == null) { return ;}

		switchOption(action) ;

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ENTER)) && currentMenuID <= menus.size() - 2)
		{
			currentMenuID = job.getDestination().get(currentMenuID).get(selOption) ;
			selOption = 0 ;
			return ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ENTER)) && currentMenuID == menus.size() - 1)
		{
			endInteraction() ;
			currentMenuID = 0 ;
			selOption = 0 ;
			return ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)))
		{
			if (currentMenuID <= 0)
			{
				endInteraction() ;
			}
			else
			{
				currentMenuID = 0 ;
				selOption = 0 ;
			}
		}
	}
	
	public void switchOption(String action)
	{
		if (menus.size() <= currentMenuID) { Log.warn("Trying to access position " + currentMenuID + " of menus with size " + menus.size() + " for npc " + name) ; return ;}
		if (menus.get(currentMenuID).getOptions() == null) { Log.warn("Trying to access null options for menu " + currentMenuID + " and NPC " + name) ; return ;}
		if (menus.get(currentMenuID).getOptions().isEmpty()) { Log.warn("Trying to access empty options for menu " + currentMenuID + " and NPC " + name) ; return ;}
		if (menus.get(currentMenuID).getOptions().size() <= selOption) { Log.warn("Trying to access option out of bounds for menu " + currentMenuID + " and NPC " + name + "! Sel option id " + selOption + " and options size " + menus.get(currentMenuID).getOptions().size()) ; return ;}
		
		if (action.equals(PlayerActions.moveDown.getKey()) & selOption <= menus.get(currentMenuID).getOptions().size() - 2)
		{
			selOption += 1 ;
		}
		if (action.equals(PlayerActions.moveUp.getKey()) & 1 <= selOption)
		{
			selOption += -1 ;
		}
	}
	
	public void displaySpeech(Point pos)
	{
		if (this instanceof NPCCitizen)
		{
			((NPCCitizen) this).displaySpeech() ;
			return ;
		}

		if (menus == null) { Log.warn("Trying to display npc null speech") ; return ;}
		if (menus.size() <= currentMenuID) { Log.warn("Not enough npc speeches to display") ; return ;}
		if (menus.isEmpty()) { Log.warn("NPC speech is empty") ; return ;}
		if (job.getImage() == null) { Log.warn("NPC job image is null") ; return ;}

		String content = menus.get(currentMenuID).getSpeech() ;		
		Point speechPos = Util.translate(pos, 0, 10 - job.getImage().getHeight(null)) ;

		Draw.speech(speechPos, content, stdfont, speakingBubble, stdColor) ;		
	}	
	
	public void displaySpeech() { displaySpeech(pos) ;}

	public void displayOptions(Point windowPos)
	{
		if (menus == null) { return ;}
		if (menus.size() <= currentMenuID) { return ;}
		if (menus.get(currentMenuID).getOptions() == null) { return ;}
		if (menus.get(currentMenuID).getOptions().size() <= 0) { return ;}
		
		GamePanel.DP.drawImage(choicesWindow, windowPos, Align.topLeft) ;
		
		List<String> opt = menus.get(currentMenuID).getOptions() ;
		for (int i = 0 ; i <= opt.size() - 1 ; i += 1)
		{
			Point textPos = Util.translate(windowPos, 5, 5 + i * (stdfont.getSize() + 5)) ;
			String text = opt.get(i) ;
			Color textColor = i == selOption ? selColor : stdColor ;
			GamePanel.DP.drawText(textPos, Align.topLeft, Draw.stdAngle, text, stdfont, textColor) ;
		}		
	}
	
	public void displayOptions() { displayOptions(Util.translate(pos, 20, -10)) ;}

	public void display(Hitbox playerHitbox)
	{
		GamePanel.DP.drawImage(job.getImage(), pos, Draw.stdAngle, Scale.unit, Align.bottomCenter) ;
		if (desk != null)
		{
			GamePanel.DP.drawImage(desk, pos, Align.centerLeft);
		}
		GamePanel.DP.drawText(Util.translate(pos, 0, -job.getImage().getHeight(null) - 20), Align.bottomCenter, name, Palette.colors[3]);
		
		if (Game.debugMode)
		{
			hitbox.display() ;
		}
		
	}

	private static NPC create(NPCJobs job, String name, String info, List<NPCMenu> menus)
	{
		switch (job) {
			case citizen:
				return new NPCCitizen(name, new Point(0, 0), menus) ;		
		
			case crafter:
				return new NPCCrafter(name, new Point(0, 0), menus, Recipe.getAll().subList(58, Recipe.getAll().size())) ;
		
			case alchemist:
				return new NPCCrafter(name, new Point(0, 0), menus, Recipe.getAll().subList(0, 40)) ;

			case woodcrafter:
				return new NPCCrafter(name, new Point(0, 0), menus, Recipe.getAll().subList(40, 58)) ;

			case banker:
				return new NPCBanker(name, new Point(0, 0), menus) ;

			case doctor:
				return new NPCDoctor(name, new Point(0, 0), menus) ;

			case elemental:
				return new NPCElemental(name, new Point(0, 0), menus) ;

			case equipsSeller:
				int[] itemIDs = new int[] {300, 305, 307, 309, 315, 322, 326, 328, 332, 336, 340, 344} ;
				// int cityID = id / 17 ;
				// for (int i = 0 ; i <= itemIDs.length - 1; i += 1) { itemIDs[i] += 200 * cityID ;}
				return new NPCSeller(name, new Point(0, 0), menus, Item.getItems(itemIDs)) ;
		
			case itemsSeller:
				itemIDs = new int[] {1329, 0, 1, 4, 5, 121, 122, 125, 130, 1301, 1305, 1702, 1708, 1710, 1713} ;
				return new NPCSeller(name, new Point(0, 0), menus, Item.getItems(itemIDs)) ;

			case smuggleSeller:
				itemIDs = new int[] {1, 2} ;
				// itemIDs = newSmuggledStock() ;
				// if (renewStocks)
				// {
				// 	itemids = newSmuggledStock() ;
				// 	renewStocks = false ;
				// }
				// int cityID = id / 17 ;
				// for (int i = 0 ; i <= itemids.length - 1; i += 1)
				// {
				// 	itemids[i] += 200 * cityID ;
				// }
				return new NPCSeller(name, new Point(0, 0), menus, Item.getItems(itemIDs)) ;
			
			case master:
				return new NPCMaster(name, new Point(0, 0), menus) ;

			case forger:
				return new NPCForger(name, new Point(0, 0), menus) ;

			case questExp: case questItem:
				return new NPCQuest(name, new Point(0, 0), menus) ;

			case saver:
				return new NPCSaver(name, new Point(0, 0), menus) ;

			default:
				Log.warn("Criando NPC sem job definido") ;
				return null ;
		}
	}

	@SuppressWarnings("unchecked")
	public static void load(String language)
	{
		allNPCs.removeAll(allNPCs) ;
		List<JSONObject> npcList = (List<JSONObject>) Util.readJsonArray(Path.DADOS + language + "/NPCMenus.json");
		List<NPC> npcs = new ArrayList<>();

		for (JSONObject npcData : npcList)
		{
			String name = (String) npcData.get("nome");
			String info = (String) npcData.get("info");
			int jobId = ((Long) npcData.get("job")).intValue();
			NPCJobs job = NPCJobs.getByID(jobId);

			List<JSONObject> menusData = (List<JSONObject>) npcData.get("menus");
			List<NPCMenu> menus = new ArrayList<>() ;

			for (int i = 0 ; i <= menusData.size() - 1 ; i += 1)
			{
				JSONObject menu = menusData.get(i) ;
				String speech = (String) menu.get("fala") ;
				List<String> options = (List<String>) menu.get("opcoes") ;
				if (options == null)
				{
					options = new ArrayList<>() ;
				}
				menus.add(new NPCMenu(job.getDestination().get(i), speech, options)) ;
			}

			NPC newNPC = NPC.create(job, name, info, menus) ;
			npcs.add(newNPC);
		}
	}

	@SuppressWarnings("unchecked")
	public static void updateText(String language)
	{
		List<JSONObject> npcList = (List<JSONObject>) Util.readJsonArray(Path.DADOS + language + "/NPCMenus.json");
		for (int i = 0 ; i <= npcList.size() - 1 ; i += 1)
		{
			JSONObject npcData = npcList.get(i) ;
			NPC npc = allNPCs.get(i) ;
			npc.setName((String) npcData.get("nome")) ;
			List<JSONObject> menusData = (List<JSONObject>) npcData.get("menus");
			for (int m = 0 ; m <= menusData.size() - 1 ; m += 1)
			{
				JSONObject menu = menusData.get(m) ;
				npc.getMenus().get(m).setSpeech((String) menu.get("fala")) ;
				npc.getMenus().get(m).setOptions((List<String>) menu.get("opcoes")) ;
				if (npc.getMenus().get(m).getOptions() != null)
				{
					npc.getMenus().get(m).updateOptionDestination() ;
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "NPC [id=" + id + ", job=" + job + "]";
	}

	
}