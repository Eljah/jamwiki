package org.jamwiki.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * I/O utilities, specifically to zip/transfer streams.
 *
 * @author  <a href="mailto:mikaty@ecircle-ag.com">Stephane Mikaty</a>
 * @version $Revision: 1.7 $ $Date: 2005/08/26 09:03:24 $
 */
public class IOUtil {

    /**
     * Transfers data between the given input stream and outout stream.
     * It does not open a thread to do it, so it is blocking.
     *
     * @param in InputStream to be read from
     * @param out OutputStream to be written to
     * @return total number of bytes transferred
     */
    public static final long transfer(InputStream in,
                                      OutputStream out)
        throws IOException
    {
        // allocate a new buffer for each transfer operation
        // a static buffer is not worth it
        byte[] buf = new byte[512];
        int read = 0;
        long total = 0L;
        try
        {
            while ((read = in.read(buf))!=-1)
            {
                out.write(buf, 0, read);
                total += read;
            }
        }
        finally
        {
            try { in.close(); } catch (IOException ioe) {}
        }
        return total;
    }

    /**
     * Fills completely the provided buffer, but up to the end of the 
     * input stream, by issueing multiple calls to the provided InputStream 
     * if necessary.
     *   
     * @param byteBuffer buffer to be filled
     * @return the number of bytes transferred, which will be less than the
     *         buffer size if the input stream reached the end of the stream.
     */
    public static int fill(InputStream in, byte[] byteBuffer) throws IOException {
        int totalReadCount = 0;
        int lastReadCount = 0;
        while ((lastReadCount = in.read(byteBuffer, totalReadCount, byteBuffer.length - totalReadCount)) != -1)
        {
            totalReadCount += lastReadCount;
            if (totalReadCount == byteBuffer.length) return totalReadCount;
        }
        return totalReadCount;
    }

    public static byte[] zipContent(String content)
      throws IOException
    {
        final ByteArrayOutputStream bout =
          new ByteArrayOutputStream(Math.max(100, content.length()));
        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
        final DeflaterOutputStream out = new DeflaterOutputStream(bout, deflater);
        out.write(content.getBytes("UTF8"));
        out.flush();
        out.close();
        return bout.toByteArray();
    }

    public static String unzipContent(byte[] zippedContent) 
      throws IOException 
    {
        InputStream bin = 
          new SequenceInputStream(
            new ByteArrayInputStream(zippedContent),
            // when using nowrap, we need one extra byte
            new ZeroInputStream()
          );
        final Inflater inflater = new Inflater(true);
        InflaterInputStream in = new InflaterInputStream(bin, inflater);
        final ByteArrayOutputStream bout =
          new ByteArrayOutputStream(zippedContent.length);
        transfer(in, bout);
        bout.flush();
        return bout.toString("UTF8");
    }

    /** useful constant */
    private static final String HEXDIGIT = "0123456789ABCDEF";

    /**
     * Converts a Unicode Java Strings to the Hexadecimal
     * representation of its bytes in the specified encoding
     *
     * @param s String to translate
     * @param enc encoding to be used when doing s.getBytes()
     */
    public static String toHexString(String s, String enc)
    {
        try
        {
            return toHexString(s.getBytes(enc));
        }
        catch (UnsupportedEncodingException uee)
        {
            return "<unsupported encoding>";
        }
    }

    /**
     * byte[] to String conversion.
     * equivalent to <code>toHexString(c, 0, c.length);</code>
     *
     * @param c byte array to be converted
     */
    public static String toHexString(byte[] c)
    {
        return toHexString(c, 0, c.length);
    }

    /**
     * byte[] to String conversion
     *
     * @param c byte array to be converted
     * @param ofs offset in the array
     * @param len length to convert
     */
    public static String toHexString(byte[] c, int ofs, int len)
    {
        final StringBuffer out = new StringBuffer();
        for (int i = ofs, n=ofs+len; i < n; i++)
        {
            if (i > 0) {
                out.append(' ');
            }
            final int k = c[i];
            out.append(HEXDIGIT.charAt((k >> 4) & 0x0f));
            out.append(HEXDIGIT.charAt(k & 0x0f));
        }
        return out.toString();
    }

    private static class ZeroInputStream extends InputStream {
        boolean finished = false;
        public int read() throws IOException {
            if (finished) return -1;
            finished = true;
            return 0;
        }
    }
}
