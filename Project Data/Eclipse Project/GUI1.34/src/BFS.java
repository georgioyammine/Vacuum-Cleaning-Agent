import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {
	public Room room;
	public static Tile[][] prev;
	
	public static ArrayList<Tile> compute(Room room, Agent agent, boolean shuffle) {
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
			if(shuffle)
				Collections.shuffle(neighbors);
			for (Tile tile : neighbors) {
				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
					queue.add(tile);
					prev[tile.getX()][tile.getY()]=current;
					if(!tile.isClean()) 
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