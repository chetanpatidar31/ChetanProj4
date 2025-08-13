package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for handling Marksheet operations like add, update, delete, and
 * search. This class interacts with the database table `st_marksheet`.
 * 
 * @author Chetan Patidar
 */
public class MarksheetModel {

	Logger log = Logger.getLogger(MarksheetModel.class);

	/**
	 * Gets the next primary key value from the st_marksheet table.
	 *
	 * @return the next primary key as Integer
	 * @throws ApplicationException if there is a database access error
	 */
	public Integer nextPk() throws ApplicationException {
		log.info("MarksheetModel nextPk Started");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_marksheet");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in marksheet next pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new Marksheet record into the database.
	 *
	 * @param bean MarksheetBean containing marksheet details
	 * @return the primary key of the newly inserted record
	 * @throws ApplicationException     if there is a database access error
	 * @throws DuplicateRecordException if the roll number already exists
	 */
	public Long add(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("MarksheetModel add Started");

		StudentModel stModel = new StudentModel();
		StudentBean stBean = stModel.findByPk(bean.getStudentId());
		bean.setName(stBean.getFirstName() + " " + stBean.getLastName());

		MarksheetBean existBean = findByRollNo(bean.getRollNo());

		if (existBean != null) {
			throw new DuplicateRecordException("Roll no. already Exist");
		}

		long pk = nextPk();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_marksheet values(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getRollNo());
			pstmt.setLong(3, bean.getStudentId());
			pstmt.setString(4, bean.getName());
			pstmt.setLong(5, bean.getPhysics());
			pstmt.setLong(6, bean.getChemistry());
			pstmt.setLong(7, bean.getMaths());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Added successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Marksheet add rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in add Marksheet" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel add Ended");
		return pk;
	}

	/**
	 * Updates an existing Marksheet record.
	 *
	 * @param bean MarksheetBean containing updated data
	 * @throws ApplicationException     if there is a database access error
	 * @throws DuplicateRecordException if the roll number is duplicate
	 */
	public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		log.info("MarksheetModel update Started");

		StudentModel stModel = new StudentModel();
		StudentBean stBean = stModel.findByPk(bean.getStudentId());
		bean.setName(stBean.getFirstName() + " " + stBean.getLastName());

		MarksheetBean existBean = findByRollNo(bean.getRollNo());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Roll no. already Exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_marksheet set roll_no=?,student_id=?,name=?,physics=?,chemistry=?,maths=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");
			pstmt.setString(1, bean.getRollNo());
			pstmt.setLong(2, bean.getStudentId());
			pstmt.setString(3, bean.getName());
			pstmt.setLong(4, bean.getPhysics());
			pstmt.setLong(5, bean.getChemistry());
			pstmt.setLong(6, bean.getMaths());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			pstmt.setLong(11, bean.getId());
			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Updated successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Marksheet update rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in update Marksheet" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel update Ended");
	}

	/**
	 * Deletes a Marksheet record by ID.
	 *
	 * @param bean MarksheetBean containing ID to delete
	 * @throws ApplicationException if there is a database access error
	 */
	public void delete(MarksheetBean bean) throws ApplicationException {
		log.info("MarksheetModel delete Started");

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id=?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("Deleted successfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in Marksheet delete rollback" + e1.getMessage());
			}
			throw new ApplicationException("Exception in delete Marksheet" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel delete Ended");
	}

	/**
	 * Finds a Marksheet record by its primary key.
	 *
	 * @param id the ID of the marksheet
	 * @return MarksheetBean object if found, otherwise null
	 * @throws ApplicationException if there is a database access error
	 */
	public MarksheetBean findByPk(Long id) throws ApplicationException {
		log.info("MarksheetModel findByPk Started");

		Connection conn = null;
		MarksheetBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where id =?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new MarksheetBean();

				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Marksheet find by pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel findByPk Ended");
		return bean;
	}

	/**
	 * Finds a Marksheet record by roll number.
	 *
	 * @param name the roll number
	 * @return MarksheetBean object if found, otherwise null
	 * @throws ApplicationException if there is a database access error
	 */
	public MarksheetBean findByRollNo(String name) throws ApplicationException {
		log.info("MarksheetModel findbyRollNo Started");
		Connection conn = null;
		MarksheetBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where roll_no=?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new MarksheetBean();

				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Marksheet find by name" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel findbyRollNo Ended");
		return bean;
	}

	/**
	 * Lists all Marksheet records.
	 *
	 * @return list of MarksheetBean
	 * @throws ApplicationException if there is a database access error
	 */
	public List<MarksheetBean> list() throws ApplicationException {
		log.info("MarksheetModel list");
		return search(null, 0, 0);
	}

	/**
	 * Searches marksheets with optional filters and pagination.
	 *
	 * @param bean     filter criteria
	 * @param pageNo   page number
	 * @param pageSize number of records per page
	 * @return list of MarksheetBean objects
	 * @throws ApplicationException if there is a database access error
	 */
	public List<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.info("MarksheetModel search Started");

		StringBuffer sql = new StringBuffer("select * from st_marksheet where 1=1 ");

		if (bean != null) {

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append("and name like '" + bean.getName() + "%'");
			}

			if (bean.getRollNo() != null && bean.getRollNo().length() > 0) {
				sql.append("and roll_no like '" + bean.getRollNo() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<MarksheetBean> list = new ArrayList<MarksheetBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new MarksheetBean();

				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));

				list.add(bean);
			}

			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception in Marksheet search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel search Ended");
		return list;
	}

	/**
	 * Retrieves the merit list of students who passed all subjects, sorted in
	 * descending order of total marks.
	 *
	 * @param pageNo   page number
	 * @param pageSize number of records per page
	 * @return list of top MarksheetBean objects
	 * @throws ApplicationException if there is a database access error
	 */
	public List<MarksheetBean> getMeritList(int pageNo, int pageSize) throws ApplicationException {
		log.info("MarksheetModel getMeritList Started");

		ArrayList<MarksheetBean> list = new ArrayList<MarksheetBean>();
		StringBuffer sql = new StringBuffer(
				"select id, roll_no, name, physics, chemistry, maths, (physics + chemistry + maths) as total from st_marksheet where physics > 33 and chemistry > 33 and maths > 33 order by total desc");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MarksheetBean bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setName(rs.getString(3));
				bean.setPhysics(rs.getInt(4));
				bean.setChemistry(rs.getInt(5));
				bean.setMaths(rs.getInt(6));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception in getting merit list of Marksheet");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.info("MarksheetModel getMeritList Ended");
		return list;
	}

}
