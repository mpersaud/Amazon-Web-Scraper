

import java.io.*;
import java.net.URL;
import java.text.ParseException;


/**
 * Created by mike on 10/11/16.
 */
public class driver  {
        // driver class for program which loads the GUI and initliazes the data base

    public static void serialize(storage database) throws IOException {
        // Write to disk with FileOutputStream
        FileOutputStream f_out = new
                FileOutputStream("myobject.data");

// Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new
                ObjectOutputStream (f_out);

// Write object out to disk
         obj_out.writeObject (database );
    }
        //holds main function to start program
    public static void main (String args[]) throws IOException, ParseException {
        //add code

        //checks parameters and intializes the input/output file
        int param = args.length;
        String input_file;
        String output_file;
        storage database = null;
        /*
        //checks if arguements are present
        // if not exit program
        if(param<2) {
            input_file=null;
            output_file=null;
            database = new storage();
            System.out.println("No input/output file arguements");
            System.exit(0);
        }
        else {
            //create new database through input/output
            input_file = args[0];
            output_file = args[1];
            database = new storage(input_file,output_file);
        }


        FileInputStream fis = new FileInputStream("myobject.data");
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            database = (storage) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        database = new storage(args[1]);
        System.out.println();
        //System.out.println("Running");
        //creates display screen
         gui myGUI= new gui(database,param);
         System.out.println("Success");
        /*
        String url_string = "https://www.amazon.com/dp/";
        //url_string = url_string +"1338099132";
        //url_string = url_string +"0553103547";
        url_string = url_string +"0425288293";
        System.out.println(url_string);

        urlProcessor url= new urlProcessor("043935806X");
        try {
            url.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */


    }


}
