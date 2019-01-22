package pl.ASVidBuild.PanelController;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import pl.ASVidBuild.UI.Point;
import pl.ASVidBuild.UI.UIHelper;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.Tag;

public class TagScreenController {

    @FXML
    private SplitPane tagsSplitPane;
	
    @FXML
    private ScrollPane categoryTagsPane;

    @FXML
    private Pane tagsPane;
    
    @FXML
    private Button addNewTagButton;
    


	private int paletteTagsCount = 0;
    
    private static final int TAG_HEIGHT = 150;
    private static final int TAG_WIDTH = 200;
    private static final int GAP_BTWEEN_TAGS = 5;
    private static final int TAGS_MAX_COL_COUNT = 3;
    
    
	@FXML
	public void initialize() {
		//Fill out the tag screen with all tags from database.
		List<Tag> allTags = TagDao.getAllTags();
		for (int i=0; i<allTags.size();i++) {
			UIHelper.addTagToPane(TagDao.getAllTags().get(i), paletteTagsCount, tagsPane, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, TAGS_MAX_COL_COUNT);
			updateNewTagButtonPosition();
			paletteTagsCount++;
		}
		tagsSplitPane.setPrefWidth(tagsPane.getPrefWidth()+15);
	}
	
    @FXML
    void addNewTagButtonClick(ActionEvent event) {
    	Optional<String> newTagName = null;
    	TextInputDialog tagInputDialog = UIHelper.newTagInputDialog();
    	
    	newTagName = tagInputDialog.showAndWait();
    	if(newTagName.isPresent()) {
    		System.out.println(newTagName.get());
    		Tag newTag = TagDao.getTagByTagName(newTagName.get());
    		if(newTag==null) {
    			//Tag not found in already existing tags
    			newTag = TagDao.createTagObject(newTagName.get(), "");
    			TagDao.addTagToDb(newTag, false);
    			
    			UIHelper.addTagToPane(newTag, paletteTagsCount, tagsPane, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, TAGS_MAX_COL_COUNT);
    			updateNewTagButtonPosition();
    			paletteTagsCount++; 
    			
    		}else {
    			Alert tagExistsAlert = UIHelper.tagExistsAlert();
    			tagExistsAlert.show();
    		}
    	}
    	
    }

	public void updateNewTagButtonPosition() {
		//moves New Tag Button to position after newly added tag button/image
		Point newAddNewTagButtonPosition = UIHelper.measureTagLayoutPosition(paletteTagsCount+1, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, TAGS_MAX_COL_COUNT);
		addNewTagButton.setLayoutX(newAddNewTagButtonPosition.getX());
		addNewTagButton.setLayoutY(newAddNewTagButtonPosition.getY());
	}





	

	
	
}
