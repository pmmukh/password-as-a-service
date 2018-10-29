package com.codesample.braincorp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codesample.braincorp.exception.MalformedFileException;
import com.codesample.braincorp.exception.NixFileNotFoundException;
import com.codesample.braincorp.exception.RecordNotFoundException;
import com.codesample.braincorp.model.Group;
import com.codesample.braincorp.model.User;
import com.codesample.braincorp.service.PasswordService;

@Service
public class GroupService {

	@Value("${groupFile}")
	private String groupFile;
	
	@Autowired
	PasswordService passwordService;
	
	private Group fillGroup(String[] groupParts) {
		Group group = new Group();
		group.setName(groupParts[0]);
		group.setGid(Integer.parseInt(groupParts[2]));
		if(groupParts.length > 3)
			group.setMembers(Arrays.asList(groupParts[3].split(",")));
		return group;
	}
	
	private void validateGroupEntry(String[] groupParts) throws MalformedFileException{
		if(groupParts.length < 3)
			throw new MalformedFileException("A group entry does not have all components; Please check file");
		if(!StringUtils.isNumeric(groupParts[2]))
			throw new MalformedFileException("Group Id for an entry is not a valid number; Please check file");
		
	}
	
	public List<Group> getGroups() throws NixFileNotFoundException{
		List<Group> groupList = new ArrayList<Group>();
		try (Stream<String> stream = Files.lines(Paths.get(groupFile))) {
			
			List<String> groups = stream.collect(Collectors.toList());
			for(String g : groups) {
				String[] groupParts = g.split(":");
				validateGroupEntry(groupParts);
				Group group = fillGroup(groupParts);
				groupList.add(group);
			}
			stream.close();
			return groupList;
		} catch (IOException e) {
			throw new NixFileNotFoundException("no group file present at the configured location");
		}
	}
	
	public List<Group> filterGroups(List<Group> groups,String name,Integer gid,List<String> members){
		Stream<Group> stream = groups.stream();
		if(name != null)
			stream = stream.filter(p -> p.getName().equals(name));
		if(gid != null)
			stream = stream.filter(p -> p.getGid().equals(gid));
		if(members != null && !members.isEmpty())
			stream = stream.filter(p -> p.getMembers().containsAll(members));
		List<Group> filteredGroups = stream.collect(Collectors.toList());
		stream.close();
		return filteredGroups;
		
	}
	
	public Group getGroupById(Integer gid) throws NixFileNotFoundException,RecordNotFoundException{
		try (Stream<String> stream = Files.lines(Paths.get(groupFile))) {
			
			List<String> groups = stream.collect(Collectors.toList());
			stream.close();
			for(String g : groups) {
				String[] groupParts = g.split(":");
				Integer currId = Integer.parseInt(groupParts[2]);
				if(!gid.equals(currId))
					continue;
				validateGroupEntry(groupParts);
				Group group = fillGroup(groupParts);
				return group;
			}
			throw new RecordNotFoundException("No Group exists for the given ID");
		} catch(NumberFormatException nfe) {
			throw new MalformedFileException("Group Id for an entry is not a valid number; Please check group file");
		} catch(RecordNotFoundException re) {
			throw re;
		} catch (IOException e) {
			throw new NixFileNotFoundException("No group file present at the configured location");
		}
	}
	
	public List<Group> getGroupByUserId(Integer uid) throws NixFileNotFoundException,RecordNotFoundException{
		User user = passwordService.getUserById(uid);
		List<Group> groupList = new ArrayList<Group>();
		
		try (Stream<String> stream = Files.lines(Paths.get(groupFile))) {
			
			List<String> groups = stream.collect(Collectors.toList());
			stream.close();
			for(String g : groups) {
				String[] groupParts = g.split(":");
				if(groupParts.length < 4)
					continue;
				List<String> members = Arrays.asList(groupParts[3].split(","));
				if(!members.contains(user.getName()))
					continue;
				validateGroupEntry(groupParts);
				Group group = fillGroup(groupParts);
				groupList.add(group);
			}
			return groupList;
		} catch (IOException e) {
			throw new NixFileNotFoundException("No group file present at the configured location");
		}
		
	}
}
