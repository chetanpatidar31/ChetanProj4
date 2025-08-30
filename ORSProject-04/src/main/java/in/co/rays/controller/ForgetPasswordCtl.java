package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * ForgetPasswordCtl handles the "Forgot Password" functionality.
 * <p>
 * Allows a user to request their password by providing their registered email.
 * Sends the password to the provided email if it exists in the system.
 * </p>
 * 
 * Operations Supported:
 * - GO: Process the request to send the password via email.
 * 
 * @author Chetan Patidar
 * @version 1.0
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {

	Logger log = Logger.getLogger(ForgetPasswordCtl.class);
	
    /**
     * Validates input email for "Forget Password" request.
     *
     * @param request HttpServletRequest object
     * @return true if valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	log.info("ForgetPasswordCtl validate Method Started");
    	
        boolean isValid = true;

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
            isValid = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Email"));
            isValid = false;
        }

        log.info("ForgetPasswordCtl validate Method Ended");
        return isValid;
    }

    /**
     * Populates the UserBean with the provided email ID.
     *
     * @param request HttpServletRequest object
     * @return UserBean containing the login email
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.info("ForgetPasswordCtl populateBean Method Started");
    	
        UserBean bean = new UserBean();

        bean.setLogin(DataUtility.getString(request.getParameter("login")));

        log.info("ForgetPasswordCtl populateBean Method Ended");
        return bean;
    }

    /**
     * Displays the Forget Password form.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("ForgetPasswordCtl doGet Method Started");
    	
    	ServletUtility.forward(getView(), request, response);

    	log.info("ForgetPasswordCtl doGet Method Ended");
    }

    /**
     * Processes the "Forget Password" form submission.
     * If the email exists, the password is sent to the email address.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("ForgetPasswordCtl doPost Method Started");
    	
        String op = DataUtility.getString(request.getParameter("operation"));

        UserModel model = new UserModel();
        UserBean bean = (UserBean) populateBean(request);

        if (OP_GO.equalsIgnoreCase(op)) {
            try {
                boolean flag = model.forgetPassword(bean.getLogin());
                if (flag) {
                    ServletUtility.setSuccessMessage("Password has been sent to your email id", request);
                }
            } catch (RecordNotFoundException e) {
                ServletUtility.setErrorMessage(e.getMessage(), request);
            } catch (ApplicationException e) {
                ServletUtility.setErrorMessage("Please check your internet connection..!!", request);
                log.error(e);
				ServletUtility.handleException(e, request, response);            }
        }
        log.info("ForgetPasswordCtl doPost Method Ended");
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Returns the view for the Forget Password page.
     *
     * @return the path to the Forget Password JSP
     */
    @Override
    protected String getView() {
        return ORSView.FORGET_PASSWORD_VIEW;
    }

}
