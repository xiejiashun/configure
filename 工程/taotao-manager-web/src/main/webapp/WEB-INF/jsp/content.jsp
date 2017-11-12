<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="contentCategoryTree" class="easyui-tree" data-options="url:'/content/category/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <div data-options="region:'center'" style="padding:5px">
            <table class="easyui-datagrid" id="contentList" data-options="toolbar:contentListToolbar,singleSelect:false,collapsible:true,pagination:true,method:'get',pageSize:20,url:'/content/query/list',queryParams:{categoryId:0}">
			    <thead>
			        <tr>
			            <th data-options="field:'id',width:30">ID</th>
			            <th data-options="field:'title',width:120">内容标题</th>
			            <th data-options="field:'subTitle',width:100">内容子标题</th>
			            <th data-options="field:'titleDesc',width:120">内容描述</th>
			            <th data-options="field:'url',width:60,align:'center',formatter:TAOTAO.formatUrl">内容连接</th>
			            <th data-options="field:'pic',width:50,align:'center',formatter:TAOTAO.formatUrl">图片</th>
			            <th data-options="field:'pic2',width:50,align:'center',formatter:TAOTAO.formatUrl">图片2</th>
			            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
			            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
			        </tr>
			    </thead>
			</table>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
	//获取标签对象
	var tree = $("#contentCategoryTree");
	//选中表格对象
	var datagrid = $("#contentList");
	//创建一棵树
	tree.tree({
		//url:
			//点击节点的时候触发 
		onClick : function(node){
			//判断被点击的节点是否为叶子节点  这里就是表示 是
			if(tree.tree("isLeaf",node.target)){
				//重新发送URL 从数据库中查询数据加载数据到表格中
				datagrid.datagrid('reload', {
					categoryId :node.id//将最新的分类的id的值传递到后台 不用原来的默认的categoryid
		        });
			}
		}
	});
});
var contentListToolbar = [{
    text:'新增',
    iconCls:'icon-add',
    //当点击新增按钮时触发
    handler:function(){
    	//获取选中的节点的对象
    	var node = $("#contentCategoryTree").tree("getSelected");
    	//node 表示存在
    	//$("#contentCategoryTree").tree("isLeaf",node.target) :判断被选中的节点是否为叶子节点
    	//如果节点不存在，或者节点不是叶子节点
    	if(!node || !$("#contentCategoryTree").tree("isLeaf",node.target)){
    		$.messager.alert('提示','新增内容必须选择一个内容分类!');
    		return ;
    	}
    	//
    		//创建一个窗口
    	TT.createWindow({
    		//在窗口中 发送URL 请求页面得到数据 展示在窗口里面中
			url : "/content-add"
		}); 
    }
},{
    text:'编辑',
    iconCls:'icon-edit',
    handler:function(){
    	var ids = TT.getSelectionsIds("#contentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一个内容才能编辑!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一个内容!');
    		return ;
    	}
		TT.createWindow({
			url : "/content-edit",
			onLoad : function(){
				var data = $("#contentList").datagrid("getSelections")[0];
				$("#contentEditForm").form("load",data);
				
				// 实现图片
				if(data.pic){
					$("#contentEditForm [name=pic]").after("<a href='"+data.pic+"' target='_blank'><img src='"+data.pic+"' width='80' height='50'/></a>");	
				}
				if(data.pic2){
					$("#contentEditForm [name=pic2]").after("<a href='"+data.pic2+"' target='_blank'><img src='"+data.pic2+"' width='80' height='50'/></a>");					
				}
				
				contentEditEditor.html(data.content);
			}
		});    	
    }
},{
    text:'删除',
    iconCls:'icon-cancel',
    handler:function(){
    	var ids = TT.getSelectionsIds("#contentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','未选中商品!');
    		return ;
    	}
    	$.messager.confirm('确认','确定删除ID为 '+ids+' 的内容吗？',function(r){
    	    if (r){
    	    	var params = {"ids":ids};
            	$.post("/content/delete",params, function(data){
        			if(data.status == 200){
        				$.messager.alert('提示','删除内容成功!',undefined,function(){
        					$("#contentList").datagrid("reload");
        				});
        			}
        		});
    	    }
    	});
    }
}];
</script>