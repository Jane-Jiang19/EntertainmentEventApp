<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.swing.event.DocumentEvent" %>
<%@ page import="org.bson.Document" %><%--
  Created by IntelliJ IDEA.
  User: Jiang
  Date: 2022/9/22
  Time: 12:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<style>
    table, th, td {
        border:1px solid black;
    }
</style>
<body>
<h1>DashBoard</h1>
<h2>Log</h2>

<table style="width:100%">
    <tr>
        <th>Keyword</th>
        <th>Event Name</th>
        <th>Time of Receiving Request</th>
        <th>Time of Sending Response</th>
        <th>Time of Sending Request to API</th>
        <th>Time of Receiving Response from API</th>
    </tr>
    <% int i = 0; %>
    <% ArrayList<Document> docs = (ArrayList<Document>) request.getSession().getAttribute("docs");%>
    <% if (docs != null){ %>
        <% while (i< docs.size()){%>
        <tr>
            <%Document d = (Document) docs.get(i);%>
            <td><%=d.get("keyword")%></td>
            <td><%=d.get("result")%></td>
            <td><%=d.get("getReqTime")%></td>
            <td><%=d.get("sendResTime")%></td>
            <td><%=d.get("reqAPITime")%></td>
            <td><%=d.get("resAPITime")%></td>
            <% i += 1;%>
        </tr>
        <%}%>
    <%}%>
</table>



<h2>Analytics</h2>

<h3>The average response time of the EventMaster web service is: </h3>
<% if (request.getAttribute("appTime") != null) { %>
<p><%=request.getAttribute("appTime")%> millionseconds</p>
<%}%>
<h3>The average response time of the 3rd party API app is: </h3>
<% if (request.getAttribute("apiTime") != null) { %>
<p><%=request.getAttribute("apiTime")%> millionseconds</p>
<%}%>
<h3>The top search word is: </h3>
<% if (request.getAttribute("topkeyword") != null) { %>
<p><%=request.getAttribute("topkeyword")%></p>
<%}%>


</body>
</html>
