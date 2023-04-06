package com.javachallenge;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import org.apache.commons.codec.binary.Base64;

import javax.mvc.security.Csrf;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

public class LoxonServerAccess {
    private static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----\n";
    private static final String PUBLIC_KEY_END = "\n-----END PUBLIC KEY-----";
    private static final int KEY_SIZE = 2048;
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static void main(String[] args) throws Exception {
        // Generate key pair
        KeyPair keyPair = generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Convert public key to PEM format
        String publicKeyPEM = getPublicKeyPEM(publicKey);
        System.out.println(publicKeyPEM);

        // Sign and verify a message
        String message = "Hello, Loxon!";
        byte[] signature = signMessage(message, privateKey);
        boolean isVerified = verifySignature(message, signature, publicKey);
        System.out.println("Signature verified: " + isVerified);
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    private static String getPublicKeyPEM(PublicKey publicKey) throws Exception {
        byte[] publicKeyBytes = publicKey.getEncoded();
        String base64PublicKey = Base64.encodeBase64String(publicKeyBytes);
        StringBuilder publicKeyPEMBuilder = new StringBuilder();
        publicKeyPEMBuilder.append(PUBLIC_KEY_BEGIN);
        publicKeyPEMBuilder.append(base64PublicKey);
        publicKeyPEMBuilder.append(PUBLIC_KEY_END);
        return publicKeyPEMBuilder.toString();
    }

    private static byte[] signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    private static boolean verifySignature(String message, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(message.getBytes());
        return sig.verify(signature);
    }
}
