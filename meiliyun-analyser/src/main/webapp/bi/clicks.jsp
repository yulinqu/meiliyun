<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>clicks</title>

<style>
        table,table tr th, table tr td { border:1px solid black; }
        table { width: 200px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse;}   
    </style>

</head>
<body>
	<h1>点击数统计报表</h1>
	<h3>查询状态 : ${status}</h3> 
	<h3>查询时间 : ${querytime}</h3>
	<c:if test="${status=='成功'}">
		<table style="width:60%;border:1px black solid">  
		    <tr style="border:1px black solid">  
		        <th style="text-align: center">时间段</th>  
		        <th style="text-align: center">URL</th>
				<th style="text-align: center">CHANNEL</th>
		        <th style="text-align: center">广告</th>  
		        <th style="text-align: center">大区域</th> 
		        <th style="text-align: center">位置</th>  
		        <th style="text-align: center">点击数</th>   
		    </tr>  
		    <c:forEach items="${clicks}" var="row" >  
		        <tr style="border:1px black solid">  
		            <td align="center">${row.timeRange}</td>
		            <td align="center">${row.url}</td>
					<td align="center">${row.channel}</td>
		            <td align="center">${row.advertisment}</td>  
		            <td align="center">${row.AREA}</td>  
		            <td align="center">${row.POSITION}</td>  
		            <td align="center">${row.totalCount}</td>    
		        </tr>  
		    </c:forEach>  
		</table>  
    </c:if>
</body>
</html>