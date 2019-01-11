# ASVidData
Desktop application that stores info (Tags, Path) about videos located on local harddrive.  

Application was made under Windows operating system and uses External Applications to display videos:
 - Windows Media Player
 - Media Player Classic (download at - https://mpc-hc.org/  also available in kazaa lite codec pack)

This application was not tested with different operating systems and above players are Windows only appliactions.
Therefore, it is recommended to not use it under different systems.

Programming and launching environement Setup:
 - Download and install MySQLInstaller from web page: https://dev.mysql.com/downloads/mysql/
 - Download this repository to your Java Programming Environement
 - Convert it to Maven project
 - Make sure your MySQL connection status is - running.






Progress and Overview of application:
At the moment application has UI with main menu that opens one of 3 selected main windows: Explorer, Database, Options;
Explorer - window that has small media player frame that displays most mp4 video files (some codecs are not compatible with default JavaFX MediaPlayer).
	Althou some video files are not supported by in-app media player user can still play em in external players.
	On the right side of media player is a playlist that consists of  file paths to added videos files.
	Videos can be added at the moment via right mouse button context menu, or by dragging file directly from windows explorer to the playlist area.
	Option to play single video file in external players is also stored in right-mouse-button context menu.
	Below the playlist are buttons that can open whole playlist in external media player.
	Above the playlist is expandable panel called Filter Tags.
	Currently it allows tags to be dropped inside Filter Tags expanded area.
	In future it will load all video files from database that have assigned every tag from Filter Tags panel area. This feature is yet to be implemented.
	Below the in-app media player frame are located controls for media player as well as Tags Picker button.
	Tags Picker button opens new panel that displays all tags as well as button to add a new tag.
	At the moment Tags from Tags Picker can be dragged into expanded panel of Filter Tags.
	There will be another panel that will allow to assign tags to currently selected media file. This feature is yet to be implemented.
Database - window added for data management.
	Currently its only added for future purposes.
Options - window for changing application settings.
	So far, here you can setup paths to executable files of Windows Media Player and Media Player Classic, depending on where those were installed.
	In future there will be option to set in-app media player behavior. 2 options have already been placed there, those options are not yet implemented.
	All options are saved and stored in settings.ini file that is located in same folder as this application executable was launched.
When application is beeing launched for the first time:
-File settings.ini is created in same folder that launched executable jar is present. Initially file stores some default settings. Most of em can be changed by user via application options window.
-MySQL Database with name AsVidData is beeing created (MySQL Database status has to be set to running)
-Empty Database Tables are beeing created for above database.
	Tables will store:
		-File paths to video files that ever been added to in-app media player playlist,
 		-Tag names added by user,
		-List of tags added to each video file.

To Do List:
-Set options that make external mediaplayers paths be set dynamically,
-Give ability to delete Tags from Tags Filter via drag-drop.
-Disallow already added tag to be placed again in Tags Filter.
-Set tags pick (add tag to file/filter list by tags) in Explorer view,
-Implement Tags adding to media files.
-Make new Drag-Drop panel to add Tags to selected video file.
-Make a field in explorer window for adding Tags to selected media file.
-Make TagsFilter loading videos from database that have tags from tags filter
-Add ability to assign pictures to tags.
-Create thumbnails from mediaplayer screen shots,
-Add small Thumbnail formatting from file on Hard Drive and mediaplayer screenshot.
-Implement small picture option for Tags and Media Files.

-Make option to automatically play another video in playlist when previous is finished.
-Set option pick for mediaplayer to play fragments/full length of Media.
-Make unsupported video files for in-app media player automatically be opened in one of external media players.
-Database table for more than 1 picture for media files.
-Add ability to rearrange Tags in Tags window via drag and drop.
-Add ability to reset application setup and data eg. database drop-and-create
-Add option to backup database data into a file.
-Add FullScreen for MediaPlayer.
-Fix issue with video not going into stop mode after finished playing.
-Fix video progressbar responsiveness.


Disclaimer: Application is still under development. Many features are yet not implemented. Some bugs may occure.
It is 1st JavaFX application I've ever made. Therefore, if you are advanced Full-Stack Java Developer you might see it beeing far from perfect.







