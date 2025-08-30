package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.CollegeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CollegeListCtl servlet class to handle operations like searching, listing,
 * deleting, and paginating College records.
 * 
 * @author Chetan Patidar
 */
@WebServlet(name = "CollegeListCtl", urlPatterns = "/ctl/CollegeListCtl")
public class CollegeListCtl extends BaseCtl {

	Logger log = Logger.getLogger(CollegeListCtl.class);

	/**
	 * Loads list of all colleges for preload data in view
	 * 
	 * @param request the HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.info("CollegeListCtl preload Method Started");

		CollegeModel model = new CollegeModel();
		try {
			List<CollegeBean> collegeList = model.list();
			request.setAttribute("collegeList", collegeList);
			return;
		} catch (ApplicationException e) {
			log.error(e);
		}
		log.info("CollegeListCtl preload Method Ended");
	}

	/**
	 * Populates CollegeBean object from request parameters
	 * 
	 * @param request the HttpServletRequest
	 * @return CollegeBean object with populated data
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("CollegeListCtl populateBean Method Started");

		CollegeBean bean = new CollegeBean();

		bean.setId(DataUtility.getLong(request.getParameter("collegeId")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));

		log.info("CollegeListCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests for displaying college list
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("CollegeListCtl doGet Method Started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CollegeModel model = new CollegeModel();
		CollegeBean bean = (CollegeBean) populateBean(request);
		try {
			List<CollegeBean> list = model.search(bean, pageNo, pageSize);
			List<CollegeBean> next = model.search(bean, pageNo + 1, pageSize);
			if (list.size() == 0) {
				ServletUtility.setErrorMessage("Record not found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.info("CollegeListCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);

	}

	/**
	 * Handles HTTP POST requests for operations like Search, Delete, New, Reset,
	 * etc.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("CollegeListCtl doPost Method Started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		CollegeBean bean = (CollegeBean) populateBean(request);
		CollegeModel model = new CollegeModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;

				if (ids != null && ids.length > 0) {
					CollegeBean deleteBean = new CollegeBean();
					for (String id : ids) {
						deleteBean.setId(DataUtility.getLong(id));
						model.delete(deleteBean);
						ServletUtility.setSuccessMessage("College Deleted Succesfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select atleast one Record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No Records Found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.info("CollegeListCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view page for this controller
	 * 
	 * @return view page path
	 */
	@Override
	protected String getView() {
		return ORSView.COLLEGE_LIST_VIEW;

	}

}
