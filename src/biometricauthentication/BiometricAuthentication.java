/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthentication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.abs;

/**
 *
 * @author Sugirjan
 */
public class BiometricAuthentication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LogIn.run();
    }

    static String register(String username, int[] values) {
        
        Boolean valuesStatus = validateValues(values);
        if (!valuesStatus) {
            return "your inputs are wrong";
        }
        
        Boolean usernameStatus = ValidateUserName(username);
        if (!usernameStatus) {
            return "user name is already exist";
        }

        String measurements = "";
        for (int integer : values) {
            measurements += String.valueOf(integer);
            measurements += " ";
        }
        
        String data = (username + " " + measurements);

 
            try (FileWriter fw = new FileWriter("data.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw)) {
                pw.println(data);
                return "Successfuly registered!";
            } catch (IOException e) {
                System.out.println(e);
                return "Failed to register!";
            }
    }

    static String authenticate(String username, int[] array) {
        if (username.length() > 0 && validateValues(array)) {
            try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] test = line.split(" ");
                    if (test[0].equalsIgnoreCase(username)) {
                        return checkValues(test, array);
                    } else {
                        return "user name is incorrect";
                    }
                }
            } catch (IOException ex) {
                return "No one have registered";
            }
        }
        return "user name or values not valid";
    }

    private static Boolean validateValues(int[] values) {
        for (int i : values) {
            if (30 > i || i > 150) {
                return false;
            }
        }
        return true;
    }

    private static Boolean ValidateUserName(String username) {
        if (username.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] test = line.split(" ");
                    if (test[0].equalsIgnoreCase(username)) {
                        return false;
                    }
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        } else {
            return false;
        }

    }

    private static String checkValues(String[] test, int[] array) {
        int[] values = new int[5];
        for (int i = 0; i < 5; i++) {
            values[i] = Integer.parseInt(test[i + 1]);
            if (((abs(values[i] - array[i]) * 100.00) / array[i] > 8)) {
                return "recheck your values";
            }
        }
        return "Successfully logged in";
    }

}
