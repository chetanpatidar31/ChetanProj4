package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * StudentModel handles database operations for Student records. It includes
 * methods for CRUD operations and search functionality.
 * 
 * @author Chetan Patidar
 */
public class StudentModel {

	Logger log = Logger.getLogger(StudentModel.class);

	/**
	 * Returns the next primary key for the student table.
	 *
	 * @return next primary key
	 * @throws ApplicationException if any database error occurs
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("StudentModel nextPk Started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_student");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in student next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new student record into the database.
	 *
	 * @param bean StudentBean containing student data
	 * @return generated primary key
	 * @throws ApplicationException     if any database error occurs
	 * @throws DuplicateRecordException if email already exists
	 */
	public Long add(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("StudentModel add Started");

		CollegeModel collModel = new CollegeModel();
		CollegeBean collegeBean = collModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		StudentBean existBean = findByEmail(bean.getEmail());

		if (existBean != null) {
			throw new DuplicateRecordException("Student email Already exists");
		}

		long pk = nextPk();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_student values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getGender());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Added successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in student add rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in add Student" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel add Ended");
		return pk;
	}

	/**
	 * Updates an existing student record in the database.
	 *
	 * @param bean StudentBean containing updated data
	 * @throws ApplicationException     if any database error occurs
	 * @throws DuplicateRecordException if duplicate email exists
	 */
	public void update(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("StudentModel update Started");

		CollegeModel collModel = new CollegeModel();
		CollegeBean collegeBean = collModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		StudentBean existBean = findByEmail(bean.getEmail());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Student email Already exists");
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_student set first_name=?,last_name=?,dob=?,gender=?,mobile_no=?,email=?,college_id=?,college_name=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getGender());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getCollegeId());
			pstmt.setString(8, bean.getCollegeName());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in student update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update Student" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel update Ended");
	}

	/**
	 * Deletes a student record from the database.
	 *
	 * @param bean StudentBean containing the ID of the student to delete
	 * @throws ApplicationException if any database error occurs
	 */
	public void delete(StudentBean bean) throws ApplicationException {
		log.info("StudentModel delete Started");

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_student where id=?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in student delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete Student" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel delete Ended");
	}

	/**
	 * Finds a student record by primary key.
	 *
	 * @param id primary key
	 * @return StudentBean if found, otherwise null
	 * @throws ApplicationException if any database error occurs
	 */
	public StudentBean findByPk(long id) throws ApplicationException {
		log.info("StudentModel findByPk Started");

		StudentBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where id=?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StudentBean();

				bean.setId(rs.getInt(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getInt(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Student find in pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a student by email address.
	 *
	 * @param email student's email
	 * @return StudentBean if found, otherwise null
	 * @throws ApplicationException if any database error occurs
	 */
	public StudentBean findByEmail(String email) throws ApplicationException {
		log.info("StudentModel findbyEmail Started");

		StudentBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where email=?");
			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new StudentBean();

				bean.setId(rs.getInt(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getInt(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Student find email" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel findbyEmail Ended");
		return bean;
	}

	/**
	 * Returns a list of all students.
	 *
	 * @return list of StudentBean
	 * @throws ApplicationException if any database error occurs
	 */
	public List<StudentBean> list() throws ApplicationException {
		log.info("StudentModel list Method");
		return search(null, 0, 0);
	}

	/**
	 * Searches for students based on given criteria and supports pagination.
	 *
	 * @param bean     StudentBean containing search filters
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return list of matching StudentBean
	 * @throws ApplicationException if any database error occurs
	 */
	public List<StudentBean> search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("StudentModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_student where 1=1 ");

		if (bean != null) {

			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append("and first_name like '" + bean.getFirstName() + "%'");
			}

			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append("and last_name like '" + bean.getLastName() + "%'");
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
		List<StudentBean> list = new ArrayList<StudentBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new StudentBean();

				bean.setId(rs.getInt(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getInt(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Student find email" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("StudentModel search Ended");
		return list;
	}
}
