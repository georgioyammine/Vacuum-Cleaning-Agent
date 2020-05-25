import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AgentPartially extends Agent {
	int nbOfDirtIteration;
	Room agentRoom;
	int state;
	int[] delay = { 60, 120, 300, 1800, 3600, 18000, Integer.MAX_VALUE };
	int countdown = 0;
	final int defaultState = -1; // <0 working else idle
	boolean[][] visited;
	Tile current;
	int steps;
	int depth = 1;
	HashMap<Tile, Integer> hm;
	ArrayList<ArrayList<Object>> arl;

	public AgentPartially(int x, int y, int battery, Room room) {
		super(x, y, battery);
		agentRoom = new Room(room.w, room.h);
		nbOfDirtIteration = 0;
		state = -1;
		current = new Tile(x, y);
		steps = 0;
		visited = new boolean[room.w][room.h];
		state = defaultState;
	}
	
	public void  resetState() {
		state = defaultState;
		countdown = 0;
		
	}
	
	public void resetMemory() {
		arl = new ArrayList<ArrayList<Object>>();
		visited = new boolean[agentRoom.getGridWidth()][agentRoom.getGridHeight()];
		resetState();
	}
	public void pause(){
		if(state>=0)
			countdown = delay[state];
	}

	public boolean AllScanned() {
		boolean result = true;
		for (int i = 0; i < agentRoom.w && result; i++) {
			for (int j = 0; j < agentRoom.h && result; j++) {
				result &= visited[i][j];
			}
		}
		return result;
	}

	public double getWeight(Tile tile) {
		double result = 0;
		for (Tile a : agentRoom.getNeighbors(tile)) {
			if (!visited[a.getX()][a.getY()]) {
				result++;
				result += (isCorner(a, agentRoom) ? 4 : 0);
			} else {
				result += 0.1;
			}
		}
		return result;
	}

	public boolean isCorner(Tile a, Room r) {
		if (a.getX() == 0 || a.getX() == r.w - 1)
			if (a.getY() == 0 || a.getY() == r.h - 1)
				return true;
		return false;
	}

	public Tile getNextPos() {
		arl = new ArrayList<>();
		Tile current = agentRoom.grid[x][y];
		for (Tile tile : agentRoom.getNeighbors(current)) {
			if (!visited[tile.getX()][tile.getY()]) {
				ArrayList<Object> arl2 = new ArrayList<>();
				arl2.add(tile);
				arl2.add(10 + (getWeight(tile) + (tile.isClean() ? 0 : 10) - (visited[tile.getX()][tile.getY()] ? 10 : 0)));
				arl.add(arl2);
			}

		}
//		 System.out.println(arl);

//		Collections.shuffle(arl);
		if (!arl.isEmpty()) {
			Collections.sort(arl, new Comparator<ArrayList<Object>>() {
				@Override
				public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
					return ((Double) o2.get(1)).compareTo((Double) o1.get(1));
				}
			});
//			 System.out.println(arl);
			return (Tile) arl.get(0).get(0);
		}
		else { // do bfs to next unknown state and get next step
			if(!AllScanned())
				return BFS.compute(agentRoom, this, false).get(1);
			else {
				
				resetMemory();
				return agentRoom.grid[x][y];
			}
			
			
		}
	}

	
	public static class BFS {
		public Room room;
		public static Tile[][] prev;
		
		public static ArrayList<Tile> compute(Room room, AgentPartially agent, boolean shuffle) {
			Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
			Tile current = room.grid[agent.x][agent.y];
			prev = new Tile[room.getGridWidth()][room.getGridHeight()];
			queue.add(current); // enqueuing first Tile
			boolean[][] visited = new boolean[room.w][room.h];
			visited[current.getX()][current.getY()] = true;
			
			while (queue.size() != 0) { // Loop Until: queue is empty
				current = queue.remove(); // Dequeue current index
				visited[current.getX()][current.getY()] = true; // Mark the start index as visited
				ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);
				if(shuffle)
					Collections.shuffle(neighbors);
				for (Tile tile : neighbors) {
					if (!visited[tile.getX()][tile.getY()]) {
						visited[tile.getX()][tile.getY()] = true;
						queue.add(tile);
						prev[tile.getX()][tile.getY()]=current;
						if(!agent.visited[tile.getX()][tile.getY()]) 
							return backtrack(tile);
					}
				}
			}
			return null;
		}

		public static ArrayList<Tile> backtrack(Tile end) {
			ArrayList<Tile> path = new ArrayList<>();
			path.add(end);
			Tile prevTile = prev[end.getX()][end.getY()];
			while(prevTile!=null) {
				path.add(prevTile);
				prevTile = prev[prevTile.getX()][prevTile.getY()];
			}
			Collections.reverse(path);
			return path;
		}
	}


	public boolean hasDirtArround() {
		if(!agentRoom.grid[x][y].isClean())
			return true;
		for(Tile a : agentRoom.getNeighbors(agentRoom.grid[x][y]))
			if(!a.isClean())
				return true;
		return false;
	}

}