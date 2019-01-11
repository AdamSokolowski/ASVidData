package pl.ASVidBuild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class SettingsData {



	private double soundVolume;
	private int mediaPreviewPlayOption = 0;
	private int autoPlayNextVidAfterCurrentIsOver = 0;
	private String workFolder = "";
	private String mpcExecPath = "C:/Program Files (x86)/K-Lite Codec Pack/MPC-HC64/mpc-hc64";
	private String windowsMediaPlayerExecPath = "C:/Program Files (x86)/Windows Media Player/wmplayer";

	private static SettingsData INSTANCE;
	
	private SettingsData() {
		loadFromFile();
	}
	
	public static SettingsData getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SettingsData();
		}
		return INSTANCE;
	}
	
	
	
	public double getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(double soundVolume) {
		this.soundVolume = soundVolume;
	}

	public String getMediaPreviewPlayOption() {
		switch (mediaPreviewPlayOption) {
		case 1:
			return "fragments";
		}
		return "full";
	}

	public void setMediaPreviewPlayOption(String mediaPreviewPlayOption) {

		switch (mediaPreviewPlayOption) {
		case "full":
			this.mediaPreviewPlayOption = 0;
			break;
		case "fragments":
			this.mediaPreviewPlayOption = 1;
			break;
		}
	}

	public boolean getAutoPlayNextVidAfterCurrentIsOver() {
		return autoPlayNextVidAfterCurrentIsOver==1;
	}

	public void setAutoPlayNextVidAfterCurrentIsOver(boolean autoPlayNextVidAfterCurrentIsOver) {
		if(autoPlayNextVidAfterCurrentIsOver) {
			this.autoPlayNextVidAfterCurrentIsOver = 1;
		} else {
			this.autoPlayNextVidAfterCurrentIsOver = 0;
		}
		
	}

	public String getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}
	
	

	public String getMpcExecPath() {
		return mpcExecPath;
	}

	public void setMpcExecPath(String mpcExecPath) {
		this.mpcExecPath = mpcExecPath;
	}

	public String getWindowsMediaPlayerExecPath() {
		return windowsMediaPlayerExecPath;
	}

	public void setWindowsMediaPlayerExecPath(String windowsMediaPlayerExecPath) {
		this.windowsMediaPlayerExecPath = windowsMediaPlayerExecPath;
	}

	public void saveToFile() {
		if (workFolder == "") {
			try {
				workFolder = new File(".").getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		PrintWriter out;
		
			try {
				out = new PrintWriter(workFolder + "\\settings.ini");
				out.println("SoundVolume = " + soundVolume);
				out.println("MediaPreviewPlayOption = " + mediaPreviewPlayOption);
				out.println("autoPlayNextVidAfterCurrentIsOver = " + autoPlayNextVidAfterCurrentIsOver);
				out.println("WorkFolder = " + workFolder);
				out.println("mpcExecPath = " + mpcExecPath);
				out.println("windowsMediaPlayerExecPath = " + windowsMediaPlayerExecPath);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Settings file could not be created. Make sure work folder exists and is not in read-only state and you have write access privilages.");
			}
			
	}
	
	public void loadFromFile() {
		File file = new File(workFolder + "\\settings.ini");
		
		if (file.exists()) {
			Scanner scan;
			try {
				scan = new Scanner(file);
				String s;
				String optionFlagName;
				while (scan.hasNextLine()) {
					s = scan.nextLine();
					optionFlagName = s.substring(0, s.indexOf(" = ")-1);
					switch(optionFlagName) {
					case "SoundVolume":
						soundVolume = Double.parseDouble(s.substring(s.indexOf(" = ")+3));
						break;
					case "MediaPreviewPlayOption":
						mediaPreviewPlayOption = Integer.parseInt(s.substring(s.indexOf(" = ")+3));
						break;
					case "autoPlayNextVidAfterCurrentIsOver":
						autoPlayNextVidAfterCurrentIsOver = Integer.parseInt(s.substring(s.indexOf(" = ")+3));
					case "WorkFolder":
						workFolder = s.substring(s.indexOf(" = ")+3);
						break;
					case "mpcExecPath":
						mpcExecPath = s.substring(s.indexOf(" = ")+3);
						break;
					case "windowsMediaPlayerExecPath":
						windowsMediaPlayerExecPath = s.substring(s.indexOf(" = ")+3);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			soundVolume = 40.0;
			mediaPreviewPlayOption = 0;
			autoPlayNextVidAfterCurrentIsOver = 0;
			workFolder = "";
			mpcExecPath = "C:/Program Files (x86)/K-Lite Codec Pack/MPC-HC64/mpc-hc64";
			windowsMediaPlayerExecPath = "C:/Program Files (x86)/Windows Media Player/wmplayer";
			saveToFile();
		}
	}
	
}
