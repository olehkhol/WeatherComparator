package ua.in.khol.oleh.touristweathercomparer.model.weather.owm;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class OwmAuth {
    private static final String SECRET_KEY = "sL/30SVMMObescisV96u05QP7IupJYjf9nNavEYfr0M="/*"a623277ed135b996d5aaad2f685b1a7b"*/;

    public static String getSecretKey() {
        return D.d(SECRET_KEY);
    }

    private static class D {
        static String algo = "ARCFOUR";
        static String kp = "AjOslNdUQLzt60bT";

        private static String d(String s) {
            String str;
            String key = "mwPSikhrwQnVHC+kfRYgGQ==";
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