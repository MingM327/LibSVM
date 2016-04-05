import java.awt.*;

import javax.swing.*;
import javax.swing.text.StringContent;

import org.python.util.PythonInterpreter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class SVMModel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4843535331313021333L;
	//文件路径
	private String filePathString="";
	private String trainModelfile="";
	private String rootFilePathString="";
	//文字区域
	private TextArea display;
	
    //容器面板
	private JPanel panel1;
	//子功能面板
	private JPanel subPanel;
	//四个主要功能按钮
	JButton scaleButton;
	JButton gridButton;
	JButton trainButton;
	JButton predictButton;
	
	//scale功能下
	JButton scaleButton_selectFile;
	JButton scaleButton_scaleFeature;
	
	JTextField scale_lower;
	JTextField scale_upper;
	
	//predict功能下
	JButton selectTrainFile;
	
	//监听器
	ActionListener command=new CommandAction();
	ActionListener scaleCommand=new ScaleCommandAction();
	ActionListener trainCommand=new TrainCommandAction();
	ActionListener predictCommand=new PredictCommandAction();
	ActionListener gridCommand=new GridCommandAction();
	
	
	public SVMModel(){
		setLayout(null);
		
		//左侧输出信息区
		display=new TextArea("", 10, 5);
		display.setFont(new Font("Dialog",Font.LAYOUT_NO_LIMIT_CONTEXT,16));
		display.setForeground(Color.BLACK);
		display.setBackground(new Color(250,250,255));
		display.setBounds(10,15,350,390);
		//右侧功能区
		panel1=new JPanel(new GridLayout(4,1,15,15));
		panel1.setBounds(420,40,115,150);
		//二级功能区
		subPanel=new JPanel(new GridLayout(4,1,15,15));
		subPanel.setBounds(420,40,150,250);
		
		scaleButton=addButton("scale特征缩放",command,panel1,16);
		gridButton=addButton("grid最优参数",command,panel1,16);
		trainButton=addButton("训练模型",command,panel1,16);
		predictButton=addButton("预测模型",command,panel1,16);
		add(panel1);
		add(subPanel);
		add(display);
		
			
	}
	private JButton addButton(String s,ActionListener listener,JPanel panel,int size){
		JButton button=new JButton(s);
		button.setMargin(new Insets(0,0,0,0));
		button.setFont(new Font("Dialog",Font.PLAIN,size));
		button.setForeground(new Color(20,50,100));
		button.addActionListener(listener);
		panel.add(button);
		return button;
	}
	
	private class CommandAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String command=event.getActionCommand();
			
			if(command.equals("scale特征缩放")){
				scaleFunction();
			}
			if(command.equals("训练模型")){
				trainFunction();
			}
			if(command.equals("预测模型")){
				predictFunction();
			}
			if(command.equals("grid最优参数")){
				gridFunction();
			}
		}

	
	}
	
	private class ScaleCommandAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String command=event.getActionCommand();
			String str="";
			if(command.equals("选择测试文件")){
				filePathString=selectFile();
				str="scale使用说明：\n"
		                +"1. 选择训练文件集;\n"
		                +"2. 点击scale进行缩放;\n"
		                +"3. 完成！\n";
				str+=filePathString;
				str+="\n------------------------------------\n";
				str+="请填写参数后点击\"scale特征\",\nlower表示缩放的下限，\nupper表示缩放的上限";
				display.setText(str);
			
			}
			if(command.equals("scale特征")){
				svm_scale s = new svm_scale();
				String lowString="-l";
				String upperString="-u";
				String filenameString=filePathString;
				String argv[]=new String[]{lowString,scale_lower.getText(),upperString,scale_upper.getText(),filenameString};
				try {
					String ss=s.run(argv);
					display.setText(ss);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
	
	private class TrainCommandAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String command=event.getActionCommand();
			String str="";
			if(command.equals("选择文件")){
				filePathString=selectFile();
				str="训练模型使用说明：\n"
		                +"1. 选择训练文件集;\n"
		                +"2. 填写训练参数;\n"
		                +"3. 完成！\n";
				str+="你选择的文件路径为：\n";
				str+=filePathString;
				str+="\n------------------------------------\n";
				str+="请填写参数后点击\"训练模型\",\nC值表示惩罚因子，\ng值表示核函数参数";
				display.setText(str);
			
			}
			if(command.equals("训练模型")){
				if(filePathString=="")
					waringWindow("请选择文件");
				else{
					String filenameString=filePathString;
					String cValue=scale_lower.getText();
					String gValue=scale_upper.getText();
					rootFilePathString=filenameString.substring(0,filenameString.lastIndexOf("\\")+1);
					String argv[] = null;
					try {
						if(!cValue.equals("")&&gValue.equals("")){
							argv=new String[]{"-c",cValue,"-g", gValue,filenameString,rootFilePathString+"newModel.model"};
						}
						else if(cValue.equals("")&&!gValue.equals(""))
							argv=new String[]{"-g", gValue,filenameString,rootFilePathString+"newModel.model"};
						else if(!cValue.equals("")&&gValue.equals(""))
							argv=new String[]{"-c",cValue,filenameString,rootFilePathString+"newModel.model"};
						else if(cValue.equals("")&&gValue.equals("")){
							argv=new String[]{filenameString,rootFilePathString+"newModel.model"};
						}
						svm_train t = new svm_train();
						String ss=t.run(argv,rootFilePathString);
						readConsoleInfo();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	private class PredictCommandAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String command=event.getActionCommand();
			String str="";
			if(command.equals("选择测试文件")){
				filePathString=selectFile();
				str="预测模型使用说明：\n"
		                +"1. 选择预测文件集;\n"
		                +"2. 选择训练模型文件;\n"
		                +"3. 完成！\n";
				str+="你选择的文件路径为：\n";
				str+=filePathString;
				str+="\n------------------------------------\n";
				str+="请选择文件后点击\"训练模型\"\n";
				display.setText(str);
			
			}
			if(command.equals("选择训练模型文件")){
				trainModelfile=selectFile();
			
			}
			if(command.equals("预测模型")){
				if(filePathString==""&&trainModelfile==""){
					waringWindow("请选择测试文件及训练模型文件！");
				}else{
					svm_predict p = new svm_predict();
					String filenameString=filePathString;
					String trainModel=trainModelfile;
					rootFilePathString=filenameString.substring(0,filenameString.lastIndexOf("\\")+1);
					String argv[]=new String[]{filenameString,trainModel,rootFilePathString+"newPredictModel.model"};
				
					String ss=p.run(argv,rootFilePathString);
					try {
						readConsoleInfo();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	private class GridCommandAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String command=event.getActionCommand();
			String str="";
			if(command.equals("选择训练文件")){
				filePathString=selectFile();
				rootFilePathString=filePathString.substring(0,filePathString.lastIndexOf("\\")+1);
				str="寻找最优参数使用说明：\n"
		                +"1. 选择训练文件;\n"
		                +"2. 点击\"训练参数\";\n"
		                +"3. 完成！\n";
				str+="你选择的文件路径为：\n";
				str+=filePathString;
				str+="\n------------------------------------\n";
				display.setText(str);
			
			}
			
			if(command.equals("训练参数")){
				if(filePathString==""){
					waringWindow("请选择文件！");
				}else{
					//将控制台输出信息输出到文件
					getConsoleInfo();
					
//					String filenameString=filePathString.substring(0, filePathString.lastIndexOf('\\')+1)+filePathString;
					String argv[]=new String[]{"grid.py","-v","6",filePathString};
					PythonInterpreter.initialize(null, null, argv);
					PythonInterpreter interp = new PythonInterpreter();  
			        interp.execfile("./tools/grid.py");
			        System.out.println("参数训练结束~~~~~~~~~~~~~~~~");
			        interp.close();
			        try {
						readConsoleInfo();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	//获取控制台输出
	public void getConsoleInfo(){
		File f=new File(rootFilePathString+"out.txt");
		try {
			f.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(f);
			PrintStream printStream = new PrintStream(fileOutputStream);
			System.setOut(printStream);
			System.out.println("\n");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	//提示窗口
	public void waringWindow(String str){
		Object[] options = { "OK", "CANCEL" };
		JOptionPane.showOptionDialog(null, str, "提示",
		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		null, options, options[0]); 
	}
	
	//选择文件
	public String selectFile(){
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showOpenDialog(null);
			String path = chooser.getSelectedFile().getPath();
			return path;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	//读取控制台输出信息文件
	public void readConsoleInfo() throws IOException{
		File file =new File(rootFilePathString+"out.txt");
		BufferedReader bf=new BufferedReader(new FileReader(file));
		
		String str="",wholeString="";
		while((str=bf.readLine())!=null){
			wholeString+=str+"\n";
		}
		display.setText(wholeString);
		BufferedWriter bw=new BufferedWriter(new FileWriter(file));
		bw.write("");
	}
	public void trainFunction(){
		String introduction="训练模型使用说明：\n"
                +"1. 选择训练文件集;\n"
                +"2. 填写训练参数;\n"
                +"3. 完成！";
		display.setText(introduction);
		//隐藏按钮
		scaleButton.setVisible(false);
		gridButton.setVisible(false);
		trainButton.setVisible(false);
		predictButton.setVisible(false);
		
	    scaleButton_selectFile=addButton("选择文件",trainCommand,subPanel,16);
		scaleButton_scaleFeature=addButton("训练模型",trainCommand,subPanel,16);
		
		JPanel subPanel1=new JPanel(new GridLayout(1, 2));
		JPanel subPanel2=new JPanel(new GridLayout(1, 2));
		
		scale_lower=new JTextField("");
		scale_upper=new JTextField("");
//		scale_lower.setFont(new Font("Dialog",Font.PLAIN,16));
//		scale_upper.setFont(new Font("Dialog",Font.PLAIN,16));
		
		JLabel lowerLabel=new JLabel("C值:");
		JLabel upperLabel=new JLabel("g值:");
		lowerLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		upperLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		
		subPanel1.add(lowerLabel);
		subPanel1.add(scale_lower);
		subPanel2.add(upperLabel);
		subPanel2.add(scale_upper);
		
		
		subPanel.add(subPanel1);
		subPanel.add(subPanel2);
	
		
	}
	
	public void predictFunction(){
		String introduction="预测模型使用说明：\n"
                +"1. 选择预测文件集;\n"
                +"2. 选择训练模型文件;\n"
                +"3. 完成！\n";
		display.setText(introduction);
		//隐藏按钮
		panel1.setVisible(false);
		
	    scaleButton_selectFile=addButton("选择测试文件",predictCommand,subPanel,16);
	    selectTrainFile=addButton("选择训练模型文件",predictCommand,subPanel,16);
		scaleButton_scaleFeature=addButton("预测模型",predictCommand,subPanel,16);
				
		subPanel.add(scaleButton_selectFile);
		subPanel.add(selectTrainFile);
		subPanel.add(scaleButton_scaleFeature);
	
		
	}
	
	public void gridFunction(){
		String introduction="寻找最优参数使用说明：\n"
                +"1. 选择训练文件;\n"
                +"2. 点击\"训练参数\";\n"
                +"3. 完成！";
		display.setText(introduction);
		//隐藏按钮
		panel1.setVisible(false);
		
	    scaleButton_selectFile=addButton("选择训练文件",gridCommand,subPanel,16);
		scaleButton_scaleFeature=addButton("训练参数",gridCommand,subPanel,16);
		
		
		subPanel.add(scaleButton_selectFile);
		subPanel.add(scaleButton_scaleFeature);
	
		
	}
	
	public void scaleFunction(){
		String introduction="scale使用说明：\n"
                +"1. 选择训练文件集;\n"
                +"2. 点击scale进行缩放;\n"
                +"3. 完成！";
		display.setText(introduction);
		//隐藏按钮
		scaleButton.setVisible(false);
		gridButton.setVisible(false);
		trainButton.setVisible(false);
		predictButton.setVisible(false);
		
	    scaleButton_selectFile=addButton("选择文件",scaleCommand,subPanel,16);
		scaleButton_scaleFeature=addButton("scale特征",scaleCommand,subPanel,16);
		
		JPanel subPanel1=new JPanel(new GridLayout(1, 2));
		JPanel subPanel2=new JPanel(new GridLayout(1, 2));
		
		scale_lower=new JTextField("-1");
		scale_upper=new JTextField("1");
//		scale_lower.setFont(new Font("Dialog",Font.PLAIN,16));
//		scale_upper.setFont(new Font("Dialog",Font.PLAIN,16));
		
		JLabel lowerLabel=new JLabel("lower值:");
		JLabel upperLabel=new JLabel("upper值:");
		lowerLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		upperLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		
		subPanel1.add(lowerLabel);
		subPanel1.add(scale_lower);
		subPanel2.add(upperLabel);
		subPanel2.add(scale_upper);
		
		
		subPanel.add(subPanel1);
		subPanel.add(subPanel2);
	
		
	}
}
