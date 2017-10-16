package edu.ldcollege.tx.xml;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ldcollege.tx.domain.Course;
import edu.ldcollege.tx.xml.service.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:edu/ldcollege/tx/xml/app.xml")
public class XmlTester {

	@Autowired
	private UserService userService;

	@Test
	public void test01() {
		List<Course> courses = userService.queryUser();
		System.out.println(courses.size());
	}

}
