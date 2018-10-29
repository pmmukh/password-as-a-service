package com.codesample.braincorp.service;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codesample.braincorp.exception.NixFileNotFoundException;
import com.codesample.braincorp.exception.RecordNotFoundException;
import com.codesample.braincorp.model.User;
import com.codesample.braincorp.exception.MalformedFileException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordServiceTest {
	
	@InjectMocks
	PasswordService passwordService;
	
	@Value("${testPasswordFile}")
	private String testPasswordFile;
	
	@Test
	public void testGetUsers() {
		Whitebox.setInternalState(passwordService, "passwordFile", testPasswordFile);
		List<User> users = passwordService.getUsers();
		Assert.assertNotNull(users);
	}
	
	@Test(expected = NixFileNotFoundException.class)
	public void testGetUsersIncorrectFile() {
		Whitebox.setInternalState(passwordService, "passwordFile", "abc.txt");
		passwordService.getUsers();
	}
	
	@Test
	public void testFilterUsers() {
		Whitebox.setInternalState(passwordService, "passwordFile", testPasswordFile);
		List<User> users = passwordService.getUsers();
		
		Assert.assertEquals(3, passwordService.filterUsers(users, null, null, null, null, null, null).size());
		Assert.assertEquals(1, passwordService.filterUsers(users, "tom", null, null, null, null, null).size());
		Assert.assertEquals(0, passwordService.filterUsers(users, "jim", null, null, null, null, null).size());
		
		Assert.assertEquals(1, passwordService.filterUsers(users, null, new Integer(1000), null, null, null, null).size());
		Assert.assertEquals(0, passwordService.filterUsers(users, null, new Integer(1005), null, null, null, null).size());
		Assert.assertEquals(1, passwordService.filterUsers(users, "tom", new Integer(1000), null, null, null, null).size());
		
		Assert.assertEquals(1, passwordService.filterUsers(users, null, new Integer(1000), new Integer(1000), null, null, null).size());
		Assert.assertEquals(3, passwordService.filterUsers(users, null, null, new Integer(1000), null, null, null).size());
		Assert.assertEquals(0, passwordService.filterUsers(users, null, new Integer(1000), new Integer(1005), null, null, null).size());
			
		Assert.assertEquals(1, passwordService.filterUsers(users, null, null,null, "John", null, null).size());
		Assert.assertEquals(1, passwordService.filterUsers(users, null, null, null, null, "/home/vivek", null).size());
		Assert.assertEquals(3, passwordService.filterUsers(users, null, null, null, null, null, "/bin/bash").size());

		Assert.assertEquals(0, passwordService.filterUsers(users, null, null, null, null, "/home/viv", null).size());
		Assert.assertEquals(0, passwordService.filterUsers(users, null, null, null, null, null, "/bin").size());
		
	}
	
	@Test
	public void testGetUserById() {
		Whitebox.setInternalState(passwordService, "passwordFile", testPasswordFile);
		User user = passwordService.getUserById(new Integer(1000));
		Assert.assertEquals("tom",user.getName());
		
	}
	
	@Test(expected = NixFileNotFoundException.class)
	public void testGetUserByIdIncorrectFile() {
		Whitebox.setInternalState(passwordService, "passwordFile", "abc.txt");
		passwordService.getUserById(new Integer(1000));
	}
	
	@Test(expected = RecordNotFoundException.class)
	public void testGetUserByIdNoRecord() {
		Whitebox.setInternalState(passwordService, "passwordFile", testPasswordFile);
		passwordService.getUserById(new Integer(1005));
	}
}
