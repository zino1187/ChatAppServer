/*
 * 서버에 접속된 소켓은 다른 접속소켓과는 별도, 독립적으로 대화를 
 * 수행해야 하므로, 별도의 쓰레드로 정의해둬야 한다..
 * 따라서 기존의 ChatServer 클래스는 더이상 대화를 관여하지 않고,
 * 서버로서 접속자만 무한대로 감지하면 된다..
 * */
package com.study.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

//쓰레드는 대화를 담당해야 하므로, 각각 소켓을 보유하면 된다..
//또한 쓰레드로 정의되면, 이 객체들간 독립적으로 수행이 보장된다..
public class ServerThread extends Thread{
	ChatServer chatServer;
	Socket client;
	BufferedReader buffr;
	BufferedWriter buffw;
	
	public ServerThread(ChatServer chatServer, Socket client) {
		this.chatServer=chatServer;
		this.client=client;
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf8"));    
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	//듣고 
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();
			send(msg);
			chatServer.area.append(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	//말하고 
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true){
			listen();
		}
	}
}














