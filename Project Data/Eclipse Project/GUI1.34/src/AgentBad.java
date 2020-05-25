
public class AgentBad extends Agent {
	public AgentBad(int x, int y, int battery) {
		super(x, y, battery);

	}
	
	public boolean getIdentitity() {
		return false;
	}
}