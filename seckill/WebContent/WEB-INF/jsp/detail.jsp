<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
  <head>
    <title>秒杀详情页</title>
     <%@include file="common/head.jsp" %>
  	 <script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
  </head>
  <body>
    <div class = "contianer">
    	<div class = "panel panel-default text-center">
    		<div class="panel-heading"> 
    			<h1>${ seckill.name }</h1>
    		 </div>
    		 
    		 <div class="panel-body" >
	    		<h2 class="text-danger">
	    			<span class="glyphicon glyphicon-time"></span>
	    			<span class="glyphicon" id="seckill-box" ></span>
	    		</h2>
    	
    		</div>
    	</div>
    	
    
    </div>
    
  	<div id="killPhoneModal" class="modal fade">
  		<div class="modal-dialog">
  			<div class="modal-content">
  				<div class = "modal-header">
  					<h3 class="modal-title text-center">
  						<span class="glyphicon glyphicon-phone"></span>秒杀电话：
  					</h3>
  				</div>
  				<div class="modal-body">
  					<div class="row">
  						<div class="col-xs-8 col-xs-offset-2">
  							<input type="text" name="killPhone" id="killPhoneKey" placeholder="填入手机号^O^" class="form-control" />
  						</div>
  					</div>
  				</div>
  				<div class="modal-footer">
  					<span id="killPhoneMessage" class="glyphicon"></span>
  					<button type = "button" id="killPhoneBtn" class="btn btn-success">
  						<span class="glyphicon glyphicon-phone"></span>
  						Submit
  					</button>
  				</div>
  			</div>
  		</div>
  	</div>
   </body>
   <script type="text/javascript" src="<%=request.getContextPath()%>/resources/script/seckill.js"></script>
   <script type="text/javascript">
   	$(function() {
		seckill.detail.init({
			seckillId : ${seckill.seckillId},
			startTime : ${seckill.startTime.time},
			endTime : ${seckill.endTime.time}
		});
	});
   </script>
   
</html>