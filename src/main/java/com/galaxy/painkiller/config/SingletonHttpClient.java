package com.galaxy.painkiller.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SingletonHttpClient {

    private static final int HTTP_POOL_MAX_CONN = 4000;

    private static final int HTTP_POOL_MAX_CONN_PER_ROUTE = 4000;

    private static final int HTTP_POOL_CONN_TIMEOUT = 3000;

    private static final int HTTP_POOL_READ_TIMEOUT = 20;


    public static CloseableHttpClient getInstance(){
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
            // Honor 'keep-alive' header
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
            // otherwise keep alive for 30 seconds
            return 30 * 1000;
        };

        // 同时支持HTTP和HTTPS。
        SSLConnectionSocketFactory ssf;
        SSLContext sslctx = getSSLContext();
        if (sslctx == null) {
            ssf = SSLConnectionSocketFactory.getSocketFactory();
        } else {
            ssf = new SSLConnectionSocketFactory(sslctx, NoopHostnameVerifier.INSTANCE);
        }
        Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", ssf)
                .build();

        // 连接池的一些配置。
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(r);
        // 最大连接数，指整个连接池容纳的最大连接数。
        connManager.setMaxTotal(HTTP_POOL_MAX_CONN);
        // 每个路由（url）可供分配的最大连接数，理论上，其必需小于上面的总最大连接数。
        connManager.setDefaultMaxPerRoute(HTTP_POOL_MAX_CONN_PER_ROUTE);

        // 创建HttpClient，它是线程安全的，可以全局设置一个实例。
        return HttpClients.custom().setKeepAliveStrategy(keepAliveStrategy)
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(HTTP_POOL_CONN_TIMEOUT)
                        .setSocketTimeout(HTTP_POOL_READ_TIMEOUT)
                        .build())
                .setRetryHandler(new StandardHttpRequestRetryHandler(3, true)).build();
    }

    private static SSLContext getSSLContext() {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

}
