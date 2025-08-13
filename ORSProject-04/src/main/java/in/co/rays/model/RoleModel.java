package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * RoleModel handles all CRUD operations and search functionality
 * for Role entities in the application.
 *
 * @author Chetan Patidar
 */
public class RoleModel {
	
	Logger log = Logger.getLogger(RoleModel.class);

    /**
     * Gets the next primary key for the Role table.
     *
     * @return next available primary key
     * @throws ApplicationException if any database exception occurs
     */
    public Integer nextPk() throws ApplicationException {
    	log.info("RoleModel nextPk Started");
    	
    	Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_role");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
            JDBCDataSource.closeConnection(rs, pstmt);
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel nextPk Ended");
        return pk + 1;
    }

    /**
     * Adds a new Role into the database.
     *
     * @param bean the RoleBean containing role data
     * @return the primary key of the new role
     * @throws ApplicationException if a database exception occurs
     * @throws DuplicateRecordException if the role already exists
     */
    public long add(RoleBean bean) throws ApplicationException, DuplicateRecordException {
    	log.info("RoleModel add Started");
    	
    	Connection conn = null;
        int pk = 0;

        RoleBean duplicataRole = findByName(bean.getName());

        if (duplicataRole != null) {
            throw new DuplicateRecordException("Role already exists");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into st_role values(?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getDescription());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDatetime());
            pstmt.setTimestamp(7, bean.getModifiedDatetime());
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in add Role");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel add Ended");
        return pk;
    }

    /**
     * Updates an existing Role in the database.
     *
     * @param bean the RoleBean containing updated role data
     * @throws ApplicationException if a database exception occurs
     * @throws DuplicateRecordException if a duplicate role name exists
     */
    public void update(RoleBean bean) throws ApplicationException, DuplicateRecordException {
    	log.info("RoleModel update Started");
    	
    	Connection conn = null;

        RoleBean duplicateRole = findByName(bean.getName());

        if (duplicateRole != null && duplicateRole.getId() != bean.getId()) {
            throw new DuplicateRecordException("Role already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                "update st_role set name = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getDescription());
            pstmt.setString(3, bean.getCreatedBy());
            pstmt.setString(4, bean.getModifiedBy());
            pstmt.setTimestamp(5, bean.getCreatedDatetime());
            pstmt.setTimestamp(6, bean.getModifiedDatetime());
            pstmt.setLong(7, bean.getId());
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Role ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel update Ended");
    }

    /**
     * Deletes a Role from the database.
     *
     * @param bean the RoleBean containing the role ID to delete
     * @throws ApplicationException if a database exception occurs
     */
    public void delete(RoleBean bean) throws ApplicationException {
    	log.info("RoleModel delete Started");
    	
    	Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_role where id = ?");
            pstmt.setLong(1, bean.getId());
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception : Exception in delete Role");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel delete Ended");
    }

    /**
     * Finds a Role by its primary key.
     *
     * @param pk the primary key
     * @return the RoleBean containing role data
     * @throws ApplicationException if a database exception occurs
     */
    public RoleBean findByPk(long pk) throws ApplicationException {
    	log.info("RoleModel findByPk Started");
    	
    	RoleBean bean = null;
        Connection conn = null;

        StringBuffer sql = new StringBuffer("select * from st_role where id = ?");

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
            }
            JDBCDataSource.closeConnection(rs, pstmt);
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in getting User by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel findByPk Ended");
        return bean;
    }

    /**
     * Finds a Role by its name.
     *
     * @param name the role name
     * @return the RoleBean if found, otherwise null
     * @throws ApplicationException if a database exception occurs
     */
    public RoleBean findByName(String name) throws ApplicationException {
    	log.info("RoleModel findbyName Started");
    	
    	StringBuffer sql = new StringBuffer("select * from st_role where name = ?");
        RoleBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
            }
            JDBCDataSource.closeConnection(rs, pstmt);
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in getting User by Role");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel findbyName Ended");
        return bean;
    }

    /**
     * Returns a list of all Roles.
     *
     * @return list of RoleBean objects
     * @throws ApplicationException if a database exception occurs
     */
    public List<RoleBean> list() throws ApplicationException {
    	log.info("RoleModel list Method");
    	return search(null, 0, 0);
    }

    /**
     * Searches roles based on provided criteria with pagination.
     *
     * @param bean RoleBean with search criteria
     * @param pageNo current page number
     * @param pageSize number of records per page
     * @return list of matching RoleBean objects
     * @throws ApplicationException if a database exception occurs
     */
    public List<RoleBean> search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {
    	log.info("RoleModel search Started");
    	
        StringBuffer sql = new StringBuffer("select * from st_role where 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" and id = " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" and name like '" + bean.getName() + "%'");
            }
            if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                sql.append(" and description like '" + bean.getDescription() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        Connection conn = null;
        ArrayList<RoleBean> list = new ArrayList<RoleBean>();

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
                list.add(bean);
            }
            JDBCDataSource.closeConnection(rs, pstmt);
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in search Role");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.info("RoleModel search Ended");
        return list;
    }
}
