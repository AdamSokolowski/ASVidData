package pl.ASVidBuild.PlaylistConvert;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * 
 * @author Adam Soko³owski
 *
 */

public class WindowsMediaPlayerListGenerator {
	public static void create(String[] listItems, String listTitle, String filePath) {
		PrintWriter out;
		try {
			out = new PrintWriter(filePath);

			out.println("<?wpl version=\"1.0\"?>");
			out.println("<smil>");
			out.println("    <head>");
			out.println(
					"        <meta name=\"Generator\" content=\"Microsoft Windows Media Player -- 12.0.14393.187\"/>");
			out.println(String.format("        <meta name=\"ItemCount\" content=\"%s\"/>",
					Integer.toString(listItems.length)));
			out.println(String.format("        <title>%s</title>", listTitle));
			out.println("    </head>");
			out.println("    <body>");
			out.println("        <seq>");
			String s = "";
			for (int i = 0; i < listItems.length; i++) {
				s = listItems[i].replaceAll("&", "&amp;").replaceAll("'", "&apos;");
				out.println("            <media src=\"" + s + "\"/>");
			}
			out.print("        </seq>\r\n" + "    </body>\r\n" + "</smil>");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
