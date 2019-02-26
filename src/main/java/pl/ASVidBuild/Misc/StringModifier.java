/**
 * 
 */
package pl.ASVidBuild.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Soko³owski
 *
 */
public class StringModifier {


	public StringModifier() {
		
	}
	
	public static String[] split(char separator, String string) {
		List<String> substringsList = new ArrayList<>();
		String substring;
		int offset = 0;  
		int separatorPos = string.indexOf(separator, offset);
		
		while(separatorPos > -1) {
			separatorPos = string.indexOf(separator, offset)-offset;
			if(separatorPos > 0) {
				substring = string.substring(offset, offset+separatorPos);
			} else {
				if(separatorPos == 0 ) {
					substring = "";
				} else {
					substring = string.substring(offset, string.length());
				}
				
			}
			offset += separatorPos+1;
			substringsList.add(substring);	
		}
		
		
		return substringsList.toArray(new String[substringsList.size()]);
		
	}

}
