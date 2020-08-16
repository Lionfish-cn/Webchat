<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/../../common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Show Map</title>
<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0px;
	padding: 0px;
}

#bmap {
	height: 100%;
}

.search-input {
	position:absolute;
	margin-top:-1000px;
	margin-left:calc(100%/2);
}
</style>
</head>
<body>
	<div id="BMap.app">
		<div id="bmap"></div>
		<div class="search">
			<el-input class="search-input" style="z-index:10000;width:200px;height:50px;" placeholder="请输入地点" suffix-icon="el-icon-search"
				v-model="search"></el-input>
		</div>
	</div>
</body>
<script type="text/javascript">
	var Main = {
	    data() {
	      return {
	        search : ''
	      }
	    },
	    created(){
	    	this.initBMap();
	    },
	    methods:{
	    	initBMap(){
	    		var map = new BMapGL.Map("bmap");
	    		var point = new BMapGL.Point("${longitude}",'${latitude}');//经纬度
	    		map.centerAndZoom(point,15);
	    		map.enableScrollWheelZoom(true);
	    		map.addControl(new BMapGL.ScaleControl());    
	    		map.addControl(new BMapGL.ZoomControl());
	    		map.addOverlay(new BMapGL.LocalSearch(map,))
	    	}
	    }
	  };
	var Ctor = Vue.extend(Main)
	new Ctor().$mount('#BMap.app')
</script>
</html>