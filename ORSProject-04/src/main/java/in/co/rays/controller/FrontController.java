package in.co.rays.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.util.ServletUtility;

@WebFilter(filterName = "FrontController", urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

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

	@Override
	public void destroy() {

	}

}
