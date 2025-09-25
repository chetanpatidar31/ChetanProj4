package in.co.rays.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.EmployeeModel;

public class TestEmployeeModel {

	public static void main(String[] args) throws Exception {
		testNextPk();
//		testSearch();
//		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByUserName();
//		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		EmployeeModel model = new EmployeeModel();
		int pk = model.nextPk();
		System.out.println("Next pk : " + pk);

	}

	private static void testAdd() throws Exception {
		EmployeeBean bean = new EmployeeBean();
		EmployeeModel model = new EmployeeModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setFullName("abc");
		bean.setUsername("xyz@gmail.com");
		bean.setPassword("abc@123");
		bean.setBirthDate(sdf.parse("2006-09-25"));
		bean.setContactNo("8889898965");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws Exception {
		EmployeeBean bean = new EmployeeBean();
		EmployeeModel model = new EmployeeModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setId(1);
		bean.setFullName("Amit Kirar");
		bean.setUsername("amit@gmail.com");
		bean.setPassword("Amit@1234");
		bean.setBirthDate(sdf.parse("2002-07-05"));
		bean.setContactNo("8989898989");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	private static void testDelete() throws Exception {
		EmployeeBean bean = new EmployeeBean();
		bean.setId(2);

		EmployeeModel model = new EmployeeModel();
		model.delete(bean);

	}

	private static void testFindByPk() throws ApplicationException {
		EmployeeModel model = new EmployeeModel();
		EmployeeBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFullName());
			System.out.print("\t" + bean.getUsername());
			System.out.print("\t" + bean.getBirthDate());
			System.out.print("\t" + bean.getContactNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}

	}

	private static void testFindByUserName() throws ApplicationException {
		EmployeeModel model = new EmployeeModel();
		EmployeeBean bean = model.findByUsername("amit@gmail.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFullName());
			System.out.print("\t" + bean.getUsername());
			System.out.print("\t" + bean.getBirthDate());
			System.out.print("\t" + bean.getContactNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}

	}

	private static void testSearch() throws Exception {
		EmployeeModel model = new EmployeeModel();
		EmployeeBean bean = new EmployeeBean();

		List list = model.list();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (EmployeeBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFullName());
			System.out.print("\t" + bean.getUsername());
			System.out.print("\t" + bean.getBirthDate());
			System.out.print("\t" + bean.getContactNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
