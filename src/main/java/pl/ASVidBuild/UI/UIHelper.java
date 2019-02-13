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
import pl.ASVidBuild.UI.TagContainer.TagsPane;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * @author Adam Soko³owski
 *
 */
public class UIHelper {
	
	public static Point measureTagLayoutPosition(int existingTagsCount, int gapBtweenTags, int tagWidth, int tagHeight,
			int tagsMaxColCountValue) {
		Point point = new Point();
		point.setX(gapBtweenTags + (existingTagsCount % tagsMaxColCountValue) * (tagWidth + gapBtweenTags));
		point.setY(gapBtweenTags + (existingTagsCount / tagsMaxColCountValue) * (tagHeight + gapBtweenTags));
		return point;
	}

	public static TextInputDialog newTagInputDialog() {
		TextInputDialog textInputDialog = new TextInputDialog("");
		textInputDialog.setTitle("Tags Editor");
		textInputDialog.setHeaderText("Add new Tag");
		textInputDialog.setContentText("New Tag Name");
		textInputDialog.initModality(Modality.APPLICATION_MODAL);
		textInputDialog.getEditor().setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				boolean tagNameHasForbiddenCharacters;
				tagNameHasForbiddenCharacters = !textInputDialog.getEditor().getText()
						.matches("^[a-zA-Z0-9_+!\\?\\+\\-\\$%()¹êœæó¿Ÿ³ñ]+$");
				textInputDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(tagNameHasForbiddenCharacters);
				if (tagNameHasForbiddenCharacters) {
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
	
	public static Alert executeTagFilterAlert() {
		Alert executeTagFilterAlert = new Alert(AlertType.CONFIRMATION);
		executeTagFilterAlert.setTitle("Filter Tags");
		executeTagFilterAlert.setHeaderText("Do you want playlist to load media files having each filter tag?");
		executeTagFilterAlert.setContentText("Current playlist will be replaced with loaded media files.");
		executeTagFilterAlert.initModality(Modality.APPLICATION_MODAL);
		return executeTagFilterAlert;
	}

	public static String openFileDialogFileName(String fileTypeDescription, String fileTypeExtension) {
		String filePath = "";
		String fileTypeExtTemp = fileTypeExtension;
		if (!fileTypeExtension.startsWith("*.")) {
			fileTypeExtTemp = "*." + fileTypeExtension;
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
