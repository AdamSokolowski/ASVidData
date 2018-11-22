package pl.ASVidBuild.PanelController;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainMenuController {

	private MainScreenController mainScreenController;

	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
	}

	@FXML
	private Button explorerButton;

	@FXML
	private Button databaseButton;

	@FXML
	private Button optionsButton;

	@FXML
	private Button exitButton;

	public FXMLLoader setActivePane(String fxmlPath) {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
		Pane pane = null;
		try {
			pane = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
		mainScreenController.setActiveScreen(pane);
		return loader;
	}

	@FXML
	void explorerButtonClick(ActionEvent event) {
		ExplorerScreenController activatedScreenController = setActivePane("/fxml/ExplorerScreen.fxml").getController();
		activatedScreenController.setMainScreenController(mainScreenController);

	}

	@FXML
	void databaseButtonClick(ActionEvent event) {
		DatabaseScreenController activatedScreenController = setActivePane("/fxml/DatabaseScreen.fxml").getController();
		activatedScreenController.setMainScreenController(mainScreenController);
	}

	@FXML
	void optionsButtonClick(ActionEvent event) {
		OptionsScreenController activatedScreenController = setActivePane("/fxml/OptionsScreen.fxml").getController();
		activatedScreenController.setMainScreenController(mainScreenController);
	}

	@FXML
	void exitButtonClick(ActionEvent event) {

	}

}
