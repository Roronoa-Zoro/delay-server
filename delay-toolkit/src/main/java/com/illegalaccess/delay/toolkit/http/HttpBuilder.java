package com.illegalaccess.delay.toolkit.http;


public class HttpBuilder {

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "http://";
    /**
     * e.g. http://127.0.0.1:8080/hello
     * @param ip
     * @param port
     * @return
     */
    public static final String buildHttpUrl(String ip, int port, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(HTTP_PREFIX).append(ip).append(":").append(port).append("/").append(path);
        return sb.toString();
    }
}
