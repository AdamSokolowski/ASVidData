package pl.ASVidBuild.PanelController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class DatabaseScreenController {

	@FXML
	private Button menuButton;
	
	private MainScreenController mainScreenController;
	
	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
	}

	@FXML
	void menuButtonClick(ActionEvent event) {
		mainScreenController.loadMainMenu();
	}

}
