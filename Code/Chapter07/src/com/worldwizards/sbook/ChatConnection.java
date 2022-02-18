package com.worldwizards.sbook;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.net.InetAddress;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ChatConnection extends JFrame implements GameCommListener{
 JTextField inputField;
 JTextArea outputArea;
 GameSession session;

 public ChatConnection(GameSession sess) {
   session = sess;
   setTitle("Chat Connection");
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
   setSize(300,300);
   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   session.addListener(this);
   setVisible(true);
 }

 private void doInput(){
   byte[] data = inputField.getText().getBytes();
   inputField.setText(""); // cler it
   session.sendData(data,data.length);
 }


 /**
  * dataArrived
  *
  * @param data byte[]
  * @param length int
  */
 public void dataArrived(byte[] data, int length) {
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
