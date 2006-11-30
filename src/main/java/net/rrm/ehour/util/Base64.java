/**
 * Created on Nov 30, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.util;

import java.io.*;

/**
 * This class contains two static methods for Base64 encoding and decoding.
 *
 * @author <a href="http://izhuk.com">Igor Zhukovsky</a>
 */
public class Base64
{

	/**
	 *  Decodes BASE64 encoded string.
	 *  @param encoded string
	 *  @return decoded data as byte array
	 */
	public static byte[] decode(String encoded)
	{
		int i;
		byte output[] = new byte[3];
		int state;

		ByteArrayOutputStream data = new ByteArrayOutputStream(encoded.length());

		state = 1;
		for (i = 0; i < encoded.length(); i++)
		{
			byte c;
			{
				char alpha = encoded.charAt(i);
				if (Character.isWhitespace(alpha))
					continue;

				if ((alpha >= 'A') && (alpha <= 'Z'))
					c = (byte) (alpha - 'A');
				else if ((alpha >= 'a') && (alpha <= 'z'))
					c = (byte) (26 + (alpha - 'a'));
				else if ((alpha >= '0') && (alpha <= '9'))
					c = (byte) (52 + (alpha - '0'));
				else if (alpha == '+')
					c = 62;
				else if (alpha == '/')
					c = 63;
				else if (alpha == '=')
					break; // end
				else
					return null; // error
			}

			switch (state)
			{
			case 1:
				output[0] = (byte) (c << 2);
				break;
			case 2:
				output[0] |= (byte) (c >>> 4);
				output[1] = (byte) ((c & 0x0F) << 4);
				break;
			case 3:
				output[1] |= (byte) (c >>> 2);
				output[2] = (byte) ((c & 0x03) << 6);
				break;
			case 4:
				output[2] |= c;
				data.write(output, 0, output.length);
				break;
			}
			state = (state < 4 ? state + 1 : 1);
		} // for

		if (i < encoded.length()) /* then '=' found, but the end of string */
			switch (state)
			{
			case 3:
				data.write(output, 0, 1);
				return (encoded.charAt(i) == '=') && (encoded.charAt(i + 1) == '=') ? data.toByteArray() : null;
			case 4:
				data.write(output, 0, 2);
				return (encoded.charAt(i) == '=') ? data.toByteArray() : null;
			default:
				return null;
			}
		else
			return (state == 1 ? data.toByteArray() : null); /* end of string */

	} // decode

	private final static String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	/**
	 *  Encodes binary data by BASE64 method.
	 *  @param data binary data as byte array
	 *  @return encoded data as String
	 */
	public static String encode(byte[] data)
	{

		char output[] = new char[4];
		int state = 1;
		int restbits = 0;
		int chunks = 0;

		StringBuffer encoded = new StringBuffer();

		for (int i = 0; i < data.length; i++)
		{
			int ic = (data[i] >= 0 ? data[i] : (data[i] & 0x7F) + 128);
			switch (state)
			{
			case 1:
				output[0] = base64.charAt(ic >>> 2);
				restbits = ic & 0x03;
				break;
			case 2:
				output[1] = base64.charAt((restbits << 4) | (ic >>> 4));
				restbits = ic & 0x0F;
				break;
			case 3:
				output[2] = base64.charAt((restbits << 2) | (ic >>> 6));
				output[3] = base64.charAt(ic & 0x3F);
				encoded.append(output);

				// keep no more the 76 character per line
				chunks++;
				if ((chunks % 19) == 0)
					encoded.append("\r\n");
				break;
			}
			state = (state < 3 ? state + 1 : 1);
		} // for

		/* final */
		switch (state)
		{
		case 2:
			output[1] = base64.charAt((restbits << 4));
			output[2] = output[3] = '=';
			encoded.append(output);
			break;
		case 3:
			output[2] = base64.charAt((restbits << 2));
			output[3] = '=';
			encoded.append(output);
			break;
		}

		return encoded.toString();
	} // encode()

} // Base64
