package com.itheima.example;

/**
 * 1.私有化成员变量
 * 2.空参和有参构造
 * 3.set/get
 * 4.成员方法
 * 学生类，属性有姓名和年龄，行为有吃饭，睡觉和学习
 */
public class Student {
	private String name;
	private int age;
	
	/**
	 * 构造方法：就是用来创建对象的
	 * public 类名（xxx）{}
	 */
	public Student(){ }
	//变量的访问原则：就近原则
	public Student (String name,int age){
		this.name=name;
		this.age=age;
	}
	/**
	 * 1.方法的权限修饰符
	 * 2.方法的返回值类型（输出）
	 * 3.方法的方法名
	 * 4.方法的参数列表（输入）
	 */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
