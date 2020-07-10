<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="common.jsp" %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>开始登陆</title>
</head>
<body>
	<div id="app">
		<div id="errorStyle">${error}</div>
		<el-form id="loginForm" action="${contextPath}loginDo/login" ref="loginForm" :model="form" label-width="80px" :rules="rules">
		  <el-form-item label="账号" prop="username">
		    <el-input v-model="form.username" name="tUsername"></el-input>
		    <a href="${contextPath}/registerDo/toRegister" target="_blank">没有账号？立即注册</a>
		  </el-form-item>
		  <el-form-item label="密码" prop="password">
		    <el-input placeholder="请输入密码" v-model="form.password" name="tPassword" show-password></el-input>
		  </el-form-item>
		  <div class="loginBtn">
			  <el-form-item>
			    <el-button type="primary" @click="onSubmit('loginForm')" @keyup.enter="enterEvent">立即登录</el-button>
			    <el-button>取消</el-button>
			  </el-form-item>
		  </div>
		</el-form>
	</div>
<script>
	var Main = {
	    data() {
	      return {
	        form: {
	          username: '',
	          password: ''
	        },
	        rules: {
		    	username:[
		    		{required:true,message:'请输入账号！ ',trigger:'blur'}
		    	],
		    	password:[
		    		{required:true,message:'请输入密码！ ',trigger:'blur'}
		    	]
		    }
	      };
	    },
	    created(){
	    	this.enterEvent();
	    }
	    ,
	    methods: {
	      enterEvent(formname){
	    		document.onkeydown = e =>{
	    			if(e.keyCode == 13){
	    				this.onSubmit("loginForm");
	    			}
	    		}
	      },
	      onSubmit(formname) {
	    	  this.$refs[formname].validate((valid)=>{
	    		  if(valid){
	    			  $("#"+formname).submit();
	    		  }else{
	    			  return false;
	    		  }
	    	  })
	      }
	    }
	  }
	var Ctor = Vue.extend(Main)
	new Ctor().$mount('#app')
</script>
</body>
</html>
