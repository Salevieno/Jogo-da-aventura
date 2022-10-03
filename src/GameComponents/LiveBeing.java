package GameComponents;

public class LiveBeing
{
	protected PersonalAttributes PA ;	// Personal attributes
	protected BattleAttributes BA ;				// Battle attributes
	protected String apelido;
	protected int level;
	
	public LiveBeing(int level, PersonalAttributes PA, BattleAttributes BA)
	{
		this.level = level;
		this.PA = PA;
		this.BA = BA;
		apelido= createNickname();
	}
	
	public String createNickname()
	{
	     return "Cool guy" ;
	}

	//public PersonalAttributes getPersonalAtt() {return PA ;}
	
	public boolean canAtk(BattleAttributes BA)
	{
		if (BA.getBattleActions()[0][2] == 1 & !BA.isStun())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
}
