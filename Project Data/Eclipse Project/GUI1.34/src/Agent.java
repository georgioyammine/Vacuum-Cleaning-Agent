

public class Agent {
	int x,y,battery,performance, timeOn, steps;
	
	public Agent(int x, int y, int battery) {
		super();
		this.x = x;
		this.y = y;
		this.battery = battery;
		this.performance = 0;
		timeOn = -1;
		steps = 0;
	}
	
	public boolean getIdentitity() {
		return true;
	}
	
	public Agent() {
		
	}

	public void addToPerformance(int a) {
		performance+=a;
	}
	
	public void setPosition(int x,int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}


	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	public int getPerformance() {
		return performance;
	}
	
}