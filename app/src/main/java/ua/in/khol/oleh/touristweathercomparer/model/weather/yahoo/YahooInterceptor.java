package ua.in.khol.oleh.touristweathercomparer.model.weather.yahoo;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

public class YahooInterceptor implements Interceptor {
    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String URL = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
    private static final Escaper ESCAPER = UrlEscapers.urlFormParameterEscaper();
    private final String mAppId;
    private final String mConsumerKey;
    private final String mConsumerSecret;

    private YahooInterceptor(String appId, String consumerKey, String consumerSecret) {
        mAppId = appId;
        mConsumerKey = consumerKey;
        mConsumerSecret = consumerSecret;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(signRequest(chain.request()));
    }

    private Request signRequest(Request request) throws IOException {

        long timestamp = System.currentTimeMillis() / 1000;
        String oauthNonce = "16839989513580";

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + mConsumerKey);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_version=1.0");
        // Make sure value is encoded
        HttpUrl httpUrl = request.url();
        for (int i = 0; i < httpUrl.querySize(); i++)
            parameters.add(ESCAPER.escape(httpUrl.queryParameterName(i))
                    + "="
                    + ESCAPER.escape(httpUrl.queryParameterValue(i)));
        Collections.sort(parameters);

        StringBuilder parametersList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
        }

        String signatureString = "GET&" +
                URLEncoder.encode(URL, "UTF-8") + "&" +
                URLEncoder.encode(parametersList.toString(), "UTF-8");


        String signature = null;
        SecretKeySpec signingKey
                = new SecretKeySpec((mConsumerSecret + "&").getBytes(), "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            signature = ByteString.of(rawHMAC).base64();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        String authorization = "OAuth "
                + OAUTH_CONSUMER_KEY + "=\"" + mConsumerKey + "\", "
                + OAUTH_SIGNATURE_METHOD + "=\"" + OAUTH_SIGNATURE_METHOD_VALUE + "\", "
                + OAUTH_TIMESTAMP + "=\"" + timestamp + "\", "
                + OAUTH_NONCE + "=\"" + oauthNonce + "\", "
                + OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + "\", "
                + OAUTH_SIGNATURE + "=\"" + ESCAPER.escape(signature) + "\"";


        return request.newBuilder()
                .addHeader("Authorization", authorization)
                .addHeader("Yahoo-App-Id", mAppId)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    static final class Builder {
        private String appId;
        private String consumerKey;
        private String consumerSecret;

        Builder appId(String appId) {
            if (appId == null) throw new NullPointerException("consumerKey = null");
            this.appId = appId;

            return this;
        }

        Builder consumerKey(String consumerKey) {
            if (consumerKey == null) throw new NullPointerException("consumerKey = null");
            this.consumerKey = consumerKey;

            return this;
        }

        Builder consumerSecret(String consumerSecret) {
            if (consumerSecret == null) throw new NullPointerException("consumerSecret = null");
            this.consumerSecret = consumerSecret;

            return this;
        }

        YahooInterceptor build() {
            if (consumerKey == null) throw new IllegalStateException("consumerKey not set");
            if (consumerSecret == null) throw new IllegalStateException("consumerSecret not set");

            return new YahooInterceptor(appId, consumerKey, consumerSecret);
        }
    }
}
