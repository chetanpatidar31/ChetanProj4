<%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="in.co.rays.bean.EmployeeBean"%>
<%@page import="in.co.rays.controller.EmployeeCtl"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Add Employee</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<form action="<%=ORSView.EMPLOYEE_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.bean.EmployeeBean"
			scope="request"></jsp:useBean>

		<%-- <%
			List<EmployeeBean> usernameList = (List<EmployeeBean>) request.getAttribute("usernameList");
		%> --%>

		<div align="center">
			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Employee
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>

				<H3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>
				<tr>
					<th align="left">Full Name<span style="color: red">*</span></th>
					<td><input type="text" name="fullName"
						placeholder="Enter Full Name"
						value="<%=DataUtility.getStringData(bean.getFullName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("fullName", request)%></font></td>
				</tr>
				
				<tr>
					<th align="left">Username<span style="color: red">*</span></th>
					<td><input type="text" name="username"
						placeholder="Enter Username"
						value="<%=DataUtility.getStringData(bean.getUsername())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("username", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Password<span style="color: red">*</span></th>
					<td><input type="password" name="password"
						placeholder="Enter Password"
						value="<%=DataUtility.getStringData(bean.getPassword())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("password", request)%></font></td>
				</tr>

				<tr>
					<th align="left">Birth Date<span style="width: 98%"
						style="color: red">*</span></th>
					<td><input type="text" id="udate" name="birthDate" placeholder="Select Date of Birth"
						value="<%=DataUtility.getDateString(bean.getBirthDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("birthDate", request)%></font></td>
				</tr>
				
				<tr>
					<th align="left">Contact No<span style="color: red">*</span></th>
					<td><input type="text" name="contactNo" maxlength="10"
						placeholder="Enter Contact No."
						value="<%=DataUtility.getStringData(bean.getContactNo())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("contactNo", request)%></font></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=EmployeeCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_CANCEL%>">
						<%
							} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=EmployeeCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=EmployeeCtl.OP_RESET%>">
						<%
							}
						%>
				</tr>
			</table>
		</div>
	</form>
	<%@ include file="Footer.jsp" %>
</body>
</html>