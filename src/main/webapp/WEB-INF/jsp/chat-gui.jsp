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
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
</head>
<body>
	<div id="chat-gui-app">
		<el-container style="height: 95%; border: 1px solid #eee">
		  <el-aside width="300px" style="background-color: #eee">
			   <el-menu style="height:100%" :default-openeds="['1', '3']">
			    <el-submenu index="1">
			        <template slot="title"><i class="el-icon-message"></i>聊天列表</template>
  		            <div id="el-menu-item">
  		            </div>
			    </el-submenu>
		      </el-menu>
		  </el-aside>
		  <el-container style="background-color:#eee">
		    <el-header style="text-align: right; font-size: 15px">
		      <el-dropdown>
		        <i class="el-icon-setting" style="margin-right: 15px"></i>
		        <el-dropdown-menu slot="dropdown">
		        	<el-dropdown-item>查看个人资料</el-dropdown-item>
		          	<el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
		        </el-dropdown-menu>
		      </el-dropdown>
		      <span>${curNickname}<div id="toUser"></div></span>
		      
		    </el-header>
		    <el-main>
		      <ul id="el-main">
		      	
		      </ul>
		    </el-main>
		    <el-footer height="100px">
		    	<el-input type="textarea" style="width:90%" :rows="3" placeholder="请输入内容"  v-model="textarea"></el-input>
		    	<el-button type="primary" @click="onSendMessage()" @keyup.enter="enterEvent" icon="el-icon-right" style="height:74%;width:5%"></el-button>
		    </el-footer>
		  </el-container>
		</el-container>
	</div>
		
<script type="text/javascript">
	var Main = {
	    data() {
	      return {
	        textarea : '',
	        to : '',
	        wsServer : null,
	        ws : null
	      }
	    },
	    created(){
	    	this.initWebSocket();
	    	this.initLoadChatList();
	    },
	    methods:{
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
	    	initLoadChatList(){
					    		
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
			    				$("#el-menu-item").append("<li role='menuitem' tabindex='-1' onclick='startChat(this)' userid='"+userid+"' class='el-menu-item' style='padding-left: 40px;'>"+userid+"</li>");	
			    			}
		    			}
		    		}
    			}else if(type=="message"){
    				var li = "";
    				if(json.from=='${curUsername}'){
	    				li = "<li style='text-align:right'>"+json.content+"</li>";
    				}else{
    					li = "<li style='text-align:left'>"+json.content+"</li>";
    				}
    				$("#el-main").append(li);
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
	    	}
	    }
	  };
	var Ctor = Vue.extend(Main)
	new Ctor().$mount('#chat-gui-app')
	
	function startChat(obj){
		var userid = $(obj).attr("userid");
		$("#toUser").text(userid);
		
	}
</script>
</body>
</html>