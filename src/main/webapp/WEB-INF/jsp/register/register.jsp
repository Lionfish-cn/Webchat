<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp" %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>开始注册...</title>
</head>
<body>
	<div id="app">
		<el-form id="registerForm" action="${contextPath}registerDo/register" ref="registerForm" :model="form" label-width="80px" :rules="rules">
		  <el-form-item label="昵称" prop="nickname">
		    <el-input v-model="form.nickname" name="tNickName"></el-input>
		  </el-form-item>
		  <el-form-item label="账号" prop="username">
		    <el-input v-model="form.username" name="tUsername"></el-input>
		  </el-form-item>
		  
		  <el-form-item label="密码" prop="password">
		    <el-input placeholder="请输入密码" v-model="form.password" name="tPassword" show-password></el-input>
		  </el-form-item>
		  <el-form-item label="邮箱" prop="email">
		    <el-input v-model="form.email" name="tEmail" style="width:74%"></el-input>
		    <el-select v-model="form.selectVal" name="emailSuffix" style="width:25%" placeholder="请选择">
			    <el-option
			      v-for="item in options"
			      :key="item.value"
			      :label="item.label"
			      :value="item.value">
			    </el-option>
			</el-select>
		  </el-form-item>
		  <el-form-item label="地址" prop="addr">
		    <el-input v-model="form.addr" name="tAddr"></el-input>
		  </el-form-item>
		  <el-form-item label="默认权限">
		  	<el-select v-model="form.roleUrl" name="roleUrl">
		  		<el-option
			  		v-for="item in roleUrls"
			  		:key="item.value"
			  		:label="item.label"
			  		:value="item.value">
		  		</el-option>
		  	</el-select>
		  </el-form-item>
		  <div class="loginBtn">
			  <el-form-item>
			    <el-button type="primary" @click="onSubmit('registerForm')">立即注册</el-button>
			    <el-button>取消</el-button>
			  </el-form-item>
		  </div>
		</el-form>
	</div>
<script>
var Main = {
	  data() {
    	var checkUsername = (rule,value,callback)=>{
    		if(value==""){
    			return callback(new Error("账号不能为空"));
    		}
    		
    		$.ajaxSetup({
    			async : false
    		});
			
    		var isExist = true;
    		$.ajax({
    			url:'toJudgeExist',
    			type:'post',
    			data:{"username":value},
    			dataType:'text',
    			success:function(data){
    				if(data!=""){
    					isExist = false;
    					callback(new Error(data));
    				}
    			}
    		})
    		if(isExist){
    			callback();
    		}
    	};
        return {
	        form: {
	          username: '',
	          password: '',
	          email:'',
	          addr:'',
	          selectVal:'@163.com',
	          nickname:'',
	          roleUrl:'/chatDo/toChatGui'
	        },
	        rules: {
		    	username:[
		    		{validator:checkUsername,trigger:'blur'},
		    		{min:2,max:10,message:'账号至少需要2-7个字符!'}
		    	],
		    	password:[
		    		{required:true,message:'请输入密码！ ',trigger:'blur'}
		    	],
		    	email:[
		    		{required:true,message:'请输入邮箱！ ',trigger:'blur'}
		    	],
		    	selectVal:[
		    		{required:true,message:'请选择邮箱后缀！ ',trigger:'change'}
		    	]
		    },
		    options: [{
		          value: '@163.com',
		          label: '@163.com'
		        }, {
		          value: '@qq.com',
		          label: '@qq.com'
		        }, {
		          value: '@126.com',
		          label: '@126.com'
		        }],
		    roleUrls:[{
		    	value:'/chatDo/toChatGui',
		    	label:'访问聊天页面'
		    }]
	      };
	    },
	    methods: {
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
