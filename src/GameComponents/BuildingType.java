package GameComponents;

import java.awt.Image;

public class BuildingType
{
	private String name ;
	private Image outsideImage ;
	private Image insideImage ;
	private Image[] ornamentImages ;
	
	public BuildingType(String name, Image outsideImage, Image insideImage, Image[] ornamentImages)
	{
		this.name = name ;
		this.outsideImage = outsideImage ;
		this.insideImage = insideImage ;
		this.ornamentImages = ornamentImages ;
	}
	

	public String getName() {return name ;}
	public Image getOutsideImage() {return outsideImage ;}
	public Image getInsideImage() {return insideImage ;}
	public Image[] getOrnamentImages() {return ornamentImages ;}
}
