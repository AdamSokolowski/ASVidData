/**
 * 
 */
package pl.ASVidBuild.UI.TagContainer;

import java.nio.file.Paths;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import pl.ASVidBuild.UI.UIHelper;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * @author Adam Soko³owski
 *
 */
public class TagNode {
	private Tag tag;
	private Node node;

	/**
	 * 
	 */
	public TagNode(Tag tag, TagsPane ownerTagsPane) {
		this.tag = tag;

		if (tag.getPicturePath().equals("")) {
			Button button = new Button(tag.getTagName());

			button.setId(String.valueOf(tag.getId()));
			this.node = button;
			button.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					Dragboard dragB = button.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString(button.getId());
					dragB.setContent(content);
					TagClipboard.setDraggedSourceIsATag(true);
					TagClipboard.setDraggedTagId(tag.getId());
					TagClipboard.setDraggedSourceParent(ownerTagsPane);
					event.consume();
				}

			});

		} else {
			ImageView imageView = new ImageView();
			Image img = new Image(Paths.get(tag.getPicturePath()).toUri().toString());
			imageView.setImage(img);
			imageView.setId(String.valueOf(tag.getId()));
			Tooltip tooltip = new Tooltip(tag.getTagName());
			tooltip.setFont(new Font("Arial", 16));
			Tooltip.install(imageView, tooltip);
			imageView.setOnDragDetected(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					Dragboard dragB = imageView.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString(imageView.getId());
					dragB.setContent(content);
					TagClipboard.setDraggedSourceIsATag(true);
					TagClipboard.setDraggedTagId(tag.getId());
					TagClipboard.setDraggedSourceParent(ownerTagsPane);
					event.consume();
				}

			});

		}
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getTagName() {
		return tag.getTagName();
	}

	public String getTagPicturePath() {
		return tag.getPicturePath();
	}

}
