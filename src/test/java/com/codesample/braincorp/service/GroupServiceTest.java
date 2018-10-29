package com.codesample.braincorp.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyInt;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codesample.braincorp.exception.NixFileNotFoundException;
import com.codesample.braincorp.exception.RecordNotFoundException;
import com.codesample.braincorp.model.Group;
import com.codesample.braincorp.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupServiceTest {

	@InjectMocks
	GroupService groupService;
	
	@Value("${testGroupFile}")
	private String testGroupFile;
	
	@Mock
	PasswordService passwordService;
	
	@Test
	public void testGetGroups() {
		Whitebox.setInternalState(groupService, "groupFile", testGroupFile);
		List<Group> groups = groupService.getGroups();
		Assert.assertNotNull(groups);
	}
	
	@Test(expected = NixFileNotFoundException.class)
	public void testGetGroupsFileNotPresent() {
		Whitebox.setInternalState(groupService, "groupFile", "abc.txt");
		groupService.getGroups();
	}
	
	@Test
	public void testFilterGroups() {
		Whitebox.setInternalState(groupService, "groupFile", testGroupFile);
		List<Group> groups = groupService.getGroups();
		
		Assert.assertEquals(3, groupService.filterGroups(groups, null, null, null).size());

		Assert.assertEquals(1, groupService.filterGroups(groups, "cdrom", null, null).size());
		Assert.assertEquals(1, groupService.filterGroups(groups, null, new Integer(1000), null).size());
		Assert.assertEquals(1, groupService.filterGroups(groups, "cdrom", new Integer(1000), null).size());
		Assert.assertEquals(0, groupService.filterGroups(groups, "cdro2", new Integer(1000), null).size());
		
		List<String> members = new ArrayList<String>();
		members.add("john");
		Assert.assertEquals(1, groupService.filterGroups(groups, null, null, members).size());
		members.add("tom");
		Assert.assertEquals(1, groupService.filterGroups(groups, null, null, members).size());
		members.add("jim");
		Assert.assertEquals(0, groupService.filterGroups(groups, null, null, members).size());
		
	}
	
	@Test
	public void testGetGroupById() {
		Whitebox.setInternalState(groupService, "groupFile", testGroupFile);
		Group group  = groupService.getGroupById(new Integer(1000));
		Assert.assertEquals("cdrom",group.getName());
		
	}
	
	@Test(expected = NixFileNotFoundException.class)
	public void testGetUserByIdIncorrectFile() {
		Whitebox.setInternalState(groupService, "groupFile", "abc.txt");
		groupService.getGroupById(new Integer(1000));
	}
	
	@Test(expected = RecordNotFoundException.class)
	public void testGetUserByIdNoRecord() {
		Whitebox.setInternalState(groupService, "groupFile", testGroupFile);
		groupService.getGroupById(new Integer(1005));
	}
	
	@Test
	public void testGetGroupByUserId() {
		Whitebox.setInternalState(groupService, "groupFile", testGroupFile);
		User user = new User();
		user.setName("tom");
		when(passwordService.getUserById(anyInt())).thenReturn(user);
		List<Group> group  = groupService.getGroupByUserId(new Integer(1000));
		Assert.assertEquals("cdrom",group.get(0).getName());
		Assert.assertEquals(1,group.size());
	}
}
