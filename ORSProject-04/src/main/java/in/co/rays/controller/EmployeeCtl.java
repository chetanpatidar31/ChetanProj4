package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.EmployeeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "EmployeeCtl",urlPatterns = {("/ctl/EmployeeCtl")})
public class EmployeeCtl extends BaseCtl {

	Logger log = Logger.getLogger(EmployeeCtl.class);

	/**
	 * Loads list of Roles and sets in request for dropdown.
	 * 
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.info("EmployeeCtl preload Method Started");

		EmployeeModel model = new EmployeeModel();
		try {
			List<EmployeeBean> usernameList = model.list();
			request.setAttribute("usernameList", usernameList);
		} catch (ApplicationException e) {
			log.error(e);
		}
		log.info("EmployeeCtl preload Method Ended");
	}

	/**
	 * Validates Employee form data from the request.
	 * 
	 * @param request HttpServletRequest
	 * @return boolean true if valid, else false
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("EmployeeCtl validate Method Started");

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "Full Name"));
			isValid = false;
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.name", "Full Name"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("username"))) {
			request.setAttribute("username", PropertyReader.getValue("error.require", "Username"));
			isValid = false;
		} else if (!DataValidator.isEmail(request.getParameter("username"))) {
			request.setAttribute("username", PropertyReader.getValue("error.email", "Username"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			isValid = false;
		} else if (!DataValidator.isPasswordLength(request.getParameter("password"))) {
			request.setAttribute("password", "Password should be 8 to 12 characters");
			isValid = false;
		} else if (!DataValidator.isPassword(request.getParameter("password"))) {
			request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.require", "Birth Date"));
			isValid = false;
		} else if (!DataValidator.isDate(request.getParameter("birthDate"))) {
			request.setAttribute("birthDate", PropertyReader.getValue("error.date", "Birth Date"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("contactNo"))) {
			request.setAttribute("contactNo", PropertyReader.getValue("error.require", "Contact No"));
			isValid = false;
		} else if (!DataValidator.isPhoneLength(request.getParameter("contactNo"))) {
			request.setAttribute("contactNo", "Contact No must have 10 digits");
			isValid = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("contactNo"))) {
			request.setAttribute("contactNo", PropertyReader.getValue("error.invalid", "Contact No"));
			isValid = false;
		}

		log.info("EmployeeCtl validate Method Ended");
		return isValid;
	}

	/**
	 * Populates EmployeeBean from request parameters.
	 * 
	 * @param request HttpServletRequest
	 * @return BaseBean populated EmployeeBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("EmployeeCtl populateBean Method Started");

		EmployeeBean bean = new EmployeeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));
		bean.setUsername(DataUtility.getString(request.getParameter("username")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));
		bean.setContactNo(DataUtility.getString(request.getParameter("contactNo")));

		populateDTO(bean, request);

		log.info("EmployeeCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles GET request for Employee form. If id is passed, loads EmployeeBean to
	 * edit.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("EmployeeCtl doGet Method Started");

		long id = DataUtility.getLong(request.getParameter("id"));

		EmployeeModel model = new EmployeeModel();

		if (id > 0) {
			EmployeeBean bean;
			try {
				bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		log.info("EmployeeCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles POST request for save, update, cancel, and reset operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("EmployeeCtl doPost Method Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		EmployeeModel model = new EmployeeModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			EmployeeBean bean = (EmployeeBean) populateBean(request);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Employee Add Successful", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Username Already Exists", request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			EmployeeBean bean = (EmployeeBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Employee Updated Successfully!", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Username already exists", request);
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
			return;
		}

		log.info("EmployeeCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view for Employee form.
	 * 
	 * @return String view path
	 */
	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_VIEW;
	}

}
