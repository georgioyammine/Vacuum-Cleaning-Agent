import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class SA_TSP {

	public static void main(String[] args) {

		Room room = new Room(20, 20);
		room.addDirt(0, 0);
		room.addDirt(3, 3);
		room.addDirt(2, 3);
		room.addDirt(2, 2);
		room.addDirt(4, 0);
		room.addDirt(3, 4);
		room.addDirt(5, 3);
		room.addDirt(1, 0);

		room.addDirt(10, 0);
		room.addDirt(3, 13);
		room.addDirt(2, 13);
		room.addDirt(2, 12);
		room.addDirt(4, 10);
		room.addDirt(13, 4);
		room.addDirt(15, 3);
		room.addDirt(1, 10);
		System.out.println(room.nbOfDirt());
		ArrayList<Tile> arl2;
		// ArrayList<Tile> arl2 = compute(room, new Agent(1, 1, 0));
		// System.out.println(arl2);
		// System.out.println(arl2.size());
		arl2 = solve(room, new Agent(1, 1, 0), 100000, 0.001);
		System.out.println(arl2);
		System.out.println(arl2.size());
	}

	public static ArrayList<Tile> solve(Room room, Agent agent, int temp, double cool) {
		ArrayList<Tile> result = new ArrayList<>();
		if (room.nbOfDirt() != 0) {
			ArrayList<Tile> arl = compute(room, agent, temp, cool);
			// System.out.println(arl);
			// return arl;
			if (!arl.isEmpty()) {
				while (arl.size() > 1) {
					result.add(arl.get(0));
					try {
						result.addAll(BFS_SA.compute(room, arl.remove(0), arl.get(0)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				result.add(arl.remove(0));
			}
		}
		if (result.isEmpty())
			return null;
		return result;
	}

	public static class BFS_SA {
		public Room room;
		public static Tile[][] prev;

		public static ArrayList<Tile> compute(Room room, Tile a, Tile b) {
			// System.out.println("TT" + a + " " + b);
			Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
			Tile current = room.grid[a.getX()][a.getY()];
			prev = new Tile[room.getGridWidth()][room.getGridHeight()];
			queue.add(current);
			boolean[][] visited = new boolean[room.getGridWidth()][room.getGridHeight()];
			visited[current.getX()][current.getY()] = true;
			// System.out.println(prev[0][0]);

			while (queue.size() != 0) {
				current = queue.remove(); // Dequeue current index
				visited[current.getX()][current.getY()] = true; // Mark the start index as visited
				current = room.grid[current.getX()][current.getY()];
				for (Tile tile : room.getNeighbors(current)) {
					if (!visited[tile.getX()][tile.getY()]) {
						visited[tile.getX()][tile.getY()] = true;
						queue.add(tile);
						prev[tile.getX()][tile.getY()] = current;
						if (tile.getX() == b.getX() && tile.getY() == b.getY()) {
							// System.out.println("ppp"+prev[a.getX()][a.getY()]);
							return backtrack(tile);

						}
					}
				}
			}
			return null;
		}

		private static ArrayList<Tile> backtrack(Tile end) {
			// System.out.println(end + "E");
			ArrayList<Tile> path = new ArrayList<>();
			path.add(end);
			Tile prevTile = prev[end.getX()][end.getY()];
			while (prevTile != null) {
				// System.out.println(prevTile + "prev");
				path.add(prevTile);
				prevTile = prev[prevTile.getX()][prevTile.getY()];
			}
			Collections.reverse(path);
			// System.out.println("path" + path);
			// path.remove(0); //modified
			// System.out.println("path" + path);
			return path;
		}

	}

	public static ArrayList<Tile> compute(Room room, Agent agent, int tem, double cool) {
		TourManager.Reset();
		int[][][][] allDistances = new int[room.w][room.h][room.w][room.h];
		// for (int i = 0; i < room.w; i++) {
		// for (int j = 0; j < room.h; j++) {
		// if (room.isDirt(i, j) || (i == agent.x && j == agent.y)) {
		// TourManager.addTile(new Tile(i, j));
		// allDistances[i][j] = room.findAllDistances(new Agent(i, j, 0));
		// }
		// }
		// }
		Stack<Tile> stack = new Stack<>();
//		Queue<Tile> queue = new LinkedList<>(); // create the LinkedList and using it as a queue
		Tile current = room.grid[agent.x][agent.y];
//		queue.add(current); // enqueuing the agent
		stack.add(current);
		TourManager.addTile(current);
		allDistances[current.getX()][current.getY()] = room
				.findAllDistances(new Agent(current.getX(), current.getY(), 0));

		boolean[][] visited = new boolean[room.getGridWidth()][room.getGridHeight()];
		visited[current.getX()][current.getY()] = true;

//		while (queue.isEmpty()) { // Loop Until: queue is empty
		while (!stack.isEmpty()) { // Loop Until: queue is empty
//			current = queue.remove(); // Dequeue current index
			current = stack.pop();
			visited[current.getX()][current.getY()] = true; // Mark the start index as visited
			ArrayList<Tile> neighbors = (ArrayList<Tile>) room.getNeighbors(current);
			for (Tile tile : neighbors) {
				if (!visited[tile.getX()][tile.getY()]) {
					visited[tile.getX()][tile.getY()] = true;
//					queue.add(tile);
					stack.add(tile);
					if (!tile.isClean()) {
						TourManager.addTile(tile);
						allDistances[tile.getX()][tile.getY()] = room
								.findAllDistances(new Agent(tile.getX(), tile.getY(), 0));
					}
				}
			}
		}
		if (TourManager.numberOfDirtyTiles() > 1) {

			// Set initial temp
			double temp = tem;
			// Cooling rate
			double coolingRate = cool;

			// create random intial solution
			Tour currentSolution = new Tour();
			currentSolution.generateIndividual();

			System.out.println("Total distance of initial solution: "
					+ currentSolution.getTotalDistance(room, agent, allDistances));
			// System.out.println("Tour: " + currentSolution);
			Tour best = new Tour(currentSolution.getTour());
			// Loop until system has cooled
			while (temp > 0.01) {
				// Create new neighbour tour
				Tour newSolution = new Tour(currentSolution.getTour());

				// Get random positions in the tour
				int tourPos1 = Utility.randomInt(0, newSolution.tourSize());
				int tourPos2 = Utility.randomInt(0, newSolution.tourSize());

				// to make sure that tourPos1 and tourPos2 are different
				while (tourPos1 == tourPos2) {
					tourPos2 = Utility.randomInt(0, newSolution.tourSize());
				}

				// Get the cities at selected positions in the tour
				Tile tileSwap1 = newSolution.getTile(tourPos1);
				Tile tileSwap2 = newSolution.getTile(tourPos2);

				// Swap them
				newSolution.setTile(tourPos2, tileSwap1);
				newSolution.setTile(tourPos1, tileSwap2);

				// Get energy of solutions
				int currentDistance = currentSolution.getTotalDistance(room, agent, allDistances);
				int neighbourDistance = newSolution.getTotalDistance(room, agent, allDistances);

				// Decide if we should accept the neighbour
				double rand = Math.random();
				if (Utility.acceptanceProbability(currentDistance, neighbourDistance, temp) > rand) {
					currentSolution = new Tour(newSolution.getTour());
				}

				// Keep track of the best solution found
				if (currentSolution.getTotalDistance(room, agent, allDistances) < best.getTotalDistance(room, agent,
						allDistances)) {
					best = new Tour(currentSolution.getTour());
				}

				// Cool system
				temp *= 1 - coolingRate;
			}

			System.out.println("Final solution distance: " + best.getTotalDistance(room, agent, allDistances));
			Tile first = best.tour.get(0);
			while (first.getX() != agent.x || first.getY() != agent.y) {
				best.tour.add(best.tour.remove(0));
				first = best.tour.get(0);
			}
			// System.out.println("Best Tour" + best.tour);
			return best.tour;
		}
		else
			return new ArrayList<>();
	}

	static class Utility {

		/**
		 * Calculates the acceptance probability
		 * 
		 * @param currentDistance
		 *            the total distance of the current tour
		 * @param newDistance
		 *            the total distance of the new tour
		 * @param temperature
		 *            the current temperature
		 * @return value the probability of whether to accept the new tour
		 */
		public static double acceptanceProbability(int currentDistance, int newDistance, double temperature) {
			// If the new solution is better, accept it
			if (newDistance < currentDistance) {
				return 1.0;
			}
			// If the new solution is worse, calculate an acceptance probability
			return Math.exp((currentDistance - newDistance) / temperature);
		}
		/**
		 * returns a random int value within a given range min inclusive .. max not
		 * inclusive
		 * 
		 * @param min
		 *            the minimum value of the required range (int)
		 * @param max
		 *            the maximum value of the required range (int)
		 * @return rand a random int value between min and max [min,max)
		 */
		public static int randomInt(int min, int max) {
			Random r = new Random();
			double d = min + r.nextDouble() * (max - min);
			return (int) d;
		}
	}

	static class Tour {

		// to hold a tour of cities
		public ArrayList<Tile> tour = new ArrayList<Tile>();

		// we assume initial value of distance is 0
		private int distance = 0;

		// Constructor
		// starts an empty tour
		public Tour() {
			for (int i = 0; i < TourManager.numberOfDirtyTiles(); i++) {
				tour.add(null);
			}
		}

		// another Constructor
		// starts a tour from another tour
		@SuppressWarnings("unchecked")
		public Tour(ArrayList<Tile> tour) {
			this.tour = (ArrayList<Tile>) tour.clone();
		}

		/**
		 * Returns tour information
		 * 
		 * @return currenttour
		 */
		public ArrayList<Tile> getTour() {
			return tour;
		}

		/**
		 * Creates a random tour (i.e. individual or candidate solution)
		 */
		public void generateIndividual() {
			// Loop through all our destination cities and add them to our tour
			for (int tileIndex = 0; tileIndex < TourManager.numberOfDirtyTiles(); tileIndex++) {
				setTile(tileIndex, TourManager.getTile(tileIndex));
			}
			// Randomly reorder the tour
			Collections.shuffle(tour);
		}

		/**
		 * Returns a city from the tour given the city's index
		 * 
		 * @param index
		 * @return City at that index
		 */
		public Tile getTile(int index) {
			return tour.get(index);
		}

		/**
		 * Sets a city in a certain position within a tour
		 * 
		 * @param index
		 * @param city
		 */
		public void setTile(int index, Tile tile) {
			tour.set(index, tile);
			// If the tour has been altered we need to reset the fitness and distance
			distance = 0;
		}

		/**
		 * Computes and returns the total distance of the tour
		 * 
		 * @return distance total distance of the tour
		 */
		public int getTotalDistance(Room room, Agent agent, int[][][][] allDistances) {
			if (distance == 0) {
				int tourDistance = 0;
				// Loop through our tour's cities
				for (int tileIndex = 0; tileIndex < tourSize(); tileIndex++) {
					// Get city we're traveling from
					Tile fromTile = getTile(tileIndex);
					// City we're traveling to
					Tile destinationTile;
					// Check we're not on our tour's last city, if we are set our
					// tour's final destination city to our starting city
					if (tileIndex + 1 < tourSize()) {
						destinationTile = getTile(tileIndex + 1);
					} else {
						destinationTile = getTile(0);
					}
					// Get the distance between the two cities
					int i;
					if (destinationTile.getX() == agent.x && destinationTile.getY() == agent.y)
						i = 0;
					else
						i = allDistances[fromTile.getX()][fromTile.getY()][destinationTile.getX()][destinationTile
								.getY()];
					// i = room.getDistance(fromTile, destinationTile);
					tourDistance += i;
				}
				distance = tourDistance;
			}
			return distance;
		}

		/**
		 * Get number of cities on our tour
		 * 
		 * @return number how many cities there are in the tour!
		 */
		public int tourSize() {
			return tour.size();
		}

		/**
		 * To print out a list of all the cities in the tour
		 */

	}

	static class TourManager {

		// Holds our cities
		private static ArrayList<Tile> destinationTiles = new ArrayList<Tile>();

		/**
		 * Adds a destination tile
		 *
		 * @param tile
		 */
		public static void addTile(Tile tile) {
			destinationTiles.add(tile);
		}

		public static void Reset() {
			destinationTiles = new ArrayList<Tile>();
		}

		/**
		 * returns a tile given its index
		 *
		 * @param index
		 * @return tile the tile at index
		 */
		public static Tile getTile(int index) {
			return (Tile) destinationTiles.get(index);
		}

		/**
		 * Returns the number of destination tiles
		 *
		 * @return size the number of destination tiles
		 */
		public static int numberOfDirtyTiles() {
			return destinationTiles.size();
		}

		public static void removeTile(Tile tile) {
			destinationTiles.remove(tile);
		}

	}

}