
package pl.ASVidBuild.PanelController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pl.ASVidBuild.SettingsData;
import pl.ASVidBuild.PanelController.MainScreenController;
import pl.ASVidBuild.PlaylistConvert.*;
import pl.ASVidBuild.UI.UIHelper;
import pl.ASVidBuild.UI.TagContainer.TagClipboard;
import pl.ASVidBuild.UI.TagContainer.TagsPane;
import pl.ASVidBuild.UI.TagContainer.TagsPane.ButtonPlaceHolderType;
import pl.ASVidBuild.database.DbRepository;
import pl.ASVidBuild.database.dao.MediaFileDao;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * 
 * @author Adam Sokołowski
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

	private TagsPane fileTagsListPanel;

	@FXML
	private ScrollPane fileTagsListScrollPanel;

	@FXML
	private ScrollPane filterTagsScrollPanel;

	private TagsPane filterTagsPanel;

	@FXML
	private Button fileTagsListPanelAnyFileButton;

	@FXML
	private TitledPane filterTagsFrame;

	@FXML
	private ImageView tagsTrash;

	private MediaPlayer mediaPlayer;

	private String selectedFilePath = "";

	private boolean tagWindowButtonOpened = false;
	
	private MediaPlayer.Status playerStatus = Status.UNKNOWN;
	
	private Stage tagsStage;

	private SettingsData settingsData;

	private MainScreenController mainScreenController;

	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;

		initExplorerScreen();

	}

	private void initExplorerScreen() {
		settingsData = SettingsData.getInstance();
		mpcExecPath = settingsData.getMpcExecPath();
		windowsMediaPlayerExecPath = settingsData.getWindowsMediaPlayerExecPath();
		File file = new File(settingsData.getWorkFolder() + "\\LastPlaylist.asvpl");

		fileTagsListPanel = new TagsPane(fileTagsListScrollPanel);
		fileTagsListPanel.setId("fileTagsListPanel");
		fileTagsListPanel.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (TagClipboard.isDraggedSourceATag()) {
					event.acceptTransferModes(TransferMode.ANY);
				}
			}
		});
		fileTagsListPanel.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Integer tagId = TagClipboard.getDraggedTagId();
				if (fileTagsListPanel.tagFoundInTagItems(tagId) == -1) {
					Tag tag = TagDao.getTagById(tagId);
					MediaFile mediaFile = MediaFileDao
							.getMediaFileByFilePath(vidPlayList.getSelectionModel().getSelectedItem().toString());
					MediaFileDao.addTagToMediaFile(tag, mediaFile);
					fileTagsListPanel.addTag(tag);
				}
			}
		});

		filterTagsPanel = new TagsPane(filterTagsScrollPanel, ButtonPlaceHolderType.EMPTY_TAG_LIST, "No Tags Added");
		filterTagsPanel.setId("filterTagsPanel");
		filterTagsPanel.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (TagClipboard.isDraggedSourceATag()) {
					event.acceptTransferModes(TransferMode.ANY);
				}
			}
		});
		filterTagsPanel.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Integer tagId = TagClipboard.getDraggedTagId();
				if (filterTagsPanel.tagFoundInTagItems(tagId) == -1) {
					Tag tag = TagDao.getTagById(tagId);
					filterTagsPanel.addTag(tag);
				}

			}

		});

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
				if (newValue == false && filterTagsPanel.wereTagsModified()) {
					Alert executeTagFilterAlert = UIHelper.executeTagFilterAlert();
					Optional<ButtonType> buttonPressed = executeTagFilterAlert.showAndWait();
					if (buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
						executeFilterTagsQuery();
						filterTagsPanel.setTagsUnmodified();
					}
				}
			}

		});

	}

	private void executeFilterTagsQuery() {
		Tag[] tags = filterTagsPanel.getTags();
		List<MediaFile> queryMediaFiles;
		if (tags == null) {
			queryMediaFiles = MediaFileDao.getAllMediaFiles();
		} else {
			queryMediaFiles = MediaFileDao.getAllMediaFilesTaggedHavingAllTags(tags);
		}
		vidPlayList.getItems().clear();
		for (int i = 0; i < queryMediaFiles.size(); i++) {

			vidPlayList.getItems().add(queryMediaFiles.get(i).getFilePath());
		}

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

			reloadSelectedVidFileTags();

			playSelectedVidPlayListItem();
		}
	}


	private void reloadSelectedVidFileTags() {
		fileTagsListPanel.clearTagItemsList();

		String filePath = vidPlayList.getSelectionModel().getSelectedItem().toString();
		fileTagsListPanel.loadFileTags(filePath);
	}
	
	private void playSelectedVidPlayListItem() {
		try {
			Path path = Paths.get(vidPlayList.getSelectionModel().getSelectedItem().toString());
			String filePath = path.toUri().toString();

			if (selectedFilePath.compareTo(filePath) != 0) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
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
						if(mainMediaViewPlayProgress.getValue() < newValue.toSeconds()) {
							mainMediaViewPlayProgress.setValue(newValue.toSeconds());
						}
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
						playerStatus = newValue;
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
			reloadSelectedVidFileTags();
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
			reloadSelectedVidFileTags();
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
		Slider progressBar = (Slider)event.getSource();
		double value;
		value = (event.getX()-9) * (progressBar.getMax() / (progressBar.getWidth()-19));
		
		progressBar.setValue(value); 
		
		mediaPlayerSeekToProgressSlider();
	}

	@FXML
	void mainMediaViewPlayProgressKeyClick(KeyEvent event) {
		mediaPlayer.pause();
		mediaPlayerSeekToProgressSlider();
	}

	private void mediaPlayerSeekToProgressSlider() {

		mediaPlayer.seek(Duration.seconds(mainMediaViewPlayProgress.getValue()));

	}

	@FXML
	void TagWindowButtonClick(ActionEvent event) {

		if (tagWindowButtonOpened) {
			tagsStage.toFront();
		} else

		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/TagScreen.fxml"));
			SplitPane splitPane = null;
			try {
				splitPane = loader.load();

			} catch (IOException e) {
				e.printStackTrace();
			}
			Scene sceneTags = new Scene(splitPane);
			tagsStage = new Stage();
			tagsStage.setScene(sceneTags);
			tagsStage.setTitle("Tags Palette");
			tagsStage.show();
			tagWindowButtonOpened = true;
			tagsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {

					tagWindowButtonOpened = false;
				}

			});
		}

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
	void tagsTrashDragDropped(DragEvent event) {
		Integer tagId = TagClipboard.getDraggedTagId();
		Tag tag = TagDao.getTagById(tagId);

		if (TagClipboard.getDraggedSourceParent().equals(fileTagsListPanel.getId())) {
			MediaFile selectedMediaFile = MediaFileDao
					.getMediaFileByFilePath(vidPlayList.getSelectionModel().getSelectedItem().toString());

			MediaFileDao.deleteTagFromMediaFile(tag, selectedMediaFile);

			reloadSelectedVidFileTags();

			System.out.println("Tag " + tag.getTagName() + " is no longer assigned to media file: "
					+ vidPlayList.getSelectionModel().getSelectedItem().toString());

		} else {
			if (TagClipboard.getDraggedSourceParent().equals(filterTagsPanel.getId())) {
				filterTagsPanel.removeTagfromTagsPaneLayout(tag);
			}
		}
	}

	@FXML
	void tagsTrashDragOver(DragEvent event) {
		if (TagClipboard.isDraggedSourceATag()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

}
