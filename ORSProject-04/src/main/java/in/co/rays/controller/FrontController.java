package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.util.ServletUtility;

/**
 * FrontController filter acts as a security layer to intercept all incoming
 * requests to protected resources (under `/doc/*` and `/ctl/*` URL patterns).
 * <p>
 * It checks whether the user is logged in or not. If not, it redirects to the
 * login page with an appropriate session timeout message.
 * </p>
 * 
 * This is part of the Front Controller design pattern, often used to implement
 * centralized request handling in web applications.
 * 
 * @author Chetan Patidar
 * @version 1.0
 */
@WebFilter(filterName = "FrontController", urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

	/**
	 * Initializes the filter.
	 *
	 * @param filterConfig the filter configuration
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// No initialization required
	}

	/**
	 * Filters each request to check for an active session. If the session has
	 * expired or no user is logged in, it forwards the request to the login page.
	 *
	 * @param request  the incoming ServletRequest
	 * @param response the outgoing ServletResponse
	 * @param chain    the filter chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession();

		String uri = req.getRequestURI();
		req.setAttribute("uri", uri);

		if (session.getAttribute("user") == null) {
			req.setAttribute("error", "Your session is expired. Please login again!!");
			ServletUtility.forward(ORSView.LOGIN_VIEW, req, resp);
			return;
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * Cleans up any resources held by the filter.
	 */
	@Override
	public void destroy() {

	}

}
