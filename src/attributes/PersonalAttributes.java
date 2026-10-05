package attributes ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import org.json.simple.JSONObject;

import graphics.Align;
import main.GamePanel;
import main.Palette;
import utilities.Util;

public class PersonalAttributes
{
	private BasicAttribute life ;
	private BasicAttribute mp ;
	private BasicAttribute exp ;
	private BasicAttribute satiation ;
	private BasicAttribute thirst ;
	
	public PersonalAttributes(BasicAttribute Life, BasicAttribute Mp, BasicAttribute Exp, BasicAttribute Satiation, BasicAttribute Thirst)
	{
		this.life = Life ;
		this.mp = Mp ;
		this.exp = Exp ;
		this.satiation = Satiation ;
		this.thirst = Thirst ;
	}

	public PersonalAttributes(int lifeCurrent, int lifeMax, double lifeMult,
		int mpCurrent, int mpMax, double mpMult,
		int expCurrent, int expMax, double expMult,
		int satiationCurrent, int satiationMax, double satiationMult,
		int thirstCurrent, int thirstMax, double thirstMult)
	{
		this.life = new BasicAttribute(lifeCurrent, lifeMax, lifeMult) ;
		this.mp = new BasicAttribute(mpCurrent, mpMax, mpMult) ;
		this.exp = new BasicAttribute(expCurrent, expMax, expMult) ;
		this.satiation = new BasicAttribute(satiationCurrent, satiationMax, satiationMult) ;
		this.thirst = new BasicAttribute(thirstCurrent, thirstMax, thirstMult) ;
	}
	
	public PersonalAttributes(PersonalAttributes PA)
	{
		this.life = new BasicAttribute(PA.getLife()) ;
		this.mp = new BasicAttribute(PA.getMp()) ;
		this.exp = new BasicAttribute(PA.getExp()) ;
		this.satiation = new BasicAttribute(PA.getSatiation()) ;
		this.thirst = new BasicAttribute(PA.getThirst()) ;
	}
	public BasicAttribute getLife() {return life ;}
	public BasicAttribute getMp() {return mp ;}
	public BasicAttribute getExp() {return exp ;}
	public BasicAttribute getSatiation() {return satiation ;}
	public BasicAttribute getThirst() {return thirst ;}

	public BasicAttribute mapAttributes(Attributes att)
	{
		switch (att)
		{
			case life: return life ;
			case mp: return mp ;
			case exp: return exp ;
			case satiation: return satiation ;
			case thirst: return thirst ;
			
			default: return null ;
		}
	}

    public List<BasicAttribute> getAttributes() { return List.of(life, mp, exp, satiation, thirst) ;}
    public List<Color> getColors() { return List.of(Palette.colors[7], Palette.colors[20], Palette.colors[5], Palette.colors[15], Palette.colors[21]) ;}
	
	public static int numberFightsToLevelUp(int currentExp, int totalExp, int opponentExp, double expMult)
	{
		return 1 + (int) ((totalExp - currentExp) / (opponentExp * expMult)) ;
	}

    public void display(Point topLeft, Dimension barSize)
    {
        for (int i = 0; i <= this.getAttributes().size() - 1; i += 1)
        {
            BasicAttribute att = this.getAttributes().get(i) ;
            Point barPos = Util.translate(topLeft, 0, 26 * i) ;
            Point textPos = Util.translate(topLeft, barSize.width / 2, 26 * i) ;
            Dimension rateSize = new Dimension(barSize.width, (int) (att.getRate() *  barSize.height)) ;

            GamePanel.getDP().drawRect(barPos, Align.centerLeft, rateSize, 1, this.getColors().get(i), null, 1.0) ;
            GamePanel.getDP().drawRect(barPos, Align.centerLeft, barSize, 1, null, Palette.colors[0], 1.0) ;
            GamePanel.getDP().drawText(textPos, Align.center, att.getCurrentValue() + " / " + att.getMaxValue(), Palette.colors[0]);
        }
    }
	
	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject()
	{

        JSONObject content = new JSONObject();
        content.put("life", life.toJson());
        content.put("mp", mp.toJson());
        content.put("exp", exp.toJson());
        content.put("satiation", satiation.toJson());
        content.put("thirst", thirst.toJson());
        
        return content ;
        
	}
	
	public static PersonalAttributes fromJson(JSONObject jsonData)
	{

		BasicAttribute life = BasicAttribute.fromJson((JSONObject) jsonData.get("life")) ;
		BasicAttribute mp = BasicAttribute.fromJson((JSONObject) jsonData.get("mp")) ;
		BasicAttribute exp = BasicAttribute.fromJson((JSONObject) jsonData.get("exp")) ;
		BasicAttribute satiation = BasicAttribute.fromJson((JSONObject) jsonData.get("satiation")) ;
		BasicAttribute thirst = BasicAttribute.fromJson((JSONObject) jsonData.get("thirst")) ;
		
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	@Override
	public String toString()
	{
		return String.format("Personal Attributes:\n  Life: %s\n  Mp: %s\n  Exp: %s\n  Satiation: %s\n  Thirst: %s\n",
				life.toString(), mp.toString(), exp.toString(), satiation.toString(), thirst.toString()) ;
	}

}