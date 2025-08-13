package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.util.ServletUtility;

/**
 * WelcomeCtl is a servlet controller class that forwards requests to the
 * Welcome view. It handles HTTP GET requests and directs users to the welcome
 * page.
 * 
 * @author
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseCtl {
	
	Logger log  = Logger.getLogger(WelcomeCtl.class);

	/**
	 * Handles HTTP GET request by forwarding to the welcome view.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException if servlet-specific error occurs
	 * @throws IOException      if input/output error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("WelcomeCtl doGet Method Started");
		
		ServletUtility.forward(getView(), request, response);
		
		log.info("WelcomeCtl doGet Method Ended");

	}

	/**
	 * Returns the welcome view page.
	 * 
	 * @return String representing the welcome view path.
	 */
	@Override
	protected String getView() {
		return ORSView.WELCOME_VIEW;
	}
}