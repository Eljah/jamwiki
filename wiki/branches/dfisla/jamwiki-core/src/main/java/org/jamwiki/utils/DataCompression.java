/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author dfisla
 */
public class DataCompression {

    private static final WikiLogger logger = WikiLogger.getLogger(DataCompression.class.getName());
    public static final int PROP_DB_COMPRESSION_NONE = 0;
    public static final int PROP_DB_COMPRESSION_DEFAULT = 1;

    /**
     *
     * @param buffer
     * @return
     */
    public static byte[] decompressByteArray(byte[] buffer) {

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
                        logger.severe(e.getMessage(), e);
                    }
                }

                output = bos.toByteArray();
            } catch (Exception e) {
                logger.severe(e.getMessage(), e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                        decompressor = null;
                    } catch (Exception ex) {
                        logger.warning(ex.getMessage(), ex);
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
    public static byte[] compressByteArray(byte[] buffer) {

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
                logger.severe(e.getMessage(), e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                        compressor = null;
                    } catch (Exception ex) {
                        logger.warning(ex.getMessage(), ex);
                    }
                }
            }
        }

        return output;
    }
}
