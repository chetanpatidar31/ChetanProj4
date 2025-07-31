package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.MarksheetModel;

public class TestMarksheetModel {

	public static void main(String[] args) throws Exception {
		// testNextPk();
//		testAdd();
		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByRollNo();
		// testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		MarksheetModel model = new MarksheetModel();
		int pk = model.nextPk();
		System.out.println("Next Pk : " + pk);

	}

	private static void testAdd() throws ApplicationException, DuplicateRecordException {
		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		bean.setRollNo("RT102");
		bean.setStudentId(5);
		bean.setPhysics(55);
		bean.setChemistry(78);
		bean.setMaths(65);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		bean.setId(2);
		bean.setRollNo("RT102");
		bean.setStudentId(3);
		bean.setPhysics(65);
		bean.setChemistry(62);
		bean.setMaths(82);
		bean.setCreatedBy("chetan@gmail.com");
		bean.setModifiedBy("chetan@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		bean.setId(2);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = model.findByPk(1l);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not Found");
		}
	}

	private static void testFindByRollNo() throws ApplicationException {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = model.findByRollNo("RT102");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not Found");
		}

	}

	private static void testSearch() throws ApplicationException {
		MarksheetBean bean = null;
		MarksheetModel model = new MarksheetModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (MarksheetBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

}
