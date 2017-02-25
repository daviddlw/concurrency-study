package com.david.nettydemo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class MainRun
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
//		serializeObjectByJDk();
	}
	
	private static User getUser(){
		User user = new User("戴维", 1);
		return user;
	}
	
	private static void serializeObjectByJDk() throws IOException, ClassNotFoundException{

		ByteArrayOutputStream baos=  new ByteArrayOutputStream();	
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(getUser());
		oos.flush();
		oos.close();
		byte[] byteData = baos.toByteArray();
		System.out.println(byteData.length);
		System.out.println(Arrays.toString(byteData));
		System.out.println("==========================");
		
		
		ByteArrayInputStream bais =new ByteArrayInputStream(byteData);
		ObjectInputStream ois = new ObjectInputStream(bais);
		User newUser = (User)ois.readObject();
		System.out.println(newUser);
	}
	
	private static void serializeObjectByProtostuff(){
		
	}
}
