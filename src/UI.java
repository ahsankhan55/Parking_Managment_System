import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class UI {
   public void outputs(String a){
       JFrame results = new JFrame("Result");
       results.setBounds(200,200,500,170);
       results.setLayout(null);
       results.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       results.setResizable(false);
       results.setVisible(true);


       JLabel lbl = new JLabel();
       lbl.setText(a);
       lbl.setBounds(50,30,400,30);
       lbl.setVisible(true);
       results.add(lbl);

       JButton close = new JButton("Close");
       close.setBounds(205,90,70,30);
       close.setVisible(true);
       close.setBackground(Color.LIGHT_GRAY);
       close.addActionListener(new AbstractAction() {
           @Override
           public void actionPerformed(ActionEvent e) {
               System.exit(0);
           }
       });
       results.add(close);
   }
}
