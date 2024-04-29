package com.social.socialnetwork.Domain;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


/**This class follows the singleton pattern design to be able to provide the same secretKey
 * for multiple places where it is needed...*/
// Advanced Encryption Standard
public class AES {
    private static AES instance = null;
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes();
    private Key secretKey;


    private AES() throws Exception {
        secretKey=generateKey();
    }
    public static AES getInstance() throws Exception {
        if(instance==null)
            instance= new AES();
        return instance;
    }

    public Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

   public byte[] encryptMessage(byte[] message) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException
   {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(message);
   }

}
