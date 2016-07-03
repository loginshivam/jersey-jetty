package com.sample.jersey;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		while(true){
			
			try {
				URL url = new URL("http://localhost:8071/jersey/data");
				try {
				System.out.println(url.openConnection().getContent());
					Thread.sleep(3000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
