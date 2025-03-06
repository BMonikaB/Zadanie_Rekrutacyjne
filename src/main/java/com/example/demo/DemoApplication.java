package com.example.demo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class DemoApplication implements QuarkusApplication {

	@Override
	public int run(String... args) throws Exception {
		Quarkus.run(args);
		return 0;
	}

	public static void main(String... args) {
		Quarkus.run(DemoApplication.class, args);
	}
}