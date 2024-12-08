import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Elangovan extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;

    Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chat;

    Elangovan() {
        // Initialize components
        textField = new TextField(30); // Text field for input
        textArea = new TextArea(20, 50); // Text area for chat messages
        textArea.setEditable(false); // Make text area read-only
        send = new Button("Send"); // Send button

        send.addActionListener(this); // Attach event listener to the button

        try {
            // Connect to the server
            socket = new Socket("localhost", 5000);
            System.out.println("Connected to server!");

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout setup
        setLayout(new BorderLayout());

        // Add components to the frame
        add(textArea, BorderLayout.CENTER); // Text area in the center
        Panel bottomPanel = new Panel(new FlowLayout()); // Panel for text field and button
        bottomPanel.add(textField);
        bottomPanel.add(send);
        add(bottomPanel, BorderLayout.SOUTH); // Add panel to the bottom

        // Start chat thread
        chat = new Thread(this);
        chat.start();

        // Frame settings
        setSize(600, 400);
        setTitle("Elangovan (Client)");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText(); // Get text from text field
        if (!msg.isEmpty()) {
            textArea.append("Elangovan: " + msg + "\n"); // Append message to text area
            textField.setText(""); // Clear the text field
            try {
                dataOutputStream.writeUTF(msg); // Send the message to the server
                dataOutputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = dataInputStream.readUTF(); // Receive message from server
                textArea.append("Harini: " + msg + "\n"); // Display server message
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        new Elangovan(); // Create and show the client frame
    }
}
