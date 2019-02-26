package pl.ASVidBuild.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.ASVidBuild.database.DbRepository;
import pl.ASVidBuild.database.DbUtil;
import pl.ASVidBuild.database.pojo.MediaFile;
import pl.ASVidBuild.database.pojo.Tag;

public class MediaFileDao {

	private static Connection conn = null;

	private static MediaFile createMediaFileObject(ResultSet rs) {
		MediaFile mf = new MediaFile();

		try {
			mf.setId(rs.getInt("id"));
			mf.setFilePath(rs.getString("filePath"));
			mf.setPicturePath(rs.getString("picturePath"));
			mf.setFileRating(rs.getByte("fileRating"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mf;
	}

	public static MediaFile createMediaFileObject(String filePath, String picturePath, byte fileRating) {
		MediaFile mf = new MediaFile();
		mf.setFilePath(filePath);
		mf.setPicturePath(picturePath);
		mf.setFileRating(fileRating);
		return mf;
	}

	public static void addMediaFileToDb(MediaFile mediaFile, boolean autoUpdateWhenMediaFileExistsInDb) {
		String filePath = mediaFile.getFilePath();
		String picturePath = mediaFile.getPicturePath();
		byte fileRating = mediaFile.getFileRating();
		try {
			conn = DbUtil.getConn();
			if (mediaFileExistsInDb(filePath)) {
				
				if (autoUpdateWhenMediaFileExistsInDb) {
					DbRepository.mySQLUpdateRecord("MediaFile", "picturePath, fileRating",
							picturePath + ", " + fileRating,
							"filePath='" + filePath.replace("\\", "\\\\") + "'", conn);
				}
			} else {
				DbRepository.mySQLAddRecord("MediaFile", "filePath, picturePath, fileRating",
						filePath + ", " + picturePath + ", "
								+ fileRating,
						conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				DbUtil.releaseConnection();
			}
		}

	}

	public static void addMediaFilesToDb(String[] filePathList) {

		try {
			for (int i = 0; i < filePathList.length; i++) {
				if (!mediaFileExistsInDb(filePathList[i])) {
					conn = DbUtil.getConn();
					DbRepository.mySQLAddRecord("MediaFile", "filePath", filePathList[i], conn);

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				DbUtil.releaseConnection();
			}
		}
	}

	public static void deleteMediaFilefromDb(int id) {
		String sql = "DELETE FROM MediaFile WHERE id=" + id;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static MediaFile getMediaFileById(int id) {
		String sql = "SELECT * FROM MediaFile WHERE id=" + id;
		MediaFile result = null;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result = createMediaFileObject(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}
		return result;

	}

	public static boolean mediaFileExistsInDb(String filePath) {
		return getMediaFileByFilePath(filePath) != null;
	}

	public static boolean mediaFileTagAlreadyAdded(MediaFile mf, Tag tag) {

		String sql = "SELECT * from MediaFile " + "JOIN MediaFile_Tag ON MediaFile.id=MediaFile_Tag.MediaFile_id "
				+ "JOIN Tag ON Tag.id=MediaFile_Tag.Tag_id WHERE MediaFile.id=" + mf.getId() + " AND Tag.id="
				+ tag.getId();
		boolean result = false;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}
		return result;
	}

	public static MediaFile getMediaFileByFilePath(String filePath) {
		String filePath4SQL = "'" + filePath.replace("\\", "\\\\") + "'";
		String sql = "SELECT * FROM MediaFile WHERE filePath= " + filePath4SQL;
		MediaFile result = null;
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result = createMediaFileObject(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}
		return result;

	}

	public static List<MediaFile> getAllMediaFiles() {
		String sql = "SELECT * FROM MediaFile";
		List<MediaFile> result = new ArrayList<MediaFile>();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createMediaFileObject(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}
		return result;

	}

	public static void addTagToMediaFile(Tag tag, MediaFile mediaFile) {
		String sql = "INSERT INTO MediaFile_Tag(mediafile_id, tag_id) VALUES(" + mediaFile.getId() + ", " + tag.getId() + ")";
		System.out.println(sql);
		if (!mediaFileTagAlreadyAdded(mediaFile, tag)) {
			try {
				conn = DbUtil.getConn();
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DbUtil.releaseConnection();
			}
		}
	}

	public static void deleteTagFromMediaFile(Tag tag, MediaFile mediaFile) {
		String sql = "DELETE FROM MediaFile_Tag WHERE MediaFile_id = " + mediaFile.getId() + " AND Tag_id = "
				+ tag.getId();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}
	}

	public static List<MediaFile> getAllMediaFilesTaggedWithTag(Tag tag) {
		String sql = "SELECT MediaFile.* FROM MediaFile "
				+ "JOIN MediaFile_Tag ON MediaFile.id=MediaFile_Tag.mediafile_id "
				+ "JOIN Tag ON MediaFile_Tag.tag_id=Tag.id WHERE Tag.id=" + tag.getId();
		List<MediaFile> result = new ArrayList<MediaFile>();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createMediaFileObject(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();

		}

		return result;

	}

	public static List<MediaFile> getAllMediaFilesTaggedHavingAllTags(Tag[] tags) {
		String sqlTagsIds = "";
		for (int i = 0; i < tags.length - 1; i++) {
			sqlTagsIds += tags[i].getId() + ", ";
		}
		sqlTagsIds += tags[tags.length - 1].getId();

		String sql = "SELECT mediafile.* FROM mediafile JOIN MediaFile_Tag ON MediaFile.id=MediaFile_Tag.mediafile_id JOIN Tag ON MediaFile_Tag.tag_id=Tag.id"
				+ " WHERE Tag.id in (" + sqlTagsIds + ") GROUP BY filePath HAVING COUNT(Tag.id) = " + tags.length;
		List<MediaFile> result = new ArrayList<MediaFile>();
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createMediaFileObject(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.releaseConnection();
		}

		return result;
	}

}
