package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.EmployeeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet(name = "EmployeeListCtl",urlPatterns = {("/ctl/EmployeeListCtl")})
public class EmployeeListCtl extends BaseCtl{

	Logger log = Logger.getLogger(EmployeeListCtl.class);

	/**
	 * Loads Role list to request scope for dropdown.
	 * 
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.info("EmployeeListCtl preload Method Started");

		EmployeeModel model = new EmployeeModel();

		try {
			List<EmployeeBean> usernameList = model.list();
			request.setAttribute("usernameList", usernameList);
		} catch (ApplicationException e) {
			log.error(e);
			return;
		}
		log.info("EmployeeListCtl preload Method Ended");
	}

	/**
	 * Populates EmployeeBean from request parameters for search criteria.
	 * 
	 * @param request HttpServletRequest
	 * @return BaseBean containing EmployeeBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("EmployeeListCtl populateBean Method Started");

		EmployeeBean bean = new EmployeeBean();

		bean.setFullName(DataUtility.getString(request.getParameter("fullName")));
		bean.setUsername(DataUtility.getString(request.getParameter("username")));
		bean.setContactNo(DataUtility.getString(request.getParameter("contactNo")));
		bean.setBirthDate(DataUtility.getDate(request.getParameter("birthDate")));

		log.info("EmployeeListCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET request for initial Employee list page load.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("EmployeeListCtl doGet Method Started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		EmployeeModel model = new EmployeeModel();
		EmployeeBean bean = (EmployeeBean) populateBean(request);

		try {
			List<EmployeeBean> list = model.search(bean, pageNo, pageSize);
			List<EmployeeBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}

		log.info("EmployeeListCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles HTTP POST request for searching, pagination, delete, reset
	 * operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("EmployeeListCtl doPost Method Started");

		List<EmployeeBean> list = null;
		List<EmployeeBean> next = null;

		int pageNo = DataUtility.getInt(request.getParameter("PageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		EmployeeBean bean = (EmployeeBean) populateBean(request);
		EmployeeModel model = new EmployeeModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EMPLOYEE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					EmployeeBean deleteBean = new EmployeeBean();
					for (String id : ids) {
						deleteBean.setId(DataUtility.getLong(id));
						model.delete(deleteBean);
					}
					ServletUtility.setSuccessMessage("Data deleted Successfully", request);
				} else {
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.EMPLOYEE_LIST_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
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
		}
		log.info("EmployeeListCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view path of Employee List.
	 * 
	 * @return String view path
	 */
	@Override
	protected String getView() {
		return ORSView.EMPLOYEE_LIST_VIEW;
	}

}
