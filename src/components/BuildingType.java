package components;

import java.awt.Image;
import java.util.Arrays;

public class BuildingType
{
	private BuildingNames name ;
	private Image image ;
	private Image insideImage ;
	private Image[] ornamentImages ;
	
	public BuildingType(BuildingNames name, Image image)
	{
		this.name = name ;
		this.image = image ;
		this.insideImage = null ;
		this.ornamentImages = null ;
	}
	

	public BuildingNames getName() {return name ;}
	public Image getImage() {return image ;}
	public Image getInsideImage() {return insideImage ;}
	public void setInsideImage(Image insideImage) { this.insideImage = insideImage ;}
	public Image[] getOrnamentImages() {return ornamentImages ;}
	public void setOrnamentImages(Image[] ornamentImages) { this.ornamentImages = ornamentImages ;}



	@Override
	public String toString() {
		return "BuildingType [name=" + name + ", image=" + image + ", insideImage=" + insideImage + ", ornamentImages="
				+ Arrays.toString(ornamentImages) + "]";
	}
}
