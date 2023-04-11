package com.example.javachallenge.auth;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----\n";
    private static final String PUBLIC_KEY_END = "\n-----END PUBLIC KEY-----";
    private static final String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----\n";
    private static final String PRIVATE_KEY_END = "\n-----END PRIVATE KEY-----";
    
    public static KeyPair generateKey(){
        KeyPairGenerator kpg = null;
        try{
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);

        }catch (Exception e){
            e.printStackTrace();
        }
        return kpg.genKeyPair();
    }

    public static String getKeyPEM() {
        // Public key
        var keyPair = generateKey();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        String base64PublicKey = Base64.encodeBase64String(publicKeyBytes);
        StringBuilder publicKeyPEMBuilder = new StringBuilder();
        publicKeyPEMBuilder.append(PUBLIC_KEY_BEGIN);
        publicKeyPEMBuilder.append(base64PublicKey);
        publicKeyPEMBuilder.append(PUBLIC_KEY_END);
        System.out.println(publicKeyPEMBuilder);
        System.out.println();

        // Private key
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        String base64PrivateKey = Base64.encodeBase64String(privateKeyBytes);
        StringBuilder privateKeyPEMBuilder = new StringBuilder();
        privateKeyPEMBuilder.append(PRIVATE_KEY_BEGIN);
        privateKeyPEMBuilder.append(base64PrivateKey);
        privateKeyPEMBuilder.append(PRIVATE_KEY_END);
        System.out.println(privateKeyPEMBuilder);

        return publicKeyPEMBuilder.toString();
    }

}
