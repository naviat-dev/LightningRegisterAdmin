package lightning;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> validPasswords = new ArrayList<String>();
        Scanner getPasswords = new Scanner(new File("validId.txt"));
        while (getPasswords.hasNextLine()) {
            validPasswords.add(getPasswords.nextLine());
        }
        getPasswords.close();
    }
}