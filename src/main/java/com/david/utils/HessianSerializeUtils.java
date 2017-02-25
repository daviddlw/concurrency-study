package com.david.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class HessianSerializeUtils {

	/**
	 * Hessian2序列化对象
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException {
		if (obj == null)
			throw new NullPointerException();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Hessian2Output hessian2Output = new Hessian2Output(out);
		hessian2Output.writeObject(obj);
		hessian2Output.flush();
		return out.toByteArray();
	}

	/**
	 * Hessian2反序列化
	 * 
	 * @throws IOException
	 */
	public static Object deserialize(byte[] arr) throws IOException {
		if (arr == null)
			throw new NullPointerException();

		ByteArrayInputStream in = new ByteArrayInputStream(arr);
		Hessian2Input hessian2Input = new Hessian2Input(in);
		return hessian2Input.readObject();
	}
}
