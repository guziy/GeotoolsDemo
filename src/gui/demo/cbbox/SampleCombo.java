package gui.demo.cbbox;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;

import com.vividsolutions.jts.geom.Point;


public class SampleCombo extends JInternalFrame { 

    /**
	 * 
	 */
	private static final long serialVersionUID = 500274342644293836L;
	private Point coordinate;
	int line =0;
	String sr = null;
	private BufferedReader br;
	
	public SampleCombo(final Point point) {
		this.setCoordinate(point);
	    setTitle("Register");  
	    BorderLayout layout = new BorderLayout();
	    setSize(new Dimension(250, 120));
	    
	    final JComboBox<String> cbox = new JComboBox<String>();
	    
	  //For Printing
		
	  	class boxListener implements ActionListener {
	          public void actionPerformed (ActionEvent e1) 
	          { 
	          	DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy ; HH:mm:ss");//.parse(originalString)
	          	DateFormat printFormat = new SimpleDateFormat("dd:MM:yyyy ; HH:mm");
	          	Date print = new Date();
	  	    	Date date = new Date();
	          	FileWriter saveFile = null;
	  
	  	        try {
	  	        	
	  	        	String sn;
	  	        	sn =(String)cbox.getSelectedItem();
	  	        	
	  	        	saveFile = new FileWriter("Final.txt", true);
	  				saveFile.write(( sn + ";" + point +";"+(dateFormat.format(date))+'\n'));
	  				saveFile.close();
	  				saveFile = new FileWriter("Print.txt", true);
	  				saveFile.write(( sn + ";" + (printFormat.format(print))+'\n'));
					System.out.println((sn + ";" + point +";"+(dateFormat.format(date))+'\n'));
	  				saveFile.close();
	  	        	
	  			} 	catch (IOException e2) {
	  				// TODO Auto-generated catch block
	  				e2.printStackTrace();
	  			}
	          }
	  	}
	  	//For Printing
	  	
	    cbox.addActionListener (new boxListener());
	    
	    try {
			br = new BufferedReader(new FileReader("data/Read.txt"));
			while((sr = br.readLine()) !=null){
				line++;
				cbox.addItem(sr);
				cbox.setSelectedItem(false);
			}
	   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	                   cbox.setSize(200, 30);
	                   
	                   add(cbox);
	                   this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
	                   this.setLayout(layout);
	                   this.setClosable(true);
	                   setVisible(true);            
	                  
	  }
	public Point getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Point point) {
		this.coordinate = point;
	}
 
};