package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphics.Align;
import graphics.Scale;
import graphics.UtilAlignment;
import main.GamePanel;
import main.ImageLoader;
import main.Path;
import music.GameSound;
import utilities.Util;

public class GameTextButton extends GameButton
{
    private List<Point> positions ;
    private Point center ;
    private String text ;
    private Dimension minSize ;

    private List<Image> boxStretched ;
    private List<Image> boxSelectedStretched ;
    private List<Image> boxInactiveStretched ;

    private static final List<Image> BOX_IMGS ;
    private static final List<Image> BOX_SELECTED_IMGS ;
    private static final List<Image> BOX_INACTIVE_IMGS ;

    private static final int EDGE_SIZE ;
    private static final int PADDING ;
    private static final Set<GameTextButton> ALL = new HashSet<>() ;

    /*
        ____________
        |  1  8  7  |
        |  2  9  6  |
        |__3__4__5__|
    */
// TODO dá pra otimizar updating e stretching reutilizando botões iguais
    static
    {
        BOX_IMGS = imagesToListImages("TextBox1.png", "TextBox2.png", "TextBox3.png", "TextBox4.png") ;
        BOX_SELECTED_IMGS = imagesToListImages("TextBoxSelected1.png", "TextBoxSelected2.png", "TextBoxSelected3.png", "TextBoxSelected4.png") ;
        BOX_INACTIVE_IMGS = imagesToListImages("TextBoxInactive1.png", "TextBoxInactive2.png", "TextBoxInactive3.png", "TextBoxInactive4.png") ;
        EDGE_SIZE = BOX_IMGS.get(0).getWidth(null) ;
        PADDING = 2 * EDGE_SIZE + 2 ;
    }

    private static List<Image> imagesToListImages(String path1, String path2, String path3, String path4)
    {
        Image img1 = ImageLoader.loadImage(Path.UI_IMG + path1) ;
        Image img2 = ImageLoader.loadImage(Path.UI_IMG + path2) ;
        Image img3 = ImageLoader.loadImage(Path.UI_IMG + path3) ;
        Image img4 = ImageLoader.loadImage(Path.UI_IMG + path4) ;
        List<Image> allImages = List.of(
            img1,
            img2,
            flipVertically(img1),
            img3,
            flipVertically(flipHorizontally(img1)),
            flipHorizontally(img2),
            flipHorizontally(img1),
            flipVertically(img3),
            img4
        ) ;

        return allImages ;
    }

    public GameTextButton(Point pos, Align alignment, String name, Dimension size, String text, Image image, Image selectedImage, ButtonFunction action, GameSound soundOnHover)
    {
        super(pos, alignment, name, image, selectedImage, action, soundOnHover);
        Dimension textSize = Util.calcTextSize(text, FONT) ;
        this.minSize = new Dimension(textSize.width + PADDING, textSize.height + PADDING) ;
        this.size = new Dimension(Math.max(size.width, minSize.width), Math.max(size.height, minSize.height)) ;
        resize(size) ;
		this.topLeft = UtilAlignment.getTopLeft(pos, alignment, size) ;
        updatePositions(topLeft) ;
        this.text = text ;
        ALL.add(this) ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, Image image, Image selectedImage, ButtonFunction action, GameSound soundOnHover)
    {
        this(pos, alignment, name, new Dimension(10 + PADDING, 50 + PADDING), text, image, selectedImage, action, soundOnHover) ;
    }

    public GameTextButton(Point pos, Align alignment, String name, String text, ButtonFunction action)
    {
        this(pos, alignment, name, new Dimension(10 + PADDING, 5 + PADDING), text, null, null, action, null) ;
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

    private void updatePositions(Point topLeftPos)
    {
        this.positions = List.of(
            topLeftPos,
            new Point(topLeftPos.x, topLeftPos.y + EDGE_SIZE),
            new Point(topLeftPos.x, topLeftPos.y + size.height - EDGE_SIZE),
            new Point(topLeftPos.x + EDGE_SIZE, topLeftPos.y + size.height - EDGE_SIZE),
            new Point(topLeftPos.x + size.width - EDGE_SIZE, topLeftPos.y + size.height - EDGE_SIZE),
            new Point(topLeftPos.x + size.width - EDGE_SIZE, topLeftPos.y + EDGE_SIZE),
            new Point(topLeftPos.x + size.width - EDGE_SIZE, topLeftPos.y),
            new Point(topLeftPos.x + EDGE_SIZE, topLeftPos.y),
            new Point(topLeftPos.x + EDGE_SIZE, topLeftPos.y + EDGE_SIZE)
        ) ;
        this.center = new Point(topLeftPos.x + size.width / 2, topLeftPos.y + size.height / 2) ;
    }

    public void displayStdTextButton()
    {
        List<Image> displayedImages = boxStretched ;
        if (!isActive)
        {
            displayedImages = boxInactiveStretched ;
        }
        else if (isSelected)
        {
            displayedImages = boxSelectedStretched ;
        }

        for (int i = 0 ; i <= displayedImages.size() - 1 ; i += 1)
        {
            GamePanel.getDP().drawImage(displayedImages.get(i), positions.get(i), Align.topLeft);
        }
    }

	public void display(boolean displayText, Point mousePos, Color textColor, double opacity)
	{
		Image imageDisplayed = isSelected ? selectedImage : image ;
		
		if (imageDisplayed == null || !isActive)
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
        display(true, null, TEXT_COLOR, 1.0) ;
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

        this.boxStretched = getStrecthedImages(BOX_IMGS) ;
        this.boxSelectedStretched = getStrecthedImages(BOX_SELECTED_IMGS) ;
        this.boxInactiveStretched = getStrecthedImages(BOX_INACTIVE_IMGS) ;
    }

    private List<Image> getStrecthedImages(List<Image> originalImages)
    {
        return List.of(
            originalImages.get(0),
            stretchImage(originalImages.get(1), originalImages.get(1).getWidth(null), size.height - 2 * EDGE_SIZE),
            originalImages.get(2),
            stretchImage(originalImages.get(3), size.width - 2 * EDGE_SIZE, originalImages.get(3).getHeight(null)),
            originalImages.get(4),
            stretchImage(originalImages.get(5), originalImages.get(5).getWidth(null), size.height - 2 * EDGE_SIZE),
            originalImages.get(6),
            stretchImage(originalImages.get(7), size.width - 2 * EDGE_SIZE, originalImages.get(7).getHeight(null)),
            stretchImage(originalImages.get(8), size.width - 2 * EDGE_SIZE, size.height - 2 * EDGE_SIZE)
        ) ;
    }
    
}
