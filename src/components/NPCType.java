package components;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCType
{
	private String name ;
	private NPCJobs job ;
	private String info ;
	private Color color ;
	private Image image ;
	private String[] speech ;
	private List<List<String>> options ;
	private List<List<Integer>> destination ;
	
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


	private void MapOptionsToDestinyMenu(NPCJobs job)
	{
		switch (job)
		{
			case doctor: return ;
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
				destination.add(Arrays.asList(1)) ; destination.add(Arrays.asList(2)) ; return ;
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

	@Override
	public String toString()
	{
		return "NPCType [name=" + name + ", job=" + job + ", info=" + info + ", color=" + color + ", image=" + image
				+ ", speech=" + speech + ", options=" + options + "]";
	}
	
}
