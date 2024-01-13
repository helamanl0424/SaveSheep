
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Water extends Sprite {

	public Water(int x, int y) {
		super();
		image = null;
		setLocation(x,y);
	}
	
	@Override
	public void draw(Graphics g) {
		if (absolutePosition != null) {
			g.setColor(Color.BLUE);
			g.fillRect(absolutePosition.x, absolutePosition.y, 50, 50);
		}
	}
	
	@Override
	public void setLocation(Point p) {
		if (p == null) {
			relativePosition = null;
			absolutePosition = null;
		} else {
			relativePosition.setLocation(p);
			int x = 10+SIZE*relativePosition.x;
			int y = 10+SIZE*relativePosition.y;
			absolutePosition.setLocation(x, y);
		}
	}

}
