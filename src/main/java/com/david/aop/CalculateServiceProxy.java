package com.david.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class CalculateServiceProxy implements InvocationHandler {

	private Object target;

	public CalculateServiceProxy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CalculateServiceProxy(Object target) {
		super();
		this.target = target;
	}

	private void logBefore() {
		System.out.println("log before...");
	}

	private void logAfter() {
		System.out.println("log after...");
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		logBefore();
		// TimeUnit.MILLISECONDS.sleep(100);
		Object result = method.invoke(target, args);
		System.out.println(String.format("execute method： %s, result: %s", method.getName(), result));
		// TimeUnit.MILLISECONDS.sleep(100);
		logAfter();
		return result;
	}

}
