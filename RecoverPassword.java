import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * University of Central Florida
 * CIS 3360 - Fall 2017
 * Author: Keira Taylor
 */
public class RecoverPassword
{
    private final static int LEFT_HASH_NUM = 243;
    private final static long MOD_HASH_NUM = 85767489;
    private static String hashValue = "";
    private static long numCombos = 0;
    private static int correctSalt = -1;
    //main class
    public static void main(String [] args)
    {
        //take in arguments
        String fileName = args[0];
        hashValue = args[1];
        String extension = "";
        
        //if fileName does not include an extension, add one
        if(fileName.length() > 4)
        {
            extension = fileName.substring(fileName.length() - 4);
        }
        if(!(extension.equals(".txt")))
        {
            extension = fileName + ".txt";
        }
        else
        {
            extension = fileName;
        }
        //print header
        printHeader(fileName, hashValue);
        
        //read dictionary
        String correctPass = readFile(extension);
        
        //password was recovered
        if(correctSalt > -1)
        {
            //print out results
            System.out.println("\nPassword recovered:");
            System.out.printf("\t%-19s : %s%n", "Password", correctPass);
            System.out.printf("\t%-19s : %s%n", "ASCII value", stringToNumber(correctPass));
            System.out.printf("\t%-19s : %s%n", "Salt value", createSalt(correctSalt));
            System.out.printf("\t%-19s : %d%n", "Combinations tested", numCombos);
        }
        //password was not recovered
        else
        {
            //print out results
            System.out.println("\nPassword not found in dictionary\n");
            System.out.println("Combinations tested: " + numCombos);
        }
    }
    //converts strings into ASCII-number equivalent
    public static String stringToNumber(String pass)
    {
        //create and initialize varaible
        String num = "";
        //add ASCII values to num
        while(pass.length() > 0)
        {
            num += (int)pass.charAt(0);
            pass = pass.substring(1);
        }
        //return num
        return num;
    }
    //adds salt to password
    public static String addSalt(String pass, String salt)
    {
        return salt + pass;
    }
    //computes hash value
    public static long hash(String pass)
    {
        //divide password into two parts
        long left = Long.parseLong(pass.substring(0, 7));
        long right = Long.parseLong(pass.substring(7));
        long computedHash = 0;
        
        //compute hash
        computedHash = (((LEFT_HASH_NUM * left) + right)%MOD_HASH_NUM);
        
        return computedHash;
    }
    public static String readFile(String fileName)
    {
        //declare and initialize varaibles
        String correctPass = "";
        File file = new File(fileName);
        //try to read from file
        try 
        {
            //declare and initialize variables
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            int c = 0;
            int index = 1;
            String pass = "";
            //go through each character
            while((c = br.read()) != -1)
            {
                //if the password is not yet 6 letters, add one more char
                if(pass.length() < 6)
                {
                    pass += (char)c;
                }
                else
                {
                    //check to seee if password matches the given hash
                    int salt = analyzePassword(pass, index);
                    //if the returned value is not negative, the password has been found
                    if(salt > -1)
                    {
                        correctSalt = salt;
                        correctPass = pass;
                    }
                    //reset password
                    pass = "";
                    //if c is not a newline character, add it to pass
                    if((char)c != '\n')
                    {
                        pass += (char)c;
                    }
                    //increment counter
                    index++;
                }
            }
            //get last password
            int salt = analyzePassword(pass, index);
            if(salt > -1)
            {
                 correctSalt = salt;
                 correctPass = pass;
            }
            br.close();
        } 
        catch ( Exception e ) 
        {
            e.printStackTrace();
        }
        
        return correctPass;
    }
    //returns the salt for a correct password. Returns -1 if no salt is correct
    public static int analyzePassword(String pass, int index)
    {
        //declare and initialize variables
        int salt = 0;
        
        String unsalted = stringToNumber(pass);
        
        //print password and unsalted ASCII version of it
        printLine(index, pass, unsalted);
        
        
        //try all salts
        for(salt = 0; salt < 1000; salt++)
        {
            String saltString = createSalt(salt);
            String salted = addSalt(unsalted, saltString);
            long hashed = hash(salted);
            //if the password has not yet been found, increase numCombos
            if(correctSalt < 0)
            {
                numCombos++;
            }
            //check to see if the password matches the given hash
            if(hashed == Long.parseLong(hashValue))
            {
                return salt;
            }
        }
        //if the password did not match the given hash, return -1
        return -1;
    }
    //converts the salt integer into a 3-digit string
    public static String createSalt(int salt)
    {
        String saltString = Integer.toString(salt);
        while(saltString.length() < 3)
        {
            saltString = "0" + saltString;
        }
        return saltString;
    }
    //prints out header
    public static void printHeader(String fileName, String hashValue)
    {
        int hyphenLength = 40;
        
        for(int i = 0; i < hyphenLength; i++)
        {
            System.out.print("-");
        }
        System.out.println("\n");
        System.out.println("CIS3360 Password Recovery by Keira Taylor\n");
        
        System.out.println("\tDictionary file name       : " + fileName);
        System.out.println("\tSalted password hash value : " + hashValue);
        
        System.out.println();
        
        System.out.printf("%-8s%-7s%s%n%n", "Index", "Word", "Unsalted ASCII equivalent");
    }
    //prints out a single line with the password, index, and the password's ASCII value
    public static void printLine(int index, String word, String numValue)
    {
        //if index is over 9999, the index column needs additional space
        if(index < 10000)
        {
            System.out.printf("%4d :  %-8s%s%n", index, word, numValue);
        }
        else
        {
            String indexColLength = "%" + Integer.toString(index).length() + "d";
            System.out.printf(indexColLength + " :  %-8s%s%n", index, word, numValue);
        }
    }
}
