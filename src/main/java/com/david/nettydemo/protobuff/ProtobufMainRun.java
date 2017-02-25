package com.david.nettydemo.protobuff;

import com.david.nettydemo.protobuff.SubscribeReqProto.SubscribeReq;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufMainRun
{
	public static void main(String[] args) throws InvalidProtocolBufferException
	{
		SubscribeReqProto.SubscribeReq req = getSubscribeReq();
		System.out.println("original obj: " + req);
		SubscribeReqProto.SubscribeReq req2 = decodeObj(encodeObj(req));
		System.out.println("current obj: " + req2);
		System.out.println("req equals req2: " + req.equals(req2));
	}

	private static byte[] encodeObj(SubscribeReqProto.SubscribeReq req)
	{
		return req.toByteArray();
	}

	private static SubscribeReqProto.SubscribeReq decodeObj(byte[] body) throws InvalidProtocolBufferException
	{
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}

	private static SubscribeReq getSubscribeReq()
	{
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(1);
		builder.setUserName("daviddai");
		builder.setProjectName("上海恒大互联网");
		builder.setPhoneNumber("12345678");
		builder.setAddress("上海黄浦区淮海中路");
		return builder.build();
	}
}
