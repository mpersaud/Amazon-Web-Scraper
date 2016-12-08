

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;


/**
 * Created by mike on 10/11/16.
 */
public class driver  {
        // driver class for program which loads the GUI and initliazes the data base

    public static void serialize(Storage database) throws IOException {
        // Write to disk with FileOutputStream
        String s = JOptionPane.showInputDialog("enter filename");
        if(!s.endsWith(".data")) s = s+".data";
        FileOutputStream f_out = new
                FileOutputStream(s);

// Write object with ObjectOutputStream
        ObjectOutputStream obj_out = new
                ObjectOutputStream (f_out);

// Write object out to disk
         obj_out.writeObject (database );
    }
    public static Storage deserialize(String data) throws IOException {
        Storage database =null;
        FileInputStream fis = new FileInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            database = (Storage) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return database;
    }
        //holds main function to start program
    public static void main (String args[]) throws IOException, ParseException {
        //add code

        //checks parameters and intializes the input/output file
        int param = args.length;
        String input_file;
        String output_file;
        Storage database = null;
        String log="logfile.txt";

        //checks if arguements are present
        // if not exit program
        if(param==2) {
            System.out.println("Running...");
            input_file=args[0];
            output_file=args[1];
            BufferedReader br;
            String line ="";
            //String log="logfile.txt";
            //database = new Storage(input_file,log);
            database = new Storage(log);
            if(!input_file.toString().contains(".txt"))return;
            try {
                br = new BufferedReader(new FileReader(input_file));
                line = br.readLine();
                while(line!=null){
                    if(database.table.containsKey(line)){
                        database.table.get(line).quantity++;

                    }
                    else {
                        database.insert(line, new Bookitem(line));
                    }
                    urlProcessor url = new urlProcessor(line);
                    url.parse(database);
                    line=br.readLine();
                    System.out.println("Running...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //String s = JOptionPane.showInputDialog("Insert file name");

            database.writeFullReportFile(output_file);
            System.out.println("Success");


        }

        else {
            Object[] options = {"Yes, i do ",
                    "No, i dont"
            };
            int n = JOptionPane.showOptionDialog(null,
                    "Do you have a serialized database?",
                    "Message",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if(n==0) {
                String s = JOptionPane.showInputDialog("Insert file name");
                if(!s.endsWith(".data"))s=s+".data";
                File file = new File(s);

                if (file.exists()) {
                    System.out.println("the file directed does exist");
                    database=deserialize(s);
                }
                if (!file.exists()) {
                    System.out.println("the file directed does not exist");
                    //database=deserialize(s);
                    database = new Storage(log);
                }

            }
            else{
                database = new Storage(log);
            }


            System.out.println("Running");
            //creates display screen
            GUI myGUI= new GUI(database,param);
            System.out.println("Success");



        }





    }



}
