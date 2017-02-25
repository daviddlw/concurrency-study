package com.david.aop;

import java.lang.reflect.Proxy;

public class MainRun {
	public static void main(String[] args) {
		CalculateService service = new CalculateServiceImpl();
		CalculateServiceProxy proxy = new CalculateServiceProxy(service);
		CalculateService proxyService = (CalculateService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
				service.getClass().getInterfaces(), proxy);
		int result = proxyService.minus(1, 2);
	}
}
