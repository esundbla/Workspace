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

public class ChatHost extends JFrame implements GameCommListener {
  GameCreator creator;
  JTextField inputField;
  JTextArea outputArea;
  static int port;

  public ChatHost() {
    setTitle("Chat Host");
    Container c = getContentPane();
    c.setLayout(new BorderLayout());
    inputField = new JTextField();
    inputField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doInput();
      }
    });
    outputArea = new JTextArea();
    c.add(new JScrollPane(outputArea),BorderLayout.CENTER);
    c.add(inputField,BorderLayout.SOUTH);
    creator = new GameCreator(port);
    creator.addListener(this);
    setSize(300,300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void doInput(){
    byte[] data = inputField.getText().getBytes();
    inputField.setText(""); // cler it
    creator.sendData(data,data.length);
  }

  public static void main(String[] args){
    if (args.length >0) {
      port = Integer.parseInt(args[0]);
    } else{
      port = 1138;
    }
    new ChatHost();
  }

  /**
   * dataArrived
   *
   * @param data byte[]
   * @param length int
   */
  public void dataArrived(byte[] data, int length) {
    // echo to clients
    creator.sendData(data,data.length);
    // add to us
    Document doc = outputArea.getDocument();
    try {
      doc.insertString(doc.getEndPosition().getOffset() - 1,
                       new String(data)+"\n",
                       new SimpleAttributeSet());
    }
    catch (BadLocationException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * socketClosed
   */
  public void socketClosed() {
    System.out.println("Server socket has unexpetedly quit.");
    System.exit(2);
  }
}
