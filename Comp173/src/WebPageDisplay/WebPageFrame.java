package WebPageDisplay;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * A frame that has URL entry and displays web pages
 *
 * @author Craig Isenor
 */
public class WebPageFrame extends JFrame implements ActionListener {

    /**
     * The initial size of the frame
     */
    public static final Dimension FRAME_SIZE = new Dimension(800, 600);
    /**
     * Pane that displays the web page
     */
    private JEditorPane webPagePane;
    /**
     * Field for entering the URL
     */
    private JTextField urlField;
    /**
     * Back Button
     */
    private JButton backButton;
    
    /**
     * Refresh Button
     */
    
    private JButton refreshButton;
    /**
     * Keep track of most recent url (for the history stack)
     */
    
    private Stack<URL> stack =  new Stack();
    private int last;
    private URL currentURL = null;
    private URL lastUrl = null;
    private ArrayList<URL> previousURLs = new ArrayList();
    /**
     * Hyperlink listener
     */
    private URL link;
    private HyperlinkListener linkListener;

    public WebPageFrame() {
        this.linkListener = new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                HyperlinkEvent.EventType type = e.getEventType();

                if (type == HyperlinkEvent.EventType.ACTIVATED) {
                    
                    link = e.getURL();
                    stack.push(link);
                    setEditorPaneURL(stack.peek());
                }

            }
        };
        // Instantiate data structures

        // Instantiate GUI components
        webPagePane = new JEditorPane();
        urlField = new JTextField("http://", 40);
        backButton = new JButton("Back");
        refreshButton = new JButton("Refresh");
        
        // Set initial properties of GUI components
        webPagePane.setEditable(false);
        webPagePane.addHyperlinkListener(linkListener);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Register callbacks
        urlField.addActionListener(this);
        backButton.addActionListener(this);

        // Lay out the GUI components
        layoutGUI();

        // If the user clicks on the window close button, exit
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Lay out the GUI components
     */
    private void layoutGUI() {
        // Layout the top mainPanel for user input
        JPanel topPanel = new JPanel();
        BoxLayout topLayout = new BoxLayout(topPanel, BoxLayout.X_AXIS);
        topPanel.setLayout(topLayout);
        topPanel.add(backButton);
        topPanel.add(new JLabel("Web Site: "));
        topPanel.add(urlField);
        topPanel.add(new JPanel());
        topPanel.add(refreshButton);

        // The webPagePane is viewed through a scrollPane
        JScrollPane scroller = new JScrollPane(webPagePane);

        // Add the scroller, which views the mainPanel to the frame's container
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(topPanel, BorderLayout.NORTH);
        c.add(scroller, BorderLayout.CENTER);

        // Set the default size and make the frame visible
        this.setSize(FRAME_SIZE);
        this.setPreferredSize(FRAME_SIZE);
        this.setVisible(true);
    }

    /**
     * Set the web page to view
     *
     * @param url The URL of the web page
     */
    private void setEditorPaneURL(URL url) {
        if (url != null) {
            try {
                webPagePane.setPage(url);
                currentURL = url;

                urlField.setText(url.toString());
            } catch (IOException ex) {
                webPagePane.setText("Unable to read from: " + url);
            }
        } else {
            webPagePane.setText("Couldn't find the URL");
        }
    }

    /**
     * Handle events from the URL field and the backButton
     *
     * @param ev The event
     */
    @Override
    public void actionPerformed(ActionEvent ev) {

        // get the last URL from the array and use it. then remove if from the list.
        if (ev.getSource() == backButton) {
            stack.pop();
            lastUrl = stack.peek();
            setEditorPaneURL(lastUrl);
        }
       
        if (ev.getSource() == refreshButton){
            setEditorPaneURL(stack.peek());
        }


        if (ev.getSource() == urlField) {
            URL webSite;
            try {
                webSite = new URL(urlField.getText());
                stack.push(webSite);
                System.out.println(stack.peek());
                setEditorPaneURL(stack.peek());

            } catch (MalformedURLException ex) {
                webPagePane.setText(urlField.getText() + " has a bad URL format");
            }
        }
    }
    

    /**
     * This allows us to run this class as a simple application. We can also use
     * this class as a re-usable component
     **/
    public static void main(String[] args) {
        new WebPageFrame();
    }
}