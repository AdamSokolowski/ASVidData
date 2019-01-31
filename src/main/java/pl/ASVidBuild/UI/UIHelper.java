/**
 * 
 */
package pl.ASVidBuild.UI;

import java.io.File;
import java.nio.file.Paths;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * @author Adam Soko³owski
 *
 */
public class UIHelper {
	
	private static boolean draggedSourceIsATag = false;		//flag for controlling if drag-dropped item is a tag so it can be accepted in tags panel.
	private static int draggedTagId = 0;
	private static String draggedSourceParent = "";			//String representation of parent pane to dragged Tag

	public static int getDraggedTagId() {
		return draggedTagId;
	}

	public static void setDraggedTagId(int draggedTagId) {
		UIHelper.draggedTagId = draggedTagId;
	}

	public static boolean isDraggedSourceATag() {
		return draggedSourceIsATag;
	}

	public static void setDraggedSourceIsATag(boolean draggedSourceIsATagFromTagsPicker) {
		draggedSourceIsATag = draggedSourceIsATagFromTagsPicker;
	}

	public static void setDraggedSourceParent(Pane ownerPane) {
		if(ownerPane==null) {
			draggedSourceParent = "";
		} else {	
			draggedSourceParent = ownerPane.getId();
			System.out.println("draggedSourceParent: "+draggedSourceParent);
		}
	}
	
	public static String getDraggedSourceParent() {
		return draggedSourceParent;
	}
	
	public static void tagsListPanelClear(Pane ownerPane, Button initButton, int gapBtweenTags, int tagWidth, int tagHeight) {

		ownerPane.getChildren().clear();
		initButton = new Button("[ Any File ]");
		initButton.setLayoutX(gapBtweenTags);
		initButton.setLayoutY(gapBtweenTags);
		initButton.setPrefWidth(tagWidth);
		initButton.setPrefHeight(tagHeight);
		initButton.setTextFill(Paint.valueOf("#4b8f32"));
		ownerPane.getChildren().add(initButton);
	}
	
	
	
	public static int addTagToPane(Tag tag, int existingTagsCount, Pane ownerPane, int gapBtweenTags, int tagWidth, int tagHeight, int tagsMaxColCount) {
		//Method adds to pane button or image representing tag. Method returns index of added tag in ownerPane children list.
		int paneChildrenListId = ownerPane.getChildren().size();
		int tagsMaxColCountValue = tagsMaxColCount;
		if (tagsMaxColCountValue == 0) {
			tagsMaxColCountValue = existingTagsCount+1;
		}
		Point panelCoordinatesOfTag = measureTagLayoutPosition(existingTagsCount, gapBtweenTags, tagWidth, tagHeight, tagsMaxColCountValue);
		if(tag.getPicturePath().equals("")) {
			Button button = new Button(tag.getTagName());
			ownerPane.getChildren().add(button); 
			button.setLayoutX(panelCoordinatesOfTag.getX());
			button.setLayoutY(panelCoordinatesOfTag.getY());
			button.setPrefSize(tagWidth, tagHeight);
			button.setId(String.valueOf(tag.getId()));
			button.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					Dragboard dragB = button.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString(button.getId());
					dragB.setContent(content);
					UIHelper.setDraggedSourceIsATag(true);
					UIHelper.setDraggedSourceParent(ownerPane);
					UIHelper.setDraggedTagId(tag.getId());
					event.consume();
				}
				
			});
			
		} else {
			ImageView imageView = new ImageView();
			ownerPane.getChildren().add(imageView);
			imageView.setX(panelCoordinatesOfTag.getX());
			imageView.setY(panelCoordinatesOfTag.getY());
			imageView.setFitWidth(tagWidth);
			imageView.setFitHeight(tagHeight);
			Image img = new Image(Paths.get(tag.getPicturePath()).toUri().toString());
			imageView.setImage(img);
			imageView.setId(String.valueOf(tag.getId()));
			Tooltip tooltip = new Tooltip(tag.getTagName());
			tooltip.setFont(new Font("Arial", 16));
			Tooltip.install(imageView,tooltip);
			imageView.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					Dragboard dragB = imageView.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString(imageView.getId());
					dragB.setContent(content);
					UIHelper.setDraggedSourceIsATag(true);
					UIHelper.setDraggedTagId(tag.getId());
					UIHelper.setDraggedSourceParent(ownerPane);
					event.consume();
				}
				
			});
			
		}
		double newHeight = gapBtweenTags+(1+existingTagsCount / tagsMaxColCountValue)*(tagHeight+gapBtweenTags);
		double newWidth = gapBtweenTags+(1+tagsMaxColCountValue)*(tagWidth+gapBtweenTags);
		if (ownerPane.getPrefHeight()<newHeight) {
			ownerPane.setPrefHeight(newHeight);
		}
		if (ownerPane.getPrefWidth()<newWidth) {
			ownerPane.setPrefWidth(newWidth);
		}
		
		
		UIHelper.setDraggedSourceIsATag(false);		// dragged items control - turn flag to false after dragged tag item is docked in panel.
		UIHelper.clearClipboardData(); 		// clearing data from system clipboard so its not visible outside on application.
		
		return paneChildrenListId;
	}
	



		public static void clearClipboardData() {
			ClipboardContent content = new ClipboardContent();
			content.clear();
		}

		public static Point measureTagLayoutPosition(int existingTagsCount, int gapBtweenTags, int tagWidth,
			int tagHeight, int tagsMaxColCountValue) {
		Point point= new Point();
		point.setX(gapBtweenTags+(existingTagsCount % tagsMaxColCountValue)*(tagWidth+gapBtweenTags));
		point.setY(gapBtweenTags+(existingTagsCount / tagsMaxColCountValue)*(tagHeight+gapBtweenTags));
		return point;
	}
	public static TextInputDialog newTagInputDialog() {
		TextInputDialog textInputDialog = new TextInputDialog("");
    	textInputDialog.setTitle("Tags Editor");
    	textInputDialog.setHeaderText("Add new Tag");
    	textInputDialog.setContentText("New Tag Name");
    	textInputDialog.initModality(Modality.APPLICATION_MODAL);
    	textInputDialog.getEditor().setOnKeyReleased(new EventHandler<KeyEvent>(){
    		
				@Override
				public void handle(KeyEvent event) {
					boolean tagNameHasForbiddenCharacters;
		    		tagNameHasForbiddenCharacters = !textInputDialog.getEditor().getText().matches("^[a-zA-Z0-9_+!\\?\\+\\-\\$%()¹êœæó¿Ÿ³ñ]+$");
		    			textInputDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(tagNameHasForbiddenCharacters);
		    			if(tagNameHasForbiddenCharacters) {
		    				textInputDialog.setHeaderText("Allowed characters are letters digits and signs: !?+-$()");
		    			} else {
		    				textInputDialog.setHeaderText("Add new Tag");
		    			}
				}
    		
    	});
		return textInputDialog;
	}
	
	public static Alert tagExistsAlert() {
		Alert tagExistsAlert = new Alert(AlertType.ERROR);
		tagExistsAlert.setTitle("Tags Editor");
		tagExistsAlert.setHeaderText("Tag with this name already exists.");
		tagExistsAlert.setContentText("Please find this tag in tags list.");
		tagExistsAlert.initModality(Modality.APPLICATION_MODAL);
		return tagExistsAlert;
	}
	
	public static String openFileDialogFileName(String fileTypeDescription, String fileTypeExtension) {
		String filePath ="";
		String fileTypeExtTemp = fileTypeExtension;
		if(!fileTypeExtension.startsWith("*.")) {
			fileTypeExtTemp = "*."+fileTypeExtension;
		}
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(fileTypeDescription, fileTypeExtTemp);

		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			filePath = file.getAbsolutePath();
		}
		System.out.println("File selected: " + filePath);
		return filePath;
	}
	
}
