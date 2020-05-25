package launcher;

import java.io.IOException;

import javafx.fxml.FXML;

public class Helper {

	@FXML public void BFSF() {
		String[] command = { "cmd","/C", "start javaw -jar FOEA-BFS.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void TSPF() {
		String[] command = { "cmd","/C", "start javaw -jar FOEA-SA_TSP.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void SMART() {
		String[] command = { "cmd","/C", "start javaw -jar FOEA-SMART.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void Weight() {
		String[] command = { "cmd","/C", "start javaw -jar POEA-WeightedAlgorithm.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void bfsP() {
		String[] command = { "cmd","/C", "start javaw -jar POEA-BFS.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void Discover() {
		String[] command = { "cmd","/C", "start javaw -jar DISCOVERUnobservable.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void TSPP() {
		String[] command = { "cmd","/C", "start javaw -jar Unobservable-SA_TSP.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML public void adversarial() {
		String[] command = { "cmd","/C", "start javaw -jar Multi_Aversarial_Vacuum_Agents.jar", "/B"};
		try {
			Runtime.getRuntime().exec(command, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
