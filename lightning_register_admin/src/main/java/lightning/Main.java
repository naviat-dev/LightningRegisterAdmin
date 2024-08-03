package lightning;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.mysql.cj.jdbc.Driver;

class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // We will need to have passwords so that unauthorised people cannot screw things up
        ArrayList<String> validPasswords = new ArrayList<String>();
        Scanner getPasswords = new Scanner(new File("validId.txt"));
        while (getPasswords.hasNextLine()) {
            validPasswords.add(getPasswords.nextLine());
        }
        getPasswords.close();
    }
}