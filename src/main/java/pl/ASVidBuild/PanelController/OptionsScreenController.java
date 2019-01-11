
package pl.ASVidBuild.PanelController;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pl.ASVidBuild.SettingsData;
import pl.ASVidBuild.UI.UIHelper;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class OptionsScreenController {

	@FXML
	private Button menuButton;
	
    @FXML
    private TextField windowsMediaPlayerExecPathTextField;

    @FXML
    private TextField mpcExecPathTextField;

    @FXML
    private Button windowsMediaPlayerExecPathBrowse;

    @FXML
    private Button mpcExecPathBrowse;

    @FXML
    private CheckBox autoPlayNextVidCheckBox;

    @FXML
    private CheckBox overviewModeCheckBox;

	
	private MainScreenController mainScreenController;

	private SettingsData settingsData;
	
	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
		setOptionsSettingsFromSettingsFile();
	}

	public void setOptionsSettingsFromSettingsFile() {
		SettingsData settingsData = SettingsData.getInstance();
		mpcExecPathTextField.setText(settingsData.getMpcExecPath());
		windowsMediaPlayerExecPathTextField.setText(settingsData.getWindowsMediaPlayerExecPath());
	}
	
	@FXML
	void menuButtonClick(ActionEvent event) {
		mainScreenController.loadMainMenu();
	}
	
    @FXML
    void windowsMediaPlayerExecPathBrowseClick(ActionEvent event) {
    	String filePath = UIHelper.openFileDialogFileName("Application(*.exe)", "exe");
			if(filePath.endsWith(".exe")) {
				filePath = filePath.substring(1, filePath.length()-4);
				windowsMediaPlayerExecPathTextField.setText(filePath);
				settingsData.setWindowsMediaPlayerExecPath(filePath);
				settingsData.saveToFile();
			}			
		}


    
    @FXML
    void mpcExecPathBrowseClick(ActionEvent event) {
    	String filePath = UIHelper.openFileDialogFileName("Application(*.exe)", "exe");
			if(filePath.endsWith(".exe")) {
				filePath = filePath.substring(1, filePath.length()-4);
				mpcExecPathTextField.setText(filePath);
				settingsData.setMpcExecPath(filePath);
				settingsData.saveToFile();
			}			
		}
    
    @FXML
    void autoPlayNextVidCheckBoxClick(ActionEvent event) {
    	settingsData.setAutoPlayNextVidAfterCurrentIsOver(autoPlayNextVidCheckBox.isSelected());
    	settingsData.saveToFile();
    }

    @FXML
    void overviewModeCheckBoxClick(ActionEvent event) {
    	if (overviewModeCheckBox.isSelected()) {
    		settingsData.setMediaPreviewPlayOption("fragments");
    	} else {
    		settingsData.setMediaPreviewPlayOption("full");
    	}
    	settingsData.saveToFile();
    }

    
}



