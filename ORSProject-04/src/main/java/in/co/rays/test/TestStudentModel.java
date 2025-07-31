package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.StudentModel;

public class TestStudentModel {
	public static void main(String[] args) throws Exception {
//		testNextPk();
		testSearch(); 
		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByEmail();
		testSearch();
	}

	private static void testAdd() throws ParseException, ApplicationException, DuplicateRecordException {
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setFirstName("Akbar");
		bean.setLastName("Mansuri");
		bean.setDob(sdf.parse("2000-02-08"));
		bean.setGender("male");
		bean.setMobileNo("7986594955");
		bean.setEmail("akbar@gmail.com");
		bean.setCollegeId(3);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	private static void testUpdate() throws ParseException, ApplicationException, DuplicateRecordException {
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setId(1);
		bean.setFirstName("Aastik");
		bean.setLastName("Sahu");
		bean.setDob(sdf.parse("1998-06-02"));
		bean.setGender("male");
		bean.setMobileNo("9326599495");
		bean.setEmail("aastik@gmail.com");
		bean.setCollegeId(2);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);
	}

	private static void testDelete() throws ApplicationException, DuplicateRecordException {
		StudentBean bean = new StudentBean();
		StudentModel model = new StudentModel();

		bean.setId(2);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		StudentModel model = new StudentModel();
		StudentBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());

			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}

	}

	private static void testFindByEmail() throws ApplicationException {
		StudentModel model = new StudentModel();
		StudentBean bean = model.findByEmail("aastik@gmail.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}
	}

	private static void testSearch() throws ApplicationException {
		StudentBean bean = null;
		StudentModel model = new StudentModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (StudentBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
