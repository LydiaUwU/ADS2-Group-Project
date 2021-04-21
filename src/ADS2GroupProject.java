import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

//this is the main class for now

class ADS2GroupProject {
    public static void main(String[] args) {
        try {
            ArrayList<Transfer> transfers = new ArrayList<Transfer>();

            /*the file opening code assumes you have the input files in a folder called "inputs" that is in the project
            direcory */
            Scanner transferScanner = new Scanner(new File("inputs/transfers.txt"));

            int i = 0;
            while (i < 10) {
                System.out.println(transferScanner.nextLine());
                transfers.add(new Transfer(1000,1001,2,300));
                i++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
