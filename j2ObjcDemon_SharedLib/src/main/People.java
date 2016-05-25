package main;

public class People {
	private String name = "";
	public People(String name){
		this.name = name;
	}
	
	public String say(String text){
		return this.name + " say:" + text;
	}
}
