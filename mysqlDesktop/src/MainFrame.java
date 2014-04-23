import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	
	private static DatabaseAdapter dbAdpter ;
	private JTextArea desktopText;
	private JTextArea androidText;
	private static String readContent ="";
	private static String updateContent = "";	
	
	public MainFrame(String desktopContent,String androidContent){
		setTitle("Sync Application");		
		
		setLocation(200,200);		
		
		JPanel p = (JPanel) getContentPane();
		p.setLayout(new BorderLayout());		
		
		desktopText = new JTextArea(desktopContent);
        desktopText.setFont(new Font("Serif", Font.PLAIN, 40));
        desktopText.setLineWrap(true);
        desktopText.setWrapStyleWord(true);
        
        androidText = new JTextArea(androidContent);
        androidText.setFont(new Font("Serif", Font.PLAIN, 40));
        androidText.setLineWrap(true);
        androidText.setWrapStyleWord(true);
        
        KeyListener updateDbEvent = new KeyListener() {
        	        	
        	@Override
        	public void keyPressed(KeyEvent e) {
        		// TODO Auto-generated method stub        		
        	}

        	@Override
        	public void keyReleased(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER){
        			try{
        				String s = desktopText.getText().toString();
        				System.out.println(s+"");        				
        				dbAdpter.updateData("desktopTalk", s);
        				desktopText.setText("");
        			}catch(Exception exception){
        				System.out.println("Error of updating data");
        			}
        		}        		
        	}

        	@Override
        	public void keyTyped(KeyEvent e) {
        		// TODO Auto-generated method stub        		
        	}
        };
        
        desktopText.addKeyListener(updateDbEvent);
        
        // ------------------------------------------------
        
        JScrollPane androidAreaPane = new JScrollPane(desktopText);
        androidAreaPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        androidAreaPane.setPreferredSize(new Dimension(500, 120));
        androidAreaPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("By Desktop"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                androidAreaPane.getBorder()));
        
        JScrollPane desktopAreaPane = new JScrollPane(androidText);
        desktopAreaPane.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        desktopAreaPane.setPreferredSize(new Dimension(500,120));
        desktopAreaPane.setBorder(
        		BorderFactory.createCompoundBorder(
        				BorderFactory.createCompoundBorder(
        						BorderFactory.createTitledBorder("By Android"),        						
                				BorderFactory.createEmptyBorder(5, 5, 5, 5)),                				
        				desktopAreaPane.getBorder()));
        // --------------------------------------------------
		
		p.add(androidAreaPane,BorderLayout.NORTH);	
		p.add(desktopAreaPane,BorderLayout.SOUTH);
	}	
		
	public static String readingData() throws Exception{
		String rst = "";
				
		ResultSet rs = dbAdpter.retrieveData();
		while(rs.next()){
			rst += rs.getString("androidTalk") + "\n";				
		}		
		
		return rst;		
	}	
	
	public static void main(String[] args){		
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				
				try{
					dbAdpter = new DatabaseAdapter();
					readContent = readingData();
					final MainFrame j = new MainFrame("",readContent);
					j.addWindowListener( new WindowAdapter(){
						public void windowClosing(WindowEvent e){
							System.exit(0);							
							
							try {
								dbAdpter.close();
							} catch (SQLException e1) {								
								e1.printStackTrace();
							}
						}
					});
					j.setVisible(true);					
					j.pack();				
					//j.textArea.setText(readContent);					
					Timer timer = new Timer();
					TimerTask keepSyncText = new TimerTask(){
						@Override
						public void run(){
							try {
								String syncText = readingData();
								j.androidText.setText(syncText);								
							}catch(Exception e){
								System.out.println("Cannot sync the data");
							}
						}
					};
					timer.schedule(keepSyncText, 20000, 2000);
					//timer task start at 20000ms and carry out every 8000ms

				}catch(Exception e){
					e.printStackTrace();
					System.out.println("Error of retrieve data");
				}				
			}			
		});			
	}

}
