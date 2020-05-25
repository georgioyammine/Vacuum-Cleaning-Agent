package launcher;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;



public class Launcher extends Application {
	@Override
	    public void start(Stage stage) throws Exception {
	       Parent root = FXMLLoader.load(getClass().getResource("Model1.fxml"));
	    
	        Scene scene = new Scene(root);
	        stage.setResizable(false);
	        stage.setTitle("Vacuum Cleaner Agent");
	        new JMetro(scene, Style.LIGHT);
	        stage.setScene(scene);
	        stage.getIcons().add(new Image("robot.png"));
	        stage.show();
	        
	    }	
	public static void main(String[] args) throws IOException {
//		 Process p = Runtime.getRuntime().exec("cmd.exe /c start java -jar " + (new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getAbsolutePath() + " cmd");
		launch(args);
	}
}
