/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

/**
 *
 * @author Faizun
 */
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

public class  Encoding  {
    public static synchronized String encrypt(String pass) throws Exception {
        String hashValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes("UTF-8"));
            byte bt[] = md.digest();
            hashValue = (new BASE64Encoder()).encode(bt);
        } catch(Exception e) {
            System.out.println("Something went wrong. Change your password.");
        }
        return hashValue;
    } 
    public String getHash(String str) throws Exception {
        String passwordToHash = encrypt(str);
        return passwordToHash;
    }
    
}