package pl.ASVidBuild.database.pojo;

public class MediaFile {
	private int id;
	private String filePath = "";
	private String picturePath = "";
	private byte fileRating = 0;
	public MediaFile() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		if(filePath!=null) {
		this.filePath = filePath;}else {this.filePath="";}
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		if(picturePath!=null) {
		this.picturePath = picturePath;}else {this.picturePath="";}
	}
	public byte getFileRating() {
		return fileRating;
	}
	public void setFileRating(byte fileRating) {
		this.fileRating = fileRating;
	}
	
	
	
	
}
