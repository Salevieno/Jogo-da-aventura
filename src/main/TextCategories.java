package main;

public enum TextCategories
{	
	allIsGood("tudo está bem"),
	attributes("atributos"),
	bagMenus("menus da mochila"),
	bestiary("bestiário"),
	classes("classes"),
	equipSellerSpeech("Vendedor de equipamentosFalas"),
	equipSellerOptions0("Vendedor de equipamentosOpcoes0"),
	itemsSellerSpeech("Vendedor de itensFalas"),
	itemsSellerOptions0("Vendedor de itensOpcoes0"),
	equipments("equipamentos"),
	forgerSpeech("ForjadorFalas"),
	forgerOptions0("ForjadorOpcoes0"),
	hints("menu de dicas"),
	newGame("novo jogo"),
	npcs("NPCs"),
	doctorSpeech("npcsDoutorFalas"),
	masterSpeech("npcsMestreFalas"),
	equipsSellerSpeech("npcsVendedor de equipamentosFalas"),
	doctorOptions0("npcsDoutorOpcoes0"),
	doctorOptions1("npcsDoutorOpcoes1"),
	masterOptions0("npcsMestreOpcoes0"),
	masterOptions1("npcsMestreOpcoes1"),
	equipsSellerOptions0("npcsVendedor de equipamentosOpcoes0"),
	equipsSellerOptions1("npcsVendedor de equipamentosOpcoes1"),
	settings("menu de opções"),
	signMessages("mensagem das placas"),
	specialAttributesProperties("propriedades dos atributos especiais"),
	spellsBar("barra de magias"),
	playerWindow("janela do jogador"),
	proclasses("proClasses"),
	pterodactile("pterodactile");
	
	public static TextCategories catFromBRName(String brName)
	{
		
		for (TextCategories cat : TextCategories.values())
		{
			if (cat.brName.equals(brName))
			{
				return cat ;
			}
		}

		return null ;
		
	}
	
	private String brName ;
	
	public String getBrName()
	{
		return brName;
	}

	private TextCategories(String brName)
	{
		this.brName = brName ;
	}
}
