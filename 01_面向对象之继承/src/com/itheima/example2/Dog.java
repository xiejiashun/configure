package com.itheima.example2;

public class Dog {
	private String color;
	private int leg;
	public Dog(){}
	public Dog(String color,int leg){
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
		System.out.println("˯��");
	}
	public void eat(){
		System.out.println("�Է�");
	}
}
