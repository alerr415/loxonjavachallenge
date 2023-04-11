package com.example.javachallenge.old;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import java.net.HttpURLConnection;
import java.net.URL;

public class RestRequestSender {

    public static void main(String[] args) {
        String email = "alerr415@gmail.com";
        String team = "Monad";
        long ts = System.currentTimeMillis();

        String endpoint = "http://javachallenge.loxon.eu:8081/game_api/getGameKey";
        String queryString = String.format("email=%s&team=%s&ts=%d&signature=%s", urlEncode(email), urlEncode(team), ts, urlEncode(generateSignature(email, team, ts)));
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

    private static String generateSignature(String email, String team, long ts) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();

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
