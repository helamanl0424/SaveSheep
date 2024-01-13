
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the "main" class of the program.
 * This is where all the other objects are created, and where
 * all the rendering (drawing) is initiated.
 *
 */
public class Game extends JPanel implements KeyListener {

	private ArrayList<Tree> trees;
	private ArrayList<Sheep> flock;
	private ArrayList<Robber> thugs;
	private ArrayList<Water> waters;

	private Ammon ammon;
	private int numRows, numColumns;
	private int sheepCount;
	private int slain;

	public Game() {

		trees = new ArrayList<>();
		thugs = new ArrayList<>();
		flock = new ArrayList<>();
		waters = new ArrayList<>();
		
		//TODO: the default map is intentionally boring.
		//You must replace it with your own.
		numRows = 7;
		numColumns = 7;		
		ammon = new Ammon(0,0);		
		Water lake1 = new Water(4,1);
		waters.add(lake1);		
		Tree tree1 = new Tree(2,4);
		trees.add(tree1);
		reset();
		addKeyListener(this);
	}


	/**
	 * This method initiates all the drawing for the program.
	 * Rather than drawing things directly, however, it delegates
	 * all the rendering to the individual Sprite objects.
	 */
	@Override
	public void paintComponent(Graphics g) {
		requestFocusInWindow();
		
		//fill in the background color
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//draw the grass
		g.setColor(new Color(200, 255, 200));
		g.fillRect(10, 10, ammon.SIZE * numColumns, ammon.SIZE * numRows);

		//draw border around field perimeter
		g.setColor(Color.BLACK);
		g.drawRect(10, 10, ammon.SIZE * numColumns, ammon.SIZE * numRows);
		g.drawString("Sheep Remaining: " + sheepCount, 10, 25+50 * numRows);
		String robbercount = "Robbers Remaining: " + (thugs.size() - slain);
		g.drawString(robbercount, 10+50 * numColumns - g.getFontMetrics().stringWidth(robbercount),
				25+50 * numRows);


		for (Tree t : trees) {
			t.draw(g);
		}
		for (Sheep t : flock) {
			t.draw(g);
		}
		for (Robber t : thugs) {
			t.draw(g);
		}
		for (Water w : waters) {
			w.draw(g);
		}
		ammon.draw(g);
	}
	/**
	 * Accept the user's keyboard input.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		Point current = ammon.getLocation();
		Point nextPos = new Point(current);

		switch (key) {
		case KeyEvent.VK_UP:
			--nextPos.y;
			break;
		case KeyEvent.VK_DOWN:
			++nextPos.y;
			break;
		case KeyEvent.VK_LEFT:
			--nextPos.x;
			break;
		case KeyEvent.VK_RIGHT:
			++nextPos.x;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_E:
		case KeyEvent.VK_W:
		case KeyEvent.VK_N:
			shoot(""+e.getKeyChar());			
			return;

		default: break;
		}

		if (nextPos.y >= 0 && nextPos.y < numRows &&
				nextPos.x >= 0 && nextPos.x < numColumns) {

			if (!(hitATree(nextPos))) {

				ammon.setLocation(nextPos);
				repaint();

				//what happens if Ammon finds a sheep?
				for (Sheep s : flock) {
					if (ammon.isTouching(s)) {
						s.setLocation(null);
						sheepCount--;
						if (sheepCount == 0) {
							JOptionPane.showMessageDialog(this, "WELL DONE, AMMON! You rescued all the sheep!");
							reset();
							break;
						}
					}
				}

				//what happens if Ammon comes near a robber?
				for (Robber r : thugs) {
					if (ammon.isNear(r)) {
						JOptionPane.showMessageDialog(this, "OH NO AMMON! The robber got you! Try again.");
						reset();
						break;
					}
				}

				//what happens if Ammon gets wet?
				for (Water s : waters) {
					if (ammon.isTouching(s)) {
						JOptionPane.showMessageDialog(this, "OH NO AMMON! You can't swim! Try again.");
						reset();
						break;
					}
				}
			}
		}
	}

	public void reset() {
		flock.clear();
		thugs.clear();
		flock.add(new Sheep(2,2));
		flock.add(new Sheep(3,2));
		thugs.add(new Robber(5,3));		
		thugs.add(new Robber(5,4));
		ammon.setLocation(0,0);
		sheepCount = flock.size();
		slain = 0;
		repaint();
	}
	
	private void shoot(String dir) {
		//shoot_Easy(dir.toUpperCase());
		shoot_Hard(dir.toUpperCase());
		repaint();
	}
	
	
	
	private void shoot_Easy(String dir) {
		Point nephiPos = ammon.getLocation();
		
		for (Robber robber : thugs) {
			Point robPos = robber.getLocation();
			if (robPos == null) continue;
			boolean gotHim = false;
			if (dir.equals("N")) {
				if (nephiPos.x == robPos.x && robPos.y < nephiPos.y) {
					gotHim = true;
				}
			}
			if (dir.equals("S")) {
				if (nephiPos.x == robPos.x && robPos.y > nephiPos.y) {
					gotHim = true;
				}
			}
			if (dir.equals("E")) {
				if (nephiPos.y == robPos.y && robPos.x > nephiPos.x) {
					gotHim = true;
				}
			}
			if (dir.equals("W")) {
				if (nephiPos.y == robPos.y && robPos.x < nephiPos.x) {
					gotHim = true;
				}
			}
			if (gotHim) {
				slain++;
				robber.setLocation(null);
			}
		}
	}

	private void shoot_Hard(String dir) {
		Point ap = ammon.getLocation();
		
		switch (dir) {
		case "N":
			for (int y=ap.y-1; y>=0; y--) {
				if (didIHitAnything(ap.x, y)) {
					break;
				}
			}
			break;
		case "S":
			for (int y=ap.y+1; y<numRows; y++) {
				if (didIHitAnything(ap.x, y)) {
					break;
				}
			}
			break;
		case "E":
			for (int x=ap.x+1; x<numColumns; x++) {
				if (didIHitAnything(x, ap.y)) {
					break;
				}
			}
			break;
		case "W":
			for (int x=ap.x-1; x>=0; x--) {
				if (didIHitAnything(x, ap.y)) {
					break;
				}
			}
			break;
		}

		

	}
	
	/**
	 * Given an (x,y) coordinate, did Ammon's stone hit anything there?
	 * @param x
	 * @param y
	 * @return true if hit; false if no hit
	 */
	private boolean didIHitAnything(int x, int y) {
		for (Robber r : thugs) {
			Point loc = r.getLocation();
			if (loc != null) {
				if (loc.x == x && loc.y == y) {
					r.setLocation(null);
					slain++;
					return true;
				}
			}
		}
		for (Tree t : trees) {
			Point loc = t.getLocation();
			if (loc != null) {
				if (loc.x == x && loc.y == y) {
					//can't shoot through trees, so we just stop.
					return true;
				}
			}
		}
		for (Sheep s : flock) {
			Point loc = s.getLocation();
			if (loc != null) {
				if (loc.x == x && loc.y == y) {
					JOptionPane.showMessageDialog(this, "OH NO, AMMON! YOU JUST KILLED A SHEEP! TRY AGAIN.");
					reset();
					return true;
				}
			}
		}
		return false;
	}


	private boolean hitATree(Point p) {
		for (Tree t : trees) {
			if (t.getLocation().equals(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		JFrame window = new JFrame("Ammon at the Waters of Sebus");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(450,450);
		window.setContentPane(new Game());
		window.setVisible(true);
	}

}
