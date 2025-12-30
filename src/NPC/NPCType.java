package NPC;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Game;
import main.Languages;
import main.Path;
import main.TextCategories;
import utilities.Util;


public class NPCType
{
	private final String name ;
	private final NPCJobs job ;
	private final String info ;
	private final Color color ;
	private final Image image ;
	private final String[] speech ;
	private final List<List<String>> options ;
	private final List<List<Integer>> destination ;
	
	private static final String path = Path.DADOS + "npcs\\" ;
	
	public NPCType(String name, NPCJobs job, String info, Color color, Image image, String[] speech, List<List<String>> options)
	{
		this.name = name ;
		this.job = job ;
		this.info = info ;
		this.color = color ;
		this.image = image ;
		this.speech = speech ;
		this.options = options ;
		destination = new ArrayList<>() ;
		MapOptionsToDestinyMenu(job) ;
	}
	

	public String getName() {return name ;}
	public NPCJobs getJob() {return job ;}
	public String getInfo() {return info ;}
	public Color getColor() {return color ;}
	public Image getImage() {return image ;}
	public String[] getSpeech() {return speech ;}
	public List<List<String>> getOptions() {return options ;}
	public List<List<Integer>> getDestination() { return destination ;}
	
	public int height() { return image.getHeight(null) ;}


	private void MapOptionsToDestinyMenu(NPCJobs job)
	{
		switch (job)
		{
			case alchemist: case woodcrafter: case crafter:
			{
				 destination.add(Arrays.asList(1, 4)) ;
				 destination.add(Arrays.asList()) ;
				 destination.add(Arrays.asList()) ;
				 destination.add(Arrays.asList()) ;
				 destination.add(Arrays.asList(4)) ;
				 return ;
			}
			case banker:
			{
				destination.add(Arrays.asList(1, 2, 3, 3, 8)) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList(8)) ;
				destination.add(Arrays.asList(8)) ;
				destination.add(Arrays.asList(8)) ;
				return ;
			}
			case doctor:
				destination.add(Arrays.asList(3, 4)) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList(4)) ;
				return ;
				
			case elemental:
				destination.add(Arrays.asList(0, 1)) ;
				destination.add(Arrays.asList(1)) ;
				return ;
				
			case forger:
				destination.add(Arrays.asList(0, 1)) ;
				destination.add(Arrays.asList(1)) ;
				return ;
				
			case master: 
			{
				destination.add(Arrays.asList(1, 6)) ;
				destination.add(Arrays.asList()) ;
				destination.add(Arrays.asList(3, 5)) ;
				destination.add(Arrays.asList(4, 4)) ;
				destination.add(Arrays.asList(6)) ;
				destination.add(Arrays.asList(1, 6)) ;
				destination.add(Arrays.asList(6)) ;
				return ;
			}
			
			case questExp: case questItem:
				destination.add(Arrays.asList(1, 4)) ;
				destination.add(Arrays.asList(2)) ;
				destination.add(Arrays.asList(2)) ;
				destination.add(Arrays.asList(4)) ;
				destination.add(Arrays.asList(4)) ;
				return ;
				
			case equipsSeller: case itemsSeller: case smuggleSeller:
			{
				 destination.add(Arrays.asList(1, 2)) ;
				 destination.add(Arrays.asList()) ;
				 destination.add(Arrays.asList(2)) ;
				 return ;
			}
			case citizen0: case citizen1: case citizen2: case citizen3: case citizen4:
			case citizen5: case citizen6: case citizen7: case citizen8: case citizen9:
			case citizen10: case citizen11: case citizen12: case citizen13: case citizen14:
			case citizen15: case citizen16: case citizen17: case citizen18: case citizen19:
			{
				destination.add(Arrays.asList(1)) ; destination.add(Arrays.asList(2)) ; destination.add(Arrays.asList(2)) ; return ;
			}
			case saver: 
			{
				destination.add(Arrays.asList(1, 3)) ;
				destination.add(Arrays.asList(2, 2, 2)) ;
				destination.add(Arrays.asList(3)) ;
				destination.add(Arrays.asList(3)) ;
				return ;
			}
			default: return ;
		}
	}

	public static NPCType[] load(Languages language)
	{
		List<String[]> input = Util.readcsvFile(path + "NPCTypes.csv") ;
		List<String[]> text = Util.readcsvFile(path + "NPCTypes-" + language.toString() + ".csv") ;
		
		if (input.isEmpty()) { System.out.println("Erro ao carregar NPC types") ; return null;}
		
		NPCType[] npcType = new NPCType[input.size()] ;
		for (int i = 0 ; i <= npcType.length - 1 ; i += 1)
		{
			String name = text.get(i)[1] ;
			NPCJobs job = NPCJobs.valueOf(input.get(i)[0]) ;
			String info = text.get(i)[2] ;
			Color color = job.getColor() ;
			Image image = Game.loadImage(Path.NPC_IMG + "NPC_" + job.toString() + ".png") ;
			String[] speech = null ;
			List<List<String>> options = new ArrayList<>() ;
			TextCategories speechName = TextCategories.catFromBRName("npcs" + name + "Falas") ;

			if (Game.allText.get(speechName) != null)
			{
				speech = Game.allText.get(speechName) ;

				for (int o = 0 ; o <= speech.length - 1 ; o += 1)
				{
					TextCategories optionName = TextCategories.catFromBRName("npcs" + name + "Opcoes" + o) ;

					if (Game.allText.get(optionName) != null)
					{
						List<String> option = Arrays.asList(Game.allText.get(optionName)) ;
						options.add(option) ;
					}
				}
			}

			npcType[i] = new NPCType(name, job, info, color, image, speech, options) ;
		}

		return npcType ;
	}
	
	@Override
	public String toString()
	{
		return "\nNPCType" +
				"\n name: " + name +
				"\n job: " + job +
				"\n info: " + info +
				"\n" ;
	}
	
}
