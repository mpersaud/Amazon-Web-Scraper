import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class urlProcessor extends Thread {


    public static String url_string = "https://www.amazon.com/dp/";
     static final String url_string_def = "https://www.amazon.com/dp/";
    private static String url_string_piece = "";

    //private static String url_string_upc ="";
        // final string of the USER_AGENT we are acting as
        public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        // this writes the URL Info to file
    //constructor
    public urlProcessor(String url_stringpeice) {
        url_string_piece= url_stringpeice;
        url_string = url_string+url_stringpeice;
    }

    public static BufferedImage fetchImageFromURL (URL url) {
        BufferedImage image = null;
        try {
            // Read from a URL
            image = ImageIO.read(url);
        } catch (IOException e) {
        } // catch

        return image;

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

    public static void parse(Storage database) throws Exception {
        URL url = new URL(url_string);
        BufferedReader br = read(url_string);

        String title = null;
        String s="";
        String author = null;
        String rating="";

        Pattern pattern = Pattern.compile("((<meta name=\\\"description\\\").*(Amazon\\.com))");

        Pattern pattern1 = Pattern.compile("(\\b(content\\=\")(.*)(\\[)((.*)\\]))");


        String line = br.readLine();

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

        sleep(100);
        Pattern product_pattern = Pattern.compile("(<li><b>)(.*)(:<\\/b>)(.*)(<\\/li>)");
        br = read(url_string);
        line = br.readLine();
        ArrayList<String > product_ary = new ArrayList<>();

        while (line!= null ){

            matcher = product_pattern.matcher(line);
            while (matcher.find()) {
                product_ary.add(matcher.group(2)+(matcher.group(4)));

            }
            line = br.readLine();
        }
        String publisher = null;
        String yearpublished = null;
        for(String e: product_ary){

            if(e.contains("Publisher")){
                publisher = e.split(";")[0].replace("Publisher","");
                yearpublished = e.split("\\(")[1];
            }
        }
        yearpublished =yearpublished.substring(0, yearpublished.length()-1);

        double price=0.0;
        sleep(100);
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

        }
        if(p!=null) {
            price = Double.parseDouble(p);
            //System.out.println("Price " + price);
        }
        sleep(100);
        ArrayList<String> x= new ArrayList<>();
        pattern = Pattern.compile("(<span class=\"olp-(.*) olp-link\">)",Pattern.DOTALL);

        Pattern p2 = Pattern.compile("<a(.*)/gp/offer-listing/"+url_string_piece+"/",Pattern.DOTALL);
        Pattern p3 = Pattern.compile("(\\d*\\d+)(.*)(Used|New|Collectible)(.*)\\$(\\d+.\\d+)",Pattern.DOTALL);

        br = read(url_string);
        line = br.readLine();
        found =false;

        while (line!= null && found == false){


            matcher = pattern.matcher(line);
            if(matcher.find()){

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

            line = br.readLine();
        }

        for (String e : x){
            //System.out.println(e);
        }
        Pattern p5 = Pattern.compile("\\=\"\\d.+stars",Pattern.DOTALL);
        br = read(url_string);
        line = br.readLine();
        found =false;
        String q = "";

        while (line!= null && found == false){


            matcher = p5.matcher(line);
            if(matcher.find()){

                q= matcher.group();


            }

            line = br.readLine();
        }
        rating=q.substring(2);


        Pattern p6 = Pattern.compile("id=\"imgBlkFront\"");
        br = read(url_string);
        line = br.readLine();
        found =false;
        String l = "";
        String t ="";
        while (line!= null && found == false ){

            Matcher matcher3 = p6.matcher(line);
            if(matcher3.find()){

                l=line;
                break;

            }
            line = br.readLine();
        }
        t = l.split("&quot;")[1];


        if(database.table.containsKey(url_string_piece)){
            database.modify((url_string_piece),new Bookitem(url_string_piece,author,yearpublished,publisher,title,price,rating));
        }
        else{
            database.insert((url_string_piece),new Bookitem(url_string_piece,author,yearpublished,publisher,title,price,rating));
        }
        URL url_image = new URL(t);
        //System.out.println(url_image.toString());
        BufferedImage image ;
        image = fetchImageFromURL(url_image);
        ImageIcon icon=new ImageIcon(image);
        database.table.get(url_string_piece).icon = icon;
        url_string=url_string_def;
    }

}
