package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.Clip;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import utilities.Util;

public class GameTextButton extends GameButton
{
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
    private Dimension minSize ;

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

    private static final Image BOX_PART1 = ImageLoader.loadImage(Path.UI_IMG + "TextBox1.png") ;
    private static final Image BOX_PART2 = ImageLoader.loadImage(Path.UI_IMG + "TextBox3.png") ;
    private static final Image BOX_PART3 = flipVertically(BOX_PART1) ;
    private static final Image BOX_PART4 = ImageLoader.loadImage(Path.UI_IMG + "TextBox2.png") ;
    private static final Image BOX_PART5 = flipVertically(flipHorizontally(BOX_PART1)) ;
    private static final Image BOX_PART6 = flipHorizontally(BOX_PART2) ;
    private static final Image BOX_PART7 = flipHorizontally(BOX_PART1) ;
    private static final Image BOX_PART8 = flipVertically(BOX_PART4) ;
    private static final Image BOX_PART9 = ImageLoader.loadImage(Path.UI_IMG + "TextBox4.png") ;
    private static final Image BOX_SELECTED_PART1 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected1.png") ;
    private static final Image BOX_SELECTED_PART2 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected3.png") ;
    private static final Image BOX_SELECTED_PART3 = flipVertically(BOX_SELECTED_PART1) ;
    private static final Image BOX_SELECTED_PART4 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected2.png") ;
    private static final Image BOX_SELECTED_PART5 = flipVertically(flipHorizontally(BOX_SELECTED_PART1)) ;
    private static final Image BOX_SELECTED_PART6 = flipHorizontally(BOX_SELECTED_PART2) ;
    private static final Image BOX_SELECTED_PART7 = flipHorizontally(BOX_SELECTED_PART1) ;
    private static final Image BOX_SELECTED_PART8 = flipVertically(BOX_SELECTED_PART4) ;
    private static final Image BOX_SELECTED_PART9 = ImageLoader.loadImage(Path.UI_IMG + "TextBoxSelected4.png") ;
    private static final int EDGE_SIZE = BOX_PART1.getWidth(null) ;
    private static final int PADDING = 2 * EDGE_SIZE + 2 ;
    private static final Set<GameTextButton> ALL = new HashSet<>() ;

    public GameTextButton(Point pos, Align alignment, String name, Dimension size, String text, Image image, Image selectedImage, ButtonFunction action, Clip soundEffectOnHover)
    {
        super(pos, alignment, name, image, selectedImage, action, soundEffectOnHover);
        Dimension textSize = Util.calcTextSize(text, FONT) ;
        this.minSize = new Dimension(textSize.width + PADDING, textSize.height + PADDING) ;
        this.size = new Dimension(Math.max(size.width, minSize.width), Math.max(size.height, minSize.height)) ;
        resize(size) ;
		this.topLeft = UtilAlignment.getTopLeft(pos, alignment, size) ;
        updatePositions(topLeft) ;
        this.text = text ;
        ALL.add(this) ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, Image image, Image selectedImage, ButtonFunction action, Clip soundEffectOnHover)
    {
        this(pos, alignment, name, new Dimension(10 + PADDING, 50 + PADDING), text, image, selectedImage, action, soundEffectOnHover) ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, ButtonFunction action)
    {
        this(pos, alignment, name, new Dimension(10 + PADDING, 5 + PADDING), text, null, null, action, null) ;
    }

    public GameTextButton(Point pos, Align alignment, Dimension size, String text, ButtonFunction action)
    {
        this(pos, alignment, "", size, text, null, null, action, null) ;
    }

    public GameTextButton(Point pos, Align alignment, String text, ButtonFunction action)
    {
        this(pos, alignment, "", new Dimension(10 + PADDING, 5 + PADDING), text, null, null, action, null) ;
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
        this.pos2 = new Point(pos.x, pos.y + EDGE_SIZE) ;
        this.pos3 = new Point(pos.x, pos.y + size.height - EDGE_SIZE) ;
        this.pos4 = new Point(pos.x + EDGE_SIZE, pos.y + size.height - EDGE_SIZE) ;
        this.pos5 = new Point(pos.x + size.width - EDGE_SIZE, pos.y + size.height - EDGE_SIZE) ;
        this.pos6 = new Point(pos.x + size.width - EDGE_SIZE, pos.y + EDGE_SIZE) ;
        this.pos7 = new Point(pos.x + size.width - EDGE_SIZE, pos.y) ;
        this.pos8 = new Point(pos.x + EDGE_SIZE, pos.y) ;
        this.pos9 = new Point(pos.x + EDGE_SIZE, pos.y + EDGE_SIZE) ;
        this.center = new Point(pos1.x + size.width / 2, pos1.y + size.height / 2) ;
    }

    public void displayStdTextButton()
    {
        if (isSelected)
        {
            GamePanel.getDP().drawImage(BOX_SELECTED_PART1, pos1, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart2, pos2, Align.topLeft);
            GamePanel.getDP().drawImage(BOX_SELECTED_PART3, pos3, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart4, pos4, Align.topLeft);
            GamePanel.getDP().drawImage(BOX_SELECTED_PART5, pos5, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart6, pos6, Align.topLeft);
            GamePanel.getDP().drawImage(BOX_SELECTED_PART7, pos7, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart8, pos8, Align.topLeft);
            GamePanel.getDP().drawImage(boxSelectedStretchedPart9, pos9, Align.topLeft);

            return ;
        }

        GamePanel.getDP().drawImage(BOX_PART1, pos1, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart2, pos2, Align.topLeft);
        GamePanel.getDP().drawImage(BOX_PART3, pos3, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart4, pos4, Align.topLeft);
        GamePanel.getDP().drawImage(BOX_PART5, pos5, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart6, pos6, Align.topLeft);
        GamePanel.getDP().drawImage(BOX_PART7, pos7, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart8, pos8, Align.topLeft);
        GamePanel.getDP().drawImage(boxStretchedPart9, pos9, Align.topLeft);
    }

	public void display(boolean displayText, Point mousePos, Color textColor, double opacity)
	{
		
		if (!isActive) { return ;} // TODO move this logic
		
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null)
		{
            displayStdTextButton() ;
		}
        else
        {
            GamePanel.getDP().drawImage(imageDisplayed, center, Scale.unit, Align.center, opacity) ;
        }

		GamePanel.getDP().drawText(center, Align.center, 0, text, FONT, textColor) ;
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
            GamePanel.getDP().drawImage(imageDisplayed, center, Scale.unit, Align.center, 1.0) ;
        }

		GamePanel.getDP().drawText(center, Align.center, 0, text, FONT, TEXT_COLOR) ;
	}

    public Dimension getSize() { return size ;}
    public String getText() { return text ;}

    public void setTopLeftPos(Point topLeftPos)
    {
        this.topLeft = topLeftPos ;
        updatePositions(topLeftPos) ;
    }
    public void setText(String text) { this.text = text ;}
    public static void updateAllTextSize()
    {
        ALL.forEach(btn -> {
            Dimension textSize = GamePanel.calcTextSize(btn.text, FONT) ;
            btn.minSize = new Dimension(textSize.width + PADDING, textSize.height + PADDING) ;
            btn.size = new Dimension(Math.max(btn.size.width, btn.minSize.width), Math.max(btn.size.height, btn.minSize.height)) ;
            btn.resize(btn.size) ;
        });
    }
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
        this.boxStretchedPart2 = stretchImage(BOX_PART2, BOX_PART2.getWidth(null), size.height - 2 * EDGE_SIZE) ;
        this.boxStretchedPart4 = stretchImage(BOX_PART4, size.width - 2 * EDGE_SIZE, BOX_PART4.getHeight(null)) ;
        this.boxStretchedPart6 = stretchImage(BOX_PART6, BOX_PART6.getWidth(null), size.height - 2 * EDGE_SIZE) ;
        this.boxStretchedPart8 = stretchImage(BOX_PART8, size.width - 2 * EDGE_SIZE, BOX_PART8.getHeight(null)) ;
        this.boxStretchedPart9 = stretchImage(BOX_PART9, size.width - 2 * EDGE_SIZE, size.height - 2 * EDGE_SIZE) ;
        this.boxSelectedStretchedPart2 = stretchImage(BOX_SELECTED_PART2, BOX_SELECTED_PART2.getWidth(null), size.height - 2 * EDGE_SIZE) ;
        this.boxSelectedStretchedPart4 = stretchImage(BOX_SELECTED_PART4, size.width - 2 * EDGE_SIZE, BOX_SELECTED_PART4.getHeight(null)) ;
        this.boxSelectedStretchedPart6 = stretchImage(BOX_SELECTED_PART6, BOX_SELECTED_PART6.getWidth(null), size.height - 2 * EDGE_SIZE) ;
        this.boxSelectedStretchedPart8 = stretchImage(BOX_SELECTED_PART8, size.width - 2 * EDGE_SIZE, BOX_SELECTED_PART8.getHeight(null)) ;
        this.boxSelectedStretchedPart9 = stretchImage(BOX_SELECTED_PART9, size.width - 2 * EDGE_SIZE, size.height - 2 * EDGE_SIZE) ;
    }
    
}
