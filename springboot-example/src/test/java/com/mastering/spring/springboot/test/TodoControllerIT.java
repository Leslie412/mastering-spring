package com.mastering.spring.springboot.test;

import java.net.URI;
import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mastering.spring.springboot.Application;
import com.mastering.spring.springboot.bean.Todo;
import com.mastering.spring.springboot.test.util.Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT {

	@LocalServerPort
	private int port;

	private TestRestTemplate template = new TestRestTemplate();

	@Test
	public void retrieveTodos() throws Exception {
		String expected = "[" + "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}" + ","
				+ "{id:2,user:Jack,desc:\"Learn Struts\",done:false}" + "]";

		String uri = "/users/Jack/todos";

		ResponseEntity<String> response = template.getForEntity(Utils.createURL(uri, port), String.class);

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void retrieveTodo() throws Exception {
		String expected = "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}";
		ResponseEntity<String> response = template.getForEntity(Utils.createURL("/users/Jack/todos/1", port),
				String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	public void addTodo() throws Exception {
		Todo todo = new Todo(-1, "Jill", "Learn Hibernate", new Date(),  
			      false);
		URI location = template.postForLocation(Utils.createURL("/users/Jill/todos", port), todo);
		System.out.println("##############" + location.getPath());
		Assert.assertThat(location.getPath(), CoreMatchers.containsString("/users/Jill/todos/4"));
	}
}
