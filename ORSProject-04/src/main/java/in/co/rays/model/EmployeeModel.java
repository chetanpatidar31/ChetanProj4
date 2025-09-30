package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class EmployeeModel {

	public static Logger log = Logger.getLogger(EmployeeModel.class);

	/**
	 * Returns the next primary key for Employee table.
	 *
	 * @return next primary key
	 * @throws ApplicationException if any database error occurs
	 */
	public int nextPk() throws ApplicationException {
		log.info("EmployeeModel nextPk started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_employee");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			throw new ApplicationException("Exception in Employee nextPk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel nextPk ended");
		return pk + 1;
	}

	/**
	 * Adds a new Employee to the database.
	 * 
	 * @param bean EmployeeBean containing Employee data
	 * @return primary key of the newly inserted Employee
	 * @throws ApplicationException     if application-level error occurs
	 * @throws DuplicateRecordException if Employee login already exists
	 */
	public long add(EmployeeBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("EmployeeModel add Started");

		EmployeeBean existBean = findByUsername(bean.getUsername());

		if (existBean != null) {
			throw new DuplicateRecordException("Employee Username already exist");
		}

		int pk = nextPk();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_employee value(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFullName());
			pstmt.setString(3, bean.getUsername());
			pstmt.setString(4, bean.getPassword());
			pstmt.setDate(5, new java.sql.Date(bean.getBirthDate().getTime()));
			pstmt.setString(6, bean.getContactNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
			System.out.println("Inserted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in EmployeeModel add rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in add Employee " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel add Ended");
		return pk;
	}

	/**
	 * Updates an existing Employee in the database.
	 * 
	 * @param bean EmployeeBean containing updated data
	 * @throws ApplicationException     if application-level error occurs
	 * @throws DuplicateRecordException if updated login already exists
	 */
	public void update(EmployeeBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("EmployeeModel update Started");

		EmployeeBean existBean = findByUsername(bean.getUsername());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Employee Username already exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_employee set full_name=?,username=?,password=?,birth_date=?,contact_no=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getFullName());
			pstmt.setString(2, bean.getUsername());
			pstmt.setString(3, bean.getPassword());
			pstmt.setDate(4, new java.sql.Date(bean.getBirthDate().getTime()));
			pstmt.setString(5, bean.getContactNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("Updated Succesfully...." + i);
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in Employee update rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update Employee " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel update Ended");
	}

	/**
	 * Deletes a Employee from the database.
	 * 
	 * @param bean EmployeeBean with Employee ID to delete
	 * @throws ApplicationException if application-level error occurs
	 */
	public void delete(EmployeeBean bean) throws ApplicationException {
		log.info("EmployeeModel delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_employee where id=?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			System.out.println("Deleted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in delete Employee rollback " + e);
			}
			throw new ApplicationException("Exception : Exception in delete Employee " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel delete Ended");
	}

	/**
	 * Finds a Employee by primary key.
	 * 
	 * @param id Employee's ID
	 * @return EmployeeBean with Employee data
	 * @throws ApplicationException if database error occurs
	 */
	public EmployeeBean findByPk(long id) throws ApplicationException {
		log.info("EmployeeModel findByPk Started");

		EmployeeBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new EmployeeBean();

				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setUsername(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setBirthDate(rs.getDate(5));
				bean.setContactNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting Employee by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a Employee by Username.
	 * 
	 * @param login Employee's Username
	 * @return EmployeeBean if found, else null
	 * @throws ApplicationException if database error occurs
	 */
	public EmployeeBean findByUsername(String username) throws ApplicationException {
		log.info("EmployeeModel findByUsername Started");

		Connection conn = null;
		EmployeeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where username=?");
			pstmt.setString(1, username);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new EmployeeBean();

				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setUsername(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setBirthDate(rs.getDate(5));
				bean.setContactNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Employee find by Username");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel findByUsername Ended");
		return bean;
	}

	/**
	 * Returns a list of all Employees.
	 * 
	 * @return list of EmployeeBeans
	 * @throws ApplicationException if database error occurs
	 */
	public List<EmployeeBean> list() throws ApplicationException {
		log.info("EmployeeModel list");
		return search(null, 0, 0);
	}

	/**
	 * Searches Employees based on criteria with optional pagination.
	 * 
	 * @param bean     criteria bean
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return list of EmployeeBeans matching search criteria
	 * @throws ApplicationException if database error occurs
	 */
	public List<EmployeeBean> search(EmployeeBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("EmployeeModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_employee where 1=1");

		if (bean != null) {
			if (bean.getFullName() != null && bean.getFullName().length() > 0) {
				sql.append(" and full_name like '" + bean.getFullName() + "%'");
			}

			if (bean.getUsername() != null && bean.getUsername().length() > 0) {
				sql.append(" and username like '" + bean.getUsername() + "%'");
			}

			if (bean.getBirthDate() != null && bean.getBirthDate().getTime() > 0) {
				sql.append(" and birth_date like '" + new java.sql.Date(bean.getBirthDate().getTime()) + "%'");
			}

			if (bean.getContactNo() != null && bean.getContactNo().length() > 0) {
				sql.append(" and contact_no like '" + bean.getContactNo() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		System.out.println("sql :" + sql.toString());

		Connection conn = null;
		List<EmployeeBean> list = new ArrayList<EmployeeBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new EmployeeBean();

				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setUsername(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setBirthDate(rs.getDate(5));
				bean.setContactNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));

				list.add(bean);

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in search Employee");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("EmployeeModel search Ended");
		return list;
	}
}
