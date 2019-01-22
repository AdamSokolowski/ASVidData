
package pl.ASVidBuild.PanelController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.ASVidBuild.SettingsData;
import pl.ASVidBuild.PanelController.MainScreenController;
import pl.ASVidBuild.PlaylistConvert.*;
import pl.ASVidBuild.UI.UIHelper;
import pl.ASVidBuild.database.DbRepository;
import pl.ASVidBuild.database.dao.MediaFileDao;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class ExplorerScreenController {

	private String mpcExecPath = "C:/Program Files (x86)/K-Lite Codec Pack/MPC-HC64/mpc-hc64";

	private String windowsMediaPlayerExecPath = "C:/Program Files (x86)/Windows Media Player/wmplayer";

	@FXML
	private Button menuButton;

	@FXML
	private Button openListWMPlayer;

	@FXML
	private Button openListMPClassic;

	@FXML
	private Button buttonClearList;

	@FXML
	private MediaView mainMediaView;

	@FXML
	private HBox mainMediaViewPanel;

	@FXML
	private Button mainMediaViewPlay;

	@FXML
	private Button mainMediaViewStop;

	@FXML
	private Button mainMediaViewPrev;

	@FXML
	private Button mainMediaViewNext;

	@FXML
	private Slider mainMediaViewVolume;

	@FXML
	private Button mainMediaViewMute;

	@FXML
	private Button mainMediaViewFullScreen;

	@FXML
	private Button TagWindowButton;

	@FXML
	private Slider mainMediaViewPlayProgress;

	@FXML
	private ListView<String> vidPlayList;

	@FXML
	private ContextMenu vidPlayListMenu;

	@FXML
	private Pane filterTagsVidPlayList;

	@FXML
	private Pane fileTagsListPanel;

	@FXML
	private Button fileTagsListPanelAnyFileButton;

	@FXML
	private TitledPane filterTagsFrame;

	private MediaPlayer mediaPlayer;

	private String selectedFilePath = "";

	private List<Integer> fileTagsListPanelId = new LinkedList<Integer>();

	private List<Integer> filterTagsVidPlayListId = new LinkedList<Integer>();

	private SettingsData settingsData;

	private MainScreenController mainScreenController;

	private static final int TAG_WIDTH = 100;
	private static final int TAG_HEIGHT = 75;
	private static final int GAP_BTWEEN_TAGS = 3;
	private static final int TAGS_MAX_ROWS_COUNT = 1;

	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;

		initExplorerScreen();

	}

	private void initExplorerScreen() {
		settingsData = SettingsData.getInstance();
		mpcExecPath = settingsData.getMpcExecPath();
		windowsMediaPlayerExecPath = settingsData.getWindowsMediaPlayerExecPath();
		File file = new File(settingsData.getWorkFolder() + "\\LastPlaylist.asvpl");
		if (file.exists()) {
			vidPlayList.setItems(FXCollections
					.observableArrayList(LastPlayList.load(settingsData.getWorkFolder() + "\\LastPlaylist.asvpl")));
			System.out.println("Loaded list - " + settingsData.getWorkFolder() + "\\LastPlaylist.asvpl");
		}
		mainMediaViewVolume.setValue(settingsData.getSoundVolume());
		filterTagsFrame.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				tagsFilterFrameExpandedResize(newValue);
			}
		});

	}

	@FXML
	void menuButtonClick(ActionEvent event) {
		String[] listItems = new String[vidPlayList.getItems().size()];
		listItems = (String[]) vidPlayList.getItems().toArray(listItems);
		LastPlayList.save(listItems, settingsData.getWorkFolder() + "\\LastPlaylist.asvpl");
		System.out.println("Saved list - " + settingsData.getWorkFolder() + "\\LastPlaylist.asvpl");
		mainScreenController.loadMainMenu();

	}

	@FXML
	void openListMPClassicClick(ActionEvent event) {
		mediaPlayerListGenAndPlay("Media Player Classic");
	}

	@FXML
	void openListWMPlayerClick(ActionEvent event) {
		mediaPlayerListGenAndPlay("Windows Media Player");

	}

	@FXML
	void buttonClearListClick(ActionEvent event) {
		vidPlayList.getItems().clear();
	}

	public void tagsFilterFrameExpandedResize(Boolean isExpanded) {
		if (isExpanded) {
			filterTagsFrame.setPrefHeight(125);
		} else {
			filterTagsFrame.setPrefHeight(25);
		}

	}

	public void mediaPlayerListGenAndPlay(String mediaPlayerType) {
		// method converts list of videos in vidPlaylist to list compatible with given
		// type media player then launches it in that player. Converted list is stored
		// as a file on hard drive
		String[] listItems = new String[vidPlayList.getItems().size()];
		for (int i = 0; i < listItems.length; i++) {
			listItems[i] = vidPlayList.getItems().get(i).toString();
		}
		String appPath = "";
		try {
			appPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mediaPlayerType == "Windows Media Player") {
			WindowsMediaPlayerListGenerator.create(listItems, "ASVidData Autogenerated List",
					appPath + "\\WMPlist.wpl");
			launchExternalProg(windowsMediaPlayerExecPath, appPath + "\\WMPlist.wpl");
		} else {
			if (mediaPlayerType == "Media Player Classic") {
				MediaPlayerClassicListGenerator.create(listItems, "ASVidData Autogenerated List",
						appPath + "\\MPClist.mpcpl");
				launchExternalProg(mpcExecPath, appPath + "\\MPClist.mpcpl");

			}
		}
	}

	@FXML
	void menuPlayMPClassicClick(ActionEvent event) {
		launchExternalProg(mpcExecPath, vidPlayList.getSelectionModel().getSelectedItem().toString());
	}

	@FXML
	void menuPlayWMPlayerClick(ActionEvent event) {
		launchExternalProg(windowsMediaPlayerExecPath, vidPlayList.getSelectionModel().getSelectedItem().toString());

	}

	public Process launchExternalProg(String progPath, String filePath) {
		Process process = null;

		try {
			String command = "\"" + progPath + "\" \"" + filePath + "\"";
			System.out.println("Launched command - " + command);
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return process;
	}

	@FXML
	void menuAddFileToPlayListClick(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filterMp4 = new FileChooser.ExtensionFilter("mpeg4", "*.mp4");
		FileChooser.ExtensionFilter filterAvi = new FileChooser.ExtensionFilter("Avi", "*.avi");
		FileChooser.ExtensionFilter filterAll = new FileChooser.ExtensionFilter("All Files", "*.*");

		fileChooser.getExtensionFilters().addAll(filterMp4, filterAvi, filterAll);
		File file = fileChooser.showOpenDialog(null);

		addFileToVidPlayListAndDatabase(file);

	}

	private void addFileToVidPlayListAndDatabase(File file) {
		if (file != null) {
			String filePath = file.getAbsolutePath();
			vidPlayList.getItems().add(filePath);
			String[] filePathList = { filePath };
			MediaFileDao.addMediaFilesToDb(filePathList);
		}
	}

	@FXML
	void vidPlayListClick(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)
				&& vidPlayList.getSelectionModel().getSelectedItem().isEmpty() == false) {

			fileTagsListPanelId.clear();
			UIHelper.tagsListPanelClear(fileTagsListPanel, fileTagsListPanelAnyFileButton, GAP_BTWEEN_TAGS, TAG_WIDTH,
					TAG_HEIGHT);
			fileTagsListPanelLoadFileTags();
			playSelectedVidPlayListItem();
		}
	}

	private void playSelectedVidPlayListItem() {
		try {
			Path path = Paths.get(vidPlayList.getSelectionModel().getSelectedItem().toString());
			String filePath = path.toUri().toString();
			
			if (selectedFilePath.compareTo(filePath) != 0) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					System.out.println("Media file could not be opened.");
				}
				selectedFilePath = filePath;
				System.out.println("Opened file in MediaPlayer - " + filePath);
				Media media = new Media(filePath);
				mediaPlayer = new MediaPlayer(media);
				mainMediaView.setMediaPlayer(mediaPlayer);
				
				
				mediaPlayer.play();
				mediaPlayer.setVolume(mainMediaViewVolume.getValue() / 100);
				if (mainMediaViewMute.textProperty().isEqualTo("Unmute").getValue()) {
					mediaPlayer.muteProperty().set(true);
				}
				mainMediaViewPanel.setDisable(false);

				// mainMediaViewPlayProgress.maxProperty().bind(Bindings.createDoubleBinding(
				// () -> mediaPlayer.getTotalDuration().toSeconds(),
				// mediaPlayer.totalDurationProperty()));

				// mainMediaViewPlayProgress.valueProperty().bind(Bindings.createDoubleBinding(()
				// -> mediaPlayer.getCurrentTime().toSeconds(),
				// mediaPlayer.currentTimeProperty()));

				mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
					@Override
					public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
							Duration newValue) {
						mainMediaViewPlayProgress.setMax(newValue.toSeconds());
					}
				});

				mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
					@Override
					public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
							Duration newValue) {
						mainMediaViewPlayProgress.setValue(newValue.toSeconds());
					}
				});

				mediaPlayer.setOnEndOfMedia(new Runnable() {

					@Override
					public void run() {
						mediaPlayer.stop();
						mainMediaViewPlay.setText("Play");

					}

				});
				mediaPlayer.statusProperty().addListener(new ChangeListener<Status>() {

					@Override
					public void changed(ObservableValue<? extends Status> observable, Status oldValue,
							Status newValue) {
						System.out.println(newValue.toString());
					}

				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void mainMediaViewPlayClick(ActionEvent event) {
		if (mainMediaViewPlay.textProperty().isEqualTo("Play").getValue()) {
			mainMediaViewPlay.setText("Pause");
			mediaPlayer.play();
		} else {
			mainMediaViewPlay.setText("Play");
			mediaPlayer.pause();
		}
	}

	@FXML
	void mainMediaViewStopClick(ActionEvent event) {
		mediaPlayer.stop();
		mainMediaViewPlay.setText("Play");
	}

	@FXML
	void mainMediaViewPrevClick(ActionEvent event) {
		if (vidPlayList.getItems().isEmpty() == false) {
			int selectedItemIndex = vidPlayList.getSelectionModel().getSelectedIndex();
			if (selectedItemIndex > 0) {
				vidPlayList.getSelectionModel().clearAndSelect(selectedItemIndex - 1);
			} else {
				vidPlayList.getSelectionModel().clearAndSelect(vidPlayList.getItems().size() - 1);
			}
			playSelectedVidPlayListItem();
		}
	}

	@FXML
	void mainMediaViewNextClick(ActionEvent event) {
		if (vidPlayList.getItems().isEmpty() == false) {
			int selectedItemIndex = vidPlayList.getSelectionModel().getSelectedIndex();
			if (selectedItemIndex < vidPlayList.getItems().size() - 1) {
				vidPlayList.getSelectionModel().clearAndSelect(selectedItemIndex + 1);
			} else {
				vidPlayList.getSelectionModel().clearAndSelect(0);
			}
			playSelectedVidPlayListItem();
		}
	}

	@FXML
	void mainMediaViewVolumeClick(MouseEvent event) {
		mediaPlayer.setVolume(mainMediaViewVolume.getValue() / 100);
	}

	@FXML
	void mainMediaViewVolumeKeyClick(KeyEvent event) {
		mediaPlayer.setVolume(mainMediaViewVolume.getValue() / 100);
	}

	@FXML
	void mainMediaViewMuteClick(ActionEvent event) {
		if (mainMediaViewMute.textProperty().isEqualTo("Mute").getValue()) {
			mainMediaViewMute.setText("Unmute");
			mediaPlayer.muteProperty().set(true);
		} else {
			mainMediaViewMute.setText("Mute");
			mediaPlayer.muteProperty().set(false);
		}

	}

	@FXML
	void mainMediaViewFullScreenClick(ActionEvent event) {

	}

	@FXML
	void mainMediaViewPlayProgressClick(MouseEvent event) {
		mediaPlayerSeekToProgressSlider();
	}

	@FXML
	void mainMediaViewPlayProgressKeyClick(KeyEvent event) {
		mediaPlayerSeekToProgressSlider();
	}

	private void mediaPlayerSeekToProgressSlider() {
		mediaPlayer.pause();
		mediaPlayer.seek(Duration.seconds(mainMediaViewPlayProgress.getValue()));
		mediaPlayer.play();
	}

	@FXML
	void TagWindowButtonClick(ActionEvent event) {
		Scene scene = null;
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/TagScreen.fxml"));
		SplitPane splitPane = null;
		try {
			splitPane = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
		scene = new Scene(splitPane);
		Stage tagsStage = new Stage();
		tagsStage.setScene(scene);
		tagsStage.setTitle("Tags Palette");
		tagsStage.show();
	}

	@FXML
	void vidPlayListOnDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	@FXML
	void vidPlayListOnDragDropped(DragEvent event) {
		List<File> files = event.getDragboard().getFiles();

		for (int i = 0; i < files.size(); i++) {
			addFileToVidPlayListAndDatabase(files.get(i));
		}
	}

	@FXML
	void filterTagsVidPlayListDragDropped(DragEvent event) {

		Integer tagId = UIHelper.getDraggedTagId();
		if (!filterTagsVidPlayListId.contains(tagId)) {

			UIHelper.addTagToPane(TagDao.getTagById(tagId), filterTagsVidPlayList.getChildren().size() - 1,
					filterTagsVidPlayList, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, 0);
			filterTagsVidPlayListId.add(tagId);
		}
	}

	@FXML
	void filterTagsVidPlayListDragOver(DragEvent event) {
		if (UIHelper.isDraggedSourceATag()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	@FXML
	void fileTagsListPanelDragDropped(DragEvent event) {

		Integer tagId = UIHelper.getDraggedTagId();
		if (!fileTagsListPanelId.contains(tagId)) {

			UIHelper.addTagToPane(TagDao.getTagById(UIHelper.getDraggedTagId()), fileTagsListPanel.getChildren().size(),
					fileTagsListPanel, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, 0);
			fileTagsListPanelId.add(tagId);
			Tag tag = TagDao.getTagById(tagId);
			MediaFile mediaFile = MediaFileDao
					.getMediaFileByFilePath(vidPlayList.getSelectionModel().getSelectedItem().toString());
			MediaFileDao.AddTagToMediaFile(tag, mediaFile);
		}
	}

	@FXML
	void fileTagsListPanelDragOver(DragEvent event) {
		if (UIHelper.isDraggedSourceATag()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	private void fileTagsListPanelLoadFileTags() {
		MediaFile selectedMediaFile = MediaFileDao
				.getMediaFileByFilePath(vidPlayList.getSelectionModel().getSelectedItem().toString());
		List<Tag> selectedMediaFileTags = TagDao.getAllTagsOfMediaFile(selectedMediaFile);
		if (!selectedMediaFileTags.isEmpty()) {
			for (int i = 0; i < selectedMediaFileTags.size(); i++) {
				UIHelper.addTagToPane(selectedMediaFileTags.get(i), fileTagsListPanel.getChildren().size(),
						fileTagsListPanel, GAP_BTWEEN_TAGS, TAG_WIDTH, TAG_HEIGHT, 0);
				fileTagsListPanelId.add(selectedMediaFileTags.get(i).getId());
			}
		}
	}

}
