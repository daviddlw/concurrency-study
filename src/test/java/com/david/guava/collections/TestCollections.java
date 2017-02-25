package com.david.guava.collections;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class TestCollections {


	@Test
	public void testMutilSet() {
		List<String> list = Arrays.asList(new String[] { "a", "b", "a", "c", "d", "d", "a" });
		Multiset<String> multiset = HashMultiset.create();
		multiset.addAll(list);

		for (String key : multiset.elementSet()) {
			System.out.println(String.format("key: %s, count: %d", key, multiset.count(key)));
		}
		
	}
}
