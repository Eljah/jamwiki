package org.jamwiki.test.utils;

import junit.framework.TestCase;
import org.jamwiki.utils.IOUtil;

import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.8 $ $Date: 2002/11/07 13:32:18 $
 */
public class TestIOUtil extends TestCase {

    public void testZipUnzipContent() throws Exception {
        String testContent = "Some short text with umlauts: äüßÄÜÖö.";
        System.out.println("testContent = " + testContent);
        System.out.println("byte size: " + testContent.getBytes("UTF8").length);
        byte[] zipBytes = IOUtil.zipContent(testContent);
        System.out.println("zipBytes: " + IOUtil.toHexString(zipBytes));
        System.out.println("zipped size: " + zipBytes.length);
        String result = IOUtil.unzipContent(zipBytes);
        System.out.println("result = " + result);
        assertEquals(testContent, result);
    }

    public void test8bitEncoding() throws Exception {
        byte[] test = new byte[256];
        for (int i = 0; i < 256; i++) {
            test[i] = (byte)(i - 128);
            assertEquals("byte " + i, (i - 128), test[i]);
        }
        String testString = new String(test, 0, test.length, "iso8859-1");
        byte[] resultBytes = testString.getBytes("iso8859-1");
        for (int i = 0; i < resultBytes.length; i++) {
            assertEquals("byte " + i, test[i], resultBytes[i]);
        }
    }

    public void testZipContent() throws Exception {
        Random rand = new Random();
        boolean nowrap = true;
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, nowrap);
//        byte[] input = new byte[80];
        byte[] input =
          ("redirect: some other page, and have length around around: 60"
//        +"012345678901234567890123456789012345678901234567890123456789"
//           + "<font size=\"-3\">All contents copyright of the author. &copy;2006.</font>" 
//           + "Change beginning. Some short string."
//           + "Some string which is " 
//           + "a bit longer than the one before. "
//           + "I wonder how long I have to get it to get a real compression... "
//           + "Looks like this is still not enough. "
//           + "So I'll add some more to it... "
//           + "And it was still not enough !!! Furthermore, that was STILL not enough... "
//           + "So I will add some more again... "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I wonder if compression is working at all... So what is compressed ? "
//           + "I think almost nothing..."
          ).getBytes();
//        rand.nextBytes(input);
        System.out.println("origSize: " + input.length);
        System.out.println("input: " + IOUtil.toHexString(input));
        byte[] result = new byte[2 * input.length];
        deflater.setInput(input);
        deflater.finish();
        int count = deflater.deflate(result);
        System.out.println("deflateSize = " + count);
        System.out.println("deflateResult: " + IOUtil.toHexString(result, 0, count));

        byte[] test = new byte[input.length];
        Inflater inflater = new Inflater(nowrap);
        inflater.setInput(result, 0, count);
        int testCount = inflater.inflate(test);
        System.out.println("inflateSize = " + testCount);
        System.out.println("inflateResult: " + IOUtil.toHexString(test));

        ByteArrayOutputStream out = new ByteArrayOutputStream(2 * input.length);
        GZIPOutputStream gzOut = new GZIPOutputStream(out);
        gzOut.write(input);
        gzOut.flush();
        gzOut.close();
        int size = out.size();
        byte[] gzResult = out.toByteArray();
        System.out.println("gzSize = " + size);
        System.out.println("gzResult: " + IOUtil.toHexString(gzResult));

        test = new byte[input.length];
        GZIPInputStream gzIn =
          new GZIPInputStream(new ByteArrayInputStream(gzResult));
        testCount = IOUtil.fill(gzIn, test);
        System.out.println("gunzipSize = " + testCount);
        System.out.println("gunzipResult: " + IOUtil.toHexString(test, 0, testCount));
    }

    private String zipContent(String content)
      throws IOException
    {
        final ByteArrayOutputStream bout =
          new ByteArrayOutputStream(Math.max(100, content.length()));
        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
        final DeflaterOutputStream out = new DeflaterOutputStream(bout, deflater);
        out.write(content.getBytes());
        out.flush();
        out.close();
        return bout.toString();
    }

}
