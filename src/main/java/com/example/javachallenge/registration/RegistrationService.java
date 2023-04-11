package com.example.javachallenge.registration;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    // @Value("${keypair.private}")
    private static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDtZiQV1+MJRYGuMjVPFhfB7QQSbGQ6x99HGnSjOBQDLWo4OwU7/ZqUE6M7L8mAx3o0bfti5WihqBNdeJAoTpJvd7qQPvQkOWcXHC6r2U662zONmG3BHv8dDe9hxS3J27IqcMLbfkPZPYVTGyPWuxkJl2ZC7cP46RRXed2/DCE0bA+k/4SvF1wryZZ3OW1wCa5ni1oa88nHqrDHsmq43PnROdOg8n2ALmM10PrXHJXWFDSAWgQh4PYCUMwH699OrIp+c7Fl84uk96lz2fNSiyTE6d/n+O6Y7TFy28N73Crle9ZghHtXrzHz9/xCYLgOe810JpVSxTdo+vemhkwi2fvbAgMBAAECggEAE2mJw9t+bjCn1QW4BDqLQMlPzu3cGE5lvJu2M+6O/F+HUwfIespXDIq6i0lyEby+CnF/3+tgbe+ffcIxD4q1QLYqpEbxkdC71FDypAzF+lOYWJjBSMxvEgIks/r6ronvfLbxyB8AD1CPiWcrvBBakMU3kHt+zejJSAnoDwo/4NhY60Xyx0UALBQfQTzymw5CJFezUl18P88JEd5TUbOwj90YWPT4Xoa7EebntZyCIkS4oI06pNOOcFj+HA0EiMCIVUQY/GsaMhjTMpSgVoGlpIhoOozKXYWf4KVIqVqK+kPCkY0xU7R0KknweJoTMNuQsSuMicyH+hmqvDbeJNpaoQKBgQDt3JB2wwotx7qioiN+AJrNMF9XRtRAEs0tbf5IRtkDiU4nBVE/56iItwrjv4einxBZqI9ORSktRvb8aKimRvouLtZDu5T43vi5MdFn2kXrDSVY8hfzOQs+OC2B73OzU3mpxgeswiicUbd3PTRWiWe5gzCtOQgAVTt3QVaiA7AQiwKBgQD/gIvPPL7ppViZGYfTogc1jH8x4QfETm69cYWlMNtMDLO5sceuEhfPelUVrp7Zw5wB9CBaVP5T8EJt+lqmYweshtEOSq6C20Cn9l+6qQteZY2OIMaFdoawrCy8kuUhl+WRYi2P6iMV6KkBGeEnJeJe57q7wNXsSWkwVpZmostb8QKBgQDVKdEdXXUFyfRsQznjdc9FC5CluogEU9UkXWqAg12AWdB8D7vctVLIEqHjsIVxK3OPpgunbTuVo/87/N8HBJy4PqkuUjETR19HOGod0/LMzx8lMvBbvEdjHx1JULozfx+NEjdWjzmtvV3ZsjPxSXp3OiapVm7hPYDpQtjMoNlsOQKBgQDEG2g0Ek3+i5irOjtUpuKdqU7fe/c4J3M8GOwZVJydjIpOFzRKWTzJVP4FfFgUkN0nareCXgYvaxOx0hzN5Oi49Wl0bMHxUlQYFzR20tUVCSFTW7jEK6tJ430a6CzSUgI0SoTqFy4C3OcLH0//ekZ7zaU4AtSvXdFM9sjN0loAIQKBgHtfd9F03+kpQWzamYy55YEHTOv4QD5hw1iqP+3I636j0RWr6EhxRaWh2xXIoTXHNaG1jx9FrYGWFtVxwE3Cs7JJ7AIdcQoG0S7GatXBbrU/BLszNcG8g2g7phOLgFvf3T3VhxwuIZffaNOcKKIY2mhdqiUhfRDkRiP2/n6SfxyL";

    // @Value("${SERVER_URL}")
    private static String SERVER_URL = "http://javachallenge.loxon.eu:8081";

    private static String EMAIL = "djoliver0917@gmail.com";
    private static String TEAM = "Monad";

    private static String urlEncode(String input) {
        try{
            return URLEncoder.encode(input, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static PrivateKey decodePrivateKey() throws Exception{
        byte[] decodedKey = Base64.decodeBase64(PRIVATE_KEY);
        System.out.println(" - Private key: " + PRIVATE_KEY);
        // System.out.println(" - Decoded key: " + Arrays.toString(decodedKey));
        var keySpec = new PKCS8EncodedKeySpec(decodedKey);
        var keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private static String generateSignature(Long timestamp) {
        try{
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(decodePrivateKey());
            signature.update(("email=" + urlEncode(EMAIL) + "&team=" + urlEncode(TEAM) + "&ts=" + timestamp.toString()).getBytes("UTF-8"));
            byte[] signatureBytes = signature.sign();
            return Base64.encodeBase64String(signatureBytes);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static String registerTeam() throws Exception{
        var timestamp = System.currentTimeMillis();
        String endpointUrl = SERVER_URL + "/game_api/getGameKey?email=" +
            urlEncode(EMAIL) + 
            "&team=" + urlEncode(TEAM) + 
            "&ts=" + timestamp + 
            "&signature=" + urlEncode(generateSignature(timestamp));
        System.out.println(" - URL: " + endpointUrl);
        URL url = new URL(endpointUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        System.out.println(" - Response Code: " + connection.getResponseCode());
        System.out.println(" - Response message: " + connection.getResponseMessage());
        return connection.getResponseMessage();
    }
    
}
