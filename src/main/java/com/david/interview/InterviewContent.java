package com.david.interview;

public class InterviewContent {
	public static void main(String[] args) {
		NullTest obj = null;
		System.out.println(obj.getInt());
	}
}

class NullTest
{
	public static int getInt() {
		return 1;
	}
}
