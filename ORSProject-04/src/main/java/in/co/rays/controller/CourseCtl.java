package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CourseCtl Servlet Controller.
 * <p>
 * This controller handles Create, Read, Update, and Reset operations for Course
 * entities. It communicates with the {@link CourseModel} to perform database
 * operations and uses {@link ServletUtility} to handle request and response
 * forwarding/redirecting.
 * </p>
 *
 * <h2>Features:</h2>
 * <ul>
 * <li>Validates course details such as name, duration, and description.</li>
 * <li>Supports Save, Update, Reset, and Cancel operations.</li>
 * </ul>
 *
 * @author Chetan Patidar
 * @version 1.0
 */
@WebServlet(name = "CourseCtl", urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseCtl {

	Logger log = Logger.getLogger(CourseCtl.class);

	/**
	 * Validates the input parameters from the request. Checks if the course name,
	 * duration, and description are present and valid.
	 *
	 * @param request the HTTP request containing form input data
	 * @return {@code true} if all validations pass, otherwise {@code false}
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("CourseCtl validate Method Started");

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			isValid = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.invalid", "Name"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;
		}

		log.info("CourseCtl validate Method Ended");
		return isValid;
	}

	/**
	 * Populates a {@link CourseBean} from the request parameters.
	 *
	 * @param request the HTTP request containing form data
	 * @return a populated {@link CourseBean} instance
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("CourseCtl populateBean Method Started");

		CourseBean bean = new CourseBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDuration(DataUtility.getString(request.getParameter("duration")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		log.info("CourseCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests.
	 * <p>
	 * If an ID is provided, retrieves the corresponding course record and sets it
	 * in the request for display in the view.
	 * </p>
	 *
	 * @param request  the HTTP request
	 * @param response the HTTP response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("CourseCtl doGet Method Started");

		CourseModel model = new CourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {
			try {
				CourseBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			}
		}

		log.info("CourseCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles HTTP POST requests.
	 * <p>
	 * Supports the following operations:
	 * <ul>
	 * <li>{@code OP_SAVE} - Adds a new course.</li>
	 * <li>{@code OP_UPDATE} - Updates an existing course.</li>
	 * <li>{@code OP_RESET} - Resets the form.</li>
	 * <li>{@code OP_CANCEL} - Redirects to the course list.</li>
	 * </ul>
	 * </p>
	 *
	 * @param request  the HTTP request containing form data
	 * @param response the HTTP response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("CourseCtl doPost Method Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		CourseModel model = new CourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			CourseBean bean = (CourseBean) populateBean(request);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Course added successfully", request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Course Already Exists", request);
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			CourseBean bean = (CourseBean) populateBean(request);

			if (id > 0) {
				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Course Updated successfully", request);
				} catch (ApplicationException e) {
					e.printStackTrace();
					return;
				} catch (DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Course Already Exists", request);
				}
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}

		log.info("CourseCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view page for the Course Controller.
	 *
	 * @return the path of the course view JSP page
	 */
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}
