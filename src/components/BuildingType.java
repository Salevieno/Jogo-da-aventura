package components;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;

public class BuildingType
{
	private String name ;
	private Image image ;
	private Image insideImage ;
	private Image[] ornamentImages ;
	private List<NPCs> npc ;	// NPCs in the building
	
	public BuildingType(String name, Image image, List<NPCs> npc)
	{
		this.name = name ;
		this.image = image ;
		this.insideImage = null ;
		this.ornamentImages = null ;
		this.npc = npc ;
	}
	

	public String getName() {return name ;}
	public Image getImage() {return image ;}
	public Image getInsideImage() {return insideImage ;}
	public void setInsideImage(Image insideImage) { this.insideImage = insideImage ;}
	public Image[] getOrnamentImages() {return ornamentImages ;}
	public void setOrnamentImages(Image[] ornamentImages) { this.ornamentImages = ornamentImages ;}
	public List<NPCs> getNPCs() {return npc ;}	

	public boolean hasNPCs() {return npc != null ;}

	@Override
	public String toString() {
		return "BuildingType [name=" + name + ", image=" + image + ", insideImage=" + insideImage + ", ornamentImages="
				+ Arrays.toString(ornamentImages) + ", npc=" + npc + "]";
	}
}
