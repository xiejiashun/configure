Date.prototype.format = function(format){ 
    var o =  { 
    "M+" : this.getMonth()+1, //month 
    "d+" : this.getDate(), //day 
    "h+" : this.getHours(), //hour 
    "m+" : this.getMinutes(), //minute 
    "s+" : this.getSeconds(), //second 
    "q+" : Math.floor((this.getMonth()+3)/3), //quarter 
    "S" : this.getMilliseconds() //millisecond 
    };
    if(/(y+)/.test(format)){ 
    	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
    for(var k in o)  { 
	    if(new RegExp("("+ k +")").test(format)){ 
	    	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	    } 
    } 
    return format; 
};

var TT = TAOTAO = {
	// 编辑器参数
	kingEditorParams : {
		//指定上传文件参数名称
		filePostName  : "uploadFile",//相当于<input type="file" name="uploadFile"
		//指定上传文件请求的url。
		uploadJson : '/pic/upload',//
		//上传类型，分别为image、flash、media、file
		dir : "image"
	},
	// 格式化时间
	formatDateTime : function(val,row){
		var now = new Date(val);
    	return now.format("yyyy-MM-dd hh:mm:ss");
	},
	// 格式化连接
	formatUrl : function(val,row){
		if(val){
			return "<a href='"+val+"' target='_blank'>查看</a>";			
		}
		return "";
	},
	// 格式化价格
	formatPrice : function(val,row){
		return (val/1000).toFixed(2);
	},
	// 格式化商品的状态
	formatItemStatus : function formatStatus(val,row){
        if (val == 1){
            return '正常';
        } else if(val == 2){
        	return '<span style="color:red;">下架</span>';
        } else {
        	return '未知';
        }
    },
    
    init : function(data){
    	// 初始化图片上传组件
    	this.initPicUpload(data);
    	// 初始化选择类目组件
    	this.initItemCat(data);
    },
    // 初始化图片上传组件
    initPicUpload : function(data){
    	//循环遍历  
    	$(".picFileUpload").each(function(i,e){
    		//转成juqery对象
    		var _ele = $(e);
    		//获取兄弟节点删除<div class="pics"> 标签
    		_ele.siblings("div.pics").remove();
    		//追加一个标签
    		_ele.after('<div class="pics"><ul></ul></div>');
    		
        	//给“上传图片按钮”绑定click事件
        	$(e).click(function(){
        		// 获取表单itemAddForm  类似于：$("#itemAddForm")
        		var form = $(this).parentsUntil("form").parent("form");
        		
        		//打开图片上传窗口
        		//打开第三方的图片上传的插件
        		KindEditor.editor(TT.kingEditorParams).loadPlugin('multiimage',function(){
        			var editor = this;
        			//打开多图片上传的窗口
        			editor.plugin.multiImageDialog({
        				//当点击全部插入之后执行的业务逻辑
        				//urlList 就是 上传成功之后的图片的路径的列表  （在数据库中存储的是以逗号分隔的字符串：12314.jpg,2342342.jpg）
						clickFn : function(urlList) {
							var imgArray = [];
							
							KindEditor.each(urlList, function(i, data) {
								//添加数据到数组对象中
								//data.url  :就是图片的路径
								imgArray.push(data.url);
								// 回显图片
								form.find(".pics ul").append("<li><a href='"+data.url+"' target='_blank'><img src='"+data.url+"' width='80' height='50' /></a></li>");
							});
							//将12314.jpg,2342342.jpg路径放入 隐藏域image中
							form.find("[name=image]").val(imgArray.join(","));//分隔数组 以逗号分隔成：12314.jpg,2342342.jpg
							//关闭窗口
							editor.hideDialog();
						}
					});
        		});
        		
        		
        	});
    	});
    },
    
    // 初始化选择类目组件
    initItemCat : function(data){
    	//类选择器 循环遍历
    	$(".selectItemCat").each(function(i,e){
    		//转成jquery的对象
    		var _ele = $(e);
    		
    		if(data && data.cid){
    			_ele.after("<span style='margin-left:10px;'>"+data.cid+"</span>");
    		}else{
    			//追加标签的内容到 <a标签后面
    			_ele.after("<span style='margin-left:10px;'></span>");
    		}
    		//当点击的时候，触发一下的业务逻辑
    		_ele.unbind('click').click(function(){
    			//创建一个div标签对象，添加样式 之后再在div中创建<ul>标签
    			$("<div>").css({padding:"5px"}).html("<ul>")
    			//创建一个窗口     打开这个窗口
    			.window({
    				width:'500',
    			    height:"450",
    			    modal:true,
    			    closed:true,
    			    iconCls:'icon-save',
    			    title:'选择类目',
    			    //当打开窗口的时候触发一下的业务逻辑
    			    onOpen : function(){
    			    	//获取窗口本身的对象
    			    	var _win = this;
    			    	//$("ul",_win)  在 当前窗口下的ul标签中创建树控件
    			    	$("ul",_win).tree({
    			    		url:'/item/cat/list',//参数就是id
    			    		animate:true,
    			    		//当点击节点的时候触发
    			    		onClick : function(node){
    			    			//判断被点击的节点是否为叶子节点 这个表示是叶子节点执行以下的逻辑
    			    			if($(this).tree("isLeaf",node.target)){
    			    				// 填写到cid中
    			    				//获取隐藏域cid 并且赋值给他为叶子节点的id 
    			    				_ele.parent().find("[name=cid]").val(node.id);
    			    				// 将文本值显示，并设置标签(上边追加的<span)的cid属性为节点的id
    			    				_ele.next().text(node.text).attr("cid",node.id);
    			    				//关闭窗口
    			    				$(_win).window('close');
    			    				
    			    				if(data && data.fun){
    			    					data.fun.call(this,node);
    			    				}
    			    			}
    			    		}
    			    	});
    			    },
    			    //关闭时销毁窗口
    			    onClose : function(){
    			    	$(this).window("destroy");
    			    }
    			}).window('open');
    		});
    	});
    },
    
    createEditor : function(select){
    	return KindEditor.create(select, TT.kingEditorParams);
    },
    
    /**
     * 创建一个窗口，关闭窗口后销毁该窗口对象。<br/>
     * 
     * 默认：<br/>
     * width : 80% <br/>
     * height : 80% <br/>
     * title : (空字符串) <br/>
     * 
     * 参数：<br/>
     * width : <br/>
     * height : <br/>
     * title : <br/>
     * url : 必填参数 <br/>
     * onLoad : function 加载完窗口内容后执行<br/>
     * 
     * 
     */
    createWindow : function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : params.width?params.width:"80%",
    		height : params.height?params.height:"80%",
    		modal:true,
    		title : params.title?params.title:" ",
    		href : params.url,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onLoad : function(){
		    	if(params.onLoad){
		    		params.onLoad.call(this);
		    	}
		    }
    	}).window("open");
    },
    
    closeCurrentWindow : function(){
    	$(".panel-tool-close").click();
    },
    
    changeItemParam : function(node,formId){
    	$.getJSON("/item/param/query/itemcatid/" + node.id,function(data){
			  if(data.status == 200 && data.data){
				 $("#"+formId+" .params").show();
				 var paramData = JSON.parse(data.data.paramData);
				 var html = "<ul>";
				 for(var i in paramData){
					 var pd = paramData[i];
					 html+="<li><table>";
					 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
					 
					 for(var j in pd.params){
						 var ps = pd.params[j];
						 html+="<tr><td class=\"param\"><span>"+ps+"</span>: </td><td><input autocomplete=\"off\" type=\"text\"/></td></tr>";
					 }
					 
					 html+="</li></table>";
				 }
				 html+= "</ul>";
				 $("#"+formId+" .params td").eq(1).html(html);
			  }else{
				 $("#"+formId+" .params").hide();
				 $("#"+formId+" .params td").eq(1).empty();
			  }
		  });
    },
    getSelectionsIds : function (select){
    	var list = $(select);
    	var sels = list.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    },
    
    /**
     * 初始化单图片上传组件 <br/>
     * 选择器为：.onePicUpload <br/>
     * 上传完成后会设置input内容以及在input后面追加<img> 
     */
    initOnePicUpload : function(){
    	$(".onePicUpload").click(function(){
			var _self = $(this);
			KindEditor.editor(TT.kingEditorParams).loadPlugin('image', function() {
				this.plugin.imageDialog({
					showRemote : false,
					clickFn : function(url, title, width, height, border, align) {
						var input = _self.siblings("input");
						input.parent().find("img").remove();
						input.val(url);
						input.after("<a href='"+url+"' target='_blank'><img src='"+url+"' width='80' height='50'/></a>");
						this.hideDialog();
					}
				});
			});
		});
    }
};
