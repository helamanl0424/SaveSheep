
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

/**
 * The word "sprite" is an old word in computer graphics.
 * http://en.wikipedia.org/wiki/Sprite_(computer_graphics)
 * In simple terms, a sprite is simply a 2D picture that
 * can move around the screen.
 *
 */
public abstract class Sprite {
	protected Point absolutePosition;
	protected Point relativePosition;
	protected ImageIcon image;
	public final int SIZE = 50;

	public Sprite() {
		relativePosition = new Point(-1,-1);
		absolutePosition = new Point();
	}

	public void setLocation(Point p) {
		if (p == null) {
			relativePosition = null;
			absolutePosition = null;
		} else {
			relativePosition.setLocation(p);
			if (image != null) {
				int iconWidth = image.getIconWidth();
				int iconHeight = image.getIconHeight();
				int x = 10+SIZE*relativePosition.x + (SIZE - iconWidth)/2;
				int y = 10+SIZE*relativePosition.y + (SIZE - iconHeight)/2;
				absolutePosition.setLocation(x, y);
			}
		}
	}
	
	public void setLocation(int x, int y) {
		setLocation(new Point(x,y));
	}

	public Point getLocation() {
		return relativePosition;
	}

	public void draw(Graphics g) {
		if (absolutePosition != null) {
			image.paintIcon(null, g, absolutePosition.x, absolutePosition.y);
		}
	}
	
	public boolean isNear(Sprite other) {
		boolean result = false;
		if (other.relativePosition != null) {
			if ((Math.abs(this.relativePosition.x - other.relativePosition.x) <= 1) &&
					(Math.abs(this.relativePosition.y - other.relativePosition.y) <= 1)) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean isTouching(Sprite other) {
		return this.relativePosition.equals(other.relativePosition);
	}


}
