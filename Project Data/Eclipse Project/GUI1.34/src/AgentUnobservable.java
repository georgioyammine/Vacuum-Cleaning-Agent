import java.util.ArrayList;

public class AgentUnobservable extends Agent{
	int nbOfDirtIteration;
	Room agentRoom;
	ArrayList<Tile> road;
	int[] delay = {60,120,300,1800,3600,18000,Integer.MAX_VALUE};
	final int defaultState = -2; // <0 working else idle
	int state;
	int countdown =0;
	int steps;
	
	public AgentUnobservable(int x, int y, int battery, Room room) {
		super(x, y,battery);
		agentRoom = room;
		road = new ArrayList<>();
		nbOfDirtIteration = 0;
		state = defaultState;
		steps = 0;
		state = defaultState;
	}
	

	public void pause() {
		countdown = delay[state];
	}
}