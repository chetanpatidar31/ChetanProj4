package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for College entity that handles JDBC operations such as add,
 * update, delete, find, and search on the st_college table.
 * 
 * @author Chetan Patidar
 */
public class CollegeModel {

	Logger log = Logger.getLogger(CollegeModel.class);

	/**
	 * Returns the next primary key value for the st_college table.
	 *
	 * @return next primary key
	 * @throws ApplicationException if database error occurs
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("Model nextPk Started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_college");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in college next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new College record into the database.
	 *
	 * @param bean CollegeBean object containing college data
	 * @return generated primary key
	 * @throws ApplicationException     if database error occurs
	 * @throws DuplicateRecordException if college name already exists
	 */
	public Long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("Model add Started");

		CollegeBean existBean = findByName(bean.getName());

		if (existBean != null) {
			throw new DuplicateRecordException("College Name Already exists");
		}

		long pk = nextPk();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_college values(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Added successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in college add rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in add college" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model add Ended");
		return pk;
	}

	/**
	 * Updates an existing College record in the database.
	 *
	 * @param bean CollegeBean object containing updated college data
	 * @throws ApplicationException     if database error occurs
	 * @throws DuplicateRecordException if college name already exists
	 */
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("Model update Started");

		CollegeBean existBean = findByName(bean.getName());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("College Name already Exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_college set name=?,address=?,state=?,city=?,phone_no=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in college update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update college" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model update Ended");
	}

	/**
	 * Deletes a College record from the database.
	 *
	 * @param bean CollegeBean object containing the ID to be deleted
	 * @throws ApplicationException if database error occurs
	 */
	public void delete(CollegeBean bean) throws ApplicationException {
		log.info("Model delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id=?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in college delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete college" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model delete Ended");
	}

	/**
	 * Finds a College by primary key.
	 *
	 * @param id the primary key of the college
	 * @return CollegeBean if found, otherwise null
	 * @throws ApplicationException if database error occurs
	 */
	public CollegeBean findByPk(Long id) throws ApplicationException {
		log.info("Model findByPk Started");

		Connection conn = null;
		CollegeBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where id =?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in college find by pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model findByPk Ended");
		return bean;
	}

	/**
	 * Finds a College by its name.
	 *
	 * @param name the name of the college
	 * @return CollegeBean if found, otherwise null
	 * @throws ApplicationException if database error occurs
	 */
	public CollegeBean findByName(String name) throws ApplicationException {
		log.info("Model findByName Started");

		Connection conn = null;
		CollegeBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where name=?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();

				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in college find by name" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model findByName Ended");
		return bean;
	}

	/**
	 * Lists all colleges from the database.
	 *
	 * @return List of CollegeBean objects
	 * @throws ApplicationException if database error occurs
	 */
	public List<CollegeBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Searches colleges based on provided criteria with optional pagination.
	 *
	 * @param bean     CollegeBean object as search criteria
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return List of CollegeBean matching search criteria
	 * @throws ApplicationException if database error occurs
	 */
	public List<CollegeBean> search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("Model search Started");
		StringBuffer sql = new StringBuffer("select * from st_college where 1=1 ");

		if (bean != null) {

			if (bean.getId() > 0) {
				sql.append("and id = " + bean.getId());
			}

			if (bean.getCity() != null && bean.getCity().length() > 0) {
				sql.append("and city like '" + bean.getCity() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<CollegeBean> list = new ArrayList<CollegeBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in college search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("Model search Ended");
		return list;
	}

}
