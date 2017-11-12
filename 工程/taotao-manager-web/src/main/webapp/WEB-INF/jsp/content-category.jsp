<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
//当文档加载完成之后执行以下的逻辑
$(function(){
	//选中ul标签 在这里创建一棵树
	$("#contentCategory").tree({
		url : '/content/category/list',//异步发送请求的URL  参数:id 
		animate: true,
		method : "GET",//get请qiu
		//当右击鼠标时触发   传递被点击的节点的对象
		onContextMenu: function(e,node){
			//禁用原来的鼠标的默认事件行为
            e.preventDefault();
			//选中被点击的节点
            $(this).tree('select',node.target);
			//显示菜单栏
            $('#contentCategoryMenu').menu('show',{
            	//设置鼠标的坐标
                left: e.pageX,
                top: e.pageY
            });
        },
        //在编辑之后 触发
        onAfterEdit : function(node){
        	//获取树本身
        	//node就是被编辑的这个节点
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点 表示要新增一个节点
        		//parentId：就是新增节点的父节点的id
        		//name:就是该新增的名称
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        				//更新新增的节点 将节点的id值设置为数据库中的id值
        				_tree.tree("update",{
            				target : node.target,
            				id : data.data.id//从数据库中查询到新增的节点的id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		//重命名
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
//点击菜单栏的菜单时  触发以下的逻辑
function menuHandler(item){
	//获取树对象
	var tree = $("#contentCategory");
	//获取被选中的节点
	var node = tree.tree("getSelected");
	//判断点击的是否为add rename delete
	//被点击的菜单的名字如果=add :说明点击的是添加的菜单栏。
	// 1=="1"   true    1==="1" false
	
	if(item.name === "add"){
		//追加节点
		//{} 里面就是新增的节点的配置项。
		tree.tree('append', {
            parent: (node?node.target:null),//新增节点的父 
            //新增节点的数据
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id//它的父节点的id值
            }]
        }); 
		//查询id值为0的节点对象，其实就是获取新增的这个节点对象
		var _node = tree.tree('find',0);//根节点
		//获取新增的节点对象      进入编辑状态
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
		
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
		
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>