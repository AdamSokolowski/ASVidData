/**
 * 
 */
package pl.ASVidBuild.database;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.List;
import java.util.Scanner;

import pl.ASVidBuild.Misc.StringModifier;
import pl.ASVidBuild.database.dao.MediaFileDao;
import pl.ASVidBuild.database.dao.TagDao;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

/**
 * @author Adam Soko³owski
 *
 *
 *@fileStructure
 *<pre>
 *Tags
 *tag1RecordLength;tag1ID;tag1Name;tag1PicturePath;
 *...
 *tagNRecordLength;tagNID;tagNName;tagNPicturePath;
 *MediaFiles
 *mf1Recordlength;mf1ID;mf1FilePath;mf1PicturePath;mf1FileRating;mf1Tag1;...;mf1TagN;
 *...
 *mfNRecordlength;mfNID;mfNFilePath;mfNPicturePath;mfNFileRating;mfNTag1;...;mfNTagN;
 *</pre>
 */
public class DBFileBackup {

	private enum RecordDataType {
		NONE, MEDIAFILE, TAG
	}
	
	public DBFileBackup() {}
	
	public static void DBToFile(String fileName) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(fileName);
		int recordLength;
		out.println("ASVidData Database Backup file v.1.0");
		out.println("Tags");
		
		List<Tag> tags = TagDao.getAllTags();
		Tag currentTag;
		recordLength = 4;
		for (int i = 0; i<tags.size(); i++) {
			currentTag = tags.get(i);
			out.println(recordLength+";"+currentTag.getId()+";"+currentTag.getTagName()+";"+currentTag.getPicturePath()+";");
		}
		
		out.println("MediaFiles");
		
		List<MediaFile> mediaFiles = MediaFileDao.getAllMediaFiles();
		MediaFile currentMediaFile;
		String mediaFileTags = "";
		for (int i = 0; i<mediaFiles.size(); i++) {
			currentMediaFile = mediaFiles.get(i);
			
			List<Tag> currentMediaFileTags = TagDao.getAllTagsOfMediaFile(currentMediaFile);
			mediaFileTags = "";
			for(int j = 0; j<currentMediaFileTags.size(); j++) {
				mediaFileTags += currentMediaFileTags.get(j).getTagName()+";";
			}
			recordLength = 5+currentMediaFileTags.size();
			
			out.println(recordLength+";"+currentMediaFile.getId()+";"+currentMediaFile.getFilePath()+";"+currentMediaFile.getPicturePath()+";"+currentMediaFile.getFileRating()+";"+mediaFileTags);
		}
		
		out.close();

	}
	
	public static void FileToDB(String fileName) throws FileNotFoundException {
		if(DbRepository.dropAllTables() &&	DbRepository.initDatabaseTables()) {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			String line;
			String[] lineRecords;
			int recordIterator = 0;
			MediaFile currentMediaFile = new MediaFile();
			Tag currentTag = new Tag();
			RecordDataType recordDataType = RecordDataType.NONE;
			while(scan.hasNextLine()) {
				line = scan.nextLine();
				if (!line.contains(";")) {
					recordIterator = 0;
					switch (line) {
					case "MediaFiles":
						recordDataType = RecordDataType.MEDIAFILE;
						break;
					case "Tags":
						recordDataType = RecordDataType.TAG;
					}
					
				} else {
					lineRecords = StringModifier.split(';', line);
					switch(recordDataType) {
					case TAG:
						currentTag.setTagName(lineRecords[2]);
						currentTag.setPicturePath(lineRecords[3]);
						TagDao.addTagToDb(currentTag, true);
						break;
					case MEDIAFILE:
						recordIterator++;
						currentMediaFile.setId(recordIterator);
						currentMediaFile.setFilePath(lineRecords[2]);
						currentMediaFile.setPicturePath(lineRecords[3]);
						currentMediaFile.setFileRating((byte)Integer.parseInt(lineRecords[4]));
						MediaFileDao.addMediaFileToDb(currentMediaFile, false);
						int i = 5;
						while(i<lineRecords.length-1) {
							MediaFileDao.addTagToMediaFile(TagDao.getTagByTagName(lineRecords[i]), currentMediaFile);
							i++;
						}					

					}
					
				}
				
			}
			
			
		} else {
			System.out.println("Failed to alter Database tables.");
		}
	}

	

	
}
