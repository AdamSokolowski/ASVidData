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
		String sql = "";
		if (mediaFileExistsInDb(filePath)) {
			if (autoUpdateWhenMediaFileExistsInDb) {
				/*
				 * sql = "UPDATE MediaFile SET picturePath = '" +
				 * picturePath.replace("\\", "\\\\") + "', fileRating = " + fileRating +
				 * " WHERE filePath = '" + filePath.replace("\\", "\\\\") + "'";
				 * System.out.println(sql);
				 */
				sql = DbRepository.mySQLQueryGeneratorUpdateRecordValues("MediaFile", "picturePath, fileRating",
						"'" + picturePath.replace("\\", "\\\\") + "', " + fileRating,
						"filePath='" + filePath.replace("\\", "\\\\") + "'");
			}
		} else {

			/*
			 * sql = "INSERT INTO MediaFile(filePath, picturePath, fileRating) VALUES('" +
			 * filePath.replace("\\", "\\\\") + "', '" +
			 * picturePath.replace("\\", "\\\\") + "', " + fileRating + ")";
			 * System.out.println(sql);
			 */
			sql = DbRepository.mySQLQueryGeneratorAddRecord("MediaFile", "filePath, picturePath, fileRating", "'"
					+ filePath.replace("\\", "\\\\") + "', '" + picturePath.replace("\\", "\\\\") + "', " + fileRating);

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
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void addMediaFilesToDb(String[] filePathList) {

		try {
			for (int i = 0; i < filePathList.length; i++) {
				if (!mediaFileExistsInDb(filePathList[i])) {
					String sqlAddFileToDb = DbRepository.mySQLQueryGeneratorAddRecord("MediaFile", "filePath",
							"'" + filePathList[i].replace("\\", "\\\\") + "'");
					conn = DbUtil.getConn();
					Statement statement = conn.createStatement();
					statement.executeUpdate(sqlAddFileToDb);
					System.out.println(sqlAddFileToDb);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;

	}

	public static boolean mediaFileExistsInDb(String filePath) {
		return getMediaFileByFilePath(filePath) != null;
	}

	public static MediaFile getMediaFileByFilePath(String filePath) {
		String sql = "SELECT * FROM MediaFile WHERE filePath='" + filePath.replace("\\", "\\\\") + "'";
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
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

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
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;

	}

	public static void AddTagToMediaFile(Tag tag, MediaFile mediaFile) {
		String sql = "INSERT INTO MediaFile_Tag(mediafile_id, tag_id) VALUES(" + mediaFile.getId() + ", " + tag.getId()
				+ ")";
		try {
			conn = DbUtil.getConn();
			Statement stmt = conn.createStatement();
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
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;

	}

	public static String mediaFileDataToSting(MediaFile mf) {
		return mf.getId() + ", " + mf.getFilePath() + ", " + mf.getPicturePath() + ", " + mf.getFileRating();
	}
}
