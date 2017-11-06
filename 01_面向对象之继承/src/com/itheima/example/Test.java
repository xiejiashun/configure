package com.itheima.example;
//学生类，属性有姓名和年龄，行为有吃饭，睡觉和学习

/**
 * 构造方法主要用于：定义一种规则，初始化值
 * setXXX()与getXXX()主要用于：修改值，获取值。
 */
public class Test {
	public static void main(String[] args){
		
		//创建的对象的格式：类名对象名=构造方法
		Student student = new Student();
		student.setName("张三");
		student.setAge(35);
		String name = student.getName();
		int age = student.getAge();
		System.out.println(name+"..."+age);
		System.out.println("================");
		Student stu =new Student("张三",25);
		String name2 = stu.getName();
		int age2 = stu.getAge();
		System.out.println(name2+"..."+age2);
		
		
	}
}
