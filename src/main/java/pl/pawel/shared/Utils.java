package pl.pawel.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        LOGGER.info("=== Inside generateUserId()");
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        LOGGER.info("=== Inside generateRandomString()");
        StringBuilder returnValue = new StringBuilder(length);

        for(int i = 0; i< length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);

    }
}
