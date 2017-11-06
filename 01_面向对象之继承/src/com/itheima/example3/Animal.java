package com.itheima.example3;
//被私有化的成员只允许在本类中访问，不允许在其他类中进行访问
public class Animal{
	private String color;
	private int leg;
	public Animal(){}
	public Animal(String color,int leg){
		this.color=color;
		this.leg=leg;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getLeg() {
		return leg;
	}
	public void setLeg(int leg) {
		this.leg = leg;
	}
	public void sleep (){
		System.out.println("睡觉");
	}
	public void eat(){
		System.out.println("吃饭");
	}
}
