import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

//this is the main class for now

class ADS2GroupProject {
    public static void main(String[] args) {
        //everything is in a try-catch loop to handle FileNotFoundException
        try {
            ArrayList<Transfer> transfers = new ArrayList<Transfer>();

            /*
            all the file opening code assumes you have the input files in a folder called "inputs" that is in the project
            directory (not included in the repo as they would be too large to zip)
            */

            // ---------- getting transfers data ----------

            Scanner transferScanner = new Scanner(new File("inputs/transfers.txt"));

            //skip first line of transfers.txt as this is just the variable names
            transferScanner.nextLine();

            //reading lines from transfers.txt and putting them in an ArrayList of transfer objects
            while(transferScanner.hasNextLine()) {
                String[] currentLine = transferScanner.nextLine().trim().split(",");

                //if no value for minTransferTime is provided, set it to 0 for simplicity
                int time = 0;
                if (currentLine.length==4) time = Integer.parseInt(currentLine[3]);

                transfers.add(new Transfer(Integer.parseInt(currentLine[0]),
                        Integer.parseInt(currentLine[1]),Integer.parseInt(currentLine[2]),time));

            }

            // test graph

            String[] filenames = {"inputs/stops.txt","inputs/stop_times.txt","inputs/transfers.txt"};
            Graph testGraph = new Graph(filenames);

            /*//print statements to see if the data has been read correctly
            for (int i=0;i<transfers.size();i++) {
                System.out.println("transfers["+i+"] = "+transfers.get(i).fromStopId+","
                +transfers.get(i).toStopId+","+transfers.get(i).transferType+","
                +transfers.get(i).minTransferTime);
            }*/
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
