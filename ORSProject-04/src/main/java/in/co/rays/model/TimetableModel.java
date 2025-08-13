package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for Timetable management. Handles CRUD operations and
 * validations.
 * 
 * @author Chetan Patidar
 */
public class TimetableModel {

	Logger log = Logger.getLogger(TimetableModel.class);

	/**
	 * Returns the next primary key value.
	 * 
	 * @return next primary key
	 * @throws ApplicationException if database error occurs
	 */
	public int nextPk() throws ApplicationException {
		log.info("TimetableModel nextPk Started");

		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_timetable");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			throw new ApplicationException("Exception in timetable nextPk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new Timetable record to the database.
	 * 
	 * @param bean TimetableBean object to add
	 * @throws DuplicateRecordException if duplicate entry is found
	 * @throws ApplicationException     if database error occurs
	 */
	public void add(TimetableBean bean) throws DuplicateRecordException, ApplicationException {
		log.info("TimetableModel add Started");

		CourseModel couModel = new CourseModel();
		CourseBean couBean = couModel.findByPk(bean.getCourseId());
		bean.setCourseName(couBean.getName());

		SubjectModel subModel = new SubjectModel();
		SubjectBean subBean = subModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subBean.getName());

		int pk = nextPk();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_timetable values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getSemester());
			pstmt.setString(3, bean.getDescription());
			pstmt.setDate(4, new java.sql.Date(bean.getExamDate().getTime()));
			pstmt.setString(5, bean.getExamTime());
			pstmt.setLong(6, bean.getCourseId());
			pstmt.setString(7, bean.getCourseName());
			pstmt.setLong(8, bean.getSubjectId());
			pstmt.setString(9, bean.getSubjectName());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
			System.out.println("Inserted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in TimetableModel add rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in add Timetable " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel add Ended");
	}

	/**
	 * Updates an existing Timetable record in the database.
	 * 
	 * @param bean TimetableBean object with updated data
	 * @throws ApplicationException     if database error occurs
	 * @throws DuplicateRecordException if duplicate entry is found
	 */
	public void update(TimetableBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("TimetableModel update Started");

		CourseModel couModel = new CourseModel();
		CourseBean couBean = couModel.findByPk(bean.getCourseId());
		bean.setCourseName(couBean.getName());

		SubjectModel subModel = new SubjectModel();
		SubjectBean subBean = subModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subBean.getName());

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_timetable set semester=?,description=?,exam_date=?,exam_time=?,course_id=?,course_name=?,subject_id=?,subject_name=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getSemester());
			pstmt.setString(2, bean.getDescription());
			pstmt.setDate(3, new java.sql.Date(bean.getExamDate().getTime()));
			pstmt.setString(4, bean.getExamTime());
			pstmt.setLong(5, bean.getCourseId());
			pstmt.setString(6, bean.getCourseName());
			pstmt.setLong(7, bean.getSubjectId());
			pstmt.setString(8, bean.getSubjectName());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("Updated Succesfully...." + i);
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in Timetable update rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update Timetable " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel update Ended");
	}

	/**
	 * Deletes a Timetable record.
	 * 
	 * @param bean TimetableBean to delete (only ID is used)
	 * @throws ApplicationException if database error occurs
	 */
	public void delete(TimetableBean bean) throws ApplicationException {
		log.info("TimetableModel delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_timetable where id=?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			System.out.println("Deleted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in delete Timetable rollback " + e);
			}
			throw new ApplicationException("Exception : Exception in delete Timetable " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel delete Ended");
	}

	/**
	 * Finds a Timetable record by its primary key.
	 * 
	 * @param id primary key
	 * @return TimetableBean if found, null otherwise
	 * @throws ApplicationException if database error occurs
	 */
	public TimetableBean findByPk(long id) throws ApplicationException {
		log.info("TimetableModel findByPk Started");

		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_timetable where id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new TimetableBean();

				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting Timetable by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel findByPk Ended");
		return bean;
	}

	/**
	 * Checks if a Timetable entry exists by course and exam date.
	 * 
	 * @param courseId course ID
	 * @param examDate exam date
	 * @return TimetableBean if match found, null otherwise
	 * @throws ApplicationException if database error occurs
	 */
	public TimetableBean checkByCourseName(Long courseId, Date examDate) throws ApplicationException {
		log.info("TimetableModel checkByCourseName Started");

		StringBuffer sql = new StringBuffer("select * from st_timetable where course_id = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setDate(2, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get Timetable");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel checkByCourseName Ended");
		return bean;
	}

	/**
	 * Checks if a Timetable entry exists by course, subject, and exam date.
	 * 
	 * @param courseId  course ID
	 * @param subjectId subject ID
	 * @param examDate  exam date
	 * @return TimetableBean if match found, null otherwise
	 * @throws ApplicationException if database error occurs
	 */
	public TimetableBean checkBySubjectName(Long courseId, Long subjectId, Date examDate) throws ApplicationException {
		log.info("TimetableModel checkBySubjectName Started");

		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setDate(3, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get Timetable");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel checkBySubjectName Ended");
		return bean;
	}

	/**
	 * Checks if a Timetable entry exists by course, subject, semester, and exam
	 * date.
	 * 
	 * @param courseId  course ID
	 * @param subjectId subject ID
	 * @param semester  semester
	 * @param examDate  exam date
	 * @return TimetableBean if match found, null otherwise
	 * @throws ApplicationException if database error occurs
	 */
	public TimetableBean checkBySemester(Long courseId, Long subjectId, String semester, Date examDate)
			throws ApplicationException {
		log.info("TimetableModel checkBySemester Started");

		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setString(3, semester);
			pstmt.setDate(4, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel checkBySemester Ended");
		return bean;
	}

	/**
	 * Checks if a Timetable entry exists by full exam schedule.
	 * 
	 * @param courseId    course ID
	 * @param subjectId   subject ID
	 * @param semester    semester
	 * @param examDate    exam date
	 * @param examTime    exam time
	 * @param description exam description
	 * @return TimetableBean if match found, null otherwise
	 * @throws ApplicationException if database error occurs
	 */
	public TimetableBean checkByExamTime(Long courseId, Long subjectId, String semester, Date examDate, String examTime,
			String description) throws ApplicationException {
		log.info("TimetableModel checkByExamTime Started");

		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ? and exam_time = ? and description = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setString(3, semester);
			pstmt.setDate(4, new java.sql.Date(examDate.getTime()));
			pstmt.setString(5, examTime);
			pstmt.setString(6, description);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel checkByExamTime Ended");
		return bean;
	}

	/**
	 * Returns a list of all Timetable records.
	 * 
	 * @return list of TimetableBean
	 * @throws ApplicationException if database error occurs
	 */
	public List<TimetableBean> list() throws ApplicationException {
		log.info("TimetableModel list Method");
		return search(null, 0, 0);
	}

	/**
	 * Searches for Timetable records based on given criteria.
	 * 
	 * @param bean     search criteria bean
	 * @param pageNo   page number for pagination
	 * @param pageSize number of records per page
	 * @return list of TimetableBean matching criteria
	 * @throws ApplicationException if database error occurs
	 */
	public List<TimetableBean> search(TimetableBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("TimetableModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_timetable where 1=1");

		if (bean != null) {

			if (bean.getSubjectId() > 0) {
				sql.append(" and subject_id = " + bean.getSubjectId());
			}

			if (bean.getCourseId() > 0) {
				sql.append(" and course_id = " + bean.getCourseId());
			}

			if (bean.getExamDate() != null && bean.getExamDate().getTime() > 0) {
				sql.append(" and exam_date like '" + new java.sql.Date(bean.getExamDate().getTime()) + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<TimetableBean> list = new ArrayList<TimetableBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new TimetableBean();

				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

				list.add(bean);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in search Timetable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("TimetableModel search Ended");
		return list;
	}
}
