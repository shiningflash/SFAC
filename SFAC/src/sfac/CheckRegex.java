/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfac;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Faizun
 */
public class CheckRegex {

    static Pattern p1 = Pattern.compile("[A-Za-z0-9_%+-]");
    static Pattern p2 = Pattern.compile("[@]");

    static Pattern p3 = Pattern.compile("[A-Z]");
    static Pattern p4 = Pattern.compile("[a-z]");
    static Pattern p5 = Pattern.compile("[0-9]");

    public static boolean checkEmail(String str, int num) {
        int state = 0;
        for (int x = 0; x < str.length(); x++) {
            char c = str.charAt(x);
            String ch = String.valueOf(c);
            Matcher m1 = p1.matcher(ch);
            Matcher m2 = p2.matcher(ch);

            if ((state == 0) && (m1.find())) {
                state = 1;
            } else if ((state == 1) && (m1.find())) {
                state = 1;
            } else if ((state == 1) && (m2.find())) {
                state = 2;
            } else if ((state == 2) && (num == 1) && ((str.endsWith("@bracu.ac.bd")))) {
                state = 3;
            } else if ((state == 2) && (num == 2) && (((str.endsWith("gmail.com"))) || ((str.endsWith("yahoo.com"))) || ((str.endsWith("live.com"))))) {
                state = 3;
            } else if ((state == 2) && (num == 3) && (((str.endsWith("@g.bracu.ac.bd"))))) {
                state = 3;
            }
        }
        if (state == 3) return true;
        else return false;
    }

    public static boolean checkPassword(String str) {
        int state = 0;
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            String ch = String.valueOf(c);
            Matcher m3 = p3.matcher(ch);
            if (m3.find()) {
                state++;
                break;
            }
        }
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            String ch = String.valueOf(c);
            Matcher m4 = p4.matcher(ch);
            if (m4.find()) {
                state++;
                break;
            }
        }
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            String ch = String.valueOf(c);
            Matcher m5 = p5.matcher(ch);
            if (m5.find()) {
                state++;
                break;
            }
        }
        if(state==3) return true;
        return false;
    }
}
