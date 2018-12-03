package pl.ASVidBuild;

import javafx.application.Application;

/**
 * 
 * @author Adam Soko³owski
 *
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.ASVidBuild.database.DbRepository;

public class Main extends Application{
	
	public static void main(String[] args) {
		DbRepository.initDatabases();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/MainScreen.fxml"));
		
		StackPane stackPane = loader.load();
		
		Scene scene = new Scene(stackPane);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("ASVidData");
		primaryStage.show();
	}
}
