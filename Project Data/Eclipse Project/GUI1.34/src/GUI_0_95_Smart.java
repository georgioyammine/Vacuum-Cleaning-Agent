
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GCScheme;
import g4p_controls.GCheckbox;
import g4p_controls.GCustomSlider;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GTextField;
import processing.core.PApplet;
import processing.core.PImage;

public class GUI_0_95_Smart extends PApplet {
	PImage robot, dirt;
	int x, y;
	Room start;
	float k, kold = -1, koldR = -1;
	int pWidth, pHeight; // size of window
	ArrayList<Tile> results = new ArrayList<>(), tspResults = new ArrayList<>();
	boolean Run = false, idle = false;
	boolean wallPressed = false, dirtPressed = false, agentPressed = false, useBfs = false;
	boolean first = true;
	int delayOriginal = 0;
	int delay = delayOriginal;
	Agent agent;
	int appTime = -1, fc = -1, reset = 0, tint = 0;;
	Timer t;
	boolean empty = true, pause = true;

	public void setup() {
		agent = new Agent(0, 0, 100000);
		

		frameRate(30);
		pWidth = 0;
		pHeight = 0;
		x = 6; // default height and width of the room
		y = 5;

		robot = loadImage("robot.png");
		dirt = loadImage("dirt.png");
		registerMethod("pre", this);
		surface.setResizable(true);
		surface.setTitle("TSP Agent");
		surface.setIcon(loadImage("icon.png"));
		start = new Room(x, y);
		createGUI();
		startTimer();
	}

	public void startTimer() {
		if (t != null)
			t.cancel();
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if (agent.battery > 0 && startBtn != null && startBtn.getText() == "Stop Agent" && Run == false)
					start_click(null, GEvent.PRESSED);
				if (Run) {
					if (!idle) {
						if (pause) {
							agent.performance--;
							agent.battery--;
							agent.timeOn++;
						} else {
							int temp = 8 - (delayOriginal == 0 ? 0
									: delayOriginal == 1 ? 4 : delayOriginal == 3 ? 7 : delayOriginal == 7 ? 8 : 9);
							if (temp == -1) {
								agent.performance -= 2;
								agent.battery -= 2;
								agent.timeOn += 2;
								appTime +=2;
							}
							if (tint++ == temp) {
								tint = 0;
								agent.performance--;
								agent.battery--;
								agent.timeOn++;
								appTime++;
							}
						}

					}
					if (agent.battery <= 0) {
						agent.battery = 0;
						Run = false;
					}
				}
				surface.setTitle("[Fully Observable Environement Agents] Smart Agent (combination of TSP and BFS) " + nf(appTime / 60, 2) + ":"
						+ nf(appTime == -1 ? 0 : appTime % 60, 2));
				if(pause)
					appTime++;
			}
		}, 0, (long) (125 * (1 + delayOriginal)));
	}

	public void draw() {
		x = start.getGridWidth();
		y = start.getGridHeight();

		background(209);
		k = (float) Math.min(((height - 1.0) / y), (width - 150.0) / x);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				fill(255);
				rect(i * k, j * k, k, k);
				if (!start.grid[i][j].isClean()) {
					if (k < 5) {
						fill(200, 200, 0);
						rect(i * k, j * k, k, k);
					} else {
						// Choosing the picture size based on the number of grids
						if (kold != k) {
							if (x * y < 41)
								dirt = loadImage("dirt.png");
							else if (x * y < 81)
								dirt = loadImage("dirt50%c.png");
							else if (x * y < 401)
								dirt = loadImage("dirt16%c.png");
							else
								dirt = loadImage("dirt10%c.png");

							dirt.resize((int) k, (int) k);
						}
						kold = k;
						image(dirt, i * k, j * k);
					}

				}

				if (start.grid[i][j].hasWalls()) {
					fill(0, 0, 0);
					float wallWidth = (float) (k / 26.0);
					if (start.grid[i][j].HasWallUp())
						rect(i * k, j * k, k, wallWidth);
					if (start.grid[i][j].HasWallDown())
						rect(i * k, (j + 1) * k - wallWidth, k, wallWidth);
					if (start.grid[i][j].HasWallLeft())
						rect(i * k, j * k, wallWidth, k);
					if (start.grid[i][j].HasWallRight())
						rect((i + 1) * k - wallWidth, j * k, wallWidth, k);
				}

			}
		}

		// fill(0, 0, 150);
		// arc((o + 0.5f) * k, (u + 0.5f) * k, k * 0.9f, k * 0.9f, QUARTER_PI / 2, PI +
		// 7 * QUARTER_PI / 2);

		if (koldR != k) {
			robot = loadImage("robot.png");
			robot.resize((int) k, (int) k);
		}
		koldR = k;
		image(robot, agent.x * k, agent.y * k);

		if (Run) {
			try {
				if (delay-- == 0) {
					thread("Run");
					delay = delayOriginal;
				}
			} catch (Exception e) {
			}
		}
		if (Run) {
			fill(0, 220, 0);
			if (agent.battery < 30)
				fill(210 - 5 * agent.battery, 7 * agent.battery, 0);
		} else {
			fill(155, 155, 0);
			if (agent.battery < 30)
				fill(210 - 5 * agent.battery, 7 * agent.battery, 0);
		}

		rect((float) (width - 147), (float) startBtn.getY() + 35, 144, 103, 7);
		fill(0);

		float val = (float) startBtn.getY() + 53;
		textSize(9f);
		text(" (hold to reset)", (float) (width - 69), val - 1);
		textSize(14.5f);
		text("Agent: " + (!pause?"Computing":Run ? idle ? "Idle" : "On" : "Off"), (float) (width - 142), val);
		text("Performance:" + agent.performance, (float) (width - 142), val + 20);
		text("Steps: " + agent.steps, (float) (width - 142), val + 40);
		text("Battery: " + agent.battery, (float) (width - 142), val + 60);
		text("Time On: " + nf(agent.timeOn / 60, 2) + ":" + nf(agent.timeOn == -1 ? 0 : agent.timeOn % 60, 2),
				(float) (width - 142), val + 80);

		if (fc != -1 && (frameCount - fc) / frameRate >= 1) {
			reset();
			fc = -1;
		}
	}

	public void Run() {
		int nbd = start.getNbOfReachableDirt(agent);
		int nbw = start.getNbOfWalls();
		try {
			if (nbd > 0) {
				idle = false;
				if (start.isDirt(agent.x, agent.y)) {
					start.removeDirt(agent.x, agent.y);
					agent.performance += 100;
					agent.battery -= 20;
					if (agent.battery <= 0) {
						agent.battery = 0;
						Run = false;
					}
				} else {
					if (results.isEmpty()) {
						if (!useBfs && (nbd <= 100 || (nbd < 300 && nbw / (double) (x + y) > 0.4)) 
								&& !(nbd >= x * y - 1 && nbw == 0)) { // conditions to use TSP
							useBfs = false;
							if (pause && start.nbOfDirt() != 0) {
								pause = false;
								// long t1 = System.currentTimeMillis();
								if (tspResults.size() <= 1) {
									int temp = 100000;
									double ratio = 0.0001;
									if (start.nbOfDirt() > 20) {
										if (start.nbOfDirt() < 30) {
											temp = 100000000;
											ratio = 0.00001;
										}
										if (start.nbOfDirt() < 60) {
											temp = 1000000000;
											ratio = 0.000005;
										} else {
											temp = Integer.MAX_VALUE;
											ratio = 0.000002;
										}
									}
									int copyReset = reset;
									ArrayList<Tile> arl = SA_TSP.compute(start, agent, temp, ratio);
									if (reset == copyReset) // the result above may take lots of time, this reset is
															// used to
															// not
															// take it after computation in case
										tspResults = arl; // resets occurred in middle of a computation; reset can be
															// detected
															// if reset!copyReset befor computation

								}
								if (tspResults.size() > 1) {
									if (!tspResults.get(1).isClean())
										results = SA_TSP.BFS_SA.compute(start, tspResults.remove(0), tspResults.get(0));
									else
										tspResults.remove(1);
								}
								pause = true;
								// System.out.println(System.currentTimeMillis()-t1);

							}
						} else { // do BFS
							useBfs = true;
							results = BFS.compute(start, agent, false); // true to shuffle the neighbours before adding
						}
					}
					// pause = true;
					if (results != null && !results.isEmpty()) {
						Tile tile = (Tile) results.remove(0);
						int xold = agent.x, yold = agent.y;
						updateAgent(tile.getX(), tile.getY());
						if (agent.x != xold || agent.y != yold) {
							agent.steps++;
							agent.performance -= 10;
							agent.battery -= 10;
							if (agent.battery <= 0) {
								agent.battery = 0;
								Run = false;
							}

						}
					}
				}
			} else {
				idle = true;
				useBfs = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			results = new ArrayList<>();
		}
	}

	public void addRemoveWall(boolean addRemove, int mouseX, int mouseY, boolean clicked) {
		results = new ArrayList<>();
		tspResults = new ArrayList<>();
		useBfs = false;
		double a = mouseX / (double) k, b = mouseY / (double) k;
		int x = (int) Math.round(a), y = (int) Math.round(b);
		if (clicked || (Math.abs(a - x) > 0.2 || Math.abs(b - y) > 0.2)) {
			if (Math.abs(a - x) <= Math.abs(b - y)) {
				try {
					if (addRemove)
						start.putWallLeft(x, (int) Math.floor(b));
					else
						start.removeWallLeft(x, (int) Math.floor(b));
				} catch (Exception e) {
					if (x > 0) {
						if (addRemove)
							start.putWallRight(x - 1, (int) Math.floor(b));
						else
							start.removeWallRight(x - 1, (int) Math.floor(b));
					}
				}
				;
			} else {
				try {
					if (addRemove)
						start.putWallUp((int) Math.floor(a), y);
					else
						start.removeWallUp((int) Math.floor(a), y);
				} catch (Exception e) {
					if (y > 0) {
						if (addRemove)
							start.putWallDown((int) Math.floor(a), y - 1);
						else
							start.removeWallDown((int) Math.floor(a), y - 1);
					}
				}
				;
			}
		}
	}

	public void mouseReleased() {
		fc = -1;
	}

	public void mousePressed() {
		if (mouseX > (float) (width - 147) && mouseY > (float) startBtn.getY() + 35 && mouseX < (float) (width - 3)
				&& mouseY < (float) startBtn.getY() + 138) {
			fc = frameCount;
		} else
			fc = -1;
		int mxk = (int) (mouseX / k), myk = (int) (mouseY / k);
		if (myk < y && mxk < x && myk >= 0 && mxk >= 0) {

			if (wallPressed) {
				if (mouseButton == LEFT) {
					addRemoveWall(true, mouseX, mouseY, true);
				} else {
					if (mouseButton == RIGHT) {
						addRemoveWall(false, mouseX, mouseY, true);
					}
				}
			} else {
				if (dirtPressed) {
					if (mouseButton == LEFT) {
						start.addDirt(mxk, myk);
						useBfs = false;
					}
					else {
						if (mouseButton == RIGHT) {
							start.removeDirt(mxk, myk);
							useBfs = false;
						}
					}
				}

			}
			if (agentPressed) {
				agent.x = mxk;
				agent.y = myk;

				if (mouseButton == RIGHT)
					start.removeDirt(mxk, myk);

			}
		}
	}

	public void mouseDragged() {
		double mxk = mouseX / (double) k, myk = mouseY / (double) k;
		if (myk < y && mxk < x && myk >= 0 && mxk >= 0) {

			if (wallPressed) {
				if (mouseButton == LEFT)
					addRemoveWall(true, mouseX, mouseY, false);
				else {
					addRemoveWall(false, mouseX, mouseY, false);
				}
			} else {
				if (dirtPressed) {
					if (mouseButton == LEFT) {
						start.addDirt((int) mxk, (int) myk);
						useBfs = false;
					}
					else {
						if (mouseButton == RIGHT) {
							start.removeDirt((int) mxk, (int) myk);
							useBfs = false;
						}
					}
				}
			}
			if (agentPressed) {
				agent.x = (int) mxk;
				agent.y = (int) myk;
			}

		}
	}

	public void updateAgent(int x, int y) {
		agent.setPosition(x, y);
	}

	public void pre() {
		if (pWidth != width || pHeight != height) {
			float nx, ny; // used to calculate new control positions
			// Window has been resized so
			nx = width - wallBtn.getWidth() - 1;
			ny = (float) (8);

			nx = width - wallBtn.getWidth() - 1;
			loadBtn.moveTo(nx, ny);
			nx = nx - dirtBtn.getWidth() - 5;
			saveBtn.moveTo(nx, ny);

			ny = ny + loadBtn.getHeight() + 5;
			xField.moveTo(nx + 1, ny);

			nx = width - xField.getWidth() - 2;
			yField.moveTo(nx - 1, ny);

			nx = width - submitBtn.getWidth() - 1;
			ny += yField.getHeight() + 5;
			submitBtn.moveTo(nx, ny);

			ny += submitBtn.getHeight() + 4;
			nx = width - generateBtn.getWidth() - 1;
			generateBtn.moveTo(nx, ny);
			nx = width - submitBtn.getWidth() - 1;
			roomBox.moveTo(nx, ny - 6);
			dirtBox.moveTo(nx, ny + 10);
			wallsBox.moveTo(nx, ny + 26);

			nx = width - wallBtn.getWidth() - 1;
			ny += generateBtn.getHeight() + 3;
			dirtBtn.moveTo(nx, ny);

			nx = nx - dirtBtn.getWidth() - 5;
			wallBtn.moveTo(nx, ny);
			// helpText.moveTo(nx, ny - wallBtn.getHeight() - 8);

			ny += wallBtn.getHeight() + 5;
			batteryField.moveTo(nx, ny);
			nx = width - wallBtn.getWidth() - 1;
			submitB.moveTo(nx, ny);
			ny = ny + saveBtn.getHeight() + 5;
			nx = width - agentLoc.getWidth() - 1;
			startBtn.moveTo(nx, ny);
			nx = nx - agentLoc.getWidth() - 5;
			agentLoc.moveTo(nx, ny);

			ny += submitBtn.getHeight() + 5;

			speedSlider.moveTo(nx, height - speedSlider.getHeight() - 1);
			speed.moveTo(nx + 46, speedSlider.getCY() - speedSlider.getHeight() + 2);
			// // save current widow size
			pWidth = width;
			pHeight = height;
		}
	}

	public void updateBtnColor() {
		if (wallPressed) {
			wallBtn.setLocalColorScheme(GCScheme.GREEN_SCHEME);
			dirtBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
			agentLoc.setLocalColorScheme(GCScheme.BLUE_SCHEME);
		}
		if (dirtPressed) {
			wallBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
			dirtBtn.setLocalColorScheme(GCScheme.GREEN_SCHEME);
			agentLoc.setLocalColorScheme(GCScheme.BLUE_SCHEME);
		}
		if (agentPressed) {
			wallBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
			dirtBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
			agentLoc.setLocalColorScheme(GCScheme.YELLOW_SCHEME);
		}

	}

	public void start_click(GButton source, GEvent event) {
		if (event == GEvent.PRESSED) {
			results = new ArrayList<>();
			tspResults = new ArrayList<>();
			Run = !Run;
			if (Run) {
				startBtn.setText("Stop Agent");
				startBtn.setTextBold();

			} else {
				startBtn.setText("Start Agent");
				startBtn.setTextBold();
			}
		}
	}

	public void reset() {
		noLoop();
		agent.performance = 0;
		agent.battery = 100000;
		agent.timeOn = -1;
		agent.steps = 0;
		if (!pause && tspResults.isEmpty())
			pause = true;
		reset++;
		loop();
	}

	public void wall_click(GButton source, GEvent event) {
		wallPressed = true;
		dirtPressed = false;
		agentPressed = false;
		updateBtnColor();
	}

	public void dirt_click(GButton source, GEvent event) {
		dirtPressed = true;
		wallPressed = false;
		agentPressed = false;
		updateBtnColor();
	}

	public void agent_click(GButton source, GEvent event) {
		agentPressed = true;
		wallPressed = false;
		dirtPressed = false;
		updateBtnColor();
	}

	public void xField_change(GTextField source, GEvent event) {
	}

	public void yField_change(GTextField source, GEvent event) {
	}

	public void submit_click(GButton source, GEvent event) {
		try {
			x = Integer.parseInt(xField.getText());
			y = Integer.parseInt(yField.getText());
			background(209);
			agent = new Agent(0, 0, 1000);
			results = new ArrayList<>();
			tspResults = new ArrayList<>();
			start = new Room(x, y);

		} catch (Exception e) {
			println("Wrong input");
		}
	}

	public void save_click(GButton source, GEvent event) {
		if (event == GEvent.PRESSED)
			selectOutput("Select a file name:", "outputFileSelected");
	}

	public void outputFileSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected " + selection.getAbsolutePath());
			start.save(selection.getAbsolutePath() + ".room");
		}
	}

	public void load_click(GButton source, GEvent event) {
		if (event == GEvent.PRESSED)
			selectInput("Select a room:", "inputFileSelected");
	}

	public void inputFileSelected(File selection) {
		noLoop();
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected " + selection.getAbsolutePath());
			try {
				Room r = Room.load(selection.getAbsolutePath());
				if (r != null) {
					noLoop();
					start = r;
					agent = new Agent(0, 0, 100000);
					results = new ArrayList<>();
					tspResults = new ArrayList<>();
					x = start.w;
					y = start.w;
					loop();
					reset();
				} else
					println("Wrong Input");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		loop();
	}

	public void speedSlider_changed(GCustomSlider source, GEvent event) {
		if ((source.getValueF() == (int) source.getValueF())) {
			int newDelay = source.getValueI();
			if (newDelay == 4)
				newDelay = 0;
			else {
				if (newDelay == 3)
					newDelay = 1;
				else {
					if (newDelay == 2)
						newDelay = 3;
					else {
						if (newDelay == 1)
							newDelay = 7;
						else
							newDelay = 15;
					}
				}
			}
			if (newDelay != delayOriginal) {
				delayOriginal = newDelay;
				startTimer();
			}
		}
	}

	public void setBattery(GButton source, GEvent event) {
		try {
			int a = Integer.parseInt(batteryField.getText());
			agent.battery = a;
		} catch (NumberFormatException e) {
			println("Wrong battery value");
		}
	}

	public void generate(GButton source, GEvent event) {
		noLoop();
		boolean restart = Run;
		Run = false;
		if (!roomBox.isSelected() && !dirtBox.isSelected() && !wallsBox.isSelected())
			start.clean();
		else {
			if (roomBox.isSelected()) {
				Random random = new Random();
				agent = new Agent(0, 0, 1000);
				start = new Room(random.nextInt(20) + 1, random.nextInt(20) + 1);
				reset();
			}
			if (wallsBox.isSelected())
				start.generateWalls(agent);
			if (dirtBox.isSelected())
				start.generateDirt();

		}
		agent = new Agent(0, 0, 1000);
		results = new ArrayList<>();
		tspResults = new ArrayList<>();
		reset();

		loop();
		Run = restart;
	}

	// Create all the GUI controls.
	public void createGUI() {
		G4P.messagesEnabled(false);
		G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
		G4P.setMouseOverEnabled(false);
		wallBtn = new GButton(this, 311, 27, 70, 26);
		wallBtn.setText("Wall");
		wallBtn.setTextBold();
		wallBtn.fireAllEvents(true);
		wallBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
		wallBtn.addEventHandler(this, "wall_click");
		dirtBtn = new GButton(this, 391, 27, 70, 26);
		dirtBtn.setText("Dirt");
		dirtBtn.setTextBold();
		dirtBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
		dirtBtn.fireAllEvents(true);
		dirtBtn.addEventHandler(this, "dirt_click");
		agentLoc = new GButton(this, 309, 67, 70, 30);
		agentLoc.setText("Agent Location");
		agentLoc.setTextBold();
		agentLoc.addEventHandler(this, "agent_click");
		agentLoc.fireAllEvents(true);
		xField = new GTextField(this, 308, 107, 60, 20, G4P.SCROLLBARS_NONE);
		xField.setPromptText("Set X");
		xField.setOpaque(false);

		xField.addEventHandler(this, "xField_change");
		yField = new GTextField(this, 401, 107, 60, 20, G4P.SCROLLBARS_NONE);
		yField.setPromptText("Set Y");
		yField.setOpaque(true);
		yField.addEventHandler(this, "yField_change");
		submitBtn = new GButton(this, 356, 136, 145, 26);
		submitBtn.setText("Create new Room");
		submitBtn.setTextBold();
		submitBtn.setLocalColorScheme(GCScheme.GOLD_SCHEME);
		submitBtn.addEventHandler(this, "submit_click");
		// submitBtn.mouseEvent();
		startBtn = new GButton(this, 343, 176, 70, 30);
		submitBtn.fireAllEvents(true);
		startBtn.fireAllEvents(true);
		startBtn.setText("Start Agent");
		startBtn.setTextBold();
		startBtn.setLocalColorScheme(GCScheme.ORANGE_SCHEME);
		startBtn.addEventHandler(this, "start_click");
		saveBtn = new GButton(this, 356, 136, 70, 30);
		saveBtn.setText("Save Room");
		saveBtn.setTextBold();
		saveBtn.setLocalColorScheme(GCScheme.GREEN_SCHEME);
		saveBtn.addEventHandler(this, "save_click");
		saveBtn.fireAllEvents(true);
		loadBtn = new GButton(this, 356, 136, 70, 30);
		loadBtn.setText("Load Room");
		loadBtn.setTextBold();
		loadBtn.setLocalColorScheme(GCScheme.RED_SCHEME);
		loadBtn.addEventHandler(this, "load_click");
		loadBtn.fireAllEvents(true);
		// helpText = new GLabel(this, 391, 10, 140, 30, "Left Click to add \nRight
		// Click to Remove");
		// System.out.print("\033[H\033[2J");
		// System.out.flush();
		// println("Starts here: ");

		speedSlider = new GCustomSlider(this, 343, 176, 140, 60, "grey_blue");
		speedSlider.setLimits(0.5f, 1, 4);
		String[] tickLabels = { "0.5x", "1x", "2x", "4x", "8x" };
		speedSlider.setTickLabels(tickLabels);
		speedSlider.addEventHandler(this, "speedSlider_changed");
		speedSlider.setValue(1.0f);
		speed = new GLabel(this, 343, 176, 140, 60, "Speed");
		speed.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		batteryField = new GTextField(this, 308, 107, 70, 30, G4P.SCROLLBARS_NONE);
		batteryField.setPromptText("Set Battery");
		batteryField.setOpaque(false);
		submitB = new GButton(this, 356, 136, 70, 31);
		submitB.setText("Recharge Agent");
		submitB.setTextBold();
		submitB.fireAllEvents(true);
		submitB.addEventHandler(this, "setBattery");
		roomBox = new GCheckbox(this, 356, 136, 70, 30, "Room");
		dirtBox = new GCheckbox(this, 356, 136, 70, 30, "Dirt");
		wallsBox = new GCheckbox(this, 356, 136, 70, 30, "Walls");
		generateBtn = new GButton(this, 356, 136, 70, 50, "Generate Selection or Clean");
		generateBtn.setTextBold();
		generateBtn.setLocalColorScheme(GCScheme.PURPLE_SCHEME);
		generateBtn.addEventHandler(this, "generate");
		

	}

	// Variable declarations
	GCustomSlider speedSlider;
	GLabel speed, helpText;
	GButton wallBtn, dirtBtn, agentLoc, submitBtn, startBtn, saveBtn, loadBtn, submitB, generateBtn;
	GTextField xField, yField, batteryField;
	GCheckbox roomBox, dirtBox, wallsBox;

	public void settings() {
		size(800, 430);
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "GUI_0_95_Smart" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

}