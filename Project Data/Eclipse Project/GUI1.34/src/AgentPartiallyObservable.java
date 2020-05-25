
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;


public class AgentPartiallyObservable extends Agent {
	int vis;
	public boolean[][] visited;
	int state;
	int[] delay = { 60, 120, 300, 1800, 3600, 18000, Integer.MAX_VALUE };
	int countdown = 0;
	final int defaultState = -1; // <0 working else idle
	int nbOfDirtIteration;
	//public Room agentRoom;

	public AgentPartiallyObservable(int x, int y, int battery, int w, int h, int vis) {
		super(x, y, battery);
		this.vis = vis;
		this.visited = new boolean[w][h];
	
		nbOfDirtIteration = 0;
		state = -1;
		steps = 0;
		state = defaultState;
		//this.agentRoom= new Room(room.w,room.h);
	}

	public void  resetState() {
		state = defaultState;
		countdown = 0;
		
	}
	
	public void pause(){
		if(state>=0)
			countdown = delay[state];
	}
	
	public int getVis() {
		return vis;
	}

	public void setVis(int vis) {
		this.vis = vis;
	}

	public void displayDiscovered() {
		for (int i = 0; i < visited[0].length; i++) {
			for (int j = 0; j < visited.length; j++) {
				System.out.print(visited[j][i]?1+" ":0 + " ");
			}
			System.out.println();
		}

	}
public ArrayList<Tile> getUnvisitedNeighbors(Room room,Tile tile){
	ArrayList<Tile> list = new ArrayList<Tile>();
	int y = tile.getY();
	int x = tile.getX();

	if (!tile.HasWallDown()) {
		int a = x, b = y + 1;
		if (b > 0 && b < room.getGridHeight()) {
			if(!visited[a][b]) {
			list.add(room.grid[a][b]);
		}}
	}
	if (!tile.HasWallUp()) {
		int a = x, b = y - 1;
		if (b >= 0 && b < room.getGridHeight()) {
			if(!visited[a][b]) {
				list.add(room.grid[a][b]);
			}

		}
	}
	if (!tile.HasWallLeft()) {
		int a = x - 1, b = y;
		if (a >= 0 && a < room.getGridWidth()) {
			if(!visited[a][b]) {
				list.add(room.grid[a][b]);
			}
		}
	}
	if (!tile.HasWallRight()) {
		int a = x + 1, b = y;
		if (a > 0 && a < room.getGridWidth()) {
			if(!visited[a][b]) {
				list.add(room.grid[a][b]);
			}

		}
	}

	return list;

}
public boolean AllScanned(Room room) {
	boolean result = true;
	for(int i=0;i<room.w && result;i++) {
		for(int j=0;j<room.h && result;j++) {
			result&=visited[i][j];
		}
	}
	return result;
}
public boolean isVisible(Tile tile) {
	return (Math.abs(this.x-tile.getX())+Math.abs(this.y-tile.getY()))<=this.vis;
}

public static class BFS {
	public Room room;
	public static Tile[][] prev;

	public static ArrayList<Tile> compute(Room room, AgentPartiallyObservable agent, boolean shuffle) {
		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = room.grid[agent.x][agent.y];
		prev = new Tile[room.getGridWidth()][room.getGridHeight()];
		queue.add(current); // enqueuing first Tile
		// boolean[][] visited = new boolean[room.getGridWidth()][room.getGridHeight()];
		boolean[][] visited = copy2D(agent.visited);
		visited[current.getX()][current.getY()] = true;

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			// if(!agent.isVisible(current)) {
			// agent.visited=copy2D(visited);
			// return backtrack(current);
			// }
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited

			ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);

			if (shuffle)
				Collections.shuffle(neighbors);
			for (Tile tile : neighbors) {
				if (!agent.isVisible(tile)) {
					// prev[tile.getX()][tile.getY()]=current;
					agent.visited = copy2D(visited);
					// return backtrack(tile);
					return null;
				}

				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
					queue.add(tile);
					prev[tile.getX()][tile.getY()] = current;
					if (!tile.isClean()) {
//						agent.visited = copy2D(visited); remove by g
						return backtrack(tile);
					}
				}
			}
		}
		agent.visited=copy2D(visited);
		return null;
	}

	private static boolean[][] copy2D(boolean[][] mat) {
		boolean[][] copy = new boolean[mat.length][mat[0].length];
		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat[0].length; j++) {
				copy[i][j] = mat[i][j];

			}
		return copy;
	}

	public static ArrayList<Tile> backtrack(Tile end) {
		ArrayList<Tile> path = new ArrayList<>();
		path.add(end);
		Tile prevTile = prev[end.getX()][end.getY()];
		while (prevTile != null) {
			path.add(prevTile);
			prevTile = prev[prevTile.getX()][prevTile.getY()];
		}
		Collections.reverse(path);
		return path;
	}
}

public static  class BFS2 {
	public Room room;
	public static Tile[][] prev;

	public static ArrayList<Tile> compute(Room room, AgentPartiallyObservable agent, boolean shuffle) {
		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = room.grid[agent.x][agent.y];
		prev = new Tile[room.getGridWidth()][room.getGridHeight()];
		queue.add(current); // enqueuing first Tile
		boolean[][] visited = new boolean[room.w][room.h];
		//boolean[][] visited= copy2D(agent.visited);
		visited[current.getX()][current.getY()] = true;

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited
			ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);
			if (shuffle)
				Collections.shuffle(neighbors);
			for (Tile tile : neighbors) {
				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
					queue.add(tile);
					prev[tile.getX()][tile.getY()] = current;
					if (!agent.visited[tile.getX()][tile.getY()])
					{	//agent.visited=copy2D(visited);
//						addVisited(agent,visited);	removed by g
						return backtrack(tile);}
				}
			}
		}
		return null;
	}
	private static void addVisited(AgentPartiallyObservable agent, boolean[][] visited) {
		for(int i=0;i<visited.length;i++) {
			for(int j=0;j<visited[0].length;j++) {
				if(visited[i][j]) {
					agent.visited[i][j]=true;
				}
			}
		}
		
	}
	private static boolean[][] copy2D(boolean[][] mat) {
		boolean[][] copy = new boolean[mat.length][mat[0].length];
		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat[0].length; j++) {
				copy[i][j] = mat[i][j];

			}
		return copy;
	}

	private static ArrayList<Tile> backtracktoLimit(Tile tile, AgentPartiallyObservable agent) {
		ArrayList<Tile> results = new ArrayList<>();
		ArrayList<Tile> steps = backtrack(tile);
		Tile ntile = null;
		if (steps != null) {
			for (int i = 0; i <= agent.vis && !steps.isEmpty(); i++) {
				ntile = steps.remove(0);
				agent.visited[ntile.getX()][ntile.getY()] = true;
				results.add(ntile);

			}
		}

		return results;
	}

	public static ArrayList<Tile> backtrack(Tile end) {
		ArrayList<Tile> path = new ArrayList<>();
		path.add(end);
		Tile prevTile = prev[end.getX()][end.getY()];
		while (prevTile != null) {
			path.add(prevTile);
			prevTile = prev[prevTile.getX()][prevTile.getY()];
		}
		Collections.reverse(path);
		return path;
	}
}

}