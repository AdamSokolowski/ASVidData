package pl.ASVidBuild.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.ASVidBuild.database.DbUtil;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

public class TagDao {

	private static Connection conn = null;

	private static Tag createTagObject(ResultSet rs) {
		Tag tag = new Tag();

		try {
			tag.setId(rs.getInt("id"));
			tag.setTagName(rs.getString("tagName"));
			tag.setPicturePath(rs.getString("picturePath"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public static Tag createTagObject(String tagName, String picturePath) {
		Tag tag = new Tag();

		tag.setTagName(tagName);
		tag.setPicturePath(picturePath);
		return tag;
	}
	
	
	public static void addTagToDb(Tag tag, boolean autoUpdateWhenTagExistsInDb) {
		String tagName = tag.getTagName();
		String picturePath = tag.getPicturePath();
		String sql = "";
		if (getTagByTagName(tagName) == null) { // media file does not exist in database
			sql = "INSERT INTO Tag(tagName, picturePath) VALUES('" + tagName
					+ "',  '" + picturePath.replace("\\", "\\\\") + "')";
			System.out.println(sql);

		} else {
			if (autoUpdateWhenTagExistsInDb) {
				sql = "UPDATE Tag SET picturePath = '" + picturePath.replace("\\", "\\\\") + "' WHERE tagName = '" + tagName + "'";
				System.out.println(sql);
			}
		}
		if (!sql.equals("")) {
			try {
				conn = DbUtil.getConn();
				Statement stmt = conn.createStatement();
				System.out.println(sql);
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void deleteTagfromDb(int id){
		String sql = "DELETE FROM Tag WHERE id="+id;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Tag getTagById(int id) {
		String sql = "SELECT * FROM Tag WHERE id=" + id;
		Tag result = null;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result = createTagObject(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;

	}

	public static Tag getTagByTagName(String tagName) {

			String sql = "SELECT * FROM Tag WHERE tagName='" + tagName + "'";
			Tag result = null;
			try {
				conn = DbUtil.getConn();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					result = createTagObject(rs);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			return result;

		}
	
	public static List<Tag> getAllTags() {
		String sql = "SELECT * FROM Tag";
		List<Tag> result = new ArrayList<Tag>();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createTagObject(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;

	}
	
	public static List<Tag> getAllTagsOfMediaFile(MediaFile mediaFile){
		String sql = "SELECT Tag.* FROM Tag "
				+ "JOIN MediaFile_Tag ON Tag.id=MediaFile_Tag.tag_id "
				+ "JOIN MediaFile ON MediaFile_Tag.mediafile_id=MediaFile.id WHERE MediaFile.id="+mediaFile.getId();
		List<Tag> result = new ArrayList<Tag>();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createTagObject(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}		
		
		
		return result;
		
	}
	
	
	
	public static String TagDataToSting(Tag tag) {
		return tag.getId() + ", " + tag.getTagName() + ", " + tag.getPicturePath();
	}
}


