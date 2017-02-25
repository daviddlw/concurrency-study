package com.david.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLResult;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试dom4j开源类
 * 
 * @author David.dai
 * 
 */
public class TestDom4j {

	private User user;

	@Before
	public void init() throws Exception {
		System.out.println("init dto");
		user = new User();
		user.setId(1);
		user.setName("戴维");
		user.setMobile("1234567890");
		user.setAddresses(Arrays.asList(new String[] { "上海市黄浦区", "上海市徐汇区" }));
	}

	@Test
	public void toJavaDTO() throws FileNotFoundException, DocumentException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
		typeMap.put("id", int.class);
		typeMap.put("name", String.class);
		typeMap.put("mobile", String.class);
		typeMap.put("addresses", List.class);

		Class<User> userClass = User.class;
		String path = "D:" + File.separator + userClass.getSimpleName().toLowerCase() + ".xml";
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new FileReader(new File(path)));
		Element root = doc.getRootElement();

		User user = new User();
		@SuppressWarnings("unused")
		Class<?> uClass = user.getClass();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		Iterator<Element> it = list.iterator();
		Element obj;
		String name = "";
		Method setMethod;
		while (it.hasNext()) {
			obj = it.next();
			name = StringUtils.capitalize(obj.getName());
			Class<?> type = Class.forName(obj.attributeValue("type"));
			setMethod = uClass.getDeclaredMethod(String.format("set%s", name), new Class[] { type });
			setMethod.setAccessible(true);
			if (type.toString().equalsIgnoreCase(List.class.toString())) {
				String value = obj.getText();
				List<String> tempList = new ArrayList<String>();
				String[] arr = StringUtils.split(value, ",");
				for (String val : arr) {
					tempList.add(val);
				}
				setMethod.invoke(user, new Object[] { tempList });

			} else if (type.toString().equalsIgnoreCase(Integer.class.toString())) {
				setMethod.invoke(user, new Object[] { Integer.parseInt(obj.getText()) });
			} else {
				setMethod.invoke(user, new Object[] { obj.getText() });
			}
		}

		System.out.println(user);
	}

	@Test
	public void toXml() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
		Class<?> userClass = user.getClass();
		Document doc = DocumentHelper.createDocument();
		Element userElement = new BaseElement(userClass.getSimpleName().toLowerCase());

		Field[] fields = userClass.getDeclaredFields();
		String name;
		Method getMethod;
		for (Field field : fields) {
			name = StringUtils.capitalize(field.getName());
			getMethod = userClass.getDeclaredMethod(String.format("get%s", name), new Class[] {});
			Element attrElement = new BaseElement(field.getName().toLowerCase());
			Object obj = getMethod.invoke(user, new Object[] {});
			String text = "";
			if (field.getType().toString().equalsIgnoreCase("interface java.util.List")) {
				List<String> list = (List<String>) obj;
				text = StringUtils.join(list, ",");
			} else {
				text = obj != null ? obj.toString() : "";
			}
			attrElement.setText(text);
			attrElement.addAttribute("type", field.getType().getName());
			userElement.add(attrElement);
			System.err.println(field.getType());
		}

		doc.add(userElement);
		System.out.println(doc.asXML());
		String path = "D:" + File.separator + userClass.getSimpleName().toLowerCase() + ".xml";
		XMLWriter xmlWriter = new XMLWriter(new FileWriter(new File(path)));
		xmlWriter.write(doc);
		xmlWriter.close();

		File file = new File(path);
		if (file.exists()) {
			System.out.println("file generated successfully");
		} else {
			System.out.println("file generated failed");
		}
	}

	@Test
	public void testGetProperties() {
		Class<User> userClass = User.class;
		Field[] fields = userClass.getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		System.out.println(user);
	}
}

/*
 * user dto
 */
class User {
	private Integer id;
	private String name;
	private String mobile;
	private List<String> addresses;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer id, String name, String mobile, List<String> addresses) {
		super();
		this.id = id;
		this.name = name;
		this.mobile = mobile;
		this.addresses = addresses;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", mobile=" + mobile + ", addresses=" + addresses + "]";
	}

}
