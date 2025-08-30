package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.MarksheetModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Controller to Get Marksheet by Roll Number functionality.
 * 
 * @author Chetan Patidar
 */
@WebServlet(name = "GetMarksheetCtl", urlPatterns = { "/ctl/GetMarksheetCtl" })
public class GetMarksheetCtl extends BaseCtl {

	Logger log = Logger.getLogger(GetMarksheetCtl.class);

	
	/**
	 * Validates the input data for roll number.
	 *
	 * @param request the HttpServletRequest object
	 * @return boolean indicating if validation passed
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("GetMarksheetCtl validate Method Started");
		
		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll No"));
			isValid = false;
		}
		log.info("GetMarksheetCtl validate Method Ended");
		return isValid;
	}

	/**
	 * Populates the MarksheetBean object from request parameters.
	 *
	 * @param request the HttpServletRequest object
	 * @return populated MarksheetBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("GetMarksheetCtl populateBean Method Started");
		
		MarksheetBean bean = new MarksheetBean();

		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

		log.info("GetMarksheetCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles GET request. Forwards to the view.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("GetMarksheetCtl doGet Method Started");
		ServletUtility.forward(getView(), request, response);
		log.info("GetMarksheetCtl doGet Method Ended");
	}

	/**
	 * Handles POST request. Searches for marksheet by roll number.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("GetMarksheetCtl doPost Method Started");
		
		String op = DataUtility.getString(request.getParameter("operation"));

		MarksheetModel model = new MarksheetModel();

		MarksheetBean bean = (MarksheetBean) populateBean(request);

		if (OP_GO.equalsIgnoreCase(op)) {
			try {
				bean = model.findByRollNo(bean.getRollNo());
				if (bean != null) {
					ServletUtility.setBean(bean, request);
				} else {
					ServletUtility.setErrorMessage("RollNo Does Not exists", request);
				}
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		log.info("GetMarksheetCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view page for GetMarksheetCtl.
	 *
	 * @return String representing the view path
	 */
	@Override
	protected String getView() {
		return ORSView.GET_MARKSHEET_VIEW;
	}

}