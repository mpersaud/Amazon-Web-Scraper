
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


/**
 * Created by mike on 10/12/16.
 */

//GUI that pops on screen to display buttons and list of inventory
public class GUI extends JFrame  {
    //default list models is needed to automattically pull into JList
    DefaultListModel<String> ary = new DefaultListModel<>();
    JList<String> list = new JList<>();
    Storage database;
    final JFileChooser fc = new JFileChooser();
    String url_report="";




    public class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String str = e.getActionCommand();

            //should use switch statement instead
            if(str=="Insert"){

                insert();
                /*
                try {
                    driver.serialize(database);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                */
            }
            else if (str=="Modify"){
                modify();
                /*
                try {
                    driver.serialize(database);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                */
            }
            else if(str=="Delete"){
                delete();
                /*
                try {
                    driver.serialize(database);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                */
            }
            else if(str=="Query"){
                query();
                /*
                try {
                    driver.serialize(database);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                */

            }
            else if(str=="Search"){
                search();

            }
            else if(str=="Insert File"){
                insertFile();

            }
            else if(str=="Report"){
                report();
            }
            else if(str=="Full Report"){
                fullreport();
            }

        }
    }

    private void report() {
        String s = JOptionPane.showInputDialog("Insert file name");

        database.writeReportFile(url_report,s);
    }
    private void fullreport() {
        String s = JOptionPane.showInputDialog("Insert output file name");

        database.writeFullReportFile(s);
    }

    private void search() {
        ///add part if already in database

        String url_isbn = JOptionPane.showInputDialog("Enter ISBN");
        if(url_isbn.isEmpty())return;
        if(!url_isbn.matches("\\w{10}"))return;
        urlProcessor url= new urlProcessor(url_isbn);
        try {
            url.parse(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        url_report=url_isbn;
        urlProcessor.url_string=urlProcessor.url_string_def;
        sort();
    }

    //constructor for GUI class
    public GUI(Storage d, int param) throws IOException {

        //pack();
        //super();

        database=d;
        //change later
        setTitle("Media Evaluation Project");

        //opens a panel to output data too
        JPanel panel = new JPanel();
        //sets size and adds 3 buttons with functionality
        setSize(800,250);
        ButtonHandler btn_handl = new ButtonHandler();
        JButton b1 = new JButton("Insert");
        b1.addActionListener(btn_handl);
        JButton b2 = new JButton("Modify");
        b2.addActionListener(btn_handl);
        JButton b3 = new JButton("Delete");
        b3.addActionListener(btn_handl);
        JButton b4 = new JButton("Query");
        b4.addActionListener(btn_handl);
        JButton b5 = new JButton("Search");
        b5.addActionListener(btn_handl);
        JButton b6 = new JButton("Insert File");
        b6.addActionListener(btn_handl);
        JButton b7 = new JButton("Report");
        b7.addActionListener(btn_handl);
        JButton b8 = new JButton("Full Report");
        b8.addActionListener(btn_handl);


        /*
        We want to display the Bookitem names and not its unqiue identifier therefoe we must get
        the names of each Bookitem and then sort it to be presentable in list
         */
        Collection<Bookitem> valueSet=database.table.values();
        ArrayList<String > ar = new ArrayList<>();
        Iterator<Bookitem> val_iter=valueSet.iterator();

        while(val_iter.hasNext()){
            //iterates the value set and returns the names
             ar.add(val_iter.next().getIsbn());
        }
        //sorts names
        Collections.sort(ar);
        //adds to display array
        for(String e:ar){
            ary.addElement(e);
        }
        //adds to display array
        list = new JList<>(ary);

        //adding a scroll to the list and giving the GUI its looks
        // set layout,etc, and headline
        JTable table = new JTable(3,3);
        //table.set
        JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.setLayout(new FlowLayout());
        panel.add(b1);panel.add(b2);panel.add(b3);panel.add(b4);panel.add(b5);panel.add(b6);panel.add(b7);panel.add(b8);
        setLayout(new BorderLayout());
        JLabel headline = new JLabel("Media List(ISBNS)");
        //adding to GUI and then displaying
        add(headline, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        //add(table, BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Object[] options = {"Yes, please",
                        "No, thanks"
                };
                int n = JOptionPane.showOptionDialog(null,
                        "Program Ending: Would You like to save Database?",
                        "Message",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);

                if(n==0) {
                    try {
                        driver.serialize(database);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        setVisible(true);


    }
    public void sort(){
        //clears out list ary
        ary.clear();
        //grabs valueSet and then makes helper arraylist and iterates value returning the names
        // then sorts the list and puts back into display_ary
        Collection<Bookitem>valueSet=database.table.values();
        ArrayList<String > ar = new ArrayList<>();

        Iterator<Bookitem> val_iter=valueSet.iterator();

        while(val_iter.hasNext()){
            //iterates the value set and returns the names
            ar.add(val_iter.next().getIsbn());
        }
        //sorts names
        Collections.sort(ar);
        //adds to display array
        for(String e:ar){
            ary.addElement(e);
        }

    }
    public void modify(){

        //input all parameters
        String id= (JOptionPane.showInputDialog("Input ISBN"));
        String firstName= (JOptionPane.showInputDialog("Input Author's FirstName"));
        String lasttName= (JOptionPane.showInputDialog("Input Author's LastName"));
        String yearpublished = (JOptionPane.showInputDialog("Year Published"));
        String publisher = (JOptionPane.showInputDialog("Enter Publisher"));
        String name= (JOptionPane.showInputDialog("Input Bookname"));
        double price= Double.parseDouble(JOptionPane.showInputDialog("Input Price"));
        //if it doesnt contain do you want to add?
        if(!database.table.containsKey(id)){
            Object[] options = {"Yes, please",
                    "No, thanks"
            };
            int n = JOptionPane.showOptionDialog(getContentPane(),
                    "ISBN not in database: Add it?",
                    "Message",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if(n==0) {
                database.insert(id,new Bookitem(id,firstName,yearpublished,publisher,name,price));
                ary.addElement(name);
            }
            return;
        }
        //else if it exits,just modify
        database.modify(id, new Bookitem(id, firstName,yearpublished,publisher,name,price));
        sort();

    }
    public void delete(){
        //if not in database dont add

        if(database.isEmpty())return;
        ///input name
        String name= JOptionPane.showInputDialog("Input ISBN");
        //traverses the values to find name and then removes it from both database and ary
        Collection<Bookitem>valueSet=database.table.values();
        Iterator<Bookitem> iter=valueSet.iterator();
        while(iter.hasNext()){

            Bookitem a = iter.next();
            if(a.getIsbn().equals(name)){
                database.delete(a.getIsbn());
                ary.removeElement(a.getIsbn());
                return;

            }

        }
        //if not in database, just ooutput
        JOptionPane.showMessageDialog(getContentPane(), "not in database");

    }
    public void query(){

        String name= (JOptionPane.showInputDialog("Input ISBN"));
        //gets all names from database, can have multiple names so if one is found
        // then continue to looks through database
        Collection<Bookitem>i=database.table.values();
        Iterator<Bookitem> iter=i.iterator();
        boolean found= false;
        while(iter.hasNext()){

            Bookitem a = iter.next();
            if(a.getIsbn().equals(name)){
                JOptionPane.showMessageDialog(getContentPane(),a.toString());
                found=true;
                JFrame frame=new JFrame();
                frame.setLayout(new FlowLayout());
                frame.setSize(600,600);
                JLabel lbl=new JLabel();
                lbl.setIcon(database.table.get(a.getIsbn()).icon);
                frame.add(lbl);
                frame.setVisible(true);
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }


        }
        //if no name matches then output
        if(!found)JOptionPane.showMessageDialog(getContentPane(), "not in database");

    }
    public void insert(){
        /*
                Adding in new parameters for the table

        */

        String id= (JOptionPane.showInputDialog("Input ISBN"));
        if(database.table.containsKey(id)){
            Object[] options = {"Yes, please",
                    "No, thanks"
            };
            int n = JOptionPane.showOptionDialog(getContentPane(),
                    "ISBN exists: Add to Quantity?",
                    "Message",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if(n==0) {
                database.table.get(id).quantity++;
                sort();
            }
            return;
        }
        String firstName= (JOptionPane.showInputDialog("Input Author's Name"));
        //String lasttName= (JOptionPane.showInputDialog("Input Author's LastName"));
        String yearpublished = (JOptionPane.showInputDialog("Year Published"));
        String publisher = (JOptionPane.showInputDialog("Enter Publisher"));
        String name= (JOptionPane.showInputDialog("Input Bookname"));
        double price= Double.parseDouble(JOptionPane.showInputDialog("Input Price"));
        //if key is already in table see if want to ovveride


        //else just insert regular
        database.insert(id,new Bookitem(id,firstName,yearpublished,publisher,name,price));
        sort();

    }
    public void insertFile(){
        //Add part if in database already
        fc.showOpenDialog(getContentPane());
        File file = fc.getSelectedFile();
        String line="";
        BufferedReader br = null;
        if(!file.toString().contains(".txt"))return;
        try {
            br = new BufferedReader(new FileReader(file));

            line = br.readLine();
            while(line!=null){
                if(database.table.containsKey(line)){
                    database.table.get(line).quantity++;

                }
                else {
                    database.insert(line, new Bookitem(line));
                    sort();
                }
                urlProcessor url = new urlProcessor(line);
                url.parse(database);

                line=br.readLine();

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        sort();
    }
}

