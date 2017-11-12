${hello}


//pojo<br>

${person1.id?c}
${person1.name}
<br>
${person2.id?c}
${person2.name}
<br>
${person3.id?c}
${person3.name}

取list集合中的数据<br>

<#list list as person>
	下标：${person_index}
	下标运算：${person_index+1}
	<#if  person_index%2==0>
		
		<h1 style="color:red">${person.id?c}</h1>
	<#else>
		<h1 style="color:yellow">${person.name}</h1>
	</#if>
	
	
	
	<br>

</#list>

取map<br>

<#list map?keys as key>
	${map[key].id?c}
	${map[key].name}
</#list>

<br>另一种取map<br>

	${map.m1.id}
	${map.m1.name}
	${map.m2.id}
	${map.m2.name}
	${map.m3.id}
	${map.m3.name}
	
	<br>获取日期
	
	${date?date}
	<br>获取时间和日期
	${date?datetime}
	<br>获取时间
	${date?time}
	<br>自定义时间格式
	${date?string("yyyy/MM/dd HH:mm:ss")}
	<br>null值处理的取值
	<br>
	
	<#if keynull??>
		如果在值 我应该出现在这里
	<#else>
		如果不存在值 我就应该出现在这里
	</#if>
	
	<br> include 标签：引入其他的模板 
	
	<br>
	<#include "template.htm" />

