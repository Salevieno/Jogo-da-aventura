package graphics;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import utilities.AlignmentPoints;

public class Gif extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	Image image ;
	boolean loop ;
	boolean stopsTime ;
	boolean isDonePlaying ;
	
	public Gif(Image image, boolean loop, boolean stopsTime)
	{
		super();
		this.image = image;
		this.loop = loop;
		this.stopsTime = stopsTime;
		isDonePlaying = false;
	}
	
	public Image getImage()
	{
		return image;
	}
	public boolean isLoop()
	{
		return loop;
	}
	public boolean isStopsTime()
	{
		return stopsTime;
	}
	public boolean isDonePlaying()
	{
		return isDonePlaying;
	}
    
	protected void fireActionPerformed() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (0 < listeners.length)
        {
            ActionEvent evt = new ActionEvent(this, 0, "stopped");
            for (ActionListener listener : listeners)
            {
                listener.actionPerformed(evt);
            }
        }
    }
	
	@Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
	{
        boolean finished = super.imageUpdate(img, infoflags, x, y, w, h);
        if (!finished)
        {
            fireActionPerformed();
        }
        return finished;
    }
	
	public boolean play(Point pos, AlignmentPoints alignment, DrawingOnPanel DP)
	{
		return imageUpdate(image, 10, 0, 0, 100, 100) ;
		/*isDonePlaying = imageUpdate() ;
		DP.DrawGif(image, pos, alignment) ;
		if (true)
		{
			isDonePlaying = true ;
		}*/
	}
}
