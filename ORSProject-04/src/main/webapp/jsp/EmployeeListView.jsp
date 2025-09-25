<%@page import="java.text.SimpleDateFormat"%>
<%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.bean.EmployeeBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.controller.EmployeeListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<html>
<head>
    <title>Employee List</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
    <%@include file="Header.jsp"%>

    <jsp:useBean id="bean" class="in.co.rays.bean.EmployeeBean" scope="request"></jsp:useBean>

    <div align="center">
        <h1 align="center" style="margin-bottom: -15; color: navy;">Employee List</h1>

        <div style="height: 15px; margin-bottom: 12px">
            <h3><font color="red"><%=ServletUtility.getErrorMessage(request)%></font></h3>
            <h3><font color="green"><%=ServletUtility.getSuccessMessage(request)%></font></h3>
        </div>

        <form action="<%=ORSView.EMPLOYEE_LIST_CTL%>" method="post">
            <%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

                List<EmployeeBean> usernameList = (List<EmployeeBean>) request.getAttribute("usernameList");
                List<EmployeeBean> list = (List<EmployeeBean>) ServletUtility.getList(request);
                Iterator<EmployeeBean> it = list.iterator();

                if (list.size() != 0) {
            %>

            <input type="hidden" name="pageNo" value="<%=pageNo%>">
            <input type="hidden" name="pageSize" value="<%=pageSize%>">

            <table style="width: 100%">
                <tr>
                    <td align="center">
                        <label><b>Full Name :</b></label>
                        <input type="text" name="fullName" placeholder="Enter full Name" value="<%=ServletUtility.getParameter("fullName", request)%>">&emsp;

                    	<%-- <label><b>Contact No :</b></label>
                    	<input type="text" name="contactNo" placeholder="Enter Contact No" value="<%=ServletUtility.getParameter("contactNo", request)%>"> --%>

                        <label><b>Username : </b></label>
                        <%=HTMLUtility.getList("username", String.valueOf(bean.getUsername()), usernameList)%>&emsp;

						 <label><b>Birth Date : </b></label>
                         <input type="text" name="birthDate" placeholder="Enter Birth Date" id="udate" value="<%=ServletUtility.getParameter("birthDate", request)%>">&emsp;

                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_SEARCH%>">
                        &nbsp;
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_RESET%>">
                    </td>
                </tr>
            </table>
            <br>

            <table border="1" style="width: 100%; border: groove;">
                <tr style="background-color: #e1e6f1e3;">
                    <th width="5%"><input type="checkbox" id="selectall" /></th>
                    <th width="5%">S.No</th>
                    <th width="22%">Full Name</th>
                    <th width="26%">Username</th>
                    <th width="17%">Contact No</th>
                    <th width="17%">Date of Birth</th>
                    <th width="8%">Edit</th>
                </tr>

                <%
                    while (it.hasNext()) {
                        bean = (EmployeeBean) it.next();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = sdf.format(bean.getBirthDate());
                %>

                <tr>
                    <td style="text-align: center;"><input type="checkbox" class="case" name="ids" value="<%=bean.getId()%>"></td>
                    <td style="text-align: center;"><%=index++%></td>
                    <td style="text-align: center; text-transform: capitalize;"><%=bean.getFullName()%></td>
                    <td style="text-align: center; text-transform: lowercase;"><%=bean.getUsername()%></td>
                    <td style="text-align: center;"><%=bean.getContactNo()%></td>
                    <td style="text-align: center;"><%=date%></td>
                    <td style="text-align: center;"><a href="<%=ORSView.EMPLOYEE_CTL%>?id=<%=bean.getId()%>">Edit</a></td>
                </tr>

                <%
                    }
                %>
            </table>

            <table style="width: 100%">
                <tr>
                    <td style="width: 25%">
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_PREVIOUS%>" <%=pageNo > 1 ? "" : "disabled"%>>
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_NEW%>">
                    </td>
                    <td align="center" style="width: 25%">
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_DELETE%>">
                    </td>
                    <td style="width: 25%" align="right">
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_NEXT%>" <%=nextListSize != 0 ? "" : "disabled"%>>
                    </td>
                </tr>
            </table>
            
            <%
                } else {
            %>

            <table>
                <tr>
                    <td align="right">
                        <input type="submit" name="operation" value="<%=EmployeeListCtl.OP_BACK%>">
                    </td>
                </tr>
            </table>

            <%
                }
            %>
        </form>
        
        <br><br><br><br>
    </div>
    <%@ include file="Footer.jsp" %>
</body>
</html>