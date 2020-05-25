
import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = 60L;
	private boolean clean, hasWallUp, hasWallDown, hasWallLeft, hasWallRight;
	@SuppressWarnings("unused")
	private boolean hasWalls;
	private int x, y;
	private double distToStart;

	public Tile(int x, int y, boolean clean, boolean hasWallUp, boolean hasWallDown, boolean hasWallLeft,
			boolean hasWallRight) {
		super();
		this.x = x;
		this.y = y;
		this.clean = clean;
		this.hasWallUp = hasWallUp;
		this.hasWallDown = hasWallDown;
		this.hasWallLeft = hasWallLeft;
		this.hasWallRight = hasWallRight;
		hasWalls = hasWallUp || hasWallDown || hasWallLeft || hasWallRight;
		distToStart = 0;
	}

	public Tile(int x, int y, boolean clean) {
		super();
		this.x = x;
		this.y = y;
		this.clean = clean;
		this.hasWallUp = false;
		this.hasWallDown = false;
		this.hasWallLeft = false;
		this.hasWallRight = false;
//		hasWalls = hasWallUp||hasWallDown||hasWallLeft||hasWallRight;
		distToStart = 0;
	}

	public Tile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.clean = true;
		this.hasWallUp = false;
		this.hasWallDown = false;
		this.hasWallLeft = false;
		this.hasWallRight = false;
//		hasWalls = hasWallUp||hasWallDown||hasWallLeft||hasWallRight;
		distToStart = 0;
	}

	public double getDistToStart() {
		return distToStart;
	}

	public void setDistToStart(double distToStart) {
		this.distToStart = distToStart;
	}

	public boolean isClean() {
		return clean;
	}

	public void Clean() {
		clean = true;
	}

	public void putDirt() {
		clean = false;
	}

	public boolean HasWallUp() {
		return hasWallUp;
	}

	public void putWallUp() {
		this.hasWallUp = true;
	}

	public void removeWallUp() {
		this.hasWallUp = false;
	}

	public boolean HasWallDown() {
		return hasWallDown;
	}

	public void putWallDown() {
		this.hasWallDown = true;
	}

	public void removeWallDown() {
		this.hasWallDown = false;
	}

	public boolean HasWallLeft() {
		return hasWallLeft;
	}

	public void putWallLeft() {
		this.hasWallLeft = true;
	}

	public void removeWallLeft() {
		this.hasWallLeft = false;
	}

	public boolean HasWallRight() {
		return hasWallRight;
	}

	public void putWallRight() {
		this.hasWallRight = true;
	}

	public void removeWallRight() {
		this.hasWallRight = false;
	}

	public boolean hasWalls() {
		return hasWallUp || hasWallDown || hasWallLeft || hasWallRight;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		String str = "Tile " + x + " " + y + " " + (clean ? "Clean | " : "Dirty | ") + "Walls: "
				+ (hasWallUp ? "UP " : "") + (hasWallDown ? "Down " : "") + (hasWallLeft ? "Left " : "")
				+ (hasWallRight ? "Right" : "");
		return str;
	}

	public boolean equals(Tile tile) {
		boolean equal = true;
		if (this.clean = tile.clean && this.x == tile.x && this.y == tile.y)
			return equal;
		else
			return !equal;
	}

	public int dist(int i2, int j2) {
		return Math.abs(x - i2) + Math.abs(y - j2);
	}

	public Tile copy() {
		return new Tile(x, y, clean, hasWallUp, hasWallDown, hasWallLeft, hasWallRight);
	}

	public static void main(String[] args) {
		Tile s = new Tile(0, 0, true, true, false, false, true);
		System.out.println(s);
	}
	public int getNumberOfWalls() {
		return (hasWallDown?1:0) + (hasWallUp?1:0) + (hasWallLeft?1:0) + (hasWallRight?1:0);
	}

}