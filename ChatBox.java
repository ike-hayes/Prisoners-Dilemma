/**
* Write a description of class Chat here.
*
* @author Ike Hayes
* @version 7/5/21
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ChatBox extends JFrame implements ActionListener, KeyListener{
    JTextField tf = new JTextField(10);
    JTextArea ta = new JTextArea();
    JButton send = new JButton("Send");
    JButton reset = new JButton("Reset");
    public void main() {
        //Creating the Frame
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        
        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);
        
        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
         // accepts upto 10 characters

        
        
        
        
        reset.addActionListener(this);
        send.addActionListener(this);
        
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);
        ta.setEditable(false);
        
        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
     
        tf.addKeyListener(this);
        // Text Area at the Center
        
    }
    public ChatBox(){
        main();
    }
    public void actionPerformed(ActionEvent e) { 
       if(e.getSource()==send){
           String str;
           str = tf.getText();
           tf.setText("");
           ta.append(str+"\n");
       }
       if(e.getSource()==reset){
           ta.setText(null);
       }
    }    
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
           String str;
           str = tf.getText();
           tf.setText("");
           if(!str.equals("")){
               ta.append(str+"\n");
           }
        }
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}