package com.david.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * 程序员面试经典题库06-10
 * 
 * @author David.dai
 * 
 */
public class Interview06To10 {

	/**
	 * 像素翻转
	 */
	@Test
	public void testTransformImage06() {
		int[][] testArr = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		int[][] resultArr = new int[][] { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 } };
		int[][] newArr = transformImage(testArr, 3);
		Assert.assertArrayEquals(resultArr, newArr);
	}

	public int[][] transformImage(int[][] mat, int n) {
		// n行n列移动n-1步
		List<List<Integer>> list = new ArrayList<List<Integer>>(n);

		for (int i = 0; i < mat.length; i++) {
			List<Integer> innerList = new ArrayList<Integer>();
			for (int j = mat[i].length - 1; j >= 0; j--) {
				innerList.add(mat[j][i]);
			}
			list.add(innerList);
		}

		System.out.println(list);
		int[][] newArr = new int[n][n];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				newArr[i][j] = list.get(i).get(j);
			}
		}

		return newArr;
	}

	/**
	 * 清除行列
	 */
	@Test
	public void testClearZero07() {
		int[][] testMat = new int[][] { { 1, 2, 3 }, { 0, 1, 2 }, { 0, 0, 1 } };
		int[][] resultMat = clearZero(testMat, 3);
		int[][] checkMat = new int[][] { { 0, 0, 3 }, { 0, 0, 0 }, { 0, 0, 0 } };
		Assert.assertArrayEquals(checkMat, resultMat);
	}

	// Some bugs has already exist, cannot use the map to record the position
	public int[][] clearZero(int[][] mat, int n) {
		if (n > 300) {
			throw new IllegalArgumentException("Illegal argument for number " + n);
		}

		int[][] resultMat = new int[n][n];
		Map<Integer, Integer> targetMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				if (mat[i][j] == 0) {
					if (!targetMap.containsKey(mat[i][j])) {
						targetMap.put(i, j);
					}
				}

				resultMat[i][j] = mat[i][j];
			}
		}

		// 执行清理操作
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				// 当前行中包含0，执行清零操作
				if (targetMap.keySet().contains(i)) {
					resultMat[i][j] = 0;
				}

				// 当前列中包含0，执行清零操作
				if (targetMap.values().contains(j)) {
					resultMat[i][j] = 0;
				}
			}
		}
		
		showMat(resultMat);
		return resultMat;
	}
	
	private void showMat(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			System.out.println(Arrays.toString(mat[i]));
		}
	}
}
