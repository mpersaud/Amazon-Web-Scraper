import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Created by mike on 10/25/16.
 */
///Bookitem class that is used to store values of the table
public class Bookitem implements Serializable {

    //attributes

    //could possbibly add in date last accessed
    String isbn;
    String author;

    String year_published;
    String publisher;
    String title;
    double price;
    int quantity=1;

    ImageIcon icon= null;
    String rating;

    /*
    Optional:

    Date Purchased, Date Entered into System, Date Last Modified
     */

    public Bookitem() {

    }

    public Bookitem(String isbn) {
        this.isbn = isbn;
    }

    public Bookitem(String isbn, String author, String year_published, String publisher, String title, double price, String rating) {
        this.isbn = isbn;
        this.author = author;

        this.year_published = year_published;
        this.publisher = publisher;
        this.title = title;
        this.price = price;
        this.rating=rating;

    }

    public Bookitem(String isbn, String author, String year_published, String publisher, String title, double price) {
        this.isbn = isbn;
        this.author = author;
        this.year_published = year_published;
        this.publisher = publisher;
        this.title = title;
        this.price = price;

    }

    public String getIsbn() {
        return isbn;
    }


    @Override
    public String toString() {
        return
                "isbn=" + isbn + "\n" +
                "author= " + author + "\n" +
                "year_published= " + year_published + "\n" +
                "publisher= " + publisher + "\n" +
                "title= " + title + "\n" +
                "price= " + price +"\n"+
                "quantity= " + quantity +"\n"+
                        "rating= " + rating +"\n"
                 ;
    }
}

