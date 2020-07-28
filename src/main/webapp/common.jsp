<%@page import="com.chat.entity.Login"%>
<%@page import="com.chat.util.UserUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath()+"/";
	request.setAttribute("contextPath", contextPath);
	Login login = UserUtil.getUser();
	System.out.println(login);
	if(login!=null){
		System.out.println(login.gettUsername());
		request.setAttribute("curUsername", login.gettUsername());
		request.setAttribute("curNickname", login.gettNickName());
		request.setAttribute("curUserid", login.getId());
	}
%>
<script type="text/javascript" src="<%=contextPath %>resource/js/vue.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>resource/js/jQuery.3.5.1.js"></script>
<%-- <script type="text/javascript" src="<%=contextPath %>resource/js/sockjs.js"></script> --%>
<script type="text/javascript" src="<%=contextPath %>resource/js/common.js"></script>
<link type="text/css" rel="stylesheet"  href="<%=contextPath %>resource/css/index.css" />
<link type="text/css" rel="stylesheet"  href="<%=contextPath %>resource/css/chat-gui.css" />
<!-- Element-ui -->
<!-- 引入样式 -->
<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
<!-- 引入组件库 -->
<script src="https://unpkg.com/element-ui/lib/index.js"></script>


<style>
	.end-title{
		width:150px;
		position:absolute;
		margin-left:calc(100%-150px);
		color:#eee;
	}
</style>