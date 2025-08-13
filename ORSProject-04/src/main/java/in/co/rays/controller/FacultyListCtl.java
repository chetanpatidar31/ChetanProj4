package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.FacultyBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.FacultyModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * FacultyListCtl servlet controller to handle listing, searching, 
 * pagination, and deletion of Faculty records.
 * 
 * Supports operations like:
 * - Searching by criteria
 * - Navigating pages (next/previous)
 * - Deleting selected records
 * - Resetting or redirecting to new Faculty form
 * 
 * @author Chetan Patidar
 * @version 1.0
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl {
	
	Logger log = Logger.getLogger(FacultyListCtl.class);

    private static final long serialVersionUID = 1L;

    /**
     * Populates FacultyBean from the request parameters.
     *
     * @param request HttpServletRequest object
     * @return FacultyBean populated with request data
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.info("FacultyListCtl populateBean Method Started");
    	
        FacultyBean bean = new FacultyBean();

        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
        bean.setEmail(DataUtility.getString(request.getParameter("email")));

        log.info("FacultyListCtl populateBean Method Ended");
        return bean;
    }

    /**
     * Handles GET requests. Displays the first page of the faculty list.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("FacultyListCtl doGet Method Started");
    	
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        FacultyModel model = new FacultyModel();
        FacultyBean bean = (FacultyBean) populateBean(request);

        try {
            List<FacultyBean> list = model.search(bean, pageNo, pageSize);
            List<FacultyBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list.size() == 0) {
                ServletUtility.setErrorMessage("No Record found", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

        } catch (ApplicationException e) {
            e.printStackTrace();
            return;
        }
        log.info("FacultyListCtl doGet Method Ended");
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Handles POST requests. Supports operations such as:
     * - Search
     * - Next / Previous pagination
     * - New (redirect to Faculty form)
     * - Delete selected records
     * - Reset form
     * - Back to list
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("FacultyListCtl doPost Method Started");
    	
    	List<FacultyBean> list = null;
        List<FacultyBean> next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        FacultyModel model = new FacultyModel();
        FacultyBean bean = (FacultyBean) populateBean(request);

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
                ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    FacultyBean deleteBean = new FacultyBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.delete(deleteBean);
                        ServletUtility.setSuccessMessage("Faculty deleted Successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select atleast one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found ", request);
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.setBean(bean, request);
            request.setAttribute("nextListSize", next.size());

        } catch (ApplicationException e) {
            e.printStackTrace();
            return;
        }
		log.info("FacultyListCtl doPost Method Ended");
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Returns the view page for the Faculty list.
     *
     * @return String path of the JSP to display faculty list
     */
    @Override
    protected String getView() {
        return ORSView.FACULTY_LIST_VIEW;
    }

}
