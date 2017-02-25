package com.david.test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public final class RsaCodingUtil {
	private static final Logger log = Logger.getLogger(RsaCodingUtil.class);
	
	public static String decryptByPubCerFile(String src, String pubCerPath) {
		PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);
		if (publicKey == null) {
			return null;
		}
		return decryptByPublicKey(src, publicKey);
	}

	public static String decryptByPubCerText(String src, String pubKeyText) {
		PublicKey publicKey = RsaReadUtil.getPublicKeyByText(pubKeyText);
		if (publicKey == null) {
			return null;
		}
		return decryptByPublicKey(src, publicKey);
	}

	public static String decryptByPublicKey(String src, PublicKey publicKey) {
		try {
			byte[] destBytes = rsaByPublicKey(StringUtil.hex2Bytes(src), publicKey, 2);
			if (destBytes == null) {
				return null;
			}
			return new String(destBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("decrypt content is not formated as utf-8:", e);
		}
		return null;
	}

	public static String decryptByPublicKey(String src, String publicKey) {
		try {
			PublicKey pubKey = RsaReadUtil.getPublicKey(publicKey);
			return decryptByPublicKey(src, pubKey);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}
	
	public static byte[] rsaByPublicKey(byte[] srcData, PublicKey publicKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(mode, publicKey);

			int blockSize = mode == 1 ? 117 : 128;
			byte[] encryptedData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(srcData, i, i + blockSize));
				encryptedData = ArrayUtils.addAll(encryptedData, doFinal);
			}
			return encryptedData;
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:", e);
		} catch (NoSuchPaddingException e) {
			log.error("NoSuchPaddingException:", e);
		} catch (IllegalBlockSizeException e) {
			log.error("IllegalBlockSizeException:", e);
		} catch (BadPaddingException e) {
			log.error("BadPaddingException:", e);
		} catch (InvalidKeyException e) {
			log.error("InvalidKeyException:", e);
		}
		return null;
	}

	public static byte[] rsaByPrivateKey(byte[] srcData, PrivateKey privateKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(mode, privateKey);

			int blockSize = mode == 1 ? 117 : 128;
			byte[] decryptData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(srcData, i, i + blockSize));
				decryptData = ArrayUtils.addAll(decryptData, doFinal);
			}
			return decryptData;
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:", e);
		} catch (NoSuchPaddingException e) {
			log.error("NoSuchPaddingException:", e);
		} catch (IllegalBlockSizeException e) {
			log.error("IllegalBlockSizeException:", e);
		} catch (BadPaddingException e) {
			log.error("BadPaddingException:", e);
		} catch (InvalidKeyException e) {
			log.error("InvalidKeyException:", e);
		}
		return null;
	}
}
