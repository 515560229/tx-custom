package edu.ldcollege.tx.annotation;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ldcollege.tx.annotation.service.UserService;
import edu.ldcollege.tx.domain.Course;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AnnotationAppConfig.class})
public class AnnotationTest {

	@Autowired
	private UserService userService;

	@Test
	public void test01() {
		List<Course> courses = userService.queryUser();
		System.out.println(courses.size());
	}

}
