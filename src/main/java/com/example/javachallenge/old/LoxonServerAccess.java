package com.example.javachallenge.old;

// import java.security.*;
import java.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoxonServerAccess {
    private static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----\n";
    private static final String PUBLIC_KEY_END = "\n-----END PUBLIC KEY-----";
    private static final int KEY_SIZE = 2048;
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static void main(String[] args) throws Exception {

        // First part
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


        // Second part
        String email = "alerr415@gmail.com";
        String team = "Monad";
        long ts = System.currentTimeMillis();

        String endpoint = "http://javachallenge.loxon.eu:8081/game_api/getGameKey";
        String queryString = String.format("email=%s&team=%s&ts=%d&signature=%s", urlEncode(email), urlEncode(team), ts, urlEncode(generateSignature(email, team, ts, privateKey)));
        String requestUrl = endpoint + "?" + queryString;

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    private static String getPublicKeyPEM(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
        StringBuilder publicKeyPEMBuilder = new StringBuilder();
        publicKeyPEMBuilder.append(PUBLIC_KEY_BEGIN);
        publicKeyPEMBuilder.append(base64PublicKey);
        publicKeyPEMBuilder.append(PUBLIC_KEY_END);
        return publicKeyPEMBuilder.toString();
    }

    private static byte[] signMessage(String message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    private static boolean verifySignature(String message, byte[] signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(message.getBytes());
        return sig.verify(signature);
    }

    private static String generateSignature(String email, String team, long ts, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update((email + team + ts).getBytes("UTF-8"));

            byte[] signatureBytes = signature.sign();
            return Base64.getUrlEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String urlEncode(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
