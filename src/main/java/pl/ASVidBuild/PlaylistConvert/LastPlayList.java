package pl.ASVidBuild.PlaylistConvert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class LastPlayList {
	public static String[] load(String fileName) {
		File file = new File(fileName);
		
		if (file.exists()) {
			Scanner scan;
			List<String> listItems = new ArrayList<String>();
			try {
				scan = new Scanner(file);
				while (scan.hasNextLine()) {
					listItems.add(scan.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return listItems.toArray(new String[listItems.size()]);
		} else {
			return null;
		}
	}

	public static void save(String[] listItems, String fileName) {
		PrintWriter out;
		try {
			out = new PrintWriter(fileName);
			for (int i = 0; i < listItems.length; i++) {
				out.println(listItems[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
