/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.db;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.log4j.Logger;


/**
 *
 * @author dfisla
 */
public class DataCompression {

    private static final Logger logger = Logger.getLogger(AnsiDataHandler.class.getName());

    public static final int  PROP_DB_COMPRESSION_NONE = 0;
    public static final int  PROP_DB_COMPRESSION_DEFAULT = 1;

    /**
     *
     * @param buffer
     * @return
     */
    protected static byte[] decompressByteArray(byte[] buffer) {

        byte[] output = null;

        if (buffer != null) {
            try {
                Inflater decompressor = new Inflater();
                decompressor.setInput(buffer);

                ByteArrayOutputStream bos = new ByteArrayOutputStream(buffer.length);
                byte[] buf = new byte[1024];

                while (!decompressor.finished()) {
                    try {
                        int count = decompressor.inflate(buf);
                        bos.write(buf, 0, count);
                    } catch (DataFormatException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                bos.close();
                output = bos.toByteArray();
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.error(uee.getMessage(), uee);
            } catch (java.io.IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            }
        }

        return output;
    }

    /**
     *
     * @param buffer
     * @return
     */
    protected static byte[] compressByteArray(byte[] buffer) {

        byte[] output = null;

        if (buffer != null) {
            try {
                Deflater compressor = new Deflater();
                compressor.setLevel(Deflater.BEST_COMPRESSION);
                compressor.setInput(buffer);
                compressor.finish();

                ByteArrayOutputStream bos = new ByteArrayOutputStream(buffer.length);
                byte[] buf = new byte[1024];

                while (!compressor.finished()) {
                    int count = compressor.deflate(buf);
                    bos.write(buf, 0, count);
                }

                bos.close();
                output = bos.toByteArray();
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.error(uee.getMessage(), uee);
            } catch (java.io.IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            }
        }

        return output;
    }
}
