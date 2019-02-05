package pl.ASVidBuild.PanelController;


import java.util.List;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import pl.ASVidBuild.UI.TagContainer.TagsPane;
import pl.ASVidBuild.UI.TagContainer.TagsPane.ButtonPlaceHolderType;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.Tag;

public class TagScreenController {

    @FXML
    private SplitPane tagsSplitPane;
	
    @FXML
    private ScrollPane categoryTagsPane;
    
    @FXML
    private Button addNewTagButton;
    
    @FXML
    private ScrollPane tagsScrollPane;

    private TagsPane tagsPalettePanel;

    
    private static final int TAG_HEIGHT = 150;
    private static final int TAG_WIDTH = 200;
    private static final int GAP_BTWEEN_TAGS = 5;
    private static final int TAGS_MAX_COL_COUNT = 3;
    
    
	@FXML
	public void initialize() {
		//Fill out the tag screen with all tags from database.
		List<Tag> allTags = TagDao.getAllTags();
		tagsPalettePanel = new TagsPane(tagsScrollPane,ButtonPlaceHolderType.ADD_NEW_TAG,"Add new tag....");
		tagsPalettePanel.setId("tagsPalettePanel");
		tagsPalettePanel.setTagHeight(TAG_HEIGHT);
		tagsPalettePanel.setTagWidth(TAG_WIDTH);
		tagsPalettePanel.setGapBtweenTags(GAP_BTWEEN_TAGS);
		tagsPalettePanel.setTagsMaxColCount(TAGS_MAX_COL_COUNT);
		tagsPalettePanel.initPlaceHolderButtonPosition();
		for (int i=0; i<allTags.size();i++) {
			tagsPalettePanel.addTag(allTags.get(i));
		}
		tagsSplitPane.setPrefWidth(tagsPalettePanel.getPrefWidth()+15);
	}
		
	
}
