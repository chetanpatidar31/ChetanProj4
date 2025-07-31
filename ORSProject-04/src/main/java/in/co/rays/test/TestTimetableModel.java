package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.TimetableModel;

public class TestTimetableModel {
	public static void main(String[] args) throws Exception {
		testNextPk();
//		testAdd();
		testUpdate();
//		testDelete();
//		testFindByPk();
//		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		TimetableModel model = new TimetableModel();
		int pk = model.nextPk();
		System.out.println("Next Pk : " + pk);

	}

	private static void testAdd() throws ApplicationException, DuplicateRecordException, ParseException {
		TimetableBean bean = new TimetableBean();
		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		bean.setSemester("1st");
		bean.setDescription("Semester");
		bean.setExamDate(sdf.parse("2025-07-02"));
		bean.setExamTime("9 to 12");
		bean.setCourseId(6);
		bean.setSubjectId(2);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException, ParseException {
		TimetableBean bean = new TimetableBean();
		TimetableModel model = new TimetableModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setId(1);
		bean.setSemester("1st");
		bean.setDescription("Semester");
		bean.setExamDate(sdf.parse("2025-07-02"));
		bean.setExamTime("01:00 PM to 04:00 PM");
		bean.setCourseId(1);
		bean.setSubjectId(1);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		
		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		TimetableBean bean = new TimetableBean();
		TimetableModel model = new TimetableModel();
		
		bean.setId(6);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		TimetableModel model = new TimetableModel();
		TimetableBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}
	}

	private static void testSearch() throws ApplicationException {
		TimetableBean bean = null;
		TimetableModel model = new TimetableModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (TimetableBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
