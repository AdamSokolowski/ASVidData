package pl.ASVidBuild.PlaylistConvert;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MediaPlayerClassicListGenerator {
	public static void create(String[] listItems, String listTitle, String filePath) {
		PrintWriter out;
		try {
			out = new PrintWriter(filePath);

			out.println("MPCPLAYLIST");
			
			for (int i = 0; i < listItems.length; i++) {
				out.println(Integer.toString(i+1)+",type,0");
				out.println(Integer.toString(i+1)+",filename," + listItems[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
