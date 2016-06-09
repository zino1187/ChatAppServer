package com.study.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer extends JFrame implements Runnable{
	JPanel panel; //div �� ����� �����̳�.. ����Ʈ ���̾ƿ��� FlowLayout �̴� 
	JTextField t_port; // �ȵ���̵��� EditText 
	JButton bt_start; //���� ���� ��ư 
	JTextArea area; // �ȵ���̵��� EditText�� inputText=mulineText.. �� ���...
	JScrollPane scroll; //�ȵ���̵��� ScrollView 
	
	//��ȭ�� �ռ� ������ ����Ǿ���ϹǷ�, ��� Ŭ���̾�Ʈ�� ������ ������ ���ִ�
	//���� ������ �غ�����!!
	ServerSocket server;
	Thread thread; //�ȵ���̵�� ���������� ���ξ����带 ��Ʈ��ũ �����̳�
	//���Ѵ�� ���¿� ���߸��� ���� �ùٸ��� �ʴ�..
	//��?? ���ξ������ UI����, �׷���ó��, �̺�Ʈ���� ��� ���μ����� ��ϴ�
	//������̱� ������...�� ����θ� ���Ѵ�� ���¿� ���߸��� ���α׷� ��ü�� ���߰�
	//�ȴ�..�������� ����..
	
	public ChatServer() {
		//has a ������ ��ü���� ��ǰ�� �ش��ϹǷ�, �� ���������� ����� �Ѵ�. ����
		//�������� ������ ������ �߿��ϴ�...
		panel = new JPanel();
		t_port = new JTextField(15);
		bt_start = new JButton("��������");
		area = new JTextArea(10,20);
		scroll  =new JScrollPane(area);
		
		//�гο� ��Ʈ�Է¶��� ��ư�� ����!!
		panel.add(t_port);
		panel.add(bt_start);
		
		//������� �⺻ ���̾ƿ��� BorderLayout�̶� �Ѵ�...
		//�������� ������ ������ ���� ���̾ƿ� 
		//���ʿ� �г� ������!!
		add(panel, BorderLayout.NORTH); 
		add(scroll); //������ �߾ӿ� ����!!
		
		//���뼺�� ��������, ������ .java�� ����⵵ �������� �����͸�Ŭ������ ����
		//�ϴ°� ���ϴ�, �ַ� �̺�Ʈ ������ �����͸��� ����� ���� ���� 
		bt_start.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//���� ����!!
				thread.start();
			}
		});
		
		//������ ������!!
		thread = new Thread(this);
		
		setSize(300,400);
		setVisible(true);//����Ʈ�� �ʺ��̱� �Ӽ��̹Ƿ�, �ݵ�� true�� ��� ���δ�!!
		setDefaultCloseOperation(EXIT_ON_CLOSE);//������ â ������ ���μ�������		
	}
	
	//���� ���� �޼��� ���� 
	public void startServer(){
		//��Ʈ��ũ �� pc�� �����͸� �ְ� ��������, ������ ��ġ�� �˾ƾ� �Ѵ�
		//�츮�� ������� ���������� ���ͳ� ����̰� tcp/ip�� ����ϹǷ�, �ᱹ 
		//ip�� �˸� ���� pc�� ��ȭ�� �ְ� ���� �� �ֱ� �ϳ�...���� �ش� pc���� 
		//�������� ���μ����� ������ �̻��̸�, ������ ��ȭ�� �ְ������ �������� ��
		//���Եȴ�..�̹����� �ذ��ϱ� ���� ��� ��Ʈ��ũ ���α׷��� ��Ʈ��ȣ�� ����
		//�Ǿ�� �Ѵ�. 
		//�������� : 80, ����Ŭ 1521, mssql 1433, mysql 3306....
		//1~1024�������� �ý����� �̹� �����ϰ� �����Ƿ�, ������� ����!!
		try {
			server = new ServerSocket(7777);
			area.append("����������...\n");
			
			Socket client=server.accept(); //Ŭ���̾�Ʈ�� �����Ҷ��� �����ϴ� �޼���!!
			//�����ϱ� ������ ������ ��⿡ ������...
			
			String ip=client.getInetAddress().getHostAddress();
			area.append(ip+"�� ����\n");
			
			//������ �̿��ϸ� �����ڰ� �������� ������ ���ϰ� ��ȭ�� ���� �� �ִ� 
			//�ڵ带 �ۼ��� �� �ִµ�, �̶� ��Ʈ��ũ ������ �ʿ����� �ʴ�!!
			//����?? �����̶�� ��ü ��ü���� ��Ʈ��ũ �ڵ鸵�ڵ� �����ִ�..
			//���� �����ڴ� �����͸� �Է�����, ����������� �����ϸ� �ȴ�...
			//��� ���Ͽ��� �Է½�Ʈ���� ��½�Ʈ���� �� �� �ִ� �޼��尡 ������
			InputStream is=client.getInputStream();
			InputStreamReader reader; //�ѱ� �� �񿵾�� ���ڴ� 2byte�� �뷮�� 
			//�����Ƿ�, ��Ʈ���� ���ڱ������ ��������!!
			reader = new InputStreamReader(is, "utf-8"); //��Ʈ�� ���׷��̵�!!
			BufferedReader buffr=new BufferedReader(reader);
			
			String data=null;
			
			while(true){
				data=buffr.readLine(); //��Ʈ�����κ��� 1byte �о����..
				area.append(data +"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		startServer();		
	}
	
	public static void main(String[] args) {
		new ChatServer();

	}

}



