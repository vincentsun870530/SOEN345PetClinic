package org.springframework.samples.petclinic.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WelcomeController.class)

public class WelcomeControllerTests {
	
	@Test
	public void testWelcome() {
		
		WelcomeController wc = new WelcomeController();
		assertEquals("welcome", wc.welcome());
		
	}
	
	
}
