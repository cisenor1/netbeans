package WebPageDisplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author craig
 */
public class WebPageGrabber {

    public static void main(String[] args) throws MalformedURLException, IOException {

        String inputLine;
        if (args.length != 1) {
            System.err.println("Please provide one URL as an argument");
            return;
        }

        String arg = args[0];
        URL url = new URL(arg);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()))) {
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Unable to create new Buffered Reader");

        }


    }
}
