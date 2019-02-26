package pl.ASVidBuild.PanelController;





import java.io.File;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import pl.ASVidBuild.database.DBFileBackup;


/**
 * 
 * @author Adam Soko³owski
 *
 */

public class DatabaseScreenController {

    @FXML
    private Button DumpDB2FileButton;

    @FXML
    private Button RestoreDBfromFileButton;
	
    @FXML
    private Button menuButton;

	private MainScreenController mainScreenController;

	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
	}
	
	
    @FXML
    void DumpDB2FileButtonClick(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filterBkp = new FileChooser.ExtensionFilter("Database backup", "*.bkp");
		fileChooser.getExtensionFilters().addAll(filterBkp);
		fileChooser.setTitle("Select file for database backup storage");
		File file = fileChooser.showSaveDialog(null);
		
		if (file != null) {
			String fileName = file.getAbsolutePath();
	    	try {
				DBFileBackup.DBToFile(fileName);
				System.out.println("Database has been dumped to file: "+ fileName);
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
				e.printStackTrace();
			}	
		}
		
    }

    @FXML
    void RestoreDBfromFileButtonClick(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filterBkp = new FileChooser.ExtensionFilter("Database backup", "*.bkp");
		fileChooser.getExtensionFilters().addAll(filterBkp);
		fileChooser.setTitle("Select file to restore database from");
		File file = fileChooser.showOpenDialog(null);
		
		if (file != null) {
			String fileName = file.getAbsolutePath();
	    	try {
	    		DBFileBackup.FileToDB(fileName);
				System.out.println("Database has been restored from file: "+ fileName);
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
				e.printStackTrace();
			}	
		}
		
    	
    	
    }
    
	
	@FXML
	void menuButtonClick(ActionEvent event) {
		mainScreenController.loadMainMenu();
	}

}
