import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SVMFrame extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2544720183711962793L;
	private JLabel jbl;
	private JLabel jb2;
	private JLabel jb3;
	
	public SVMFrame(){
		setTitle("LibSVM");
		setLayout(null);
		Container contentpane=getContentPane();
		SVMModel panel=new SVMModel();
		
		//文字信息
		jbl=new JLabel("回到首页");
		jb2=new JLabel("Author: shijing");
		jb3=new JLabel("联系方式: shijing_0214@163.com");
		jbl.setFont(new Font("Dialog",Font.TRUETYPE_FONT,16));
		jb2.setFont(new Font("Dialog",Font.PLAIN,16));
		jb3.setFont(new Font("Dialog",Font.ITALIC,16));
		jbl.setForeground(Color.BLACK);
		jb2.setForeground(Color.BLACK);
		jb3.setForeground(Color.BLACK);
		jbl.setBounds(15,0,240,22);
		jb2.setBounds(370,420,240,22);
		jb3.setBounds(370,440,280,22);
		
		contentpane.add(panel);
		contentpane.add(jbl);
		contentpane.add(jb2);
		contentpane.add(jb3);
		panel.setBounds(0,25,660,400);
		
		
        jbl.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自动生成的方法存根
				setVisible(false);
				Thread thread=new Thread(new SVMFrame());
				thread.start();
			}
		});
	}
	
	
	public static void main(String[] args){
		
		Thread thread=new Thread(new SVMFrame());
		thread.start();
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		SVMFrame frm=new SVMFrame();
		frm.setSize(650,500);
		frm.setLocation(500, 200);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
}
