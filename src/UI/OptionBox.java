package UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import graphics.Align;
import main.GamePanel;
import main.ImageLoader;
import main.Palette;
import main.Path;

public class OptionBox
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
    private Dimension size ;
    private String text ;
    private boolean isHovered ;

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

    public OptionBox(Point pos, Dimension size, String text)
    {
        this.size = new Dimension(Math.max(size.width, 2 * edgeSize + 2), Math.max(size.height, 2 * edgeSize + 2)) ;
        this.text = text ;
        this.boxStretchedPart2 = stretchImage(boxPart2, boxPart2.getWidth(null), this.size.height - 2 * edgeSize) ;
        this.boxStretchedPart4 = stretchImage(boxPart4, this.size.width - 2 * edgeSize, boxPart4.getHeight(null)) ;
        this.boxStretchedPart6 = stretchImage(boxPart6, boxPart6.getWidth(null), this.size.height - 2 * edgeSize) ;
        this.boxStretchedPart8 = stretchImage(boxPart8, this.size.width - 2 * edgeSize, boxPart8.getHeight(null)) ;
        this.boxStretchedPart9 = stretchImage(boxPart9, this.size.width - 2 * edgeSize, this.size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart2 = stretchImage(boxSelectedPart2, boxSelectedPart2.getWidth(null), this.size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart4 = stretchImage(boxSelectedPart4, this.size.width - 2 * edgeSize, boxSelectedPart4.getHeight(null)) ;
        this.boxSelectedStretchedPart6 = stretchImage(boxSelectedPart6, boxSelectedPart6.getWidth(null), this.size.height - 2 * edgeSize) ;
        this.boxSelectedStretchedPart8 = stretchImage(boxSelectedPart8, this.size.width - 2 * edgeSize, boxSelectedPart8.getHeight(null)) ;
        this.boxSelectedStretchedPart9 = stretchImage(boxSelectedPart9, this.size.width - 2 * edgeSize, this.size.height - 2 * edgeSize) ;
        updatePositions(pos) ;
    }

    public OptionBox(Point pos, Font font, String text)
    {
        this(pos, new Dimension(GamePanel.DP.textLength(text, font) + 2 * edgeSize, GamePanel.DP.textHeight(font) + 2 * edgeSize), text) ;
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
    public String getText() { return text ;}
    public Dimension getSize() { return size ;}
    public void setText(String text) { this.text = text ;}
    public void setTopLeftPos(Point pos)
    {
        updatePositions(pos) ;
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

    public void display()
    {
        if (isHovered)
        {
            GamePanel.DP.drawImage(boxSelectedPart1, pos1, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedStretchedPart2, pos2, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedPart3, pos3, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedStretchedPart4, pos4, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedPart5, pos5, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedStretchedPart6, pos6, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedPart7, pos7, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedStretchedPart8, pos8, Align.topLeft);
            GamePanel.DP.drawImage(boxSelectedStretchedPart9, pos9, Align.topLeft);
            if (text != null)
            {
                GamePanel.DP.drawText(center, Align.center, text, Palette.colors[3]);
            }

            return ;
        }

        GamePanel.DP.drawImage(boxPart1, pos1, Align.topLeft);
        GamePanel.DP.drawImage(boxStretchedPart2, pos2, Align.topLeft);
        GamePanel.DP.drawImage(boxPart3, pos3, Align.topLeft);
        GamePanel.DP.drawImage(boxStretchedPart4, pos4, Align.topLeft);
        GamePanel.DP.drawImage(boxPart5, pos5, Align.topLeft);
        GamePanel.DP.drawImage(boxStretchedPart6, pos6, Align.topLeft);
        GamePanel.DP.drawImage(boxPart7, pos7, Align.topLeft);
        GamePanel.DP.drawImage(boxStretchedPart8, pos8, Align.topLeft);
        GamePanel.DP.drawImage(boxStretchedPart9, pos9, Align.topLeft);
        if (text != null)
        {
            GamePanel.DP.drawText(center, Align.center, text, Palette.colors[3]);
        }
    }
}
