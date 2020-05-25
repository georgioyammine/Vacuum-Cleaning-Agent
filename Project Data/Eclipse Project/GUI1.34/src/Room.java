
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Room implements Serializable {
	private static final long serialVersionUID = 82L; // old 82
	public Tile[][] grid;
	int w, h;
	private int nbOfDirt;

	public int nbOfDirt() {
		return nbOfDirt;
	}

	public Room(int w, int h) {
		this.w = w;
		this.h = h;
		grid = new Tile[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				grid[i][j] = new Tile(i, j);
			}
		}
		nbOfDirt = 0;
		addWallsAtEdges();
	}

	public int getGridWidth() {
		return grid.length;
	}

	public int getGridHeight() {
		return grid[0].length;
	}

	public void displayGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
	}

	public boolean hasWalls(int i, int j) {
		return grid[i][j].hasWalls();
	}

	public void addDirt(int i, int j) {
		if (grid[i][j].isClean()) {
			grid[i][j].putDirt();
			nbOfDirt++;
		}
	}

	public boolean isDirt(int i, int j) {
		return !grid[i][j].isClean();
	}

	public void removeDirt(int i, int j) {
		if (!grid[i][j].isClean()) {
			grid[i][j].Clean();
			nbOfDirt--;
		}
	}

	public boolean isBoundary(int i, int j) {
		if (i == getGridHeight() - 1 || i == 0 || j == getGridWidth() - 1 || j == 0)
			return true;
		else
			return false;
	}

	public void putWallUp(int a, int b) {
		grid[a][b].putWallUp();
		try {
			grid[a][b - 1].putWallDown();
		} catch (Exception e) {
		}

	}
	
	public int getNbOfWalls() {
		int result=0;
		for(int i =0;i<h;i++) {
			for(int j =0;j<w;j++) {
				if(j<w-1 && grid[j][i].HasWallRight())
					result++;
				if(i<h-1 && grid[j][i].HasWallDown())
					result++;
			}
		}
		return result;
	}

	public void putWallDown(int a, int b) {
		grid[a][b].putWallDown();
		try {
			grid[a][b + 1].putWallUp();
		} catch (Exception e) {
		}
	}

	public void putWallLeft(int a, int b) {
		grid[a][b].putWallLeft();
		try {
			grid[a - 1][b].putWallRight();
		} catch (Exception e) {
		}
	}

	public void putWallRight(int a, int b) {
		grid[a][b].putWallRight();
		try {
			grid[a + 1][b].putWallLeft();
		} catch (Exception e) {
		}
	}

	public void removeWallUp(int a, int b) {
		grid[a][b].removeWallUp();
		try {
			grid[a][b - 1].removeWallDown();
		} catch (Exception e) {
		}

	}

	public void removeWallDown(int a, int b) {
		grid[a][b].removeWallDown();
		try {
			grid[a][b + 1].removeWallUp();
		} catch (Exception e) {
		}
	}

	public void removeWallLeft(int a, int b) {
		grid[a][b].removeWallLeft();
		try {
			grid[a - 1][b].removeWallRight();
		} catch (Exception e) {
		}
	}

	public void removeWallRight(int a, int b) {
		grid[a][b].removeWallRight();
		try {
			grid[a + 1][b].removeWallLeft();
		} catch (Exception e) {
		}
	}

	public Iterable<Tile> getNeighbors(Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		int y = tile.getY();
		int x = tile.getX();

		if (!tile.HasWallDown()) {
			int a = x, b = y + 1;
			if (b > 0 && b < getGridHeight()) {
				list.add(grid[a][b]);
			}
		}
		if (!tile.HasWallUp()) {
			int a = x, b = y - 1;
			if (b >= 0 && b < getGridHeight()) {
				list.add(grid[a][b]);

			}
		}
		if (!tile.HasWallLeft()) {
			int a = x - 1, b = y;
			if (a >= 0 && a < getGridWidth()) {
				list.add(grid[a][b]);

			}
		}
		if (!tile.HasWallRight()) {
			int a = x + 1, b = y;
			if (a > 0 && a < getGridWidth()) {
				list.add(grid[a][b]);

			}
		}

		return list;

	}
	
	public void addDirtToAll() {
		for(int i=0;i<w;i++) {
			for(int j=0;j<h;j++) {
				addDirt(i, j);
			}
		}
	}
	

	public Iterable<Tile> getUnreachableNeighbors(Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		int y = tile.getY();
		int x = tile.getX();

		if (tile.HasWallDown()) {
			int a = x, b = y + 1;
			if (b > 0 && b < getGridHeight()) {
				list.add(grid[a][b]);
			}
		}
		if (tile.HasWallUp()) {
			int a = x, b = y - 1;
			if (b >= 0 && b < getGridHeight()) {
				list.add(grid[a][b]);

			}
		}
		if (tile.HasWallLeft()) {
			int a = x - 1, b = y;
			if (a >= 0 && a < getGridWidth()) {
				list.add(grid[a][b]);

			}
		}
		if (tile.HasWallRight()) {
			int a = x + 1, b = y;
			if (a > 0 && a < getGridWidth()) {
				list.add(grid[a][b]);

			}
		}

		return list;

	}

	public List<Tile> findAllDirt() {
		List<Tile> list = new ArrayList<>();
		for (int i = 0; i < getGridWidth(); i++) {
			for (int j = 0; j < getGridHeight(); j++) {
				if (isDirt(i, j)) {
					list.add(grid[i][j]);
				}
			}
		}
		return list;
	}

	public Tile findClosestDirt(Agent agent) {
		boolean[][] visited = new boolean[w][h];

		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = grid[agent.x][agent.y];
		queue.add(current); // enqueuing first Tile
		visited[current.getX()][current.getY()] = true;

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			for (Tile tile : this.getNeighbors(current)) {
				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
					if (!grid[tile.getX()][tile.getY()].isClean())
						return grid[tile.getX()][tile.getY()];
					queue.add(tile);
				}
			}
		}
		return null;
	}

	public int[][] findAllDistances(Agent agent) {
		int[][] dist = new int[w][h];
		for (int i = 0; i < dist.length; i++)
			Arrays.fill(dist[i], Integer.MAX_VALUE);

		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = grid[agent.x][agent.y];
		queue.add(current); // enqueuing first Tile
		dist[current.getX()][current.getY()] = 0;
		// prev.put(current, null);

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			// copy.removeDirt(current.getY(), current.getX());
			for (Tile tile : this.getNeighbors(current)) {
				if (dist[tile.getX()][tile.getY()] == Integer.MAX_VALUE) {
					dist[tile.getX()][tile.getY()] = dist[current.getX()][current.getY()] + 1;
					queue.add(tile);
				}
			}
		}
		return dist;
	}

	public int getDistance(Tile a, Tile b) {
		// Room room =copy();
		int[][] dist = new int[w][h];
		for (int i = 0; i < dist.length; i++)
			Arrays.fill(dist[i], Integer.MAX_VALUE);

		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = this.grid[a.getX()][a.getY()];
		queue.add(current); // enqueuing first Tile
		dist[current.getX()][current.getY()] = 0;

		while (queue.size() != 0 && dist[b.getX()][b.getY()] == Integer.MAX_VALUE) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			for (Tile tile : getNeighbors(current)) {
				if (dist[tile.getX()][tile.getY()] == Integer.MAX_VALUE) {
					dist[tile.getX()][tile.getY()] = dist[current.getX()][current.getY()] + 1;
					queue.add(tile);
				}
			}

		}
		return dist[b.getX()][b.getY()];

	}

	public Room copy() {
		Room copy = new Room(getGridWidth(), getGridHeight());
		for (int i = 0; i < getGridWidth(); i++)
			for (int j = 0; j < getGridHeight(); j++)
				copy.grid[i][j] = this.grid[i][j].copy();
		copy.nbOfDirt = nbOfDirt;
		return copy;
	}

	public boolean equals(Room room) {
		boolean equal = true;
		for (int i = 0; i < getGridHeight(); i++)
			for (int j = 0; j < getGridWidth(); j++)
				if (this.grid[i][j] != room.grid[i][j]) {
					equal = false;
					break;

				}
		return equal;
	}

	public Room cleanRoom() {
		Room cleanedRoom = copy();
		for (int i = 0; i < getGridHeight(); i++)
			for (int j = 0; j < getGridWidth(); j++) {
				if (cleanedRoom.isDirt(i, j))
					cleanedRoom.removeDirt(i, j);
			}
		return cleanedRoom;

	}

	public void clean() {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				removeDirt(i, j);
			}
		}
	}

	public void generateDirt() {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				double threshold = 0.2;
				Tile cur = grid[i][j];
				if (cur.HasWallDown())
					threshold += 0.075;
				if (cur.HasWallUp())
					threshold += 0.075;
				if (cur.HasWallLeft())
					threshold += 0.075;
				if (cur.HasWallRight())
					threshold += 0.075;
				if (threshold == 0.5)
					threshold = 0;
				if (Math.random() < threshold)
					addDirt(i, j);
			}
		}
	}

	public void generateWalls(Agent agent) {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				double a = Math.random();
				if (a > 0.8) {
					if (a < 0.85)
						putWallUp(i, j);
					if (a < 0.9)
						putWallLeft(i, j);
					if (a < 0.95)
						putWallDown(i, j);
					else
						putWallRight(i, j);
				}
			}
		}
		// make sure all tiles are reachable
		ArrayList<Tile> unreachable = new ArrayList<>();
		Tile current;
		do {
			unreachable = new ArrayList<>();
			int[][] dist = findAllDistances(agent);
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist[0].length; j++) {
					if (dist[i][j] == Integer.MAX_VALUE)
						unreachable.add(grid[i][j]);
				}
			}
			if (!unreachable.isEmpty()) {
				Collections.shuffle(unreachable);
				current = unreachable.remove(0);
				ArrayList<Tile> arl = (ArrayList<Tile>) getUnreachableNeighbors(current);
				Collections.shuffle(arl); 	//add some randomness
				boolean flag = true;
				while (flag && !arl.isEmpty() ) {
					Tile unr = arl.remove(0);
					if(!unreachable.contains(unr)) {
						//remove wall between current and unr
						flag = false;
						if(unr.getX()<current.getX()) 
							removeWallLeft(current.getX(), current.getY());
						if(unr.getX()>current.getX()) 
							removeWallRight(current.getX(), current.getY());
						if(unr.getY()<current.getY())
							removeWallUp(current.getX(), current.getY());
						if(unr.getY()>current.getY())
							removeWallDown(current.getX(), current.getY());
					}
				}
			}
		} while (!unreachable.isEmpty());

	}
	
	public int getNbOfReachableDirt(Agent agent) {
		int result = 0;
		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = grid[agent.x][agent.y];
		queue.add(current);
		boolean[][] visited = new boolean[getGridWidth()][getGridHeight()];
		visited[current.getX()][current.getY()] = true;
		while (!queue.isEmpty()) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited
			if (!current.isClean()) 
				result++;
			for (Tile tile : getNeighbors(current)) {
				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
					queue.add(tile);
				}
			}
		}
		return result;
	}

	public void addWallsAtEdges() {
		for (int i = 0; i < w; i++) {
			grid[i][0].putWallUp();
			grid[i][h - 1].putWallDown();
		}
		for (int j = 0; j < h; j++) {
			grid[0][j].putWallLeft();
			grid[w - 1][j].putWallRight();
		}
	}

	@SuppressWarnings("resource")
	public boolean save(String filename) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(this);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("resource")
	public static Room load(String filename) {
		Room input = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
			input = (Room) ois.readObject();

		} catch (Exception e) {
		}

		return input;
	}

	public static void main(String[] args) {
		Room room = new Room(4, 4);
		room.addDirt(0, 3);
		room.addDirt(3, 2);
		room.displayGrid();
		System.out.println();
		// int[][] arr = room.findAllDistances();
		// room.save("room44_1");
		// Room room = Room.load("room44_1");
		// System.out.println(room);
		// room.displayGrid();
		// Room room2 = new Room(4, 4);

		// room2.addDirt(0, 3);
		// room2.addDirt(3, 2);
		// room2.displayGrid();
		// System.out.println(room2.equals(room));
		// System.out.println(room.getI() + " " + room.getJ());
		// System.out.println(room.neighbors());

		// System.out.println(room.neighbors());
		// room.cleanRoom().displayGrid();
		// System.out.println(room.neighbors());
		// for (Room r : room.getChildren()) {

		// r.displayGrid();
		// System.out.println();
		// }
		// room.setI(1); room.setJ(0);
		// System.out.println(room.neighbors()); room.setI(1); room.setJ(1);
		// System.out.println(room.neighbors());

	}

}