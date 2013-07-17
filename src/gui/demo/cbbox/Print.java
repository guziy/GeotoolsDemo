package gui.demo.cbbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Print extends JInternalFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int line =0;
	String sn = null;
	JScrollPane pa ;
	private BufferedReader br;
	public Print(){
		
		super("Noted Records");
	    setSize(250, 150);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    init();
	    setVisible(true);
		
	}
	
	public void init() {
		
		 JTextArea tp = new JTextArea();
		  JPanel p = new JPanel();
		    p.setSize(200, 100);
		    p.setLayout(new GridLayout());
		try {
			br = new BufferedReader(new FileReader("data/Read.txt"));
			while((sn = br.readLine()) !=null){
				line++;
				tp.append(sn +'\n');
				tp.setEditable(false);
				//System.out.println(sr);
				
			}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.add(tp);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setClosable(true);
	    pa = new JScrollPane(p);
	    getContentPane().add(pa, BorderLayout.CENTER);
	  }
	
}