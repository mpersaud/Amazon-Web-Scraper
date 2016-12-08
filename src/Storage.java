
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * Created by mike on 10/11/16.
 */
public class Storage implements Serializable {
    //Storage classes that holds all the data
    //using a hashtable to store primary key and value Sets
     Hashtable<String, Bookitem> table = new Hashtable<>();
    //files to write logs to
    String output_file;

    //constructor
    public Storage(String o){
        output_file=o;
        try {
            new FileWriter(new File(output_file),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //constructors
    public Storage(String input_file, String output_file) throws IOException, ParseException {
            this.output_file=output_file;

        //reads in inputfile
        Scanner scan = new Scanner(new File(input_file));
         new FileWriter(new File(output_file), false);
        //scanning in all attributes, creating new Bookitem and put Bookitem in table
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

            Bookitem i = new Bookitem(isbn);
            table.put(isbn,i);

            //writing log
            writetoFile("INSERT",isbn,i);

        }

    }

    public Storage() {

    }
    //insert method
    public void insert(String k, Bookitem val){
        String s = "INSERT";
        //writing to file and inputting to table
        writetoFile(s,k,val);
        table.put(k, val);

    }
    //modifiy exisiting data
    public void modify(String k, Bookitem val){
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
    public void writetoFile(String identifier,String k, Bookitem val){
        //open output file and enter in change idfentifier, the key, and the value
        try {
            FileWriter writer = new FileWriter(new File(output_file),true);
            Date dat = new Date();

            writer.write(identifier+" "+k);
            writer.write(System.lineSeparator());
            writer.write(""+val);
            writer.write("Date: "+dat);
            writer.write(System.lineSeparator());
            writer.write("----------------------------------------------------------------------------------------------------");
            writer.write(System.lineSeparator());
            writer.close();
            //close writer and catch exception if needed
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeReportFile(String k, String filename){
        if(!filename.endsWith(".txt"))filename = filename+".txt";
        try {
            FileWriter writer = new FileWriter(new File(filename),false);
            Bookitem i = table.get(k);

            writer.write("ISBN|Title|Author|Publisher|YearPublished|Price|Quantity|");
            writer.write(System.lineSeparator());
            writer.write(i.isbn+"|"+i.title+"|"+i.author+"|"+i.publisher+"|"+i.year_published+"|"+i.price+"|"+i.quantity+"|"+i.rating);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeFullReportFile(String filename){
        if(!filename.endsWith(".txt"))filename = filename+".txt";
        try {
            FileWriter writer = new FileWriter(new File(filename),false);
            writer.write("ISBN|Title|Author|Publisher|YearPublished|Price|Quantity|");
            Enumeration<String> keys= table.keys();
            String key;
            while(keys.hasMoreElements()){
                key=keys.nextElement();
                Bookitem i = table.get(key);
                writer.write(System.lineSeparator());
                writer.write(i.isbn+"|"+i.title+"|"+i.author+"|"+i.publisher+"|"+i.year_published+"|"+i.price+"|"+i.quantity+"|"+i.rating);

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
