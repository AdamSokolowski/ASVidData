/**
 * 
 */
package pl.ASVidBuild.UI.TagContainer;

import javafx.scene.input.ClipboardContent;
import pl.ASVidBuild.UI.UIHelper;

/**
 * @author Adam Soko³owski
 *
 */
public class TagClipboard {

	private static boolean draggedSourceIsATag = false; // flag for controlling if drag-dropped item is a tag so it can be accepted in tags panel.
	private static int draggedTagId = 0;
	private static String draggedSourceParent = ""; // String representation of parent pane to dragged Tag

	public static int getDraggedTagId() {
		return draggedTagId;
	}

	public static void setDraggedTagId(int draggedTagId) {
		TagClipboard.draggedTagId = draggedTagId;
	}

	public static boolean isDraggedSourceATag() {
		return draggedSourceIsATag;
	}

	public static void setDraggedSourceIsATag(boolean draggedSourceIsATagFromTagsPicker) {
		draggedSourceIsATag = draggedSourceIsATagFromTagsPicker;
	}

	public static void setDraggedSourceParent(TagsPane ownerPane) {
		if (ownerPane == null) {
			draggedSourceParent = "";
		} else {
			draggedSourceParent = ownerPane.getId();
			System.out.println("draggedSourceParent: " + draggedSourceParent);
		}
	}

	public static String getDraggedSourceParent() {
		return draggedSourceParent;
	}

	public static void clearClipboardData() {
		ClipboardContent content = new ClipboardContent();
		content.clear();
	}

}
