package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.UserModel;

public class TestUserModel {

	public static void main(String[] args) throws Exception {
		testNextPk();
//		testSearch();
//		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByLogin();
//		testAuthenticate();
//		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		UserModel model = new UserModel();
		int pk = model.nextPk();
		System.out.println("Next pk : " + pk);

	}

	private static void testAdd() throws Exception {
		UserBean bean = new UserBean();
		UserModel model = new UserModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setFirstName("Lucky");
		bean.setLastName("Kirar");
		bean.setLogin("lucky@gmail.com");
		bean.setPassword("Amit@123");
		bean.setDob(sdf.parse("2002-07-05"));
		bean.setMobileNo("8989898989");
		bean.setRoleId(2);
		bean.setGender("male");
		bean.setCreatedBy("root"); 
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws Exception {
		UserBean bean = new UserBean();
		UserModel model = new UserModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setId(2);
		bean.setFirstName("Amit");
		bean.setLastName("Kirar");
		bean.setLogin("amit@gmail.com");
		bean.setPassword("Amit@123");
		bean.setDob(sdf.parse("2002-05-15"));
		bean.setMobileNo("9755898809");
		bean.setRoleId(3);
		bean.setGender("male");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	private static void testDelete() throws Exception {
		UserBean bean = new UserBean();
		bean.setId(2);

		UserModel model = new UserModel();
		model.delete(bean);

	}
	

	private static void testFindByPk() throws ApplicationException {
		UserModel model = new UserModel();
		UserBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}

	}
	
	private static void testAuthenticate() throws ApplicationException {
		UserModel model = new UserModel();
		UserBean bean = model.authenticate("chetan@gmail.com","Chetan@123");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}
		
	}

	private static void testFindByLogin() throws ApplicationException {
		UserModel model = new UserModel();
		UserBean bean = model.findByLogin("chetan@gmail.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}

	}

	private static void testSearch() throws Exception {
		UserModel model = new UserModel();
		UserBean bean = new UserBean();

		List list = model.list();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (UserBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getLogin());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getRoleId());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
