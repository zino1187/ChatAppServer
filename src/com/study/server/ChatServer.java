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
	JPanel panel; //div 와 비슷한 컨테이너.. 디폴트 레이아웃은 FlowLayout 이다 
	JTextField t_port; // 안드로이드의 EditText 
	JButton bt_start; //서버 가동 버튼 
	JTextArea area; // 안드로이드의 EditText에 inputText=mulineText.. 와 비슷...
	JScrollPane scroll; //안드로이드의 ScrollView 
	
	//대화에 앞서 접속이 선행되어야하므로, 모든 클라이언트의 접속을 감지할 수있는
	//서버 소켓을 준비하자!!
	ServerSocket server;
	Thread thread; //안드로이드와 마찬가지로 메인쓰레드를 네트워크 접속이나
	//무한대기 상태에 빠뜨리는 것은 올바르지 않다..
	//왜?? 메인쓰레드는 UI제어, 그래픽처리, 이벤트감지 등등 프로세스를 운영하는
	//실행부이기 때문에...이 실행부를 무한대기 상태에 빠뜨리면 프로그램 자체가 멈추게
	//된다..동작하지 않음..
	
	public ChatServer() {
		//has a 관계의 객체들은 부품에 해당하므로, 그 생성시점이 빨라야 한다. 따라서
		//생성자의 역할은 언제나 중요하다...
		panel = new JPanel();
		t_port = new JTextField(15);
		bt_start = new JButton("서버가동");
		area = new JTextArea(10,20);
		scroll  =new JScrollPane(area);
		
		//패널에 포트입력란과 버튼을 부착!!
		panel.add(t_port);
		panel.add(bt_start);
		
		//윈도우는 기본 레이아웃이 BorderLayout이라 한다...
		//동서남북 센터의 방향을 갖는 레이아웃 
		//북쪽에 패널 붙이자!!
		add(panel, BorderLayout.NORTH); 
		add(scroll); //윈도우 중앙에 부착!!
		
		//재사용성이 떨어지고, 별도의 .java로 만들기도 싫을때는 내부익명클래스로 구현
		//하는게 편하다, 주로 이벤트 구현시 내부익명은 상당히 많이 쓴다 
		bt_start.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//서버 가동!!
				thread.start();
			}
		});
		
		//쓰레드 재정의!!
		thread = new Thread(this);
		
		setSize(300,400);
		setVisible(true);//디폴트가 않보이기 속성이므로, 반드시 true를 줘야 보인다!!
		setDefaultCloseOperation(EXIT_ON_CLOSE);//윈도우 창 닫으면 프로세스종료		
	}
	
	//서버 가동 메서드 정의 
	public void startServer(){
		//네트워크 상에 pc간 데이터를 주고 받으려면, 서로의 위치를 알아야 한다
		//우리가 사용중인 프로토콜은 인터넷 기반이고 tcp/ip를 사용하므로, 결국 
		//ip를 알면 상대방 pc와 대화를 주고 받을 수 있긴 하나...만일 해당 pc에서 
		//실행중인 프로세스가 여러개 이상이면, 누구와 대화를 주고받을지 결정지을 수
		//없게된다..이문제를 해결하기 위해 모든 네트워크 프로그램은 포트번호로 구분
		//되어야 한다. 
		//웹브라우저 : 80, 오라클 1521, mssql 1433, mysql 3306....
		//1~1024번까지는 시스템이 이미 점유하고 있으므로, 사용하지 말자!!
		try {
			server = new ServerSocket(7777);
			area.append("서버가동됨...\n");
			
			Socket client=server.accept(); //클라이언트가 접속할때를 감지하는 메서드!!
			//접속하기 전까지 무한정 대기에 빠진다...
			
			String ip=client.getInetAddress().getHostAddress();
			area.append(ip+"님 접속\n");
			
			//소켓을 이용하면 개발자가 원격지에 떨어진 소켓과 대화를 나눌 수 있는 
			//코드를 작성할 수 있는데, 이때 네트워크 지식은 필요하지 않다!!
			//이유?? 소켓이라는 객체 자체내에 네트워크 핸들링코드 숨어있다..
			//따라서 개발자는 데이터를 입력할지, 출력할지에만 집중하면 된다...
			//모든 소켓에는 입력스트림과 출력스트림을 얻어낼 수 있는 메서드가 지원됨
			InputStream is=client.getInputStream();
			InputStreamReader reader; //한글 등 비영어권 문자는 2byte의 용량을 
			//갖으므로, 스트림을 문자기반으로 업글하자!!
			reader = new InputStreamReader(is, "utf-8"); //스트림 업그레이드!!
			BufferedReader buffr=new BufferedReader(reader);
			
			String data=null;
			
			while(true){
				data=buffr.readLine(); //스트림으로부터 1byte 읽어들임..
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



