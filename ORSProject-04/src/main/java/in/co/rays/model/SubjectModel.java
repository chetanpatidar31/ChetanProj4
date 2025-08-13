package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for managing Subject entities in the database. Provides CRUD
 * operations and search functionalities.
 * 
 * @author Chetan Patidar
 */
public class SubjectModel {

	Logger log = Logger.getLogger(SubjectModel.class);

	/**
	 * Returns the next primary key for the subject table.
	 * 
	 * @return next primary key as Integer
	 * @throws ApplicationException if a database error occurs
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("SubjectModel nextPk Started");

		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_subject");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Subject next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new Subject to the database.
	 * 
	 * @param bean SubjectBean containing subject data
	 * @return the generated primary key
	 * @throws ApplicationException     if a database error occurs
	 * @throws DuplicateRecordException if subject name already exists
	 */
	public Long add(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("SubjectModel add Started");

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectBean existBean = findByName(bean.getName());

		if (existBean != null) {
			throw new DuplicateRecordException("Subject Name Already exists");
		}

		long pk = nextPk();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_subject values(?,?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setLong(3, bean.getCourseId());
			pstmt.setString(4, bean.getCourseName());
			pstmt.setString(5, bean.getDescription());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Added successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Subject add rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in add Subject" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel add Ended");
		return pk;
	}

	/**
	 * Updates an existing Subject in the database.
	 * 
	 * @param bean SubjectBean containing updated data
	 * @throws ApplicationException     if a database error occurs
	 * @throws DuplicateRecordException if duplicate subject name exists
	 */
	public void update(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("SubjectModel update Started");

		CourseModel cModel = new CourseModel();
		CourseBean cBean = cModel.findByPk(bean.getCourseId());
		bean.setCourseName(cBean.getName());

		SubjectBean existBean = findByName(bean.getName());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Subject Name already Exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_subject set name=?,course_id=?,course_name=?,description=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getName());
			pstmt.setLong(2, bean.getCourseId());
			pstmt.setString(3, bean.getCourseName());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Subject update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update Subject" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel update Ended");
	}

	/**
	 * Deletes a Subject from the database.
	 * 
	 * @param bean SubjectBean containing subject ID to delete
	 * @throws ApplicationException if a database error occurs
	 */
	public void delete(SubjectBean bean) throws ApplicationException {
		log.info("SubjectModel delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_subject where id=?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Subject delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete Subject" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel delete Ended");
	}

	/**
	 * Finds a Subject by its primary key.
	 * 
	 * @param id the ID of the subject
	 * @return SubjectBean if found, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public SubjectBean findByPk(Long id) throws ApplicationException {
		log.info("SubjectModel findByPk Started");

		Connection conn = null;
		SubjectBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where id =?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new SubjectBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Subject find by pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a Subject by its name.
	 * 
	 * @param name the name of the subject
	 * @return SubjectBean if found, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public SubjectBean findByName(String name) throws ApplicationException {
		log.info("SubjectModel findbyName Started");

		Connection conn = null;
		SubjectBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where name=?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new SubjectBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Subject find by name" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel findbyName Ended");
		return bean;
	}

	/**
	 * Returns a list of all Subjects.
	 * 
	 * @return list of SubjectBeans
	 * @throws ApplicationException if a database error occurs
	 */
	public List<SubjectBean> list() throws ApplicationException {
		log.info("SubjectModel list Method");
		return search(null, 0, 0);
	}

	/**
	 * Searches Subjects with optional pagination and filtering.
	 * 
	 * @param bean     filter criteria
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return list of SubjectBeans matching criteria
	 * @throws ApplicationException if a database error occurs
	 */
	public List<SubjectBean> search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("SubjectModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_subject where 1=1 ");

		if (bean != null) {

			if (bean.getId() > 0) {
				sql.append("and id = " + bean.getId());
			}

			if (bean.getCourseId() > 0) {
				sql.append("and course_id = " + bean.getCourseId());
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<SubjectBean> list = new ArrayList<SubjectBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new SubjectBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Subject search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("SubjectModel search Ended");
		return list;
	}
}
