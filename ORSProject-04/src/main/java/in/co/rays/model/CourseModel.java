package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for handling Course operations such as add, update, delete, find
 * and search.
 * 
 * @author Chetan Patidar
 */
public class CourseModel {

	Logger log = Logger.getLogger(CourseModel.class);

	/**
	 * Returns the next primary key from the database.
	 * 
	 * @return next primary key value
	 * @throws ApplicationException if a database exception occurs
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("CourseModel nextPk Started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_course");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Course next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new course to the database.
	 * 
	 * @param bean the CourseBean containing course details
	 * @return the primary key of the newly added course
	 * @throws ApplicationException     if a database exception occurs
	 * @throws DuplicateRecordException if the course name already exists
	 */
	public Long add(CourseBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("CourseModel add Started");

		CourseBean existBean = findByName(bean.getName());

		if (existBean != null) {
			throw new DuplicateRecordException("Course Name Already exists");
		}

		long pk = nextPk();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_course values(?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDuration());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Added successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Course add rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in add Course" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel add Ended");
		return pk;
	}

	/**
	 * Updates the details of an existing course.
	 * 
	 * @param bean the CourseBean containing updated details
	 * @throws ApplicationException     if a database exception occurs
	 * @throws DuplicateRecordException if the course name already exists
	 */
	public void update(CourseBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("CourseModel update Started");

		CourseBean existBean = findByName(bean.getName());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Course Name already Exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_course set name=?,duration=?,description=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDuration());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getModifiedDatetime());
			pstmt.setLong(8, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Course update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update Course" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel update Ended");
	}

	/**
	 * Deletes a course from the database.
	 * 
	 * @param bean the CourseBean containing the ID of the course to delete
	 * @throws ApplicationException if a database exception occurs
	 */
	public void delete(CourseBean bean) throws ApplicationException {
		log.info("CourseModel delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_course where id=?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Course delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete Course" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel delete Ended");
	}

	/**
	 * Finds a course by its primary key.
	 * 
	 * @param id the ID of the course to find
	 * @return the CourseBean containing course details
	 * @throws ApplicationException if a database exception occurs
	 */
	public CourseBean findByPk(Long id) throws ApplicationException {
		log.info("CourseModel findByPk Started");
		Connection conn = null;
		CourseBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where id =?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Course find by pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a course by its name.
	 * 
	 * @param name the name of the course
	 * @return the CourseBean containing course details
	 * @throws ApplicationException if a database exception occurs
	 */
	public CourseBean findByName(String name) throws ApplicationException {
		log.info("CourseModel findByName Started");
		Connection conn = null;
		CourseBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where name=?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Course find by name" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel findByName Ended");
		return bean;
	}

	/**
	 * Returns a list of all courses.
	 * 
	 * @return list of CourseBean
	 * @throws ApplicationException if a database exception occurs
	 */
	public List<CourseBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Searches for courses matching given criteria and supports pagination.
	 * 
	 * @param bean     the search criteria
	 * @param pageNo   the page number
	 * @param pageSize the number of records per page
	 * @return list of CourseBean matching criteria
	 * @throws ApplicationException if a database exception occurs
	 */
	public List<CourseBean> search(CourseBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("CourseModel search Started");
		StringBuffer sql = new StringBuffer("select * from st_course where 1=1 ");

		if (bean != null) {

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append("and name like '" + bean.getName() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<CourseBean> list = new ArrayList<CourseBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Course search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("CourseModel search Ended");
		return list;
	}
}
