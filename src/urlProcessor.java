import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class urlProcessor extends Thread {


    public static String url_string = "https://www.amazon.com/dp/";
     static final String url_string_def = "https://www.amazon.com/dp/";
    private static String url_string_piece = "";

    private static String url_string_upc ="";
        // final string of the USER_AGENT we are acting as
        public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        // this writes the URL Info to file
        public static void printURLinfo(URLConnection uc, BufferedWriter writer) throws IOException {
            // Display the URL address, and information about it.
            writer.write(uc.getURL().toExternalForm() + ":");
            writer.newLine();
            writer.write("  Content Type: " + uc.getContentType());
            writer.newLine();
            writer.write("  Content Length: " + uc.getContentLength());
            writer.newLine();
            writer.write("  Last Modified: " + new Date(uc.getLastModified()));
            writer.newLine();
            writer.write("  Expiration: " + uc.getExpiration());
            writer.newLine();
            writer.write("  Content Encoding: " + uc.getContentEncoding());
            writer.newLine();
            writer.newLine();


        } // printURLinfo

    public urlProcessor(String url_stringpeice) {
        url_string_piece= url_stringpeice;
        url_string = url_string+url_stringpeice;
    }

    // gets the URLStream according to the USER_AGENT
    public static InputStream getURLInputStream(String sURL) throws Exception {
            URLConnection oConnection = (new URL(sURL)).openConnection();
            oConnection.setRequestProperty("User-Agent", USER_AGENT);
            return oConnection.getInputStream();
        } // getURLInputStream

    //reads in the data from the new bufferReader with User_Agent
    public static BufferedReader read(String url) throws Exception {
            InputStream content = (InputStream) getURLInputStream(url);
            return new BufferedReader(new InputStreamReader(content));
        } // read


    //saves the image from an input URL
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {

        //used to keep the bytes in order and defined the destinationFile
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public static void parse(storage database) throws Exception {
        URL url = new URL(url_string);
        BufferedReader br = read(url_string);

        String title = null;
        String s="";
        String author = null;

        Pattern pattern = Pattern.compile("((<meta name=\\\"description\\\").*(Amazon\\.com))");

        Pattern pattern1 = Pattern.compile("(\\b(content\\=\")(.*)(\\[)((.*)\\]))");

       // BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        //String x = null;
        boolean found = false;

        while (line!= null && found == false){
            //System.out.println("x");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                s = matcher.group();
                found = true;
            }
            line = br.readLine();
        }
        Matcher matcher = pattern1.matcher(s);
        if(matcher.find()) {

            title = matcher.group(3);
            author = matcher.group(6);
        }
        else{
            found=false;
            Pattern pattern4 = Pattern.compile("((<meta name=\\\"title\\\").*(Amazon\\.com))");

            while(line!=null&& found==false){
                Matcher matcher4= pattern4.matcher(line);
                if(matcher4.find()){
                    found=true;
                    s=matcher4.group();
                }
                line =br.readLine();
            }
            String [] array = s.split(":");
            title = array[0].substring(28);
            author= array[1];
        }

        //System.out.println("Title: "+title);
        //System.out.println("Author: "+author);

        sleep(1000);
        Pattern product_pattern = Pattern.compile("(<li><b>)(.*)(:<\\/b>)(.*)(<\\/li>)");
        br = read(url_string);
        line = br.readLine();
        ArrayList<String > product_ary = new ArrayList<>();

        while (line!= null ){
            //System.out.println("x");
            matcher = product_pattern.matcher(line);
            while (matcher.find()) {
                product_ary.add(matcher.group(2)+(matcher.group(4)));

            }
            line = br.readLine();
        }
        String publisher = null;
        String yearpublished = null;
        for(String e: product_ary){
            //System.out.println(e);
            if(e.contains("Publisher")){
                publisher = e.split(";")[0];
                yearpublished = e.split("\\(")[1];
            }
        }
        //System.out.println(publisher);
        yearpublished =yearpublished.substring(0, yearpublished.length()-1);
        //System.out.println(yearpublished);

        double price = 0;
        sleep(1000);
        pattern = Pattern.compile("<span class=\"a-color-secondary\">List Price:</span>(.*)");
        br = read(url_string);
        line = br.readLine();
        found =false;
        while (line!= null && found == false){
            //System.out.println("x");
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                s = matcher.group();
                found = true;
            }
            line = br.readLine();
        }



        String p=null;
        Pattern onlyPrice = Pattern.compile("(\\d+.\\d+)");
        matcher =onlyPrice.matcher(s);
        while (matcher.find()) {
            p = matcher.group();
            found = true;
        }
        price = Double.parseDouble(p);
        System.out.println("Price "+price);

        sleep(1000);
        ArrayList<String> x= new ArrayList<>();
        pattern = Pattern.compile("(<span class=\"olp-(.*) olp-link\">)",Pattern.DOTALL);

        Pattern p2 = Pattern.compile("<a(.*)/gp/offer-listing/"+url_string_piece+"/",Pattern.DOTALL);
        Pattern p3 = Pattern.compile("(\\d*\\d+)(.*)(Used|New|Collectible)(.*)\\$(\\d+.\\d+)",Pattern.DOTALL);
        //BufferedReader br = new BufferedReader(new FileReader(new File("file.txt")));
        br = read(url_string);
        line = br.readLine();
        found =false;
        //String b="";

        //System.out.println(p3.pattern());

        while (line!= null && found == false){
            //System.out.println("x");

            matcher = pattern.matcher(line);
            if(matcher.find()){
                //System.out.println(matcher.group());


                line = br.readLine();
                Matcher m2 = p2.matcher(line);
                if(m2.find()){

                   br.readLine();
                    line = br.readLine();
                   Matcher m3 = p3.matcher(line);
                   if(m3.find()){
                       //System.out.println(line);
                       x.add("# of Books "+m3.group(1));
                       x.add(m3.group(3)+" "+m3.group(5));
                   }


                }

            }

            //System.out.println(line);
            line = br.readLine();
        }

        for (String e : x){
            System.out.println(e);
        }

        if(database.table.containsKey(url_string_piece)){
            database.modify((url_string_piece),new item(url_string_piece,author,yearpublished,publisher,title,price));
        }
        else{
            database.insert((url_string_piece),new item(url_string_piece,author,yearpublished,publisher,title,price));
        }

        url_string=url_string_def;
    }


}
