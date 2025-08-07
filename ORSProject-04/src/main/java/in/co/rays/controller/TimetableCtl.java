package in.co.rays.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.model.TimetableModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "TimetableCtl", urlPatterns = { "/ctl/TimetableCtl" })
public class TimetableCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("1st", "1st");
		map.put("2nd", "2nd");
		map.put("3rd", "3rd");
		map.put("4th", "4th");
		map.put("5th", "5th");
		map.put("6th", "6th");
		map.put("7th", "7th");
		map.put("8th", "8th");

		request.setAttribute("map", map);

		CourseModel courseModel = new CourseModel();
		SubjectModel subjectModel = new SubjectModel();

		try {
			List<CourseBean> courseList = courseModel.list();
			request.setAttribute("courseList", courseList);

			List<SubjectBean> subjectList = subjectModel.list();
			request.setAttribute("subjectList", subjectList);

		} catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("semester"))) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.require", "Date of Exam"));
			isValid = false;
		} else if (!DataValidator.isDate(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of Exam"));
			isValid = false;
		} else if (DataValidator.isSunday(request.getParameter("examDate"))) {
			request.setAttribute("examDate", "Exam should not be on Sunday");
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("examTime"))) {
			request.setAttribute("examTime", PropertyReader.getValue("error.require", "Exam Time"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("courseId"))) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("subjectId"))) {
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
			isValid = false;
		}

		return isValid;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		TimetableBean bean = new TimetableBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
		bean.setSemester(DataUtility.getString(request.getParameter("semester")));
		bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id = DataUtility.getLong(request.getParameter("id"));

		TimetableModel model = new TimetableModel();

		if (id > 0) {
			try {
				TimetableBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		TimetableModel model = new TimetableModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {

			TimetableBean bean = (TimetableBean) populateBean(request);

			TimetableBean bean1;
			TimetableBean bean2;
			TimetableBean bean3;

			try {
				bean1 = model.checkByCourseName(bean.getCourseId(), bean.getExamDate());

				bean2 = model.checkBySubjectName(bean.getCourseId(), bean.getSubjectId(), bean.getExamDate());

				bean3 = model.checkBySemester(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate());

				if (bean1 == null && bean2 == null && bean3 == null) {
					model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable added successfully", request);
				} else {
					bean = (TimetableBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);
				}
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			TimetableBean bean = (TimetableBean) populateBean(request);

			TimetableBean bean4;

			try {

				bean4 = model.checkByExamTime(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate(), bean.getExamTime(), bean.getDescription());

				if (id > 0 && bean4 == null) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable updated successfully", request);
				} else {
					bean = (TimetableBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);
				}

			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.TIMETABLE_VIEW;
	}

}
