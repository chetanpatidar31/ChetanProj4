package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.SubjectModel;

public class TestSubjectModel {
	public static void main(String[] args) throws Exception {
		testNextPk();
//		testAdd();
//		testUpdate();
//		testDelete();
		testFindByPk();
		testFindByName();
//		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		SubjectModel model = new SubjectModel();
		int pk = model.nextPk();
		System.out.println("Next Pk : " + pk);

	}

	private static void testAdd() throws ApplicationException, DuplicateRecordException {
		SubjectBean bean = new SubjectBean();
		SubjectModel model = new SubjectModel();
		
		bean.setName("Financial Management");
		bean.setCourseId(6);
		bean.setDescription("Financial Management");
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		SubjectBean bean = new SubjectBean();
		SubjectModel model = new SubjectModel();

		bean.setId(6);
		bean.setName("Financial Management");
		bean.setCourseId(6);
		bean.setDescription("Financial Management");
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		
		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		SubjectBean bean = new SubjectBean();
		SubjectModel model = new SubjectModel();
		
		bean.setId(6);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByPk(1l);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
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
		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByName("Financial Management");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
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
		SubjectBean bean = null;
		SubjectModel model = new SubjectModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (SubjectBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
