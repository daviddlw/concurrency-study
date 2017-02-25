package com.david.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 程序员面试经典题库01-05
 * 
 * @author David.dai
 * 
 */
public class Interview01To05Test {

	/**
	 * 确定字符串互异
	 */
	@Test
	public void testCheckDifferent01() {
		String s1 = "aeiou";
		String s2 = "BarackObama";
		boolean b1 = checkDifferent(s1);
		System.out.println("b1: " + b1);
		boolean b2 = checkDifferent(s2);
		System.out.println("b2: " + b2);

		Assert.assertEquals(b1, true);
		Assert.assertEquals(b2, false);
	}

	public boolean checkDifferent(String iniString) {
		if (iniString.length() > 3000) {
			throw new IllegalArgumentException("iniString length must be less than 3000");
		}

		List<Character> list = new ArrayList<Character>(iniString.length());
		char[] arr = iniString.toCharArray();
		for (char c : arr) {
			if (list.contains(c)) {
				return false;
			}

			list.add(c);
		}

		return true;
	}

	/**
	 * 原串翻转
	 */
	@Test
	public void testReverseString02() {
		String s = "This is nowcoder";
		String result = reverseString(s);
		System.out.println("reverse str: " + result);
		Assert.assertEquals("redocwon si sihT", result);
	}

	public String reverseString(String iniString) {
		if (iniString.length() > 5000) {
			throw new IllegalArgumentException("iniString length must be less than 5000");
		}

		StringBuilder sb = new StringBuilder();
		char[] arr = iniString.toCharArray();
		for (int i = arr.length - 1; i >= 0; i--) {
			sb.append(arr[i]);
		}
		String str = sb.toString();
		return str;
	}

	/**
	 * 确定两串乱序同构
	 */
	@Test
	public void testCheckSame03() {
		String s1 = "This is nowcoder";
		String s2 = "is This nowcoder";
		boolean same1 = checkSame(s1, s2);
		System.out.println(same1);
		Assert.assertEquals(same1, true);

		String s3 = "Here you are";
		String s4 = "Are you here";
		boolean same2 = checkSame(s3, s4);
		System.out.println(same2);
		Assert.assertEquals(same2, false);
	}

	public boolean checkSame(String stringA, String stringB) {
		if (stringA.length() > 5000) {
			throw new IllegalArgumentException("stringA length must be less than 5000");
		}

		if (stringB.length() > 5000) {
			throw new IllegalArgumentException("stringB length must be less than 5000");
		}
		char[] cA = stringA.toCharArray();
		char[] cB = stringB.toCharArray();
		Arrays.sort(cA);
		Arrays.sort(cB);
		return Arrays.equals(cA, cB);
	}

	/**
	 * 空格替换
	 */
	@Test
	public void testReplaceSpace04() {
		String s1 = "Mr John Smith";
		String nS1 = replaceSpace(s1, s1.length());
		System.out.println(nS1);
		Assert.assertEquals(nS1, "Mr%20John%20Smith");

		String s2 = "Hello  World";
		String nS2 = replaceSpace(s2, s2.length());
		System.out.println(nS2);
		Assert.assertEquals(nS2, "Hello%20%20World");
	}

	public String replaceSpace(String iniString, int length) {
		if (iniString.length() > 1000) {
			throw new IllegalArgumentException("iniString length must be less than 1000");
		}

		String s = iniString.replaceAll("\\s{1}", "%20");
		return s;
	}

	/**
	 * 基本压缩字符串
	 */
	@Test
	public void testZipString05() {
		String str1 = "aabcccccaaa";
		String str2 = "qwertyuioplkjhgfdsAzxcvbNM";

		String result1 = zipString(str1);
		System.out.println("zip str1: " + result1);
		Assert.assertEquals("a2b1c5a3", result1);

		String result2 = zipString(str2);
		System.out.println("zip str2: " + result2);
		Assert.assertEquals("qwertyuioplkjhgfdsAzxcvbNM", result2);
	}

	public String zipString(String iniString) {
		if (iniString.length() > 10000) {
			throw new IllegalArgumentException("iniString length must be less than 10000");
		}

		StringBuilder sb = new StringBuilder();
		char[] arr = iniString.toCharArray();
		int count = 0; // 记录字符个数
		char c = ' '; // 记录初始字符
		int searchIndex = 0; // 搜寻索引值
		int i;
		while (searchIndex < arr.length) {
			c = arr[searchIndex];
			count++;

			do {
				i = searchIndex + 1;
				searchIndex = i;
				if (i < arr.length && arr[i] == c) {
					count++;
				} else if (i < arr.length) {
					sb.append(c);
					sb.append(count);
					// System.out.println("current str: " + sb.toString());
					break;
				}
				i++;
			} while (i < arr.length);

			if (i >= arr.length) {
				sb.append(c);
				sb.append(count);
				searchIndex = arr.length;
				// System.out.println("current str: " + sb.toString());
			}

			count = 0; // 字符个数统计清零
		}

		String result = sb.toString();
		return result.length() >= iniString.length() ? iniString : result;
	}
}
