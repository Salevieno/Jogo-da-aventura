package main;

public enum TextCategories
{	
	allIsGood("tudo está bem"),
	attributes("atributos"),
	bagMenus("menus da mochila"),
	bestiary("bestiário"),
	classes("classes"),
	equipments("equipamentos"),
	hints("menu de dicas"),
	newGame("novo jogo"),
	

	npcs("NPCs"),
	alchemistSpeech("npcsAlquimistaFalas"),
	alchemistOptions0("npcsAlquimistaOpcoes0"),
	bankerSpeech("npcsBanqueiroFalas"),
	bankerOptions0("npcsBanqueiroOpcoes0"),
	citzen19Speech("npcsCidadão 19Falas"),
	citzen19Options0("npcsCidadão 19Opcoes0"),
	citzen19Options1("npcsCidadão 19Opcoes1"),
	citzen19Options2("npcsCidadão 19Opcoes2"),
	crafter19Speech("npcsFabricanteFalas"),
	crafter19Options0("npcsFabricanteOpcoes0"),
	doctorSpeech("npcsDoutorFalas"),
	doctorOptions0("npcsDoutorOpcoes0"),
	doctorOptions1("npcsDoutorOpcoes1"),
	elementalSpeech("npcsElementalFalas"),
	elementalOptions0("npcsElementalOpcoes0"),
	equipsSellerSpeech("npcsVendedor de equipamentosFalas"),
	equipSellerSpeech("Vendedor de equipamentosFalas"),
	equipSellerOptions0("Vendedor de equipamentosOpcoes0"),
	equipsSellerOptions0("npcsVendedor de equipamentosOpcoes0"),
	equipsSellerOptions1("npcsVendedor de equipamentosOpcoes1"),
	forgerSpeech("ForjadorFalas"),
	forgerOptions0("ForjadorOpcoes0"),
	itemsSellerSpeech("Vendedor de itensFalas"),
	itemsSellerOptions0("Vendedor de itensOpcoes0"),
	masterSpeech("npcsMestreFalas"),
	masterOptions0("npcsMestreOpcoes0"),
	masterOptions1("npcsMestreOpcoes1"),
	questSpeech("QuestFalas"),
	questOptions0("QuestOpcoes0"),
	sailorToIslandSpeech("npcsNavegador 1Falas"),
	sailorToIslandOptions0("npcsNavegador 1Opcoes0"),
	sailorToForestSpeech("npcsNavegador 2Falas"),
	sailorToForestOptions0("npcsNavegador 2Opcoes0"),
	saverSpeech("npcsSalvadorFalas"),
	saverOptions0("npcsSalvadorOpcoes0"),
	smugglerSpeech("npcsContrabandistaFalas"),
	smugglerOptions0("npcsContrabandistaOpcoes0"),
	woodcrafterSpeech("npcsMadeireiroFalas"),
	woodcrafterOptions0("npcsMadeireiroOpcoes0"),
	
	
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
