/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.ehcache.Element;

/**
 *
 * @author dfisla
 */
public class WikiDistributedCache {

    private static final WikiLogger logger = WikiLogger.getLogger(WikiCache.class.getName());
    private static String EHCACHE_SERVER_BASE = "http://localhost:8081/ehcache/rest";

    private WikiDistributedCache() {
    }

    /**
     * Add an object to the cache.
     *
     * @param cacheName The name of the cache that the object is being added
     *  to.
     * @param key A String, Integer, or other object to use as the key for
     *  storing and retrieving this object from the cache.
     * @param value The object that is being stored in the cache.
     */
    public static void addToCache(String cacheName, Object key, Object value) {

        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;

        try {

            url = new URL(EHCACHE_SERVER_BASE + "/" + cacheName + "/" + key);

            //create entry
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.connect();
            String sampleData = (String) value;
            byte[] sampleBytes = sampleData.getBytes();
            os = connection.getOutputStream();
            os.write(sampleBytes, 0, sampleBytes.length);
            os.flush();
            //System.out.println("result=" + result);
            //System.out.println("creating entry: " + connection.getResponseCode()
            //        + " " + connection.getResponseMessage());
            if (connection != null) {
                connection.disconnect();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Given two string values, generate a unique key value that can be used to
     * store and retrieve cache objects.
     *
     * @param value1 The first value to use in the key name.
     * @param value2 The second value to use in the key name.
     * @return The generated key value.
     */
    public static String key(String value1, String value2) {
        return value1 + "/" + value2;
    }

    /**
     * Remove a value from the cache with the given key and name.
     *
     * @param cacheName The name of the cache from which the object is being
     *  removed.
     * @param key The key for the record that is being removed from the cache.
     */
    public static void removeFromCache(String cacheName, Object key) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;

        try {

            url = new URL(EHCACHE_SERVER_BASE + "/" + cacheName + "/" + key);

            //create entry
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.connect();
            String sampleData = "";
            byte[] sampleBytes = sampleData.getBytes();
            os = connection.getOutputStream();
            os.write(sampleBytes, 0, sampleBytes.length);
            os.flush();
            //System.out.println("result=" + result);
            //System.out.println("creating entry: " + connection.getResponseCode()
            //        + " " + connection.getResponseMessage());
            if (connection != null) {
                connection.disconnect();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Retrieve a cached element from the cache.  This method will return
     * <code>null</code> if no matching element is cached, an element with
     * no value if a <code>null</code> value is cached, or an element with a
     * valid object value if such an element is cached.
     *
     * @param cacheName The name of the cache from which the object is being
     *  retrieved.
     * @param key The key for the record that is being retrieved from the
     *  cache.
     * @return A new <code>Element</code> object containing the key and cached
     *  object value.
     */
    public static Element retrieveFromCache(String cacheName, Object key) {

        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        int result = 0;
        StringBuilder sb = null;
        Element cacheElement = null;


        try {

            //get entry
            url = new URL(EHCACHE_SERVER_BASE + "/" + cacheName + "/" + key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            byte[] response = new byte[4096];
            

            result = is.read(response);
            while (result != -1) {
                //System.out.write(response, 0, result);
                sb.append(response);
                result = is.read(response);
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            //System.out.println("reading entry: " + connection.getResponseCode()
            //        + " " + connection.getResponseMessage());
            if (connection != null) {
                connection.disconnect();
            }

            cacheElement = new Element(key, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return cacheElement;
    }
}
