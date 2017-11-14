package com.example.amqp;

public interface T {
	int test1();
	default int test2(){
		return 1;
	}
}
