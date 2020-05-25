import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class AgentwithIdentity extends Agent {
	public boolean identity;

	public AgentwithIdentity(int x, int y, int battery, boolean identity) {
		super(x, y, battery);
		this.identity = identity;
	}

	public boolean getIdentity() {
		return identity;
	}

	public void setIdentity(boolean identity) {
		this.identity = identity;
	}
	public boolean hasAgent(int x, int y, ArrayList<AgentwithIdentity> agents) {
		for (Agent a : agents)
			if (a.getX() == x && a.getY() == y)
				return true;
		return false;
	}

	public boolean hasAdvAgent(int x, int y, ArrayList<AgentwithIdentity> agents) {
		for (AgentwithIdentity a : agents)
			if ((identity ^ a.getIdentity()) && a.getX() == x && a.getY() == y)
				return true;
		return false;
	}
	public Room room;
	public Tile[][] prev;

	public ArrayList<Tile> compute(Room room, Agent agent, boolean shuffle, ArrayList<AgentwithIdentity> agents) {
		Queue<Tile> queue = new LinkedList<>(); // creates the LinkedList and using it as a queue
		Tile current = room.grid[agent.x][agent.y];
		prev = new Tile[room.getGridWidth()][room.getGridHeight()];
		queue.add(current); // enqueuing first Tile
		boolean[][] visited = new boolean[room.getGridWidth()][room.getGridHeight()];
		visited[current.getX()][current.getY()] = true;

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited
			ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);

			if (shuffle)
				Collections.shuffle(neighbors);
			for (Tile tile : neighbors) {
				if (!hasAgent(tile.getX(), tile.getY(), agents))
					if (!visited[tile.getX()][tile.getY()]) {
						visited[tile.getX()][tile.getY()] = true;
						queue.add(tile);
						prev[tile.getX()][tile.getY()] = current;
						if ((identity ^ tile.isClean()) && !hasAgent(tile.getX(), tile.getY(), agents))
							return backtrack(tile);
					}
			}
		}
		return BFStoAdv(room, agent, false, agents);
	}

	public ArrayList<Tile> backtrack(Tile end) {
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


	public ArrayList<Tile> BFStoAdv(Room room, Agent agent, boolean shuffle, ArrayList<AgentwithIdentity> agents) {
		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = room.grid[agent.x][agent.y];
		prev = new Tile[room.getGridWidth()][room.getGridHeight()];
		queue.add(current); // enqueuing first Tile
		boolean[][] visited = new boolean[room.getGridWidth()][room.getGridHeight()];
		visited[current.getX()][current.getY()] = true;

		while (queue.size() != 0) { // Loop Until: queue is empty
			current = queue.remove(); // Dequeue current index
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited
			ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);
			if (shuffle)
				Collections.shuffle(neighbors);
			for (Tile tile : neighbors) {
				if (!visited[tile.getX()][tile.getY()]) {
					if (!hasAgent(tile.getX(), tile.getY(), agents)) {
						visited[tile.getX()][tile.getY()] = true;
						queue.add(tile);
						prev[tile.getX()][tile.getY()] = current;
					} else {
						if (hasAdvAgent(tile.getX(), tile.getY(), agents)) {
							prev[tile.getX()][tile.getY()] = current;
							return backtrack(tile);
						}
					}
				}
			}
		}
		return null;

	}
}
