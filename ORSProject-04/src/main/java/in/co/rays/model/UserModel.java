package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.util.EmailBuilder;
import in.co.rays.util.EmailMessage;
import in.co.rays.util.EmailUtility;
import in.co.rays.util.JDBCDataSource;

public class UserModel {

	public int nextPk() throws ApplicationException {
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_user");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new ApplicationException("Exception in user nextPk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(UserBean bean) throws ApplicationException, DuplicateRecordException {

		UserBean existBean = findByLogin(bean.getLogin());

		if (existBean != null) {
			throw new DuplicateRecordException("User Login already exist");
		}

		int pk = nextPk();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_user value(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getLogin());
			pstmt.setString(5, bean.getPassword());
			pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(7, bean.getMobileNo());
			pstmt.setLong(8, bean.getRoleId());
			pstmt.setString(9, bean.getGender());
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
				throw new ApplicationException("Exception in UserModel add rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in add User " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {
		UserBean existBean = findByLogin(bean.getLogin());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("user login already exist");
		}

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_user set first_name=?,last_name=?,login=?,password=?,dob=?,mobile_no=?,role_id=?,gender=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getLogin());
			pstmt.setString(4, bean.getPassword());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setLong(7, bean.getRoleId());
			pstmt.setString(8, bean.getGender());
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
				throw new ApplicationException("Exception in user update rollback " + ex.getMessage());
			}
			throw new ApplicationException("Exception in update User " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(UserBean bean) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_user where id=?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			System.out.println("Deleted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in delete User rollback " + e);
			}
			throw new ApplicationException("Exception : Exception in delete User " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public UserBean findByPk(long id) throws ApplicationException {
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new UserBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting User by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public UserBean authenticate(String login, String password) throws ApplicationException {
		UserBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where login=? and password=?");
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new UserBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in authenticate");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public UserBean findByLogin(String login) throws ApplicationException {
		Connection conn = null;
		UserBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where login=?");
			pstmt.setString(1, login);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in user find in login");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List<UserBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<UserBean> search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_user where 1=1");

		if (bean != null) {
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" and first_name like '" + bean.getFirstName() + "%'");
			}

			if (bean.getDob() != null) {
				System.out.println("search method in if of dob");
				sql.append(" and dob = '" + new java.sql.Date(bean.getDob().getTime()) + "'");
			}

			if (bean.getRoleId() > 0) {
				sql.append(" and role_id = " + bean.getRoleId());
			}

			if (bean.getLogin() != null && bean.getLogin().length() > 0) {
				sql.append(" and login like '" + bean.getLogin() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		List<UserBean> list = new ArrayList<UserBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new UserBean();

				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

				list.add(bean);

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in search user");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public boolean changePassword(Long id, String oldPassword, String newPassword)
			throws ApplicationException, RecordNotFoundException {

		boolean flag = false;

		UserBean existBean = findByPk(id);

		if (existBean != null && existBean.getPassword().equals(oldPassword)) {
			existBean.setPassword(newPassword);

			try {
				update(existBean);
			} catch (DuplicateRecordException e) {
				throw new ApplicationException("Login id Already Exists");
			}
			flag = true;
		} else {
			throw new RecordNotFoundException("Login Id not exist");
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", existBean.getLogin());
		map.put("password", existBean.getPassword());
		map.put("firstName", existBean.getFirstName());
		map.put("lastName", existBean.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();
		msg.setTo(existBean.getLogin());
		msg.setSubject("Rays ORS Password has been changed Successfully.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return flag;
	}

	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {

		boolean flag = true;

		UserBean userData = findByLogin(login);

		if (userData == null) {
			throw new RecordNotFoundException("Email Id does not Exist");
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLogin());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());

		String message = EmailBuilder.getForgetPasswordMessage(map);

		EmailMessage msg = new EmailMessage();
		msg.setTo(userData.getLogin());
		msg.setSubject("Rays ORS Password Reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return flag;
	}

	public long registerUser(UserBean bean) throws ApplicationException,DuplicateRecordException {
		
		long pk = add(bean);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", bean.getLogin());
		map.put("password", bean.getPassword());
		
		String message = EmailBuilder.getUserRegistrationMessage(map);
		
		EmailMessage msg = new EmailMessage();
		msg.setTo(bean.getLogin());
		msg.setSubject("Registration is successful for ORS Project");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		
		EmailUtility.sendMail(msg);
		
		return pk;
	}

}
