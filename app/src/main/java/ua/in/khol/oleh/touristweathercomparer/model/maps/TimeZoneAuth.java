package ua.in.khol.oleh.touristweathercomparer.model.maps;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class TimeZoneAuth {
    private static final String API_KEY = "/Unl0mgrvz43Io+/cVv7ZhdipEeovrWt+C22r/3KSoOjdGjslBIN";

    public static String getApiKey() {
        return D.d(API_KEY);
    }

    private static class D {
        static String algo = "ARCFOUR";
        static String kp = "xJl8nAGfOLc2xfO5";

        public static String d(String s) {
            String str;
            String key = "FbfQlxkHyZM/icOC8AGSDg==";
            try {
                Cipher rc4 = Cipher.getInstance(algo);
                Key kpk = new SecretKeySpec(kp.getBytes(), algo);
                rc4.init(Cipher.DECRYPT_MODE, kpk);
                byte[] bck = Base64.decode(key, Base64.DEFAULT);
                byte[] bdk = rc4.doFinal(bck);
                Key dk = new SecretKeySpec(bdk, algo);
                rc4.init(Cipher.DECRYPT_MODE, dk);
                byte[] bcs = Base64.decode(s, Base64.DEFAULT);
                byte[] byteDecryptedString = rc4.doFinal(bcs);
                str = new String(byteDecryptedString);
            } catch (Exception e) {
                str = "";
            }
            return str;
        }

    }
}
