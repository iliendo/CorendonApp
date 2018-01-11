package corendo.fys;

import corendo.fys.controller.Supervisor_medewerker_toevoegenController;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ilias Azagagh
 */
public class Password {

    /**
     * Encrypt het wachtwoord met MD5 hash
     *
     * @return Een random gegenereerd hash wachtwoord
     */
    public String getHashedPassword(String password) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());
            
            String hash = new BigInteger(1, m.digest()).toString(16);

            return hash;

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Supervisor_medewerker_toevoegenController.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }

    public String getRandomPassword() {
        /**
         * Genereert een random wachtwoord bestaande uit 8 karakters (Hoofdletters, klein letters en symbolen)
         *
         * @return het random wachtwoord
         */
        String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%";
        StringBuilder passwordBuilder = new StringBuilder();
        Random randomizer = new Random();
        while (passwordBuilder.length() < 8) {
            int index = (int) (randomizer.nextFloat() * passwordCharacters.length());
            passwordBuilder.append(passwordCharacters.charAt(index));
        }
        String randomPassword = passwordBuilder.toString();

        return randomPassword;

    }
}
