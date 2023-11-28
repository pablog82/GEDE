package com.babelgroup.gede.util;

import java.util.Base64;

import org.apache.xml.security.exceptions.Base64DecodingException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class EncodeDecode.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public class EncodeDecode {

	/**
	 * Encode.
	 *
	 * @param array the array
	 * @return the string
	 */
	public static String encode(byte[] array) {

		return Base64.getEncoder().encodeToString(array);
	}

	/**
	 * Decode.
	 *
	 * @param cadena the cadena
	 * @return the byte[]
	 * @throws Base64DecodingException the base 64 decoding exception
	 */
	public static byte[] decode(String cadena) {

		return Base64.getDecoder().decode(cadena);
	}

}