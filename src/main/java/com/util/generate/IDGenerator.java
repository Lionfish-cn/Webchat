package com.util.generate;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Random;

import org.junit.Test;


public class IDGenerator {
	public static String generatorID() {
		Date date = new Date();
		Long t = date.getTime();
		Random r = new Random(1);
		Integer rInt = r.nextInt(1000);
		Long num = t + rInt;
		Encoder encoder =  Base64.getEncoder();
		String Id = encoder.encodeToString(String.valueOf(num).getBytes());
		return Id;
	}
	
	@Test
	public void printId() {
		System.out.println(generatorID());
	}
}
