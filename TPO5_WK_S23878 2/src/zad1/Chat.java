package zad1;

import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

public class Chat extends JFrame implements Runnable {
    private final JTextField messageField;
    private final JTextArea chatArea;
    private final JButton sendButton;
    private TopicConnectionFactory topicConnectionFactory;
    private TopicConnection topicConnection;
    private TopicSession topicSession;
    private TopicPublisher topicPublisher;
    private final String clientName;
    private boolean connected;

    private Hashtable<String, String> env() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        return env;
    }

    public Chat(String clientName) {
        this.clientName = clientName;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setPreferredSize(new Dimension(600, 400));
        setTitle(clientName + "           - type \"/exit\" to disconnect");

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        InputMap inputMap = messageField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = messageField.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        actionMap.put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            try {
                sendMessage(message);
                messageField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void close() {
        if (topicConnection != null) {
            try {
                topicConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private void startChat(TopicSubscriber topicSubscriber) {
        try {
            String message = "";
            topicConnection.start();
            connected = true;
            sendMessage("connected");

            while (connected) {
                if (!message.isEmpty()) {
                    sendMessage(topicPublisher, topicSession, clientName, message);
                }
                Thread.sleep(50);
                getMessage(topicSubscriber, clientName);
            }
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        printChat(">> " + curTime() + " disconnected");
        try {
            topicConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void init(Hashtable<String, String> env) {
        try {
            Context ctx = new InitialContext(env);
            JmsAdminServerIfc a = AdminConnectionFactory.create("tcp://localhost:3035/");
            if (!a.addDestination("SimpleChat", Boolean.FALSE)) {
                System.err.println("Failed to create topic SimpleChat");
            }
            topicConnectionFactory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) ctx.lookup("SimpleChat");
            topicPublisher = topicSession.createPublisher(topic);
            TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            getMessage(topicSubscriber, clientName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessage(TopicSubscriber topicSubscriber, String name) {
        try {
            topicSubscriber.setMessageListener(m -> {
                TextMessage textMessage = (TextMessage) m;
                printChat(textMessage.toString());
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void printChat(String s) {
        chatArea.append(s + "\n");
    }

    public void sendMessage(String message) {
        try {
            sendMessage(topicPublisher, topicSession, clientName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(TopicPublisher topicPublisher, TopicSession ses, String name, String message)
            throws IOException, JMSException {
        if (message.equals("/exit")) {
            disconnect();
        } else {
            topicPublisher.publish(ses.createTextMessage(curTime() + name + ": " + message));
        }
    }

    private String curTime() {
        return " [ " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " ] ";
    }

    @Override
    public void run() {
        try {
            init(env());
            startChat(topicSession.createSubscriber((Topic) topicSession.createTopic("SimpleChat")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
