package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import graphics2.Draw;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import utilities.Util;

public class GameTextButton extends GameButton
{
    private final Dimension minSize ;
    private Point pos1 ;
    private Point pos2 ;
    private Point pos3 ;
    private Point pos4 ;
    private Point pos5 ;
    private Point pos6 ;
    private Point pos7 ;
    private Point pos8 ;
    private Point pos9 ;
    private Point center ;
    private String text ;

    private Image boxStretchedPart2 ;
    private Image boxStretchedPart4 ;
    private Image boxStretchedPart6 ;
    private Image boxStretchedPart8 ;
    private Image boxStretchedPart9 ;
    private Image boxSelectedStretchedPart2 ;
    private Image boxSelectedStretchedPart4 ;
    private Image boxSelectedStretchedPart6 ;
    private Image boxSelectedStretchedPart8 ;
    private Image boxSelectedStretchedPart9 ;

    private static final Image boxPart1 = ImageLoader.loadImage(Path.UI_IMG + "TextBox1.png") ;
    private static final Image boxPart2 = ImageLoader.loadImage(Path.UI_IMG + "TextBox3.png") ;
    private static final Image boxPart3 = flipVertically(boxPart1) ;
    private static final Image boxPart4 = ImageLoader.loadImage(Path.UI_IMG + "TextBox2.png") ;
    private static final Image boxPart5 = flipVertically(flipHorizontally(boxPart1)) ;
    private static final Image boxPart6 = flipHorizontally(boxPart2) ;
    private static final Image boxPart7 = flipHorizontally(boxPart1) ;
    private static final Image boxPart8 = flipVertically(boxPart4) ;
    private static final Image boxPart9 = ImageLoader.loadImage(Path.UI_IMG + "TextBox4.png") ;
    private static final Image boxSelectedPart1 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected1.png") ;
    private static final Image boxSelectedPart2 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected3.png") ;
    private static final Image boxSelectedPart3 = flipVertically(boxSelectedPart1) ;
    private static final Image boxSelectedPart4 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected2.png") ;
    private static final Image boxSelectedPart5 = flipVertically(flipHorizontally(boxSelectedPart1)) ;
    private static final Image boxSelectedPart6 = flipHorizontally(boxSelectedPart2) ;
    private static final Image boxSelectedPart7 = flipHorizontally(boxSelectedPart1) ;
    private static final Image boxSelectedPart8 = flipVertically(boxSelectedPart4) ;
    private static final Image boxSelectedPart9 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected4.png") ;
    private static final int edgeSize = boxPart1.getWidth(null) ;
    private static final int padding = 2 * edgeSize + 2 ;

    public GameTextButton(Point pos, Align alignment, String name, Dimension size, String text, Image image, Image selectedImage, ButtonFunction action, Clip soundEffectOnHover)
    {
        super(pos, alignment, name, image, selectedImage, action, soundEffectOnHover);
        Dimension textSize = Util.calcTextSize(text, font) ;
        this.minSize = new Dimension(textSize.width + padding, textSize.height + padding) ;
        this.size = new Dimension(Math.max(size.width, minSize.width), Math.max(size.height, minSize.height)) ;
        resize(size) ;
		this.topLeft = UtilAlignment.getTopLeft(pos, alignment, size) ;
        updatePositions(topLeft) ;
        this.text = text ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, Image image, Image selectedImage, ButtonFunction action, Clip soundEffectOnHover)
    {
        this(pos, alignment, name, new Dimension(10 + padding, 50 + padding), text, image, selectedImage, action, soundEffectOnHover) ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, ButtonFunction action)
    {
        this(pos, alignment, name, new Dimension(10 + padding, 5 + padding), text, null, null, action, null) ;
    }

    public GameTextButton(Point pos, Align alignment, Dimension size, String text, ButtonFunction action)
    {
        this(pos, alignment, "", size, text, null, null, action, null) ;
    }

    public GameTextButton(Point pos, Align alignment, String text, ButtonFunction action)
    {
        this(pos, alignment, "", new Dimension(10 + padding, 5 + padding), text, null, null, action, null) ;
    }
	
    private static Image flip(Image image, boolean flipH, boolean flipV)
    {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flippedImage.createGraphics();

        int scaleX = flipH ? -1 : 1;
        int scaleY = flipV ? -1 : 1;
        int translateX = flipH ? -width : 0;
        int translateY = flipV ? -height : 0;

        g.scale(scaleX, scaleY);
        g.translate(translateX, translateY);
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        return flippedImage;
    }

    private static Image flipHorizontally(Image image) { return flip(image, true, false) ;}
    private static Image flipVertically(Image image) { return flip(image, false, true) ;}
    private static Image stretchImage(Image image, int finalWidth, int finalHeight)
    {
        BufferedImage stretchedImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = stretchedImage.createGraphics();
        g2d.drawImage(image, 0, 0, finalWidth, finalHeight, null);
        g2d.dispose();
        return stretchedImage;
    }

    private void updatePositions(Point pos)
    {        
        this.pos1 = pos ;
        this.pos2 = new Point(pos.x, pos.y + edgeSize) ;
        this.pos3 = new Point(pos.x, pos.y + size.height - edgeSize) ;
        this.pos4 = new Point(pos.x + edgeSize, pos.y + size.height - edgeSize) ;
        this.pos5 = new Point(pos.x + size.width - edgeSize, pos.y + size.height - edgeSize) ;
        this.pos6 = new Point(pos.x + size.width - edgeSize, pos.y + edgeSize) ;
        this.pos7 = new Point(pos.x + size.width - edgeSize, pos.y) ;
        this.pos8 = new Point(pos.x + edgeSize, pos.y) ;
        this.pos9 = new Point(pos.x + edgeSize, pos.y + edgeSize) ;
        this.center = new Point(pos1.x + size.width / 2, pos1.y + size.height / 2) ;
    }

    public void displayStdTextButton()
    {
        if (isSelected)
        {
            GamePanel.getDP().drawImage(boxSelectedPart1, pos1, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart2, pos2, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedPart3, pos3, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart4, pos4, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedPart5, pos5, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart6, pos6, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedPart7, pos7, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart8, pos8, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart9, pos9, Align.topLeft);

            return ;
        }

        GamePanel.getDP().drawImage(boxPart1, pos1, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart2, pos2, Align.topLeft);
        GamePanel.getDP().drawImage(boxPart3, pos3, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart4, pos4, Align.topLeft);
        GamePanel.getDP().drawImage(boxPart5, pos5, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart6, pos6, Align.topLeft);
        GamePanel.getDP().drawImage(boxPart7, pos7, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart8, pos8, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart9, pos9, Align.topLeft);
    }

	public void display(double angle, boolean displayText, Point mousePos, Color textColor, double opacity)
	{
		
		if (!isActive) { return ;} // TODO move this logic
		
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
            displayStdTextButton() ;
		}
        else
        {
            GamePanel.getDP().drawImage(imageDisplayed, center, angle, Scale.unit, Align.center, opacity) ;
        }

		GamePanel.getDP().drawText(center, Align.center, 0, text, font, textColor) ;
	}

	public void display()
	{
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
            displayStdTextButton() ;
		}
        else
        {
            GamePanel.getDP().drawImage(imageDisplayed, center, Draw.stdAngle, Scale.unit, Align.center, 1.0) ;
        }

		GamePanel.getDP().drawText(center, Align.center, 0, text, font, textColor) ;
	}

    public Dimension getSize() { return size ;}
    public String getText() { return text ;}

    public void setTopLeftPos(Point topLeftPos)
    {
        this.topLeft = topLeftPos ;
        updatePositions(topLeftPos) ;
    }
    public void setText(String text) { this.text = text ;}
    public void resize(Dimension size)
    {
        if (size.width < minSize.width)
        {
            size.width = minSize.width ;
        }
        if (size.height < minSize.height)
        {
            size.height = minSize.height ;
        }
        this.boxStretchedPart2 = stretchImage(boxPart2, boxPart2.getWidth(null), size.height - 2 * edgeSize) ;
        this.boxStretchedPart4 = stretchImage(boxPart4, size.width - 2 * edgeSize, boxPart4.getHeight(null)) ;
        this.boxStretchedPart6 = stretchImage(boxPart6, boxPart6.getWidth(null), size.height - 2 * edgeSize) ;
        this.boxStretchedPart8 = stretchImage(boxPart8, size.width - 2 * edgeSize, boxPart8.getHeight(null)) ;
        this.boxStretchedPart9 = stretchImage(boxPart9, size.width - 2 * edgeSize, size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart2 = stretchImage(boxSelectedPart2, boxSelectedPart2.getWidth(null), size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart4 = stretchImage(boxSelectedPart4, size.width - 2 * edgeSize, boxSelectedPart4.getHeight(null)) ;
        this.boxSelectedStretchedPart6 = stretchImage(boxSelectedPart6, boxSelectedPart6.getWidth(null), size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart8 = stretchImage(boxSelectedPart8, size.width - 2 * edgeSize, boxSelectedPart8.getHeight(null)) ;
        this.boxSelectedStretchedPart9 = stretchImage(boxSelectedPart9, size.width - 2 * edgeSize, size.height - 2 * edgeSize) ;
    }
    
}
