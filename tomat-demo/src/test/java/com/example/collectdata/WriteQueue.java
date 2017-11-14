package com.example.collectdata;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteQueue implements Serializable{

	
	private static final long serialVersionUID = -7915038926565545343L;
	
	private static Queue<Object> queue = new ConcurrentLinkedQueue<Object>();
	
	private static WriteQueue writeQueue = null;
	static{
		writeQueue = new WriteQueue();
	}
	
	public static WriteQueue getInstance(){
		return writeQueue;
	}
	
	public void addQueue(Object o){
		queue.add(o);
	}
	
	public Queue getQueue(){
		return queue;
	}

}
