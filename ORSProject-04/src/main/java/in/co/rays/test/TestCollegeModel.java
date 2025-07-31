package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;

public class TestCollegeModel {
	public static void main(String[] args) throws Exception {
		testNextPk();
//		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();
//		testFindByName();
		testSearch();
	}

	private static void testNextPk() throws ApplicationException {
		CollegeModel model = new CollegeModel();
		int pk = model.nextPk();
		System.out.println("Next Pk : " + pk);

	}

	private static void testAdd() throws ApplicationException, DuplicateRecordException {
		CollegeBean bean = new CollegeBean();
		CollegeModel model = new CollegeModel();

		bean.setName("Mandsaur University");
		bean.setAddress("Indore");
		bean.setState("MP");
		bean.setCity("Mandsaur");
		bean.setPhoneNo("9473849494");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		CollegeBean bean = new CollegeBean();
		CollegeModel model = new CollegeModel();

		bean.setId(9);
		bean.setName("Mandsaur");
		bean.setAddress("East Tambaram");
		bean.setState("Tamil Nadu");
		bean.setCity("Chennai");
		bean.setPhoneNo("9756849494");
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		
		model.update(bean);
	}

	private static void testDelete() throws ApplicationException {
		CollegeBean bean = new CollegeBean();
		CollegeModel model = new CollegeModel();
		
		bean.setId(9);
		model.delete(bean);
	}

	private static void testFindByPk() throws ApplicationException {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = model.findByPk(7l);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}
	}

	private static void testFindByName() throws ApplicationException {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = model.findByName("Mandsaur University");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}else {
			System.out.println("Record not Found");
		}

	}

	private static void testSearch() throws ApplicationException {
		CollegeBean bean = null;
		CollegeModel model = new CollegeModel();

		List list = model.list();
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (CollegeBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());
			System.out.print("\t" + bean.getState());
			System.out.print("\t" + bean.getCity());
			System.out.print("\t" + bean.getPhoneNo());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
