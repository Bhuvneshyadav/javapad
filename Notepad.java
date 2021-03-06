package notepad;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Notepad  {
	
	
	 JPanel contentPanel = new JPanel();
	public JFrame frame;
	public JTextArea textArea;
	public JLabel statusBar;
	
	
	String applicationName="JavaTextEditor";
	
	private boolean saved;
	boolean newFileFlag;
	String fileName;
	String applicationTitle="JavaTextEditor";
	
	File fileRef;
	
	JFileChooser chooser;
	JColorChooser color;
	protected int INFORAMTION_MESSAGE;
	protected int PLAIN_MESSAGE;

	
	boolean isSave()
	{return saved;}
	void setSave(boolean saved ) 
	{this .saved=saved;}
	String getFileName() 
	{return new String (fileName);}
	void setFileName(String fileName)
	{this.fileName=new String(fileName);}

	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notepad win = new Notepad();
					win.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the application.
	 */
	public Notepad() {
		initialize();
		    
		    saved =true;
			newFileFlag=true;
			fileName=new String("Untitled");
			fileRef=new File(fileName);
			this.frame.setTitle(fileName+" "+applicationTitle);
			
			chooser=new JFileChooser();
			//.addChoosableFileFilter(new MyFileFilter(".java","Java Source Files(*.java)"));
			//chooser.addChoosableFileFilter(new FileFilter(".txt","Text Files(*.txt)"));
			chooser.setCurrentDirectory(new File("."));
			
		}
		
		boolean saveFile(File temp)
		{
			FileWriter fout=null;
			try
			{
				fout=new FileWriter(temp);
				fout.write(textArea.getText());
			}
			catch (IOException ioe)
			{
				updateStatus(temp,false);return false;
			}
			finally {
				try {fout.close();}
				catch(IOException ioe) {}}
			updateStatus(temp,true);
			
			return true;
			
		}
		boolean saveThisFile()
		{
			if(!newFileFlag)
			{
				return saveFile(fileRef);
			}
			return saveAsFile();
		}
		boolean saveAsFile() {
			File temp=null;
			chooser.setDialogTitle("Save AS");
	        chooser.setApproveButtonText("Save Now");
	        chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
	        chooser.setApproveButtonToolTipText("click me to save");
	        
	        do {
	        	if(chooser.showSaveDialog(this.frame)!=JFileChooser.APPROVE_OPTION)
	        		return false;
	        	temp=chooser.getSelectedFile();
	        	if(!temp.exists())break;
	        	if(JOptionPane.showConfirmDialog(this.frame,"<html>"+temp.getPath()+"already exists.<br> Do you want to replace it?</html>","Save As",
	        			JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	        		break;
	        }while(true);
			return saveFile(temp);
			
		}
		
		boolean openFile(File temp)
		{
			FileInputStream fin=null;
			BufferedReader din=null;
			
			try
			{
				fin=new FileInputStream(temp);
				din=new BufferedReader(new InputStreamReader(fin));
				String str="";
				while(str!=null)
				{
					str=din.readLine();
					if(str==null)
						break;
					this.textArea.append(str+"\n");
				}
			}catch(IOException ioe)
			{
				updateStatus(temp,false);return false;
			}
		  finally
		{
			try {din.close();fin.close();}
			catch(IOException excp) {}
		}
			updateStatus(temp,true);
			this.textArea.setCaretPosition(0);
	      return true;		
		}
		
		void openFile()
		{
			if(!confirmSave())return;
			chooser.setDialogTitle("open File");
		    chooser.setApproveButtonText("open this");
		    chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
		    chooser.setApproveButtonToolTipText("click me to open");
		    
		    File temp=null;
		    do {
		    	if(chooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION)
		    		return;
		    	temp=chooser.getSelectedFile();
		    	
		    	if(temp.exists())break;
		    	JOptionPane.showInternalMessageDialog(this.frame,"<html>"+temp.getPath()+"filenot found.<br> Please verify the correct file name?</html>","open",
		    			JOptionPane.INFORMATION_MESSAGE);
		    	}while(true);
		    
		     this.textArea.setText("");
		     
		     if(!openFile(temp))
		     {
		    	 fileName="Untitled";
		    	 saved=true;
		    	 this.frame.setTitle(fileName+"-"+applicationTitle);
		     }
		     if(!temp.canWrite())
		    	 newFileFlag=true;
		    
		}
		void updateStatus(File temp,boolean saved)
		{
			if(saved)
			{
				this.saved=true;
				fileName=new String(temp.getName());
				if(!temp.canWrite())
				{
					fileName+="(Read only)";
					newFileFlag=true;
				}
				fileRef=temp;
				this.frame.setTitle(fileName+"-"+applicationTitle);
				//npd.statusBar.setText("File :"+temp.getPath()+"saved successfully");
				newFileFlag=false;
			}
			else {
				//npd.statusBar.setText("Failed to save  :"+temp.getPath());
			}
		}
		boolean confirmSave()
		{
		  String strMsg="html> The text in the file";
		  if(!saved)
		  {
			  int x=JOptionPane.showConfirmDialog(this.frame, strMsg,applicationTitle,JOptionPane.YES_NO_CANCEL_OPTION);
			  if(x==JOptionPane.CANCEL_OPTION)return  false;
			  if(x==JOptionPane.YES_OPTION&&!saveAsFile())return false;
		  }
		  return true;
		}
		void newFile() 
		{
			if(!confirmSave()) return;
			
			this.textArea.setText("");
			fileName=new String("Untitled");
			fileRef=new File(fileName);
			saved=true;
			newFileFlag=true;
			this.frame.setTitle(fileName+"-"+applicationTitle);
			
		}
				
	 public void initialize() {
		frame = new JFrame(fileName+"-"+applicationName);
		
		frame.setBounds(100, 100, 642, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		
	                JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					{newFile();}
				
			}
			
		});
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
				
			}
			
		});
		mnFile.add(mntmOpen);		
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveThisFile();
				
			}

			
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveas = new JMenuItem("Save'As");
		mntmSaveas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsFile();
				
			}
			
		});
		mnFile.add(mntmSaveas);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(confirmSave())
				{
					System.exit(0);
				}
				
			}
			
		});
		
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
	
		mntmCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
				
			}
			
		});
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
			}
			
		});
		mnEdit.add(mntmCut);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
				
			}
			
		});
		mnEdit.add(mntmPaste);
		
		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		JMenuItem mntmForeground = new JMenuItem("Foreground");
		mntmForeground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c=JColorChooser.showDialog(frame, "choose font color", Color.BLUE);
				textArea.setForeground(c);
			}
			
		});
		mnFormat.add(mntmForeground);
		
		JMenuItem mntmBackground = new JMenuItem("Background");
		mntmBackground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c=JColorChooser.showDialog(frame, "choose font color", Color.BLUE);
				textArea.setBackground(c);
				
			}
			
		});
		mnFormat.add(mntmBackground);
		
		
		
		
		JMenu mnAbout = new JMenu("About");
	    menuBar.add(mnAbout);
	    
	    JMenuItem about=new JMenuItem("about");
	about.addActionListener(new ActionListener() {
          
			String ab="<html><big>Your JavaNotepad</big><hr><hr>"
					+"<p align=right>Prepared by Buvnesh yadav<br>Ankit rajpoot <br>Ankur <br>Abishek kumar verma"
					+"<hr><p align=left>I Used jdk1.8 to compile the source code.<br><br>"
					+"<strong>Thanx for using JavaNotepad</strong><br>"
					+"Ur Comments as well as bug reports r very welcome at<p align=center>"
					+"<hr><em><big>www.facebook.com\\vamp099</big></em><hr></html>";

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(about,ab , "about",JOptionPane.INFORMATION_MESSAGE );
				
			}
			
		});
	    mnAbout.add(about);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		 textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea.setTabSize(12);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setEnabled(true);
		scrollPane.setViewportBorder(null);
		scrollPane.setViewportView(textArea);
		frame.getContentPane().add(scrollPane);
		
		
		JPanel panel = new JPanel();
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		scrollPane.setRowHeaderView(panel_1);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 statusBar=new JLabel();
		 frame.add(statusBar,BorderLayout.EAST);
		 frame.add(new JLabel(""),BorderLayout.EAST);
		 frame.add(new JLabel(""),BorderLayout.WEST);
		 
		
	
	}

	
	

}
package notepad;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Notepad  {
	
	
	 JPanel contentPanel = new JPanel();
	public JFrame frame;
	public JTextArea textArea;
	public JLabel statusBar;
	
	
	String applicationName="JavaTextEditor";
	
	private boolean saved;
	boolean newFileFlag;
	String fileName;
	String applicationTitle="JavaTextEditor";
	
	File fileRef;
	
	JFileChooser chooser;
	JColorChooser color;
	protected int INFORAMTION_MESSAGE;
	protected int PLAIN_MESSAGE;

	
	boolean isSave()
	{return saved;}
	void setSave(boolean saved ) 
	{this .saved=saved;}
	String getFileName() 
	{return new String (fileName);}
	void setFileName(String fileName)
	{this.fileName=new String(fileName);}

	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notepad win = new Notepad();
					win.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the application.
	 */
	public Notepad() {
		initialize();
		    
		    saved =true;
			newFileFlag=true;
			fileName=new String("Untitled");
			fileRef=new File(fileName);
			this.frame.setTitle(fileName+" "+applicationTitle);
			
			chooser=new JFileChooser();
			//.addChoosableFileFilter(new MyFileFilter(".java","Java Source Files(*.java)"));
			//chooser.addChoosableFileFilter(new FileFilter(".txt","Text Files(*.txt)"));
			chooser.setCurrentDirectory(new File("."));
			
		}
		
		boolean saveFile(File temp)
		{
			FileWriter fout=null;
			try
			{
				fout=new FileWriter(temp);
				fout.write(textArea.getText());
			}
			catch (IOException ioe)
			{
				updateStatus(temp,false);return false;
			}
			finally {
				try {fout.close();}
				catch(IOException ioe) {}}
			updateStatus(temp,true);
			
			return true;
			
		}
		boolean saveThisFile()
		{
			if(!newFileFlag)
			{
				return saveFile(fileRef);
			}
			return saveAsFile();
		}
		boolean saveAsFile() {
			File temp=null;
			chooser.setDialogTitle("Save AS");
	        chooser.setApproveButtonText("Save Now");
	        chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
	        chooser.setApproveButtonToolTipText("click me to save");
	        
	        do {
	        	if(chooser.showSaveDialog(this.frame)!=JFileChooser.APPROVE_OPTION)
	        		return false;
	        	temp=chooser.getSelectedFile();
	        	if(!temp.exists())break;
	        	if(JOptionPane.showConfirmDialog(this.frame,"<html>"+temp.getPath()+"already exists.<br> Do you want to replace it?</html>","Save As",
	        			JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	        		break;
	        }while(true);
			return saveFile(temp);
			
		}
		
		boolean openFile(File temp)
		{
			FileInputStream fin=null;
			BufferedReader din=null;
			
			try
			{
				fin=new FileInputStream(temp);
				din=new BufferedReader(new InputStreamReader(fin));
				String str="";
				while(str!=null)
				{
					str=din.readLine();
					if(str==null)
						break;
					this.textArea.append(str+"\n");
				}
			}catch(IOException ioe)
			{
				updateStatus(temp,false);return false;
			}
		  finally
		{
			try {din.close();fin.close();}
			catch(IOException excp) {}
		}
			updateStatus(temp,true);
			this.textArea.setCaretPosition(0);
	      return true;		
		}
		
		void openFile()
		{
			if(!confirmSave())return;
			chooser.setDialogTitle("open File");
		    chooser.setApproveButtonText("open this");
		    chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
		    chooser.setApproveButtonToolTipText("click me to open");
		    
		    File temp=null;
		    do {
		    	if(chooser.showOpenDialog(this.frame)!=JFileChooser.APPROVE_OPTION)
		    		return;
		    	temp=chooser.getSelectedFile();
		    	
		    	if(temp.exists())break;
		    	JOptionPane.showInternalMessageDialog(this.frame,"<html>"+temp.getPath()+"filenot found.<br> Please verify the correct file name?</html>","open",
		    			JOptionPane.INFORMATION_MESSAGE);
		    	}while(true);
		    
		     this.textArea.setText("");
		     
		     if(!openFile(temp))
		     {
		    	 fileName="Untitled";
		    	 saved=true;
		    	 this.frame.setTitle(fileName+"-"+applicationTitle);
		     }
		     if(!temp.canWrite())
		    	 newFileFlag=true;
		    
		}
		void updateStatus(File temp,boolean saved)
		{
			if(saved)
			{
				this.saved=true;
				fileName=new String(temp.getName());
				if(!temp.canWrite())
				{
					fileName+="(Read only)";
					newFileFlag=true;
				}
				fileRef=temp;
				this.frame.setTitle(fileName+"-"+applicationTitle);
				//npd.statusBar.setText("File :"+temp.getPath()+"saved successfully");
				newFileFlag=false;
			}
			else {
				//npd.statusBar.setText("Failed to save  :"+temp.getPath());
			}
		}
		boolean confirmSave()
		{
		  String strMsg="html> The text in the file";
		  if(!saved)
		  {
			  int x=JOptionPane.showConfirmDialog(this.frame, strMsg,applicationTitle,JOptionPane.YES_NO_CANCEL_OPTION);
			  if(x==JOptionPane.CANCEL_OPTION)return  false;
			  if(x==JOptionPane.YES_OPTION&&!saveAsFile())return false;
		  }
		  return true;
		}
		void newFile() 
		{
			if(!confirmSave()) return;
			
			this.textArea.setText("");
			fileName=new String("Untitled");
			fileRef=new File(fileName);
			saved=true;
			newFileFlag=true;
			this.frame.setTitle(fileName+"-"+applicationTitle);
			
		}
				
	 public void initialize() {
		frame = new JFrame(fileName+"-"+applicationName);
		
		frame.setBounds(100, 100, 642, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		
	                JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					{newFile();}
				
			}
			
		});
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
				
			}
			
		});
		mnFile.add(mntmOpen);		
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveThisFile();
				
			}

			
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveas = new JMenuItem("Save'As");
		mntmSaveas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsFile();
				
			}
			
		});
		mnFile.add(mntmSaveas);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(confirmSave())
				{
					System.exit(0);
				}
				
			}
			
		});
		
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
	
		mntmCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.copy();
				
			}
			
		});
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.cut();
			}
			
		});
		mnEdit.add(mntmCut);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.paste();
				
			}
			
		});
		mnEdit.add(mntmPaste);
		
		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		JMenuItem mntmForeground = new JMenuItem("Foreground");
		mntmForeground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c=JColorChooser.showDialog(frame, "choose font color", Color.BLUE);
				textArea.setForeground(c);
			}
			
		});
		mnFormat.add(mntmForeground);
		
		JMenuItem mntmBackground = new JMenuItem("Background");
		mntmBackground.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c=JColorChooser.showDialog(frame, "choose font color", Color.BLUE);
				textArea.setBackground(c);
				
			}
			
		});
		mnFormat.add(mntmBackground);
		
		
		
		
		JMenu mnAbout = new JMenu("About");
	    menuBar.add(mnAbout);
	    
	    JMenuItem about=new JMenuItem("about");
	about.addActionListener(new ActionListener() {
          
			String ab="<html><big>Your JavaNotepad</big><hr><hr>"
					+"<p align=right>Prepared by Buvnesh yadav<br>Ankit rajpoot <br>Ankur <br>Abishek kumar verma"
					+"<hr><p align=left>I Used jdk1.8 to compile the source code.<br><br>"
					+"<strong>Thanx for using JavaNotepad</strong><br>"
					+"Ur Comments as well as bug reports r very welcome at<p align=center>"
					+"<hr><em><big>www.facebook.com\\vamp099</big></em><hr></html>";

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(about,ab , "about",JOptionPane.INFORMATION_MESSAGE );
				
			}
			
		});
	    mnAbout.add(about);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		 textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea.setTabSize(12);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setEnabled(true);
		scrollPane.setViewportBorder(null);
		scrollPane.setViewportView(textArea);
		frame.getContentPane().add(scrollPane);
		
		
		JPanel panel = new JPanel();
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		scrollPane.setRowHeaderView(panel_1);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 statusBar=new JLabel();
		 frame.add(statusBar,BorderLayout.EAST);
		 frame.add(new JLabel(""),BorderLayout.EAST);
		 frame.add(new JLabel(""),BorderLayout.WEST);
		 
		
	
	}

	
	

}

