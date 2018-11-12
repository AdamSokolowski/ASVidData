
package pl.ASVidBuild.PanelController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pl.ASVidBuild.PanelController.MainScreenController;

public class ExplorerScreenController {

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
