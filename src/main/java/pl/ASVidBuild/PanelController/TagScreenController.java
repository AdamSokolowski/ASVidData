package pl.ASVidBuild.PanelController;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.Tag;

public class TagScreenController {

    @FXML
    private SplitPane tagsSplitPane;
	
    @FXML
    private ScrollPane categoryTagsPane;

    @FXML
    private Pane tagsPane;
    
    
    private int paletteTagsCount = 0;
    
    private static final int TAG_HEIGHT = 150;
    private static final int TAG_WIDTH = 200;
    private static final int GAP_BTWEEN_TAGS = 5;
    private static final int TAGS_MAX_COL_COUNT = 3;
    
    
    
	@FXML
	public void initialize() {
		List<Tag> allTags = TagDao.getAllTags();
		for (int i=0; i<allTags.size();i++) {
			addTagToTagsPalette(TagDao.getAllTags().get(i));
		}
		tagsSplitPane.setPrefWidth(GAP_BTWEEN_TAGS+TAGS_MAX_COL_COUNT*(TAG_WIDTH+GAP_BTWEEN_TAGS)+15);
	}
	
	private void addTagToTagsPalette(Tag tag) {
		if(tag.getPicturePath().equals("")) {
			Button button = new Button(tag.getTagName());
			tagsPane.getChildren().add(button);
			button.setLayoutX(GAP_BTWEEN_TAGS+(paletteTagsCount % TAGS_MAX_COL_COUNT)*(TAG_WIDTH+GAP_BTWEEN_TAGS));
			button.setLayoutY(GAP_BTWEEN_TAGS+(paletteTagsCount / TAGS_MAX_COL_COUNT)*(TAG_HEIGHT+GAP_BTWEEN_TAGS));
			button.setPrefSize(TAG_WIDTH, TAG_HEIGHT);
			
		} else {
			ImageView imageView = new ImageView();
			tagsPane.getChildren().add(imageView);
			imageView.setX(GAP_BTWEEN_TAGS+(paletteTagsCount % TAGS_MAX_COL_COUNT)*(TAG_WIDTH+GAP_BTWEEN_TAGS));
			imageView.setY(GAP_BTWEEN_TAGS+(paletteTagsCount / TAGS_MAX_COL_COUNT)*(TAG_HEIGHT+GAP_BTWEEN_TAGS));
			imageView.setFitWidth(TAG_WIDTH);
			imageView.setFitHeight(TAG_HEIGHT);
			Image img = new Image(Paths.get(tag.getPicturePath()).toUri().toString());
			imageView.setImage(img);
			Tooltip tooltip = new Tooltip(tag.getTagName());
			tooltip.setFont(new Font("Arial", 16));
			Tooltip.install(imageView,tooltip);
		}
		tagsPane.setPrefHeight(GAP_BTWEEN_TAGS+(1+paletteTagsCount / TAGS_MAX_COL_COUNT)*(TAG_HEIGHT+GAP_BTWEEN_TAGS));
		paletteTagsCount++;
		
	}
	
	
}
