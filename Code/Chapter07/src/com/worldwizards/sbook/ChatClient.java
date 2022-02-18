package com.worldwizards.sbook;

import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.text.Document;
import javax.swing.text.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;

public class ChatClient
    extends JFrame
    implements GameSessionListener  {
  GameCreator creator;
  JList hostList = new JList(new DefaultListModel());
  GameJoiner gameJoiner;
  static int port;
  private Map ipAddressMap = new HashMap();
  private static final boolean DEBUG = true;

  public ChatClient() {
    setTitle("Chat Host List");
    Container c = getContentPane();
    c.setLayout(new BorderLayout());
    c.add(new JLabel("DiscoveredHosts"),BorderLayout.NORTH);
    c.add(hostList,BorderLayout.CENTER);
    JButton button = new JButton("Connect");
    gameJoiner = new GameJoiner(port);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doJoinChat();
      }
    });
    c.add(button,BorderLayout.SOUTH);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameJoiner.addListener(this);
    setSize (200,400);
    setVisible(true);
  }

  public void doJoinChat(){
    UUID uuid = (UUID)hostList.getSelectedValue();
    InetAddress ip = (InetAddress)ipAddressMap.get(uuid);
    GameSession session = gameJoiner.joinGame(ip);
    ChatConnection conn = new ChatConnection(session);
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }
    else {
      port = 1138;
    }
    new ChatClient();
  }

  /**
   * sessionAdded
   *
   * @param address InetAddress
   * @param sessionUUID UUID
   */
  public void sessionAdded(InetAddress address, UUID sessionUUID) {
    ipAddressMap.put(sessionUUID,address);
     ((DefaultListModel)hostList.getModel()).addElement(sessionUUID);
  }

  /**
   * sessionRemoved
   *
   * @param sessionUUID UUID
   */
  public void sessionRemoved(UUID sessionUUID) {
    ((DefaultListModel)hostList.getModel()).removeElement(sessionUUID);
  }

  /**
   * dataArrived
   *
   * @param data byte[]
   * @param length int
   */
  public void dataArrived(byte[] data, int length) {
  }

  /**
   * socketClosed
   */
  public void socketClosed() {
  }
}
