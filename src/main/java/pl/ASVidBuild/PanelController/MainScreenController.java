package pl.ASVidBuild.PanelController;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class MainScreenController {
	
	@FXML
	private StackPane mainScreenPanel;
	
	@FXML
	public void initialize() {
		loadMainMenu();
	}

	public void loadMainMenu() {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainMenu.fxml"));
		Pane pane = null;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainMenuController mainMenuController = loader.getController();
		mainMenuController.setMainScreenController(this);
		
		setActiveScreen(pane);
	}

	public void setActiveScreen(Pane pane) {
		mainScreenPanel.getChildren().clear();
		mainScreenPanel.getChildren().add(pane);
	}
}
