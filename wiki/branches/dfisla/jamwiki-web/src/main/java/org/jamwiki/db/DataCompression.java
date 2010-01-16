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
    public static final int PROP_DB_COMPRESSION_NONE = 0;
    public static final int PROP_DB_COMPRESSION_DEFAULT = 1;

    /**
     *
     * @param buffer
     * @return
     */
    protected static byte[] decompressByteArray(byte[] buffer) {

        byte[] output = null;
        ByteArrayOutputStream bos = null;
        Inflater decompressor = null;

        if (buffer != null) {
            try {
                decompressor = new Inflater();
                decompressor.setInput(buffer);

                bos = new ByteArrayOutputStream(buffer.length);
                byte[] buf = new byte[1024];

                while (!decompressor.finished()) {
                    try {
                        int count = decompressor.inflate(buf);
                        bos.write(buf, 0, count);
                    } catch (DataFormatException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                output = bos.toByteArray();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                        decompressor = null;
                    } catch (Exception ex) {
                        logger.warn(ex);
                    }
                }
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
        Deflater compressor = null;
        ByteArrayOutputStream bos = null;

        if (buffer != null) {
            try {
                compressor = new Deflater();
                compressor.setLevel(Deflater.BEST_COMPRESSION);
                compressor.setInput(buffer);
                compressor.finish();

                bos = new ByteArrayOutputStream(buffer.length);
                byte[] buf = new byte[1024];

                while (!compressor.finished()) {
                    int count = compressor.deflate(buf);
                    bos.write(buf, 0, count);
                }

                output = bos.toByteArray();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                        compressor = null;
                    } catch (Exception ex) {
                        logger.warn(ex);
                    }
                }
            }
        }

        return output;
    }
}
