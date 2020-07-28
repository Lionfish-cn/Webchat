<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>聊天开始</title>
<style>
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
</style>
</head>
<body>
	<div id="chat-gui-app">
		<el-container style="height: 95%; border: 1px solid #eee">
		  <el-aside width="300px" style="background-color: #eee">
			   <el-menu style="height:100%" :default-openeds="['1', '2']">
			   	<el-submenu index="1">
			        <template slot="title"><i class="el-icon-message"></i>个人资料</template>
			        <el-menu-item index="1-1"><el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>&emsp;${curNickname}</el-menu-item>
  		            <el-menu-item index="1-2" @click.native="showDialog">查看个人资料</el-menu-item>
			        <el-menu-item index="1-3" @click.native="logout">退出登录</el-menu-item>
			    </el-submenu>
			    <el-submenu index="2">
			        <template slot="title"><i class="el-icon-message"></i>聊天列表</template>
  		            <div id="el-menu-item"></div>
			    </el-submenu>
		      </el-menu>
		  </el-aside>
		  <el-container style="background-color:#eee">
		    <el-header style="text-align: right; font-size: 15px">
		      <el-dropdown>
		        <i class="el-icon-setting"></i>
		        <el-dropdown-menu slot="dropdown">
		        	<el-dropdown-item @click.native="showDialog">查看个人资料</el-dropdown-item>
		        </el-dropdown-menu>
		        <span id="toUser"></span>
		      </el-dropdown>
		    </el-header>
		    <el-main>
		      <ul id="el-main">
		      </ul>
		      <div style="display:none;" id="pageNo">1</div>
              <div style="display:none;" id="rowSize">10</div>
		    </el-main>
		    <el-footer height="100px">
		    	<el-input type="textarea" style="width:90%" :rows="3" placeholder="请输入内容"  v-model="textarea"></el-input>
		    	<el-button type="primary" @click="onSendMessage()" @keyup.enter="enterEvent" icon="el-icon-right" style="height:74%;width:5%"></el-button>
		    </el-footer>
		  </el-container>
		</el-container>
		<el-dialog title="个人资料" :visible.sync="dialogFormVisible">
			<el-form id="personForm" :rules="rules" ref="dialogForm" :model="dialogForm" action="${contextPath}loginDo/update">
				<el-form-item prop="avatar" label="头像">
					<el-upload
					  class="avatar-uploader"
					  action="${contextPath}avatarUploaderDo/uploader"
					  :show-file-list="false"
					  :on-success="handleAvatarSuccess"
					  :before-upload="beforeAvatarUpload">
					  <img v-if="imageUrl" :src="imageUrl" class="avatar">
					  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<el-form-item label="昵称" prop="nickname">
				    <el-input v-model="dialogForm.nickname" name="tNickName"  style="width:75%"></el-input>
				    <el-input v-model="dialogForm.Id" name="Id"  style="display:none;"></el-input>
				</el-form-item>
				<el-form-item label="账号" prop="username">
				    <el-input readonly="true" v-model="dialogForm.username" name="tUsername"  style="width:55%"></el-input>
					<div class="end-title">不允许修改账号！</div>
				</el-form-item>
				<el-form-item label="密码" prop="password">
				    <el-input readonly="true" placeholder="请输入密码" v-model="dialogForm.password" name="tPassword"  style="width:55%" show-password></el-input>
					<div class="end-title" onlick="updatePassword()">修改密码</div>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
				    <el-input v-model="dialogForm.email" name="tEmail" style="width:50%"></el-input>
				    <el-select v-model="dialogForm.selectVal" name="emailSuffix" style="width:25%" placeholder="请选择">
					</el-select>
				</el-form-item>
				<el-form-item label="地址" prop="addr">
				    <el-input v-model="dialogForm.addr" name="tAddr"  style="width:75%"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="commitDialog">取消</el-button>
				<el-button @click="cancelDialog">确定</el-button>
			</div>
		</el-dialog>
	</div>
		
<script type="text/javascript">
	var Main = {
	    data() {
	    	
	      return {
	        textarea : '',
	        to : '',
	        wsServer : null,
	        ws : null,
	        dialogFormVisible:false,
	        imageUrl:'',
	        dialogForm:{
        	  username: '',
	          password: '',
	          email:'',
	          addr:'',
	          selectVal:'@163.com',
	          nickname:'',
	        },
	        rules: {
	        	nickname:[
		    		{required:true,message:'请输入昵称！ ',trigger:'blur'}
		    	],
		    	email:[
		    		{required:true,message:'请输入邮箱！ ',trigger:'blur'}
		    	]
		    }
	      }
	    },
	    created(){
	    	this.initWebSocket();
	    	this.loadChatPerson();
	    },
	    methods:{
	    	loadChatPerson(){
	    		var data = {"from":'${curUsername}'};
	    		var rData = commonAjax("${contextPath}recordDo/searchChatPerson","post",data,"json",false);
	    		var data_=null;
	    		if(typeof(rData)=="string"){
	    			return;
	    		}else{
	    			var list = rData.list;
	    			for(var i=0;i<list.length;i++){
	    				var userid = list[i];
	    				if($("[userid='"+userid+"']").length==0){
		    				$("#el-menu-item").before("<li role='menuitem' tabindex='-1' onclick='startChat(this)' userid='"+userid+"' class='el-menu-item' style='padding-left: 40px;'>"+
				    		"<span class='el-avatar el-avatar--circle'><img src='https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' style='object-fit: cover;'></span>"+userid+"</li>");
	    				}
	    			}
	    		}
	    	},
	    	initWebSocket(){
	    		if(this.ws!=null){
	    			return;
	    		}
	    		this.wsServer = "ws://"+location.host+'${contextPath}'+"/webSocket/${curUsername}";
	    		this.ws = new WebSocket(this.wsServer);
	    		this.ws.onopen = this.onOpenWebSocket;
	    		this.ws.onclose = this.onCloseWebSocket;
	    		this.ws.onmessage = this.parseOnlineList;
	    		this.ws.onerror = function(evt){
	    			console.log("连接失败！");
	    		}
	    	},
	    	onOpenWebSocket(){
	    		console.log("连接websocket成功！");
	    	},
	    	onCloseWebSocket(){
	    		console.log("websocket已断开");
	    	},
	    	onSendMessage(){
	    		if(this.ws==null){
	    			alert("11");
	    			return;
	    		}
	    		var value = this.textarea;
	    		if(value==""){
	    			alert("说点什么吧！");
	    			return;
	    		}
	    		var to = $("#toUser").text();
	    		if(to==""){
	    			alert("你想要发给谁哦");
	    			return;
	    		}
	    		this.ws.send(JSON.stringify({
	                message : {
	                    content : value,
	                    from : '${curUsername}',
	                    to : to,      //接收人,如果没有则置空,如果有多个接收人则用,分隔
	                    time : this.getDateFull()
	                },
	                type : "message"
	            }));
	    		this.textarea = "";
	    	},
	    	enterEvent(evt){
	    		document.onkeydown = e =>{
	    			if(e.keycode == 13){
	    				onSendMessage();
	    			}
	    		}
	    	},
	    	parseOnlineList(data){
    			var json = eval('('+data.data+')');
    			var type = json.type;
    			if(type=="notice"){
		    		var list = json.list;
		    		for(var i=0;i<list.length;i++){
		    			var userid = list[i];
		    			var curuser = $("[userid='"+userid+"']");
		    			if(curuser.length<=0){
			    			if(userid != '${curUsername}'){//排除自己
			    				$("#el-menu-item").append("<li role='menuitem' tabindex='-1' onclick='startChat(this)' userid='"+userid+"' class='el-menu-item' style='padding-left: 40px;'>"+
			    				"<span class='el-avatar el-avatar--circle'><img src='https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' style='object-fit: cover;'></span>"+userid+"</li>");	
			    			}
		    			}
		    		}
    			}else if(type=="message"){
    				var li = "";
    				if(json.from=='${curUsername}'){
	    				li = "<li class='chat-right-li'>"+json.content+"</li>";
    				}else{
    					li = "<li class='chat-left-li'>"+json.content+"</li>";
    				}
    				$("#el-main").append(li);
    				
    				var data = {"to":json.to,"from":json.from,"time":json.time,"content":json.content};
    				commonAjax("${contextPath}recordDo/saveRecord","post",data,"json",false);
    			}
 	    	},
	    	getDateFull(){
	    		var d = new Date();
	    		var fullYear = d.getFullYear();
	    		var month = d.getMonth();
	    		var day = d.getDate();//一个月某一天
	    		var hour = d.getHours();
	    		var m = d.getMinutes();
	    		return fullYear + "-" + (month+1) +"-" + day+" "+hour+":"+m;
	    	},
	    	logout(){
	    		location.href="${contextPath}/logout";
	    	},
	    	cancelDialog(){
	    		this.dialogFormVisible = false;
	    	},
	    	commitDialog(){
	    		$("#personForm").submit();
	    	},
	    	showDialog(){
	    		this.dialogFormVisible = true;
	    		var curUsername = '${curUsername}';
	    		var data = {"curUsername":curUsername};
	    		var rtnData = commonAjax("${contextPath}loginDo/searchUser","post",data,"json",false);
	    		if(rtnData!="" || typeof(rtnData) == "undefined"){
	    			this.dialogForm.nickname = rtnData.tNickName;
	    			this.dialogF
	    			orm.username = rtnData.tUsername;
	    			this.dialogForm.email = rtnData.tEmail;
	    			this.dialogForm.addr = rtnData.tAddr;
	    			this.dialogForm.Id = rtnData.Id;
	    		}
	    	},
	    	handleAvatarSuccess(res, file) {
	            this.imageUrl = URL.createObjectURL(file.raw);
	            alert(this.imageUrl);
	        },
	        beforeAvatarUpload(file) {
	            const isJPG = file.type === 'image/jpeg';
	            const isLt2M = file.size / 1024 / 1024 < 2;

	            if (!isJPG) {
	              this.$message.error('上传头像图片只能是 JPG 格式!');
	            }
	            if (!isLt2M) {
	              this.$message.error('上传头像图片大小不能超过 2MB!');
	            }
	            return isJPG && isLt2M;
	       	}
	    }
	  };
	var Ctor = Vue.extend(Main)
	new Ctor().$mount('#chat-gui-app')
	
	function startChat(obj){
		var userid = $(obj).attr("userid");
		$("#toUser").text(userid);
		
		var to = userid;
		var from = '${curUsername}';
		
		var rowSize = $("#rowSize").text();
		var pageNo = $("#pageNo").text();
		var data = {"to":to,"from":from,"rowSize":rowSize,"pageNo":pageNo};
		
		var rtnData = commonAjax('${contextPath}/recordDo/searchRecord','post',data,"json",false);
		var data_ = rtnData;
		$("#pageNo").text(data_.pageNo);
		$("#rowSize").text(data_.rowSize);
		var records = data_.list;
		$("#el-main").html("");
		var lis = "";
		for(var i=0;i<records.length;i++){
			var record = records[i];
			if(record.tSend=='${curUsername}'){
				lis += "<li class='chat-right-li'>"+record.tRecord+"<span class='el-avatar el-avatar--circle'><img src='https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' style='object-fit: cover;'></span></li>";
			}else{
				lis += "<li class='chat-left-li'>"+record.tRecord+"<span class='el-avatar el-avatar--circle'><img src='https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' style='object-fit: cover;'></span></li>";
			}
		}
		$("#el-main").prepend(lis);
	}
</script>
</body>
</html>