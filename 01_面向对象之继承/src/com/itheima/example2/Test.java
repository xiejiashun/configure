package com.itheima.example2;

public class Test {
	public static void main(String[] args) {
		Cat c =new Cat("Blue",4);
		String color=c.getColor();
		int leg =c.getLeg();
		System.out.println(color+"..."+leg);
		System.out.println("--------------------");
		Dog d=new Dog("yellow",4);
		String color1=d.getColor();
		int leg1=d.getLeg();
		System.out.println(color1+"..."+leg1);
		
	}
}
