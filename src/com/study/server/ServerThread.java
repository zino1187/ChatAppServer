/*
 * ������ ���ӵ� ������ �ٸ� ���Ӽ��ϰ��� ����, ���������� ��ȭ�� 
 * �����ؾ� �ϹǷ�, ������ ������� �����ص־� �Ѵ�..
 * ���� ������ ChatServer Ŭ������ ���̻� ��ȭ�� �������� �ʰ�,
 * �����μ� �����ڸ� ���Ѵ�� �����ϸ� �ȴ�..
 * */
package com.study.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

//������� ��ȭ�� ����ؾ� �ϹǷ�, ���� ������ �����ϸ� �ȴ�..
//���� ������� ���ǵǸ�, �� ��ü�鰣 ���������� ������ ����ȴ�..
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
	
	//��� 
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
	//���ϰ� 
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














