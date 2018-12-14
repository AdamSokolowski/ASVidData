package pl.ASVidBuild.database.pojo;

public class Tag {
	private int id;
	private String tagName;
	private String picturePath="";
	public Tag() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		if(picturePath!=null) {
		this.picturePath = picturePath;}
	}
	
	
	
	
}
