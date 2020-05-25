import java.util.ArrayList;
import java.util.Stack;

public class AgentUnobservableDiscover extends Agent{
	int nbOfDirtIteration;
	Room agentRoom;
	int state;
	int countdown =0;
	Stack<Tile> stack = new Stack<>(); // create the LinkedList and using it as a queue
	ArrayList<Tile> queue;
	Tile[][] prev; 
	boolean[][] visited;
	boolean updated;
	Tile current ;
	int steps;
//	visited[current.getX()][current.getY()] = true;
	
	public AgentUnobservableDiscover(int x, int y, int battery, Room room) {
		super(x, y,battery);
		agentRoom = new Room(room.w, room.h);
		nbOfDirtIteration = 0;
		visited = new boolean[room.getGridWidth()][room.getGridHeight()];
		prev = new Tile[room.getGridWidth()][room.getGridHeight()];
		queue = new ArrayList<>();
		updated = true;
		current = new Tile(x, y);
		steps = 0;
	}
	
	public boolean AllScanned() {
		boolean result = true;
		for(int i=0;i<agentRoom.w && result;i++) {
			for(int j=0;j<agentRoom.h && result;j++) {
				result&=visited[i][j];
			}
		}
		return result;
	}
	

}