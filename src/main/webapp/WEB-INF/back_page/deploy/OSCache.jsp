<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="oscache" prefix="cache"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>jeecms-main</title>
</head>


	没有缓存的日期:
	<%=new Date()%><p>
		<!--自动刷新-->
		<cache:cache time="30"> 
每30秒刷新缓存一次的日期: <%=new Date()%>
		</cache:cache>
		<!--手动刷新-->
		<cache:cache key="testcache"> 
手动刷新缓存的日期: <%=new Date()%>
			<p>
		</cache:cache>
		<a href="cache2.jsp">手动刷新</a>
</body>
</html>
