package pl.ASVidBuild;

import java.io.File;

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
import pl.ASVidBuild.database.DbUtil;

public class Main extends Application {

	public static void main(String[] args) {
		File file = new File("settings.ini");
		if (!file.exists()) {
			firstSuccessfullRun();
		}
		
		launch(args);
	}

	private static void firstSuccessfullRun() {
		if(DbRepository.createMainDatabase()) {
			System.out.println("Database "+DbUtil.DB_NAME+" successfully created.");
		DbRepository.initDatabaseTables();
		SettingsData settingsData = SettingsData.getInstance();
		settingsData.saveToFile();
		}
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
