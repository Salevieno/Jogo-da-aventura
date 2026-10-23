package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import attributes.AttributeIncrease;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import battle.AtkResults;
import battle.AtkTypes;
import components.HitboxRectangle;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import items.PetItem;
import main.Directions;
import main.Elements;
import main.Game;
import main.GameStates;
import main.GameTimer;
import main.Log;
import main.Palette;
import main.Path;
import maps.GameMap;
import simulations.EvolutionSimulation;
import spells.Spell;
import utilities.Util;
import windows.PetAttributesWindow;

public class Pet extends LiveBeing
{
	private final Color color ;
	private final int job ;
	private final AttributeIncrease attInc ;
	private int spellPoints ;
	private PetItem equip ;
	private int alchBuffId ;
    	
	public Pet(int job)
	{
		super(PetData.getInitialpersonalattperjob().get(job), PetData.getInitialbattleattperjob().get(job), initializeMovingAnimations(job), new PetAttributesWindow()) ;
		
		this.job = job ;
		this.name = PetData.getName().get(job) ;
		this.level = 1 ;
		this.proJob = 0 ;
		this.map = null ;
		this.pos = new Point2D.Double(0, 0) ;
		this.dir = Directions.up ;
		this.state = LiveBeingStates.idle ;
		this.size = new Dimension(movingAni.spriteIdle.getFrameSize().width, movingAni.spriteIdle.getFrameSize().height) ;	
		this.range = PlayerData.getRange().get(job) ;
		this.step = PlayerData.getStep().get(job) ;
		this.atkElem = Elements.neutral ;
		this.satiationCounter = new GameTimer(PlayerData.getSatiationcounterduration().get(job)) ;
		this.thirstCounter = new GameTimer(PlayerData.getThirstcounterduration().get(job)) ;
		this.mpCounter = new GameTimer(PlayerData.getMpcounterduration().get(job)) ;
		this.battleActionCounter = new GameTimer(PlayerData.getBattleactioncounterduration().get(job)) ;
		this.movingTimer = new GameTimer(20) ;
		this.combo = new ArrayList<>();
		this.hitbox = new HitboxRectangle(getPos(), size, 0.8) ;
		this.equip = null ;
		this.alchBuffId = -1 ;
		
		Color[] colorPalette = Palette.colors ;
		Color[] petColors = new Color[] {colorPalette[3], colorPalette[5], colorPalette[21], colorPalette[21]} ;
		color = petColors[job] ;
		this.spells = new ArrayList<Spell>() ;
		this.spellPoints = 0 ;

		startCounters() ;
		attInc = calcAttributeIncrease(job) ;		
	}

	public static MovingAnimations initializeMovingAnimations(int job)
	{
		String rootPath = Path.PET_IMG + "pet" + job;
		return new MovingAnimations(
			new SpriteAnimation(rootPath + "_idle.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(rootPath + "_movingup.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(rootPath + "_movingdown.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(rootPath + "_movingleft.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(rootPath + "_movingright.png", new Point(0, 0), Align.bottomCenter, 1, 5)
		);
	}

	public List<Spell> InitializePetSpells()
    {
		int[] spellIDs = switch (job)
		{
			case 0 -> new int[] {0, 5, 6, 12, 13} ;
			case 1 -> new int[] {35, 37, 45, 47, 49} ;
			case 2 -> new int[] {72, 74, 75, 83, 148} ;
			case 3 -> new int[] {106, 109, 110, 115, 116} ;
			default -> new int[] {} ;
		};
		List<Spell> spells = new ArrayList<>() ;
		for (int id : spellIDs)
		{
			spells.add(new Spell(Spell.getAll().get(id))) ;
		}
		spells.get(0).incLevel(1) ;
		return spells ;
    }
	
	private static AttributeIncrease calcAttributeIncrease(int job)
	{
		List<Double> attIncrements = Arrays.asList(PetData.getAttributeincreaseonlevelup().get(job)).subList(1, 9).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		List<Double> incChances = Arrays.asList(PetData.getAttributeincreaseonlevelup().get(job)).subList(9, 17).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		AttributeIncrease attInc = new AttributeIncrease(attIncrements, incChances) ;
		
		return attInc ;
	}
	
	public Color getColor() {return color ;}
	public int getJob() {return job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public List<Spell> getSpells() {return spells ;}
	public int getSpellPoints() {return spellPoints ;}
	public AttributeIncrease getAttInc() { return attInc ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public BasicBattleAttribute getCritAtk() {return BA.getCritAtk() ;}
	public BasicBattleAttribute getCritDef() {return BA.getCritDef() ;}
	public BattleSpecialAttribute getStun() {return BA.getStun() ;}
	public BattleSpecialAttribute getBlock() {return BA.getBlock() ;}
	public BattleSpecialAttributeWithDamage getBlood() {return BA.getBlood() ;}
	public BattleSpecialAttributeWithDamage getPoison() {return BA.getPoison() ;}
	public BattleSpecialAttribute getSilence() {return BA.getSilence() ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public PetItem getEquip() { return equip ;}
	public void setEquip(PetItem equip) { this.equip = equip ;}
	public int getAlchBuffId() { return alchBuffId ;}
	public void setAlchBuffId(int alchBuffId) { this.alchBuffId = alchBuffId ;}

//	public boolean isAlive() { return 0 < PA.getLife().getCurrentValue() ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public boolean closeToPlayer(Point2D.Double playerPos) { return pos.distance(playerPos) <= 40 ; }
	
	public Point center() { return new Point((int) pos.x, (int) pos.y - size.height / 2) ;}
	public Point headPos() { return new Point((int) pos.x, (int) pos.y - size.height) ;}

	public Directions randomNewDirection(Directions originalDir)
	{
		Directions newDir = randomDir() ;
		while (Directions.areOpposite(originalDir, newDir))
		{
			newDir = randomDir() ;
		}
		return newDir ;
	}
	
	private Point2D.Double findNextPos(Point2D.Double playerPos, Creature opponent, double dt)
	{
		if (opponent != null)
		{
			return chase(pos, opponent.getPosAsDouble(), step) ;
		}
		else if (closeToPlayer(playerPos))
		{
			if (Util.chance(0.8)) { return pos ;}
			
			if (Util.chance(0.2))
			{
				dir = randomDir() ;
			}
			return MovePattern.calcNewPos(MovePattern.pattern0, dir, pos, step, dt) ;
		}
		else
		{
			return chase(pos, playerPos, step) ;
		}
	}


	public void think(boolean isInBattle, Point2D.Double playerPos)
	{
		if (isInBattle)
		{
			setState(LiveBeingStates.fighting) ;
			return ;
		}
		
		if (!closeToPlayer(playerPos))
		{
			restartMoving() ;
			return ;
		}
		
		if (Util.chance(0.8))
		{
			setState(LiveBeingStates.idle) ;
			return ;
		}
		else
		{
			restartMoving() ;
			return ;
		}
	}

	private void doBattleAction()
	{
		if (!isAlive()) { return ;}
		if (!canAtk()) { return ;}
		if (!isInRange(Game.getPlayer().getOpponent().getPosAsDouble())) { return ;}
		if (!hasActed()) { return ;}

		AtkTypes atkType = atkTypeFromAction() ;
		
		if (atkType == null) { return ;}
		
		setCurrentAtkType(atkType) ;
		AtkResults atkResults = performAtk(atkType, Game.getPlayer().getOpponent()) ;
		trainOffensive(atkResults) ;
		battleActionCounter.restart() ;

		if (Game.getState().equals(GameStates.simulation))
		{
			EvolutionSimulation.updateBattleStats(this, Game.getPlayer().getOpponent(), atkResults) ;
		}
		Log.debug("=======================================================\n") ;
	}

	public void act(Player player, double dt)
	{
		if (isMoving())
		{
			startMoving() ;
			move(player.getPosAsDouble(), player.getMap(), player.getOpponent(), player.getSuperElem(), dt) ;
			return ;
		}
		
		if (isFighting())
		{
			if (!player.getOpponent().isInRange(pos))
			{
				move(player.getPosAsDouble(), player.getMap(), player.getOpponent(), player.getSuperElem(), dt) ;
				return ;
			}
			chooseFightMove() ;
			doBattleAction() ;
		}
		
	}


	public void move(Point2D.Double playerPos, GameMap playerMap, Creature opponent, Elements playerElem, double dt)
	{
		Point2D.Double nextPos = findNextPos(playerPos, opponent, dt) ;
		if (playerMap.groundIsWalkable(new Point((int) nextPos.x, (int) nextPos.y), playerElem))
		{
			setPos(nextPos) ;
		}
	}
	
	public void chooseFightMove()
	{
		int move = Util.randomInt(0, 1 + getUsableSpells().size()) ;
		switch (move)
		{
			case 0: currentAction = BATTLE_KEYS[0] ; return ;
			case 1: currentAction = BATTLE_KEYS[1] ; return ;
			default: currentAction = String.valueOf(move - 2) ; return ;
		}
	}
	
	public void revives()
	{
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
	}

	public void dies()
	{
		resetBattleActions() ;
        if (AtkTypes.defense.equals(currentAtkType))
		{
			deactivateDef() ;
		}
		setPos(Game.getPlayer().getPosAsDouble()) ;
	}

	public void useAutoSpell(boolean activate, Spell spell)
	{
		
	}
	
	public void win(Creature creature)
	{
        incPersonalAttribute(PA.getExp(), (int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier()), true) ;
	}
	
	public void levelUp()
	{
		double[] attIncrease = calcAttributesIncrease() ;
		setLevel(level + 1) ;
		spellPoints += 1 ;
		PA.getLife().incMaxValue((int) attIncrease[0]) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().incMaxValue((int) attIncrease[1]) ;	
		PA.getMp().setToMaximum() ;
		BA.getPhyAtk().incBaseValue(attIncrease[2]) ;
		BA.getMagAtk().incBaseValue(attIncrease[3]) ;
		BA.getPhyDef().incBaseValue(attIncrease[4]) ;
		BA.getMagDef().incBaseValue(attIncrease[5]) ;
		BA.getAgi().incBaseValue(attIncrease[6]) ;
		BA.getDex().incBaseValue(attIncrease[7]) ;
		PA.getExp().incMaxValue((int) attIncrease[8]) ;

		// Animation.start(AnimationTypes.levelUp, new Object[] {attIncrease, level});
	}
	
	public Elements[] atkElems() { return new Elements[] {atkElem, atkElem} ;}
	public Elements[] defElems() { return new Elements[] {Elements.neutral, Elements.neutral} ;}

	public double[] calcAttributesIncrease()
	{
		double[] increase = new double[attInc.getIncrement().basic().length + 1] ;

		for (int i = 0 ; i <= attInc.getIncrement().basic().length - 1 ; i += 1)
		{
			if (attInc.getChance().basic()[i] <= Math.random()) { continue ;}
			
			increase[i] = attInc.getIncrement().basic()[i] ;
		}
		
		increase[attInc.getIncrement().basic().length] = (double) (10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5) ;
		return increase ;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject content = new JSONObject() ;
		content.put("job", job) ;
		content.put("level", level) ;
		content.put("spellPoints", spellPoints) ;
		content.put("spells", spells.stream().map(Spell::toJson).toList()) ;
		content.put("equipID", equip == null ? null : equip.getId()) ;
		content.put("alchBuffId", alchBuffId) ;
		content.put("pos", List.of(pos.x, pos.y)) ;
		content.put("dir", dir.name()) ;
		content.put("PA", PA.toJsonObject()) ;
		content.put("BA", BA.toJsonObject()) ;
		return content ;
	}

	public static Pet fromJson(JSONObject jsonData)
	{
		int job = ((Number) jsonData.get("job")).intValue() ;
		Pet pet = new Pet(job) ;
		pet.setLevel(((Number) jsonData.get("level")).intValue()) ;
		pet.spellPoints = ((Number) jsonData.get("spellPoints")).intValue() ;
		pet.spells = new ArrayList<>() ;
		for (Object spellData : (JSONArray) jsonData.get("spells"))
		{
			pet.spells.add(Spell.fromJson((JSONObject) spellData)) ;
		}

		Object equipID = jsonData.get("equipID") ;
		pet.equip = equipID == null ? null : PetItem.getAll()[((Number) equipID).intValue()] ;
		Object alchBuffId = jsonData.get("alchBuffId") ;
		pet.alchBuffId = alchBuffId == null ? -1 : ((Number) alchBuffId).intValue() ;

		JSONArray posData = (JSONArray) jsonData.get("pos") ;
		pet.setPos(new Point2D.Double(
				((Number) posData.get(0)).doubleValue(),
				((Number) posData.get(1)).doubleValue())) ;
		pet.setDir(Directions.valueOf((String) jsonData.get("dir"))) ;
		pet.setPA(PersonalAttributes.fromJson((JSONObject) jsonData.get("PA"))) ;
		pet.setBA(BattleAttributes.fromJson((JSONObject) jsonData.get("BA"))) ;
		return pet ;
	}

	public void save(int slot)
	{
		Util.writeJson(toJson(), "saves/pet " + slot) ;
    }

	public static Pet load(int slot)
	{
		JSONObject jsonData = Util.readJsonObject("saves/pet " + slot + ".json") ;
		if (jsonData == null) { return null ;}
		return fromJson(jsonData) ;
    }
	
	public void display(Point pos, Scale scale)
	{
		movingAni.displayMoving(dir, pos, scale, Align.bottomCenter) ;

		if (isDrunk())
		{
			displayDrunk() ;
		}

		if (isFighting())
		{
			displayBattleActionCounter() ;
		}
		
		if (isDefending())
		{
			displayDefending() ;
		}
		displayStatus() ;
		if (Game.DEBUG_MODE)
		{
			displayState() ;
			hitbox.display() ;
		}
	}
	
	public void display()
	{
		display(getPos(), Scale.unit) ;
	}
	
}