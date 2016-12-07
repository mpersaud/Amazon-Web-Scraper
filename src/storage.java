
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by mike on 10/11/16.
 */
public class storage implements Serializable {
    //storage classes that holds all the data
    //using a hashtable to store primary key and value Sets
     Hashtable<String, item> table = new Hashtable<>();
    //files to write logs to
    String output_file;

    //constructor
    public storage(String o){
        output_file=o;
        try {
            new FileWriter(new File(output_file),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //constructors
    public storage(String input_file, String output_file) throws IOException, ParseException {
            this.output_file=output_file;

        //reads in inputfile
        Scanner scan = new Scanner(new File(input_file));
         new FileWriter(new File(output_file), false);
        //scanning in all attributes, creating new item and put item in table
        while(scan.hasNext()){


            StringTokenizer st = new StringTokenizer(scan.nextLine());
            String isbn = st.nextToken();
            /*
            String author_firstname = st.nextToken();
            String author_lastname = st.nextToken();
            //DateFormat df = new DateFormat();
            //df.parse(scan.next());
            String year_published= st.nextToken();
            String publisher= st.nextToken();
            String title = st.nextToken();
            double price = Double.parseDouble(st.nextToken());
            */

            item i = new item(isbn);
            table.put(isbn,i);

            //writing log
            writetoFile("INSERT",isbn,i);

        }

    }

    public storage() {

    }
    //insert method
    public void insert(String k, item val){
        String s = "INSERT";
        //writing to file and inputting to table
        writetoFile(s,k,val);
        table.put(k, val);

    }
    //modifiy exisiting data
    public void modify(String k, item val){
        String s = "MODIFY";
        //write to file and then replace value at id
        writetoFile(s,k,val);
        table.replace(k, val);


    }
    //deleting data
    public void delete(String k){
        String s = "DELETE";
        //using the key write to file and then take out of table
        writetoFile(s,k,table.get(k));
        table.remove(k);
    }

    //check if empty
    public boolean isEmpty(){

        return table.isEmpty();

    }
    //writing log to file
    public void writetoFile(String identifier,String k, item val){
        //open output file and enter in change idfentifier, the key, and the value
        try {
            FileWriter writer = new FileWriter(new File(output_file),true);
            Date dat = new Date();

            writer.write(identifier+" "+k+" "+val+" "+dat);
            writer.write(System.lineSeparator());
            writer.close();
            //close writer and catch exception if needed
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
