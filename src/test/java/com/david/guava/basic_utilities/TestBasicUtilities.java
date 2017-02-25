package com.david.guava.basic_utilities;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.util.LangUtil.ProcessController.Thrown;
import org.junit.Test;

import com.david.nettydemo.httpxml.Order;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.mysql.jdbc.log.Log;

public class TestBasicUtilities {

	private static final Logger LOGGER = Logger.getLogger(TestBasicUtilities.class);

	@Test
	public void testOptional() {
		User user = new User(1, "daviddai");
		// user = null;
		Optional<User> opt = Optional.absent();

		// opt = Optional.of(user);
		System.out.println(opt.isPresent());

		opt = Optional.absent();
		opt = Optional.fromNullable(user);
		System.out.println(opt);
	}

	@Test
	public void testPreconditions() {
		boolean flag = true;
		User user = new User();
		user = null;
		try {
			// Preconditions.checkArgument(flag == false, "flag is : %s, not
			// correct", flag);
			Preconditions.checkNotNull(user, "current obj is null? %s ", "daviddai");
		} catch (IllegalArgumentException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	@Test
	public void calculateObjectHash() {
		User user = new User(1, "daviddai");
		int result = Objects.hashCode(user);
		int result2 = user.hashCode();
		System.out.println(result);
		System.out.println(result2);

		System.out.println("=====================toStringHelper===============");
		String result3 = Objects.toStringHelper(user).add("tid", user.getId()).add("tname", user.getName()).toString();
		System.out.println(result3);

		System.out.println(Throwables.getStackTraceAsString(new NoSuchAlgorithmException()));
	}

	@Test
	public void testOrdering() {
		List<String> list = Arrays.asList(new String[] { "ddddd", "bbb", null, "aaaaa", "cccc" });
		Ordering<String> naturalOrdering = Ordering.natural().nullsFirst();
		Ordering<Object> usingToStringOrdering = Ordering.usingToString().nullsLast();
		Ordering<Object> arbitraryOrdering = Ordering.arbitrary().nullsFirst();
		Ordering<String> stringLengthOrdering = new Ordering<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				return Ints.compare(arg0.length(), arg1.length());
			}
		};

		stringLengthOrdering = stringLengthOrdering.nullsFirst().reverse();

		List<String> naturalList = naturalOrdering.sortedCopy(list);
		List<String> usingToStringList = usingToStringOrdering.sortedCopy(list);
		List<String> arbitraryList = arbitraryOrdering.sortedCopy(list);
		List<String> stringLengthList = stringLengthOrdering.sortedCopy(list);

		System.out.println(naturalList);
		System.out.println(usingToStringList);
		System.out.println(arbitraryList);
		System.out.println(stringLengthList);
	}
	
}

class User {
	private Integer id;
	private String name;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}
