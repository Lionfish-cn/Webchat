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

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
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
			        <el-menu-item index="1-1">
			        <el-avatar shape="circle" @click.native="showAvatarDialog" size="100" fit="scale-down" :src="curUserImageurl"></el-avatar>&emsp;${curNickname}</el-menu-item>
  		            <el-menu-item index="1-2" @click.native="showDialog">查看个人资料</el-menu-item>
			        <el-menu-item index="1-3" @click.native="logout">
			        	退出登录
			        </el-menu-item>
			    </el-submenu>
			    <el-submenu index="2">
			        <template slot="title"><i class="el-icon-message"></i>聊天列表</template>
  		            <div id="el-menu-item"></div>
			    </el-submenu>
		      </el-menu>
		  </el-aside>
		  <el-container style="background-color:#eee">
		    <el-header style="text-align: right; font-size: 15px">
		    	<a href="${contextPath}bMapDo/toMap">查看地图</a>
		      <el-dropdown>
		        <i class="el-icon-setting"></i>
		        <el-dropdown-menu slot="dropdown">
		        	<el-dropdown-item @click.native="showDialog">查看资料</el-dropdown-item>
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
				<el-form-item label="昵称" prop="nickname">
				    <el-input v-model="dialogForm.nickname" name="tNickName"  style="width:75%"></el-input>
				    <el-input v-model="dialogForm.Id" name="Id"  style="display:none;"></el-input>
				</el-form-item>
				<el-form-item label="账号" prop="username">
				    <el-input readonly="true" v-model="dialogForm.username" name="tUsername"  style="width:55%"></el-input>
				</el-form-item>
				<el-form-item label="密码" prop="password">
				    <el-input readonly="true" placeholder="请输入密码" v-model="dialogForm.password" name="tPassword"  style="width:55%" show-password></el-input>
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
				<el-button type="primary" @click="commitDialog">提交</el-button>
				<el-button @click="cancelDialog('form')">取消</el-button>
			</div>
		</el-dialog>
		<el-dialog title="修改头像" :visible.sync="dialogAvatarVisible">
			<el-form id="avatarForm" >
				<el-form-item prop="avatar" label="头像">
				<!-- https://jsonplaceholder.typicode.com/posts/ -->
					<el-upload
					  class="avatar-uploader"
					  action="#"
					  :show-file-list="false"
					  :http-request = "handleAvatar"
					  :before-upload="beforeAvatarUpload">
					  <img v-if="imageUrl" :src="imageUrl" class="avatar">
					  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>			
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="updateAvatar">提交</el-button>
				<el-button @click="cancelDialog('avatar')">取消</el-button>
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
	        dialogAvatarVisible:false,
	        imageUrl:'',
	        file : null,
	        curUserImageurl:'${curUserImageurl}',
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
	    		if(typeof(rData)=="string"){
	    			return;
	    		}else{
	    			var list = rData.list;
	    			var _json = eval('('+list+')')
	    			for(var i=0;i<_json.length;i++){
	    				var userid = _json[i].userid;
	    				var name = _json[i].name;
	    				var imageUrl = _json[i].imageUrl;
	    				var isntRead = _json[i].isntRead;
	    				if(isntRead<=0){
	    					isntRead = "";
	    				}
	    				if($("[userid='"+userid+"']").length==0){
		    				$("#el-menu-item").before("<ul role='menu' class='el-menu el-menu--inline'>"+
			    				"<li role='menuitem' tabindex='-1' onclick='startChat(this)' userid='"+userid+"' class='el-menu-item' style='padding-left: 40px;'>"+
					    		"<span class='el-avatar el-avatar--circle'><img src='"+imageUrl+"' style='object-fit: cover;'></span>&emsp;"+
					    		"<div class='el-badge item'>"+name+
					    		"<sup class='"+(isntRead==""?"":'el-badge__content el-badge__content--undefined is-fixed')+"' supuserid='"+userid+"'>"+isntRead+"</sup></div></li></ul");
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
	    			this.$message({
	    				message:'你就写点东西吧！',
	    				type:'warning'
	    			});
	    			return;
	    		}
	    		var to = $("#toUser").text();
	    		if(to==""){
	    			this.$message({
	    				message:'你是要发给空气吗？',
	    				type:'warning'
	    			});
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
			    				$("#el-menu-item").append("<ul role='menu' class='el-menu el-menu--inline'><li role='menuitem' tabindex='-1' onclick='startChat(this)' userid='"+userid+"' class='el-menu-item' style='padding-left: 40px;'>"+
			    				"<span class='el-avatar el-avatar--circle'><img src='https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' style='object-fit: cover;'></span>"+userid+"</li></ul");	
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
	    	cancelDialog(t){
	    		if(t=="form"){
		    		this.dialogFormVisible = false;
	    		}else{
	    			this.dialogAvatarVisible = false;
	    		}
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
	    			this.dialogForm.username = rtnData.tUsername;
	    			this.dialogForm.email = rtnData.tEmail;
	    			this.dialogForm.addr = rtnData.tAddr;
	    			this.dialogForm.Id = rtnData.Id;
	    		}
	    	},
	    	handleAvatar(data) {
	            this.imageUrl = URL.createObjectURL(data.file);
	            this.file =  data.file;
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
	       	},
	       	updateAvatar(){
	       		var formdata = new FormData();
	       		formdata.append("filename",this.file);
	       		formdata.append("username",'${curUsername}');
	       		var msg = commonAjax('${contextPath}avatarUploaderDo/uploader','post',formdata,'json',false,true);
	       		if(msg!=""){
	       			this.curUserImageurl = msg;
	       			this.dialogAvatarVisible = false;
	       		}
	       	},
	       	showAvatarDialog(){
	       		this.dialogAvatarVisible = true;
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
		
		$("[supuserid='"+userid+"']").attr("class","")
		$("[supuserid='"+userid+"']").html("")
		
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
		var ids = "";
		for(var i=0;i<records.length;i++){
			var record = records[i];
			var isRead = record.tIsRead == "1" ? "已读":"未读";
			if(record.tSend=='${curUsername}'){
				lis += "<li class='chat-right-li' id='"+record.Id+"'>"+record.tRecord+"<span class='el-avatar el-avatar--circle'><img src='"+record.sendAvatar+"' style='object-fit: cover;'></span>&emsp;"+isRead+"</li>";
			}else{
				lis += "<li class='chat-left-li' id='"+record.Id+"'><span class='el-avatar el-avatar--circle'><img src='"+record.targetAvatar+"' style='object-fit: cover;'></span>"+record.tRecord+"&emsp;"+isRead+"</li>";
			}
			if(ids==""){
				ids = record.Id;
			}else{
				ids = ids + ";" + record.Id;
			}
		}
		$("#el-main").prepend(lis);
		
		//设置进入查看页面三秒后，将聊天记录设置为已读
		window.setTimeout(function(){
			var data = {"ids":ids};
			commonAjax('${contextPath}recordDo/isntRead','post',data,'json',false);
		},3000);
	}
</script>
</body>
</html>