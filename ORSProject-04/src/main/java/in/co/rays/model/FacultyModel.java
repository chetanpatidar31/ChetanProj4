package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.bean.FacultyBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for Faculty entity. Handles CRUD operations and search
 * functionality for faculty records.
 *
 * @author Chetan Patidar
 */
public class FacultyModel {

	Logger log = Logger.getLogger(FacultyModel.class);

	/**
	 * Returns the next primary key from the database.
	 *
	 * @return next primary key value
	 * @throws ApplicationException
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("FacultyModel nextPk started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_faculty");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Faculty next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new faculty record to the database.
	 *
	 * @param bean the FacultyBean containing faculty details
	 * @throws ApplicationException     if database operation fails
	 * @throws DuplicateRecordException if faculty email already exists
	 */
	public void add(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("FacultyModel add started");

		CollegeModel clgModel = new CollegeModel();
		CollegeBean clgBean = clgModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(clgBean.getName());

		CourseModel couModel = new CourseModel();
		CourseBean couBean = couModel.findByPk(bean.getCourseId());
		bean.setCourseName(couBean.getName());

		SubjectModel subModel = new SubjectModel();
		SubjectBean subBean = subModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subBean.getName());

		FacultyBean existBean = findByEmail(bean.getEmail());

		if (existBean != null) {
			throw new DuplicateRecordException("Faculty Email already exist");
		}

		int pk = nextPk();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_faculty values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getGender());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setLong(10, bean.getCourseId());
			pstmt.setString(11, bean.getCourseName());
			pstmt.setLong(12, bean.getSubjectId());
			pstmt.setString(13, bean.getSubjectName());
			pstmt.setString(14, bean.getCreatedBy());
			pstmt.setString(15, bean.getModifiedBy());
			pstmt.setTimestamp(16, bean.getCreatedDatetime());
			pstmt.setTimestamp(17, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
			System.out.println("Inserted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in FacultyModel add rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in add Faculty " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel add Ended");
	}

	/**
	 * Updates an existing faculty record.
	 *
	 * @param bean the FacultyBean containing updated details
	 * @throws ApplicationException     if database operation fails
	 * @throws DuplicateRecordException if updated email already exists for another
	 *                                  faculty
	 */
	public void update(FacultyBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("FacultyModel update Started");

		CollegeModel clgModel = new CollegeModel();
		CollegeBean clgBean = clgModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(clgBean.getName());

		CourseModel couModel = new CourseModel();
		CourseBean couBean = couModel.findByPk(bean.getCourseId());
		bean.setCourseName(couBean.getName());

		SubjectModel subModel = new SubjectModel();
		SubjectBean subBean = subModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subBean.getName());

		FacultyBean existBean = findByEmail(bean.getEmail());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Faculty Email already exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_faculty set first_name=?,last_name=?,dob=?,gender=?,mobile_no=?,email=?,college_id=?,college_name=?,course_id=?,course_name=?,subject_id=?,subject_name=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getGender());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getCollegeId());
			pstmt.setString(8, bean.getCollegeName());
			pstmt.setLong(9, bean.getCourseId());
			pstmt.setString(10, bean.getCourseName());
			pstmt.setLong(11, bean.getSubjectId());
			pstmt.setString(12, bean.getSubjectName());
			pstmt.setString(13, bean.getCreatedBy());
			pstmt.setString(14, bean.getModifiedBy());
			pstmt.setTimestamp(15, bean.getCreatedDatetime());
			pstmt.setTimestamp(16, bean.getModifiedDatetime());
			pstmt.setLong(17, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Faculty update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update Faculty" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel update Ended");
	}

	/**
	 * Deletes a faculty record.
	 *
	 * @param bean the FacultyBean to be deleted
	 * @throws ApplicationException if deletion fails
	 */
	public void delete(FacultyBean bean) throws ApplicationException {
		log.info("FacultyModel delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_faculty where id=?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Faculty delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete Faculty" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel delete Ended");
	}

	/**
	 * Finds a faculty by primary key.
	 *
	 * @param id the faculty ID
	 * @return FacultyBean or null if not found
	 * @throws ApplicationException if operation fails
	 */
	public FacultyBean findByPk(Long id) throws ApplicationException {
		log.info("FacultyModel findByPk Started");

		Connection conn = null;
		FacultyBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where id =?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Faculty find by pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a faculty by email.
	 *
	 * @param email the email address to search
	 * @return FacultyBean or null if not found
	 * @throws ApplicationException if operation fails
	 */
	public FacultyBean findByEmail(String email) throws ApplicationException {
		log.info("FacultyModel findbyEmail Started");
		Connection conn = null;
		FacultyBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where email=?");
			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Faculty find by name" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel findbyEmail Ended");
		return bean;
	}

	/**
	 * Returns list of all faculty records.
	 *
	 * @return list of FacultyBean
	 * @throws ApplicationException if operation fails
	 */
	public List<FacultyBean> list() throws ApplicationException {
		log.info("FacultyModel list");
		return search(null, 0, 0);
	}

	/**
	 * Searches faculty records based on criteria with optional pagination.
	 *
	 * @param bean     FacultyBean containing search criteria
	 * @param pageNo   page number
	 * @param pageSize number of records per page
	 * @return list of FacultyBean matching the criteria
	 * @throws ApplicationException if search fails
	 */
	public List<FacultyBean> search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("FacultyModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_faculty where 1=1 ");

		if (bean != null) {

			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append("and first_name like '" + bean.getFirstName() + "%'");
			}

			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append("and Last_name like '" + bean.getLastName() + "%'");
			}

			if (bean.getEmail() != null && bean.getEmail().length() > 0) {
				sql.append("and email like '" + bean.getEmail() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<FacultyBean> list = new ArrayList<FacultyBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Faculty search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("FacultyModel search Ended");
		return list;
	}
}
