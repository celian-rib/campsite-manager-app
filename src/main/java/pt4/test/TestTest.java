package pt4.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTest {

	@Test
	public void test() {
		
		assertTrue(true);
		
	}
	
	@Test 
	public void test2() {
		fail("it failed cuz i said so");
	}

}
