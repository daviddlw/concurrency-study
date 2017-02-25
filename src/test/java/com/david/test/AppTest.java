package com.david.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import org.junit.Before;
import org.junit.Test;

import com.david.customizingconcurrency.GeneratorFactory;
import com.david.customizingconcurrency.SearchNumberTask;
import com.david.dto.User;
import com.david.forkjoin.Document;
import com.david.utils.HessianSerializeUtils;

public class AppTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		// fail("Not yet implemented");
		// System.out.println("hello world");
		Document doc = new Document();
		String[][] result = doc.generateDocument(5, 10, "the");
		for (String[] item : result) {
			System.err.println(Arrays.toString(item));
		}
		ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();
		HashMap<String, String> hashMap = new HashMap<>();
		System.out.println(concurrentHashMap.size());
	}

	@Test
	public void testApp() {
		String[] arr = new String[] { "d", "a", "b", "c" };
		List<String> ls = Arrays.asList(new String[] { "d", "a", "b", "c" });
		Arrays.sort(arr);
		Collections.sort(ls);

		System.out.println(Arrays.toString(arr));
		System.out.println(ls);
	}

	@Test
	public void testGeneratorFactory() {
		System.out.println(GeneratorFactory.generateIntList(1000));

	}

	@Test
	public void testSearchNum() {
		int searchNum = 89;
		List<Integer> ls = GeneratorFactory.generateIntList(100000);
		loopSearch(ls, searchNum);
		forkTaskSearch(ls, searchNum);
	}

	@Test
	public void testLoopSearch() {
		int searchNum = 89;
		loopSearch(GeneratorFactory.generateIntList(100000), searchNum);
	}

	private void loopSearch(List<Integer> ls, int searchNum) {
		int index = 0;
		long start = System.currentTimeMillis();
		System.out.println("start: " + start);
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) == searchNum) {
				index = i;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("end: " + end);
		System.out.println("所查找的数据在索引： " + index + ", 耗费时间： " + (end - start) + "毫秒.");
	}

	public void forkTaskSearch(List<Integer> ls, int searchNum) {
		SearchNumberTask task = new SearchNumberTask(0, ls.size(), ls, searchNum);
		ForkJoinPool pool = new ForkJoinPool();
		long start = System.currentTimeMillis();
		System.out.println("start: " + start);
		pool.execute(task);
		long end = System.currentTimeMillis();
		System.out.println("end: " + end);
		System.out.println("耗费时间： " + (end - start) + "毫秒.");
	}

	@Test
	public void testForkTaskSearch() {
		int searchNum = 89;
		forkTaskSearch(GeneratorFactory.generateIntList(100000), searchNum);
	}

	@Test
	public void testHessian() {
		long timestamp = System.currentTimeMillis();
		User user = new User(timestamp, "测试" + timestamp);
		System.out.println(user);

		try {
			byte[] resultArr = HessianSerializeUtils.serialize(user);
			System.err.println(resultArr.length);

			User newUser = (User) HessianSerializeUtils.deserialize(resultArr);
			System.err.println(newUser);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
