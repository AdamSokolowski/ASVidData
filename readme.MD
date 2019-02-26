# ASVidData
Desktop application that stores info (Tags, Path) about videos located on local harddrive.  

Application was made under Windows operating system and uses External Applications to display videos:
 - Windows Media Player
 - Media Player Classic (download at - https://mpc-hc.org/  also available in kazaa lite codec pack)

This application was not tested with different operating systems and above players are Windows only appliactions.
Therefore, it is recommended to not use it under different systems.

# Programming and launching environement Setup:
 - Download and install MySQLInstaller from web page: https://dev.mysql.com/downloads/mysql/
 - Download this repository to your Java Programming Environement
 - Convert it to Maven project
 - Set user and password, matching your MySQL database access, inside java pl.ASVidBuild.database.DbSet interface.
 - Make sure your MySQL connection status is - running.
 
#### If you don't want to use your MySQL root account for this application then you can create new user.
Open MySQL command line and type in:
```
CREATE USER 'asviddatauser'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON asviddata.* TO 'asviddatauser'@'%';
```
Then change in pl.ASVidBuild.database.DbSet interface user to "asviddatauser" and password to "pass";



# Progress and Overview of application:
At the moment application has UI with main menu that opens one of 3 selected main windows:
- Explorer,
- Database,
- Options;
### Explorer - Window that has small media player frame that displays most mp4 video files (some codecs are not compatible with default JavaFX MediaPlayer).
Althou some video files are not supported by in-app media player user can still play em in external players.
On the right side of media player is a playlist that consists of  file paths to added videos files.
Videos can be added at the moment via right mouse button context menu, or by dragging file directly from windows explorer to the playlist area.
Option to play single video file in external players is also stored under right-mouse-button context menu.
Below the playlist are buttons that can open whole playlist in external media player.
Above the playlist is expandable panel called Filter Tags.
It allows tags to be dropped inside Filter Tags expanded area. To remove tags from this panel drag and drop them on trash icon.
When Filer Tags panel is closed playlist items are replaced with all video files from database that have assigned every tag from Filter Tags panel area.
Below the in-app media player frame are located controls for media player as well as Tags Picker button.
Tags Picker button opens new panel that displays all tags. Theres also a button to add a new tag.
Tags from Tags Picker can be dragged into expanded panel of Filter Tags and panel of Selected File Tags.
Panel of Selected File Tags allow to assign tags to currently selected media file. To remove tags from file just drag them to trash can icon.
### Database - window added for data management.
Theres an option to save database to a file and restore the database data from saved file.
### Options - window for changing application settings.
So far, here you can setup paths to executable files of Windows Media Player and Media Player Classic, depending on where those were installed.
In future there will be option to set in-app media player behavior. 2 options have already been placed there, those options are not yet implemented.
All options are saved and stored in settings.ini file that is located in same folder as this application executable was launched.
When application is beeing launched for the first time:
- File settings.ini is created in same folder that launched executable jar is present. Initially file stores some default settings. Most of em can be changed by user via application options window.
- MySQL Database with name AsVidData is beeing created (MySQL Database status has to be set to running)
- Empty Database Tables are beeing created for above database.
#### Database Tables will store:
- File paths to video files that ever been added to in-app media player playlist,
- Tag names added by user,
- List of tags added to each video file.

# To Do List:
- Add ability to assign pictures to tags.
- Create thumbnails from mediaplayer screen shots,
- Add small Thumbnail formatting from file on Hard Drive and mediaplayer screenshot.
- Implement small picture option for Tags and Media Files.
- Make option to automatically play another video in playlist when previous is finished.
- Set option pick for mediaplayer to play fragments/full length of Media.
- Make unsupported video files for in-app media player automatically be opened in one of external media players.
- Database table for more than 1 picture for media files.
- Add ability to rearrange Tags in Tags window via drag and drop.
- Add ability to reset application setup and data eg. database drop-and-create
- Add FullScreen for MediaPlayer.
- Fix video progressbar responsiveness.


# Disclaimer:
Application is still under development. Many features are yet not implemented.
It is 1st JavaFX application I've ever made.






