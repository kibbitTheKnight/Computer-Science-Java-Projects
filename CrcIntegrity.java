import java.util.ArrayList;
/*
 * University of Central Florida
 * CIS 3360 - Fall 2017
 * Author: Keira Taylor
 */
public class CrcIntegrity
{
    public static void main(String [] args)
    {
        //declare and initialize variables
        String modeLetter = args[0];
        String mode = "";
        String message = args[1];
        //turn mode into correct string
        if(modeLetter.equals("c"))
        {
            mode = "calculation";
        }
        else
        {
            mode = "verification";
        }
        
        //declare and initialize constants
        final String polynomial = "x12x11x08x07x05x04x02x00";
        final int crcSize = 12;
        final int verifyHex = 3;
        
        //declare and initialize ArrayLists
        ArrayList<Integer> poly = convertPoly(polynomial);

        ArrayList<Integer> hex = convertHexToBin(message, crcSize);
        
        ArrayList<Integer> remainder = new ArrayList<>();
        
        //print out header
        System.out.println("--------------------------------------------------------------");
        System.out.println("CIS3360 Fall 2017 Integriy Checking Using CRC");
        System.out.println("Author: Keira Taylor\n");
        
        //print out information
        System.out.println("The input string (hex): " + message);
        System.out.print("The input string (bin): ");
        printBin(convertHexToBin(message, 0), "\n\n");
        
        System.out.print("The polynomial used (binary bit string): ");
        printBin(poly, "\n\n");
        
        System.out.println("Mode of operation: " + mode);
        
        //modes
        if(modeLetter.equals("c"))
        {
            //print out information
            System.out.println("\nNumber of zeroes that will be appended to the binary input: " 
                                + crcSize);
            System.out.println("\nThe binary string difference after each XOR step of the CRC " +
                                "calculation\n");
                                
            //calculate remainder
            remainder = divide(poly, hex, true);
            
            //print out final answers
            System.out.print("\nThe CRC computed from the input: "); 
            printBin(remainder, crcSize, "");
            System.out.print("(bin) = " + convertBinToHex(remainder, crcSize) + " (hex)");
        }
        else
        {
            //declare and intialize variables
            String crc = message.substring(message.length() - verifyHex);
            String leftCrc = message.substring(0, (message.length() - verifyHex));
            ArrayList<Integer> crcBin = convertHexToBin(crc, 0);
            ArrayList<Integer> leftCrcBin = convertHexToBin(leftCrc, crcSize);
            ArrayList<Integer> testResult = new ArrayList<>();
            
            //print results
            System.out.print("\nThe CRC observed at the end of the input: ");
            printBin(crcBin, "");
            System.out.println("(bin) = " + crc + " (hex)\n");
            
            System.out.println("The binary string difference after each XOR " +
                               "step of the CRC calculation:\n");
                               
            ArrayList<Integer> messageBin = convertHexToBin(message, 0);
                               
            //calculate results from dividing the polynomials
            //actual remainder
            testResult = divide(poly, messageBin, true);
            //correct remainder
            remainder = divide(poly, leftCrcBin, false);
            
            //print out final results
            System.out.print("\nThe CRC computed from the input: ");
            printBin(remainder, crcSize, "");
            System.out.println("(bin) = " + convertBinToHex(remainder, crcSize) + " (hex)\n");
            System.out.print("Did the CRC check pass? (Yes or No): ");
            //determine if the CRC check passed
            if(isAllZero(testResult))
            {
                System.out.print("Yes");
            }
            else
            {
                System.out.print("No");
            }
        }
    }
    //converts hex numbers into binary
    public static ArrayList<Integer> convertHexToBin(String hex, int pad)
    {
        //declare and initialize variables
        int length = hex.length();
        ArrayList<Integer> bin = new ArrayList<Integer>();
        //begin loop
        while(hex.length() > 0)
        {
            //convert hex into series of integers
            char lastNumber = hex.charAt(hex.length() - 1);
            int num = 0;
            switch(lastNumber)
            {
                case 'A': num = 10;
                          break;
                case 'B': num = 11;
                          break;
                case 'C': num = 12;
                          break;
                case 'D': num = 13;
                          break;
                case 'E': num = 14;
                          break;
                case 'F': num = 15;
                          break;
                default: num = Character.getNumericValue(lastNumber);
            }
            //convert integers in binary
            
            //represent a possible 0 in hex
            if(num == 0)
            {
                for(int i = 0; i < 4; i++)
                {
                    bin.add(0, 0);
                }
            }
            
            //represent all non-zero numbers in binary
            while(num > 0)
            {   
                bin.add(0, num%2);
                num /= 2;
                length--;
            }
            //add 0's to make 4-bit
            while(bin.size()%4 > 0)
            {
                bin.add(0, 0);
            }
            hex = hex.substring(0, hex.length() - 1);
        }
        //add padded digits
        for(int i = 0; i < pad; i++)
        {
            bin.add(0);
        }
        //return result
        return bin;
    }
    //converts binary to hex
    public static String convertBinToHex(ArrayList<Integer> bin, int length)
    {
        //declare and initialize variables
        int counter = bin.size() - 1;
        String hex = "";
        //begin loop
        while(counter >= length)
        {
            //reset num
            int num = 0;
            //add powers of two to four-bit numbers to turn hex into binary
            for(int i = 0; i < 4; i++)
            {
                num += java.lang.Math.pow(2, i) * bin.get(counter);
                counter--;
            }
            //turn numbers higher than 10 into letters
            switch(num)
            {
                case 10: hex = "A" + hex;
                         break;
                case 11: hex = "B" + hex;
                         break;  
                case 12: hex = "C" + hex;
                         break;
                case 13: hex = "D" + hex;
                         break;  
                case 14: hex = "E" + hex;
                         break;
                case 15: hex = "F" + hex;
                         break;
                default: hex = num + hex;
                         break;
            }
        }
        //return result
        return hex;
    }
    //converts strings representing polynomials into their binary representations
    public static ArrayList<Integer> convertPoly(String polynomial)
    {
        //declare and initialize variables
        int exp = 0;
        String power = "";
        ArrayList<Integer> bin = new ArrayList<Integer>();
        //begin loop
        while(polynomial.length() > 0)
        {
            //if the number next to the current x equals the exponent, represent it
            //with a 1 in bin
            //else, represent it with a 0
            if(Integer.parseInt(polynomial.substring(polynomial.length() - 2)) == exp)
            {
                bin.add(0, 1);
                polynomial = polynomial.substring(0, polynomial.length() - 3);
            }
            else
            {
                bin.add(0, 0);
            }
            //increment exp
            exp++;
        }
        //return result
        return bin;
    }
    //divides two binary numbers represented in ArrayLists and return their remainder as another
    //binary number represented as an ArrayList
    public static ArrayList<Integer> divide(ArrayList<Integer> divisor, ArrayList<Integer> dividend,
                                            boolean printResults)
    {
       //declare and initialize variables
       ArrayList<Integer> currentBlock = new ArrayList<Integer>();
       ArrayList<Integer> remainder = new ArrayList<Integer>();
       ArrayList<Integer> prevLine = new ArrayList<Integer>(dividend);
       int index = 0;
       int dividendSize = dividend.size();
       
       //begin division
       while(!isBigger(divisor, prevLine))
       {
           //reset index
           index = 0;
           
           //print prevLine
           if(printResults)
           {
               printBin(prevLine, "\n");
           }
           
           //check to see if divisor can go into prevLine (dividend)
           while(index < prevLine.size() && prevLine.get(index) == 0)
           {
               //add zeroes to front of currentBlock(divisor)
               currentBlock.add(0, 0);
               //increment index
               index++;
           }
           //add divisor
           for(int i = 0; i < divisor.size(); i++)
           {
               currentBlock.add(divisor.get(i));
           }
           //pad with zeroes
           for(int i = currentBlock.size(); i < dividendSize; i++)
           {
               currentBlock.add(0);
           }
           
           //xor dividend and divisor and store in prevLine
           prevLine = xorLines(currentBlock, prevLine);
           
           //reset currentBlock
           currentBlock.removeAll(currentBlock);
       }
       //set current remainder as prevLine
       remainder = prevLine;
       
       //print remainder
       if(printResults)
       {
           printBin(remainder, "\n");
       }
       
       //return result
       return remainder;
    }
    //performs the XOR operation on two binary numbers
    public static ArrayList<Integer> xorLines(ArrayList<Integer> currentBlock,
    ArrayList<Integer> prevLine)
    {
        //declare and initialize variables
        int size = prevLine.size();
        ArrayList<Integer> result = new ArrayList<>();
        
        //for each bit, XOR the bits in both arrays at index i
        for(int i = 0; i < size; i++)
        {
            result.add(currentBlock.get(i) ^ prevLine.get(i));
        }
        
        //return result
        return result;
    }
    //determines if ArrayList a has a higher indexed most significant bit than b
    public static boolean isBigger(ArrayList<Integer> a, ArrayList<Integer> b)
    {
        //declare and initialize variables
        int msbA = 0;
        int msbB = 0;
        
        //loop through a
        for(int i = 0; i < a.size(); i++)
        {
            //exit the loop when the most significant bit is found
            //store the index in msbA
            if(a.get(i) == 1)
            {
                msbA = a.size() - i;
                break;
            }
        }
        //loop through b
        for(int i = 0; i < b.size(); i++)
        {
            //exit the loop when the most significant bit is found
            //store the index in msbB
            if(b.get(i) == 1)
            {
                msbB = b.size() - i;
                break;
            }
        }
        
        //if msbA is higher than msbB, return true
        //else, return false
        return msbA > msbB ? true : false;
    }
    //prints a binary number stored in an ArrayList with spaces after every four bits
    //takes a string formatting as input to add specific formatting to the end of it
    public static void printBin(ArrayList<Integer> bin, String formatting)
    {
        //declare and initialize variable
        int counter = 0;
        
        //loop through each bit in bin
        while(counter < bin.size())
        {
            //print out every four bits, incrementing the counter after each bit
            for(int i = 0; i < 4; i++)
            {
                if(counter < bin.size())
                {
                    System.out.print(bin.get(counter));
                    counter++;
                }
            }
            //print out a space for readability
            System.out.print(" ");
        }
        //print out formatting
        System.out.print(formatting);
        
        //return nothing
        return;
    }
    //determines if the binary number is all zeros
    public static boolean isAllZero(ArrayList<Integer> bin)
    {
        //delcare and initialize variable
        boolean allZero = true;
        
        //loop through each number in bin
        for(int num : bin)
        {
            //if the number is not zero, set allZero to false and exit the loop
            if(num > 0)
            {
                allZero = false;
                break;
            }
        }
        
        //return the result
        return allZero;
    }
    //performs the same action as the previous printBin method
    //allows for only printing out the last "length" digits in the ArrayList
    public static void printBin(ArrayList<Integer> bin, int length, String formatting)
    {
        //declare and initialize variable
        int counter = bin.size() - length;
        //loops through bin from (bin.size() - length) to length
        while(counter < bin.size())
        {
            //prints out four digits, incrementing the counter for each digit
            for(int i = 0; i < 4; i++)
            {
                if(counter < bin.size())
                {
                    System.out.print(bin.get(counter));
                    counter++;
                }
            }
            //prints out a space
            System.out.print(" ");
        }
        //prints out formatting
        System.out.print(formatting);
        
        //returns nothing
        return;
    }
}
