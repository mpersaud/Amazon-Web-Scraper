import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Created by mike on 10/25/16.
 */
///item class that is used to store values of the table
public class item implements Serializable {

    //attributes

    //could possbibly add in date last accessed
    String isbn;
    String author;

    String year_published;
    String publisher;
    String title;
    double price;
    int quantity=1;
    BufferedImage image;

    /*
    Optional:

    Date Purchased, Date Entered into System, Date Last Modified
     */

    public item() {

    }

    public item(String isbn) {
        this.isbn = isbn;
    }

    public item(String isbn, String author_firstname,  String year_published, String publisher, String title, double price) {
        this.isbn = isbn;
        this.author = author_firstname;

        this.year_published = year_published;
        this.publisher = publisher;
        this.title = title;
        this.price = price;

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor_firstname() {
        return author;
    }

    public void setAuthor_firstname(String author_firstname) {
        this.author = author_firstname;
    }


    public String getYear_published() {
        return year_published;
    }

    public void setYear_published(String year_published) {
        this.year_published = year_published;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "item{" +
                "isbn='" + isbn + '\'' +
                ", author_firstname='" + author + '\'' +
                ", year_published='" + year_published + '\'' +
                ", publisher='" + publisher + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

