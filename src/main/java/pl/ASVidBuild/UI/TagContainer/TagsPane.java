/**
 * 
 */
package pl.ASVidBuild.UI.TagContainer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import pl.ASVidBuild.UI.Point;
import pl.ASVidBuild.UI.UIHelper;
import pl.ASVidBuild.database.dao.MediaFileDao;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * @author Adam Soko³owski
 *
 */
public class TagsPane extends Pane {

	private List<TagNode> tagItems;
	private int gapBtweenTags = 3;
	private int tagWidth = 100;
	private int tagHeight = 75;
	private int tagsMaxColCount = 0;
	private Button placeHolderButton;

	public enum ButtonPlaceHolderType {
		NONE, ADD_NEW_TAG, EMPTY_TAG_LIST, CONSTANT_PLACEHOLDER
	}

	private ButtonPlaceHolderType buttonPlaceHolderType;

	public TagsPane(ScrollPane scrollPane) {
		super();
		buttonPlaceHolderType = ButtonPlaceHolderType.NONE;
		tagItems = new LinkedList<TagNode>();
		setPaneOnStage(scrollPane, "");

	}

	public TagsPane(ScrollPane scrollPane, ButtonPlaceHolderType buttonPlaceHolderType, String buttonPlaceHolderText) {
		super();
		this.buttonPlaceHolderType = buttonPlaceHolderType;
		tagItems = new LinkedList<TagNode>();
		setPaneOnStage(scrollPane, buttonPlaceHolderText);

	}

	private void setPaneOnStage(ScrollPane scrollPane, String buttonPlaceHolderText) {
		scrollPane.setContent(this);
		this.setPrefWidth(scrollPane.getPrefWidth() - 3);
		this.setPrefHeight(scrollPane.getPrefHeight() - 106);
		this.setMinWidth(this.getPrefWidth());
		this.setMinHeight(this.getPrefHeight());
		if (buttonPlaceHolderType != ButtonPlaceHolderType.NONE) {
			placeHolderButton = new Button(buttonPlaceHolderText);
			if (buttonPlaceHolderType == ButtonPlaceHolderType.ADD_NEW_TAG) {
				placeHolderButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						addNewTagButtonClick();
					}

				});
			}
			this.getChildren().add(placeHolderButton);
			Point panelCoordinatesOfButton = measureTagLayoutPosition(0, 1);
			placeHolderButton.setLayoutX(panelCoordinatesOfButton.getX());
			placeHolderButton.setLayoutY(panelCoordinatesOfButton.getY());
			placeHolderButton.setTextFill(Paint.valueOf("#4b8f32"));
			placeHolderButtonSizeRefresh();
		}
	}

	private void addNewTagButtonClick() {
		Optional<String> newTagName = null;
		TextInputDialog tagInputDialog = UIHelper.newTagInputDialog();

		newTagName = tagInputDialog.showAndWait();
		if (newTagName.isPresent()) {
			Tag newTag = TagDao.getTagByTagName(newTagName.get());
			if (newTag == null) {
				// Tag not found in already existing tags
				newTag = TagDao.createTagObject(newTagName.get(), "");
				TagDao.addTagToDb(newTag, false);

				addTag(newTag);

			} else {
				Alert tagExistsAlert = UIHelper.tagExistsAlert();
				tagExistsAlert.show();
			}
		}
	}

	private void placeHolderButtonSizeRefresh() {
		if (buttonPlaceHolderType != ButtonPlaceHolderType.NONE) {
			placeHolderButton.setPrefSize(tagWidth, tagHeight);
		}

	}

	public int getGapBtweenTags() {
		return gapBtweenTags;
	}

	public void setGapBtweenTags(int gapBtweenTags) {
		this.gapBtweenTags = gapBtweenTags;
	}

	public int getTagWidth() {
		return tagWidth;
	}

	public void setTagWidth(int tagWidth) {
		this.tagWidth = tagWidth;
		placeHolderButtonSizeRefresh();
	}

	public int getTagHeight() {
		return tagHeight;
	}

	public void setTagHeight(int tagHeight) {
		this.tagHeight = tagHeight;
		placeHolderButtonSizeRefresh();
	}

	public int getTagsMaxColCount() {
		return tagsMaxColCount;
	}

	public void setTagsMaxColCount(int tagsMaxColCount) {
		this.tagsMaxColCount = tagsMaxColCount;
	}

	public void initPlaceHolderButtonPosition() {
		if (buttonPlaceHolderType != ButtonPlaceHolderType.NONE) {
			Point panelCoordinatesOfButton = measureTagLayoutPosition(0, 1);
			placeHolderButton.setLayoutX(panelCoordinatesOfButton.getX());
			placeHolderButton.setLayoutY(panelCoordinatesOfButton.getY());

		}

	}

	private void updateAddNewTagPlaceHolderButtonPosition() {

		if (buttonPlaceHolderType == ButtonPlaceHolderType.ADD_NEW_TAG) {
			int paneChildrenListId = this.getChildren().size() - 1;

			int tagsMaxColCountValue = tagsMaxColCount;
			if (tagsMaxColCountValue < 1) {
				tagsMaxColCountValue = this.getChildren().size() + 1;
			}
			Point panelCoordinatesOfButton = measureTagLayoutPosition(paneChildrenListId, tagsMaxColCountValue);
			placeHolderButton.setLayoutX(panelCoordinatesOfButton.getX());
			placeHolderButton.setLayoutY(panelCoordinatesOfButton.getY());
		}

	}

	public int tagFoundInTagItems(int tagId) {
		int result = tagItems.size() - 1; // index of tagItems list item with TagId, if not in list returns -1
		while (result > -1 && tagItems.get(result).getTag().getId() != tagId) {
			result--;
		}
		return result;
	}

	public void addTag(Tag tag) {
		int paneChildrenListId = this.getChildren().size();
		if (buttonPlaceHolderType == ButtonPlaceHolderType.ADD_NEW_TAG
				|| buttonPlaceHolderType == ButtonPlaceHolderType.EMPTY_TAG_LIST) {
			paneChildrenListId--;

		}
		int tagsMaxColCountValue = tagsMaxColCount;
		if (tagsMaxColCountValue < 1) {
			tagsMaxColCountValue = this.getChildren().size() + 1;
		}
		Point panelCoordinatesOfTag = measureTagLayoutPosition(paneChildrenListId, tagsMaxColCountValue);
		TagNode tagNode = new TagNode(tag, this);
		tagItems.add(tagNode);
		this.getChildren().add(tagNode.getNode());
		if (tagNode.getTagPicturePath().equals("")) {
			Button node = (Button) tagNode.getNode();
			node.setLayoutX(panelCoordinatesOfTag.getX());
			node.setLayoutY(panelCoordinatesOfTag.getY());
			node.setPrefSize(tagWidth, tagHeight);
		} else {
			ImageView node = (ImageView) tagNode.getNode();
			node.setX(panelCoordinatesOfTag.getX());
			node.setY(panelCoordinatesOfTag.getY());
			node.setFitWidth(tagWidth);
			node.setFitHeight(tagHeight);
		}

		if (buttonPlaceHolderType == ButtonPlaceHolderType.ADD_NEW_TAG
				|| buttonPlaceHolderType == ButtonPlaceHolderType.CONSTANT_PLACEHOLDER) {
			paneChildrenListId++;
		} else {
			if (buttonPlaceHolderType == ButtonPlaceHolderType.EMPTY_TAG_LIST) {
				tagsMaxColCountValue--;
			}

		}

		double newTagsPaneHeight = gapBtweenTags
				+ (1 + paneChildrenListId / tagsMaxColCountValue) * (tagHeight + gapBtweenTags);
		double newTagsPaneWidth = gapBtweenTags + (tagsMaxColCountValue) * (tagWidth + gapBtweenTags);
		if (this.getPrefHeight() < newTagsPaneHeight) {
			this.setPrefHeight(newTagsPaneHeight);
		}
		if (this.getPrefWidth() < newTagsPaneWidth) {
			this.setPrefWidth(newTagsPaneWidth);
		}
		updateAddNewTagPlaceHolderButtonPosition();

	}

	private Point measureTagLayoutPosition(int existingTagsCount, int tagsMaxColCountValue) {
		Point point = new Point();
		point.setX(gapBtweenTags + (existingTagsCount % tagsMaxColCountValue) * (tagWidth + gapBtweenTags));
		point.setY(gapBtweenTags + (existingTagsCount / tagsMaxColCountValue) * (tagHeight + gapBtweenTags));
		return point;
	}

	public void clearTagItemsList() {
		tagItems.clear();
		this.getChildren().clear();
		this.setPrefWidth(this.getMinWidth());
		this.setPrefHeight(this.getMinHeight());
		if (buttonPlaceHolderType != ButtonPlaceHolderType.NONE) {
			this.getChildren().add(placeHolderButton);
			initPlaceHolderButtonPosition();
		}
	}

	public void removeTagfromTagsPaneLayout(Tag tag) {
		int tagItemsIndex = this.tagFoundInTagItems(tag.getId());
		Point tagLayoutPosition;
		int tagsMaxColCountValue = tagsMaxColCount;
		this.getChildren().remove(this.tagItems.get(tagItemsIndex).getNode());
		this.tagItems.remove(tagItemsIndex);

		if (tagsMaxColCountValue < 1) {
			tagsMaxColCountValue = tagItems.size();
		}
		for (int i = tagItemsIndex; i < tagItems.size(); i++) {
			tagLayoutPosition = measureTagLayoutPosition(i, tagsMaxColCountValue);
			tagItems.get(i).getNode().setLayoutX(tagLayoutPosition.getX());
			tagItems.get(i).getNode().setLayoutY(tagLayoutPosition.getY());

		}
	}

	public void loadFileTags(String filePath) {
		MediaFile selectedMediaFile = MediaFileDao.getMediaFileByFilePath(filePath);
		List<Tag> selectedMediaFileTags = TagDao.getAllTagsOfMediaFile(selectedMediaFile);
		if (!selectedMediaFileTags.isEmpty()) {
			for (int i = 0; i < selectedMediaFileTags.size(); i++) {
				this.addTag(selectedMediaFileTags.get(i));
			}
		}
	}

}
