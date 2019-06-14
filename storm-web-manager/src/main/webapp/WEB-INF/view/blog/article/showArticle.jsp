<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href=" <%=basePath%>">
<jsp:include page="../../head.jsp"/>
<link rel="stylesheet" href="static/css/showArticle.css" />
</head>
<body>
	<div class="main-content">
		<div class="article-view">
			<div class="header">
				<div class="title">${article.title}</div>
				<div class="time"><fmt:formatDate pattern="yyyy-MM-dd" value="${article.createTime}" /></div>
				<div class="line"></div>
			</div>
			<div class="section">
				<div class="content">${article.content}</div>
			</div>
			<div class="footer" <c:if test="${empty enclosures}">style="display: none;"</c:if>>
				<div class="line"></div>
				<div class="en-title">附件列表：</div>
				<ul class="en-list">
					<c:forEach var="en" items="${enclosures}">
						<li><a onclick="fileDownload('${en.filePath}','${en.fileName}')">${en.fileName}</a></li>
					</c:forEach>
				<ul/>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../../foot.jsp"/>
</html>