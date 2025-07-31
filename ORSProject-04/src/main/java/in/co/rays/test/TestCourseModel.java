package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;

public class TestCourseModel {

	public static void main(String[] args) throws Exception {
		testNextPk();
//		testAdd();
		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByName();
//		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		CourseModel model = new CourseModel();
		int pk = model.nextPk();
		System.out.println("Next Pk : " + pk);

	}

	private static void testAdd() throws ApplicationException, DuplicateRecordException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();
		
		bean.setName("MBA");
		bean.setDuration("2 Year");
		bean.setDescription("MBA in CS");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		bean.setId(6);
		bean.setName("BBA");
		bean.setDuration("2 Year");
		bean.setDescription("MBA in finance");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		
		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();
		
		bean.setId(6);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByPk(2l);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}
	}

	private static void testFindByName() throws ApplicationException {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByName("MBA");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}

	}

	private static void testSearch() throws ApplicationException {
		CourseBean bean = null;
		CourseModel model = new CourseModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (CourseBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getDuration());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
