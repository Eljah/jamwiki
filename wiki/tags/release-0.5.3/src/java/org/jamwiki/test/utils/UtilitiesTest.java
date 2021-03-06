/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Based on code generated by Agitar build: Agitator Version 1.0.2.000071 (Build date: Jan 12, 2007) [1.0.2.000071]
 */
package org.jamwiki.test.utils;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Vector;
import junit.framework.TestCase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.db.DatabaseUserHandler;
import org.jamwiki.model.Topic;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.SortedProperties;
import org.jamwiki.utils.Utilities;

/**
 *
 */
public class UtilitiesTest extends TestCase {

	/**
	 *
	 */
	public void testAddCookie() throws Throwable {
		// FIXME - implement
	}

	/**
	 *
	 */
	public void testBuildLocale() throws Throwable {
		Locale result = Utilities.buildLocale("");
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testBuildLocale1() throws Throwable {
		Locale result = Utilities.buildLocale("testUtilitiesLocaleString");
		assertEquals("result.getCountry()", "", result.getCountry());
	}

	/**
	 *
	 */
	public void testConvertEncoding() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "", " ");
		assertEquals("result", "testUtilitiesText", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding1() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "UTF-8", "");
		assertEquals("result", "testUtilitiesText", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding2() throws Throwable {
		String result = Utilities.convertEncoding("", "testUtilitiesFromEncoding", "testUtilitiesToEncoding");
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding3() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "testUtilitiesFromEncoding", "testUtilitiesToEncoding");
		assertSame("result", "testUtilitiesText", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding4() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "", "testUtilitiesToEncoding");
		assertSame("result", "testUtilitiesText", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding5() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "UTF-8", "UTF-16LE");
		assertEquals("result", "\u6574\u7473\u7455\u6C69\u7469\u6569\u5473\u7865\uFFFD", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding6() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "", "UTF-16LE");
		assertEquals("result", "\u6574\u7473\u7455\u6C69\u7469\u6569\u5473\u7865\uFFFD", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding7() throws Throwable {
		String result = Utilities.convertEncoding("testUtilitiesText", "testUtilitiesFromEncoding", "");
		assertSame("result", "testUtilitiesText", result);
	}

	/**
	 *
	 */
	public void testConvertEncoding8() throws Throwable {
		String result = Utilities.convertEncoding(null, "testUtilitiesFromEncoding", "testUtilitiesToEncoding");
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testCurrentUser() throws Throwable {
		// FIXME - implement
	}

	/**
	 *
	 */
	public void testDataHandlerInstance() throws Throwable {
		// FIXME - implement
	}

	/**
	 *
	 */
	public void testDecodeFromRequest() throws Throwable {
		String result = Utilities.decodeFromRequest(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testDecodeFromRequest1() throws Throwable {
		String result = Utilities.decodeFromRequest("testUtilitiesUrl");
		assertEquals("result", "testUtilitiesUrl", result);
	}

	/**
	 *
	 */
	public void testDecodeFromURL() throws Throwable {
		String result = Utilities.decodeFromURL("\uA328%\u3B9B\uB68B\uD51B\u76AA\uE89C\u272E\uA2C8\u332C\uA659\u1D83\u501A\u9272\u901E\uB8BD\u4E2D\uA5FC\uDB97\u8999\u6C95\uC2EB\u9E91\u3B04\u6002\u1274\u256A\uD8BC\u4144\u95C1\u9E1F\u8842\uF0A1\u6FD5\u14D2\u9F61\u661C\u3E9F\uD397\uFB24\u13CD\u0F25\u28EA\uEE0E\u3614\u3487\u2A89\u1BC9\u3C80\uC05A\u05CE\uE689\uFFFB\u210E\uDF28\u6674\uC552\u2ABD\uB88A\u2891\uC105\u8467\uA683\u8874C\uB113\u1D98\u7143\uF5ED\u1683\uF1A6\uB703\u06DF\uF368\uDFCA\u43A1\u09CF\uFAAE\u0CB1\uB33B\uE201\uA198\uADB8\u13D7\uEC15\u507C\u4705\uA54F\u86BD\uDAF4\u33E9\u3A37\u612C\u89E5\u0600\u38AC\u942D\uC88F\u226F\uE610\uB3CA\u33E3\u9FFE\u49BC\uB421\u791F\u04A3\u63BB\u86FB\u8BF9\u2181\u055C\uFEFF\u4680\uFA30\u4CAC\uAB50\u5266\uAAC8\u9148\uC945\u3635\u70AD\uA345\u03CB\uC5FB\uE837\u3413\uCF7F\uD783\uD562\u6FB8\uB977\u9BD5\u444A\u7A69\uCBA5\u1B02\uD6FE\u2E54\u36C5\u1268\uD5FB\u06A9\u1D30\u80B8\u3763\u42DF\uA612\uF4A1\uBAE0\u8F25\u535D\uC31A\u5DCB\uAFFB\uF8FE\u7F14\u8058\u0E90\u1765\uD698\uD1D4\u84AF\uD064\u9E6B\u7B98\u515C\u2AAE\uAE73\u885C\uCD82\uF730\uEB9E\u746D\uDA20\uEB34\uB91D\u7321\u59D7\u845D\u1FA0\uD2FA\u8E46\u16A8\u6F54\u1494\u5172\u22CD\u757F\u94D8\uBB8B\u5313\uA2B7\uF155\u694E\u6FCD\u9735\uAA4F\uCE26\u59F2\u7196\u9945\u8227\uDB80\u3EA3\u009E\u7E8B\u8115\uE71E\uAA2C\u9E7A\uF86F\uA96B\u6A72\u552F\uC2BE\u5841\u9E4A\u68C2\uB20D\u5BA6\uCC1E\u21BF\u54CB\u7EEC\u716F\uF641\u73DE\u72B3\u6D1F\u6AA2\u54DC\u2E6D\u275C\u0D9D\uE596\uF695\u1503\uE8C6\u0DCC\u3D17\u068C\uFC16\u8268\u2D9B\uA5C8\uDB15\uE0EF\uE88B\uEAB3\uEF2D\uFC44\uF34D\u3A3B\u5C00\uEBDD\u8B38\u6A7C\uDBF2\u0DD1\u78D6\u6AF2\uEAFF\u19CD\uF2F0\uAC6C\u385A\u1855\uA269\u40F2\u0B0E\u6DC0\u09A8\uAEE3\u2E4A\uACE9\u6A55\u890F\u2E65�\uCAC1\u77A9\u7B49\uEA8C\u4FC7\uAF96\u2703\u1FA8\uB071\u177A\uE2F1\u6D14\uDC74\u07E1\uFACB\u8360\uDE2A\uAE80\uF59E\u6304\uB9A0\u67D3\u4946\u2A4C\u4E11\uA6BB\uEFF3\u42B4\uA241\u4B60\u684E\u3EEE\u30EE\u29AB\uA32B\u8170\uBD34\u48A2\uFC2D\u13A0\u8118\u6ED5\uA95F\uA16B\u8FC8\u7923\u16FA\uEAAB\u824E\uE0F4\uA41D\uE5AE\uC607\uA151\uDA46\u623A\u898C\u3FDB\uE70F\uFD54\u8387\u4B4D\u2D4E\u49B8\uF137\uD572\uBB91\u01D2\uCCB9\uBE90\u5959\u144C\u8F35\u8C42\u5D57\u360F\uDDE6\uBA01\u44F5\u1280\u5BD7\u3681\uEBA7\u0E85\uF26A\u1A8B\u961F\u1B4E\u39A6\u9C98\uCDB1\u2CBB\u13C9\u2453\u3AB7\u5316\uF772\uFA81\u7408\u22CE\u05B9\uF8FD\uB5A5\u4D48\uB731\u6B8B\uF45A\u05D2\u4939\uE1C6\u169C\uDBB0\uDD9D\u6458\uB7D6\u1044\u7E52\u784C\uB16D\u7CC0\u8F9A\uC327\u57E3\uD7E4\u211A\u6549\u813F\u8043\u8BC1\u2EB9\uD01D\uE498\uBC12\u60AA\uA339\uA84A\uDFAF\u7464\u5FB6\uC840\u2EFC\u5AB5\u7B5A\u424D\u91F4\u562D\uCFB0\u3192\u96BF\u3A36\uE598\u72C2\uDAAA\uC1EE\u5691\uD675\uB7FA\u67D6\u4C86\uB3B8\uEE2C\uBAE6\uFD80\u7C62\u8B54\u7327\u9E13\u5523\uAE68\u573B\u3DF3\uD7C2\uAFB3\u79F5\uDE28\u23EC\uD0BF\u134C\u8A5C\u7563\u13E1\u9EF5\u8625\u3B04\u277A\u64D9\u9779\u6362\u1E51\uC8E2\uF79F\u67F7\uC73B\uE858\u8734\uD93A\uF6FE\u178B\u07D1\u7ABF\uB201\uBB0C\u893B\uB0C1\u5CA7\uF888\u1098\u4EE7\u8A91\u94AB\u20C2\u1C5A\u9BD3\u8977\u02FD\u5A30\u9469\uFBA8\u020E\uA891\uD366\u0204\uAE9F\u1342");
		assertEquals("result", "\uA328%\u3B9B\uB68B\uD51B\u76AA\uE89C\u272E\uA2C8\u332C\uA659\u1D83\u501A\u9272\u901E\uB8BD\u4E2D\uA5FC\uDB97\u8999\u6C95\uC2EB\u9E91\u3B04\u6002\u1274\u256A\uD8BC\u4144\u95C1\u9E1F\u8842\uF0A1\u6FD5\u14D2\u9F61\u661C\u3E9F\uD397\uFB24\u13CD\u0F25\u28EA\uEE0E\u3614\u3487\u2A89\u1BC9\u3C80\uC05A\u05CE\uE689\uFFFB\u210E\uDF28\u6674\uC552\u2ABD\uB88A\u2891\uC105\u8467\uA683\u8874C\uB113\u1D98\u7143\uF5ED\u1683\uF1A6\uB703\u06DF\uF368\uDFCA\u43A1\u09CF\uFAAE\u0CB1\uB33B\uE201\uA198\uADB8\u13D7\uEC15\u507C\u4705\uA54F\u86BD\uDAF4\u33E9\u3A37\u612C\u89E5\u0600\u38AC\u942D\uC88F\u226F\uE610\uB3CA\u33E3\u9FFE\u49BC\uB421\u791F\u04A3\u63BB\u86FB\u8BF9\u2181\u055C\uFEFF\u4680\uFA30\u4CAC\uAB50\u5266\uAAC8\u9148\uC945\u3635\u70AD\uA345\u03CB\uC5FB\uE837\u3413\uCF7F\uD783\uD562\u6FB8\uB977\u9BD5\u444A\u7A69\uCBA5\u1B02\uD6FE\u2E54\u36C5\u1268\uD5FB\u06A9\u1D30\u80B8\u3763\u42DF\uA612\uF4A1\uBAE0\u8F25\u535D\uC31A\u5DCB\uAFFB\uF8FE\u7F14\u8058\u0E90\u1765\uD698\uD1D4\u84AF\uD064\u9E6B\u7B98\u515C\u2AAE\uAE73\u885C\uCD82\uF730\uEB9E\u746D\uDA20\uEB34\uB91D\u7321\u59D7\u845D\u1FA0\uD2FA\u8E46\u16A8\u6F54\u1494\u5172\u22CD\u757F\u94D8\uBB8B\u5313\uA2B7\uF155\u694E\u6FCD\u9735\uAA4F\uCE26\u59F2\u7196\u9945\u8227\uDB80\u3EA3\u009E\u7E8B\u8115\uE71E\uAA2C\u9E7A\uF86F\uA96B\u6A72\u552F\uC2BE\u5841\u9E4A\u68C2\uB20D\u5BA6\uCC1E\u21BF\u54CB\u7EEC\u716F\uF641\u73DE\u72B3\u6D1F\u6AA2\u54DC\u2E6D\u275C\u0D9D\uE596\uF695\u1503\uE8C6\u0DCC\u3D17\u068C\uFC16\u8268\u2D9B\uA5C8\uDB15\uE0EF\uE88B\uEAB3\uEF2D\uFC44\uF34D\u3A3B\u5C00\uEBDD\u8B38\u6A7C\uDBF2\u0DD1\u78D6\u6AF2\uEAFF\u19CD\uF2F0\uAC6C\u385A\u1855\uA269\u40F2\u0B0E\u6DC0\u09A8\uAEE3\u2E4A\uACE9\u6A55\u890F\u2E65�\uCAC1\u77A9\u7B49\uEA8C\u4FC7\uAF96\u2703\u1FA8\uB071\u177A\uE2F1\u6D14\uDC74\u07E1\uFACB\u8360\uDE2A\uAE80\uF59E\u6304\uB9A0\u67D3\u4946\u2A4C\u4E11\uA6BB\uEFF3\u42B4\uA241\u4B60\u684E\u3EEE\u30EE\u29AB\uA32B\u8170\uBD34\u48A2\uFC2D\u13A0\u8118\u6ED5\uA95F\uA16B\u8FC8\u7923\u16FA\uEAAB\u824E\uE0F4\uA41D\uE5AE\uC607\uA151\uDA46\u623A\u898C\u3FDB\uE70F\uFD54\u8387\u4B4D\u2D4E\u49B8\uF137\uD572\uBB91\u01D2\uCCB9\uBE90\u5959\u144C\u8F35\u8C42\u5D57\u360F\uDDE6\uBA01\u44F5\u1280\u5BD7\u3681\uEBA7\u0E85\uF26A\u1A8B\u961F\u1B4E\u39A6\u9C98\uCDB1\u2CBB\u13C9\u2453\u3AB7\u5316\uF772\uFA81\u7408\u22CE\u05B9\uF8FD\uB5A5\u4D48\uB731\u6B8B\uF45A\u05D2\u4939\uE1C6\u169C\uDBB0\uDD9D\u6458\uB7D6\u1044\u7E52\u784C\uB16D\u7CC0\u8F9A\uC327\u57E3\uD7E4\u211A\u6549\u813F\u8043\u8BC1\u2EB9\uD01D\uE498\uBC12\u60AA\uA339\uA84A\uDFAF\u7464\u5FB6\uC840\u2EFC\u5AB5\u7B5A\u424D\u91F4\u562D\uCFB0\u3192\u96BF\u3A36\uE598\u72C2\uDAAA\uC1EE\u5691\uD675\uB7FA\u67D6\u4C86\uB3B8\uEE2C\uBAE6\uFD80\u7C62\u8B54\u7327\u9E13\u5523\uAE68\u573B\u3DF3\uD7C2\uAFB3\u79F5\uDE28\u23EC\uD0BF\u134C\u8A5C\u7563\u13E1\u9EF5\u8625\u3B04\u277A\u64D9\u9779\u6362\u1E51\uC8E2\uF79F\u67F7\uC73B\uE858\u8734\uD93A\uF6FE\u178B\u07D1\u7ABF\uB201\uBB0C\u893B\uB0C1\u5CA7\uF888\u1098\u4EE7\u8A91\u94AB\u20C2\u1C5A\u9BD3\u8977\u02FD\u5A30\u9469\uFBA8\u020E\uA891\uD366\u0204\uAE9F\u1342", result);
	}

	/**
	 *
	 */
	public void testDecodeFromURL1() throws Throwable {
		String result = Utilities.decodeFromURL("testUtilitiesUrl");
		assertEquals("result", "testUtilitiesUrl", result);
	}

	/**
	 *
	 */
	public void testDecodeFromURL2() throws Throwable {
		String result = Utilities.decodeFromURL(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEncodeForFilename() throws Throwable {
		String result = Utilities.encodeForFilename("testUtilitiesName");
		assertEquals("result", "testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testEncodeForFilename1() throws Throwable {
		String result = Utilities.encodeForFilename(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEncodeForURL() throws Throwable {
		String result = Utilities.encodeForURL(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEncodeForURL1() throws Throwable {
		String result = Utilities.encodeForURL("testUtilitiesUrl");
		assertEquals("result", "testUtilitiesUrl", result);
	}

	/**
	 *
	 */
	public void testEscapeHTML() throws Throwable {
		String result = Utilities.escapeHTML(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEscapeHTML1() throws Throwable {
		String result = Utilities.escapeHTML("");
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	public void testEscapeHTML2() throws Throwable {
		String result = Utilities.escapeHTML("testUtilitiesInput");
		assertEquals("result", "testUtilitiesInput", result);
	}

	/**
	 *
	 */
	public void testExtractCommentsLink() throws Throwable {
		// FIXME
//		String result = Utilities.extractCommentsLink("testUtilitiesName");
//		assertEquals("result", "null:testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testExtractTopicLink() throws Throwable {
		String result = Utilities.extractTopicLink("testUtilitiesName");
		assertSame("result", "testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testExtractTopicLink1() throws Throwable {
		String result = Utilities.extractTopicLink("URLDecoder: Illegal hex characters in escape (%) pattern - ");
		assertEquals("result", "URLDecoder: Illegal hex characters in escape (%) pattern -", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("(");
		assertEquals("result", "(", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation1() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("");
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation2() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(".:");
		assertEquals("result", ".:", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation3() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("..");
		assertEquals("result", "..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation4() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("\"[");
		assertEquals("result", "[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation5() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("2l+.,).,[.");
		assertEquals("result", ".,).,[.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation6() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(":,");
		assertEquals("result", ":,", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation7() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";(");
		assertEquals("result", ";(", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation8() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(",HyvFb+$[.r.[");
		assertEquals("result", ".[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation9() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("[.");
		assertEquals("result", "[.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation10() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(":");
		assertEquals("result", ":", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation11() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("8>q/U,p9:r%%/ZU+C0KQBOd,ulNw1X8[UvJ._p,");
		assertEquals("result", ",", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation12() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("7EoB  L6z\"zUTVA_hPUQ'z|q*zd#;..");
		assertEquals("result", ";..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation13() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("A%!2YR?Ols+Lr'6/4;(r4'\";FSG.$T7u<='..;,");
		assertEquals("result", "..;,", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation14() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("-^");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation15() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";.");
		assertEquals("result", ";.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation16() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";[");
		assertEquals("result", ";[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation17() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";");
		assertEquals("result", ";", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation18() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("+]");
		assertEquals("result", "]", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation19() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(" ");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation20() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("h&#U1!&B2xv.");
		assertEquals("result", ".", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation21() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(").");
		assertEquals("result", ").", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation22() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(",");
		assertEquals("result", ",", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation23() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";..:........");
		assertEquals("result", ";..:........", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation24() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(".1..[,..");
		assertEquals("result", "..[,..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation25() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("IT$&,[e;");
		assertEquals("result", ";", result);
	}

	/**
	 *
	 */
	public void testFindRedirectedTopic() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		Topic result = Utilities.findRedirectedTopic(parent, 8);
		assertSame("result", parent, result);
	}

	/**
	 *
	 */
	public void testFindRedirectedTopic1() throws Throwable {
		Topic parent = new Topic();
		parent.setTopicType(2);
		Topic result = Utilities.findRedirectedTopic(parent, 100);
		assertSame("result", parent, result);
	}

	/**
	 *
	 */
	public void testGetVirtualWikiFromRequest() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testIsAdmin() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testIsCommentsPage() throws Throwable {
		boolean result = Utilities.isCommentsPage("URLDecoder: Illegal hex characters in escape (%) pattern - ");
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testIsCommentsPage1() throws Throwable {
		boolean result = Utilities.isCommentsPage("testUtilitiesTopicName");
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testIsFirstUse() throws Throwable {
		// FIXME
//		boolean result = Utilities.isFirstUse();
//		assertTrue("result", result);
	}

	/**
	 *
	 */
	public void testIsIpAddress() throws Throwable {
		boolean result = Utilities.isIpAddress("testUtilitiesIpAddress");
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testIsIpAddress1() throws Throwable {
		boolean result = Utilities.isIpAddress("");
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testIsUpgrade() throws Throwable {
		boolean result = Utilities.isUpgrade();
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testLogin() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParse() throws Throwable {
		ParserDocument result = Utilities.parse(new ParserInput(), null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testParse1() throws Throwable {
		ParserInput parserInput = new ParserInput();
		parserInput.setTopicName("testUtilitiesTopicName");
		parserInput.setVirtualWiki("testUtilitiesVirtualWiki");
		parserInput.setContext("testUtilitiesContext");
		ParserDocument result = Utilities.parse(parserInput, "testUtilitiesContent");
		assertEquals("result.getContent()", "<p>testUtilitiesContent\n</p>", result.getContent());
	}

	/**
	 *
	 */
	public void testParseMetadata() throws Throwable {
		ParserInput parserInput = new ParserInput();
		parserInput.setVirtualWiki("testUtilitiesVirtualWiki");
		parserInput.setTopicName("testUtilitiesTopicName");
		ParserDocument result = Utilities.parseMetadata(parserInput, "testUtilitiesContent");
		assertEquals("result.getContent()", "testUtilitiesContent", result.getContent());
	}

	/**
	 *
	 */
	public void testParseSave() throws Throwable {
		ParserInput parserInput = new ParserInput();
		parserInput.setUserIpAddress("testUtilitiesUserIpAddress");
		ParserDocument result = Utilities.parseSave(parserInput, "testUtilitiesContent");
		assertEquals("result.getContent()", "testUtilitiesContent", result.getContent());
	}

	/**
	 *
	 */
	public void testParseSlice() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseSplice() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParserDocument() throws Throwable {
		ParserDocument result = Utilities.parserDocument("testUtilitiesContent", "testUtilitiesVirtualWiki", "testUtilitiesTopicName");
		assertEquals("result.getContent()", "testUtilitiesContent", result.getContent());
	}

	/**
	 *
	 */
	public void testParserRedirectContent() throws Throwable {
		String result = Utilities.parserRedirectContent("testUtilitiesTopicName");
		assertEquals("result", "#REDIRECT [[testUtilitiesTopicName]]", result);
	}

	/**
	 *
	 */
	public void testUserHandlerInstance() throws Throwable {
		DatabaseUserHandler result = (DatabaseUserHandler) Utilities.userHandlerInstance();
		assertTrue("result.isWriteable()", result.isWriteable());
	}

	/**
	 *
	 */
	public void testValidateDirectory() throws Throwable {
		// FIXME
//		WikiMessage result = Utilities.validateDirectory("");
//		assertEquals("result.getKey()", "error.directorywrite", result.getKey());
	}

	/**
	 *
	 */
	public void testValidateDirectory1() throws Throwable {
		WikiMessage result = Utilities.validateDirectory("testUtilitiesName");
		assertEquals("result.getKey()", "error.directoryinvalid", result.getKey());
	}

	/**
	 *
	 */
	public void testValidateSystemSettings() throws Throwable {
		// FIXME
//		Properties props = new Properties();
//		props.put("homeDir", "testString");
//		props.put("parser", "org.jamwiki.parser.AbstractParser");
//		props.put("file-dir-full-path", "8&=\fCcx[|;o\nlAR*JsUiU1,\\\fH/)5 h{3c4Wxc;s");
//		Vector result = Utilities.validateSystemSettings(props);
//		assertEquals("result.size()", 5, result.size());
	}

	/**
	 *
	 */
	public void testValidateSystemSettings1() throws Throwable {
		Properties props = new Properties(new SortedProperties());
		props.put("file-dir-full-path", ")%2F");
		props.put("homeDir", "testString");
		props.put("parser", "org.jamwiki.parser.AbstractParser");
		props.put("url", "testString");
		Utilities.validateSystemSettings(props);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}

	/**
	 *
	 */
	public void testValidateSystemSettings2() throws Throwable {
		Properties props = new Properties();
		props.put("file-dir-full-path", "testString");
		props.put("homeDir", "testString");
		props.put("url", "testString");
		Utilities.validateSystemSettings(props);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}

	/**
	 *
	 */
	public void testValidateUserName() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testAddCookieThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testCurrentWatchlistThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testExtractCommentsLinkThrowsException() throws Throwable {
		try {
			Utilities.extractCommentsLink("");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Empty topic name ", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testExtractTopicLinkThrowsException() throws Throwable {
		try {
			Utilities.extractTopicLink("");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Empty topic name ", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuationThrowsNullPointerException() throws Throwable {
		try {
			Utilities.extractTrailingPunctuation(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsClassNotFoundException() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		parent.setVirtualWiki("testUtilitiesVirtualWiki");
		try {
			Utilities.findRedirectedTopic(parent, 8);
			fail("Expected ClassNotFoundException to be thrown");
		} catch (ClassNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 3 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsClassNotFoundException1() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		parent.setVirtualWiki("testUtilitiesVirtualWiki");
		try {
			Utilities.findRedirectedTopic(parent, 9);
			fail("Expected ClassNotFoundException to be thrown");
		} catch (ClassNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 3 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsNullPointerException() throws Throwable {
		try {
			Utilities.findRedirectedTopic(null, 100);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsWikiException() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		try {
			Utilities.findRedirectedTopic(parent, 10);
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "topic.redirect.infinite", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsMissingResourceException() throws Throwable {
		// FIXME
//		try {
//			Utilities.formatMessage("testUtilitiesKey", Locale.JAPAN);
//			fail("Expected MissingResourceException to be thrown");
//		} catch (MissingResourceException ex) {
//			assertEquals("ex.getMessage()", "Can't find bundle for base name ApplicationResources, locale ja_JP", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsMissingResourceException1() throws Throwable {
		// FIXME
//		Object[] objects = new Object[0];
//		try {
//			Utilities.formatMessage("testUtilitiesKey", Locale.KOREA, objects);
//			fail("Expected MissingResourceException to be thrown");
//		} catch (MissingResourceException ex) {
//			assertEquals("ex.getMessage()", "Can't find bundle for base name ApplicationResources, locale ko_KR", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsNullPointerException() throws Throwable {
		try {
			Utilities.formatMessage("testUtilitiesKey", null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsNullPointerException1() throws Throwable {
		Object[] objects = new Object[2];
		try {
			Utilities.formatMessage("testUtilitiesKey", null, objects);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderFileThrowsException() throws Throwable {
		try {
			Utilities.getClassLoaderFile("testUtilitiesFilename");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Unable to find testUtilitiesFilename", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderFileThrowsNullPointerException() throws Throwable {
		try {
			Utilities.getClassLoaderFile(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertEquals("ex.getMessage()", "name", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderRootThrowsException() throws Throwable {
		// FIXME
//		try {
//			Utilities.getClassLoaderRoot();
//			fail("Expected Exception to be thrown");
//		} catch (Exception ex) {
//			assertEquals("ex.getMessage()", "Unable to find ApplicationResources.properties", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testGetTopicFromURIThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testGetVirtualWikiFromURIThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testLoginThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseMetadataThrowsException() throws Throwable {
		try {
			Utilities.parseMetadata(new ParserInput(), "testUtilitiesContent");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseMetadataThrowsNullPointerException() throws Throwable {
		try {
			Utilities.parseMetadata(null, "testUtilitiesContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseSaveThrowsException() throws Throwable {
		try {
			Utilities.parseSave(new ParserInput(), "testUtilitiesContent");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseSaveThrowsNullPointerException() throws Throwable {
		try {
			Utilities.parseSave(null, "testUtilitiesContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseSliceThrowsClassNotFoundException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseSpliceThrowsClassNotFoundException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseThrowsException() throws Throwable {
		try {
			Utilities.parse(new ParserInput(), "testUtilitiesContent");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseThrowsNullPointerException() throws Throwable {
		try {
			Utilities.parse(null, "testUtilitiesContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParserDocumentThrowsException() throws Throwable {
		try {
			Utilities.parserDocument("testUtilitiesContent", null, "testUtilitiesTopicName");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParserDocumentThrowsNullPointerException() throws Throwable {
		try {
			Utilities.parserDocument(null, "testUtilitiesVirtualWiki", "testUtilitiesTopicName");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsFileNotFoundException() throws Throwable {
		try {
			Utilities.readFile("fakeFileName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 2 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsFileNotFoundException1() throws Throwable {
		try {
			Utilities.readFile("testUtilitiesFilename");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsNullPointerException() throws Throwable {
		try {
			Utilities.readFile(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException() throws Throwable {
		try {
			Utilities.readSpecialPage(null, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException1() throws Throwable {
		try {
			Utilities.readSpecialPage(Locale.GERMAN, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException2() throws Throwable {
		try {
			Utilities.readSpecialPage(Locale.SIMPLIFIED_CHINESE, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testValidateDirectoryThrowsNullPointerException() throws Throwable {
		try {
			Utilities.validateDirectory(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateSystemSettingsThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testValidateSystemSettingsThrowsNullPointerException1() throws Throwable {
		try {
			Utilities.validateSystemSettings(new Properties());
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsNullPointerException() throws Throwable {
		try {
			Utilities.validateTopicName(null);
			fail("Expected NullPointerException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsWikiException() throws Throwable {
		try {
			Utilities.validateTopicName("");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.notopic", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsWikiException1() throws Throwable {
		try {
			Utilities.validateTopicName("testUtilities\rName");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.name", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateUserNameThrowsWikiException() throws Throwable {
		try {
			Utilities.validateUserName("testUtilities\rName");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.name", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateUserNameThrowsWikiException1() throws Throwable {
		try {
			Utilities.validateUserName("");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "error.loginempty", ex.getWikiMessage().getKey());
		}
	}
}

