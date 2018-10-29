package com.codesample.braincorp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codesample.braincorp.exception.MalformedFileException;
import com.codesample.braincorp.exception.NixFileNotFoundException;
import com.codesample.braincorp.exception.RecordNotFoundException;
import com.codesample.braincorp.model.Group;
import com.codesample.braincorp.model.User;

@Service
public class PasswordService {
	
	@Value("${passwordFile}")
	private String passwordFile;

	private User fillUser(String[] userParts) {
		User user = new User();
		user.setName(userParts[0]);
		user.setUid(Integer.parseInt(userParts[2]));
		user.setGid(Integer.parseInt(userParts[3]));
		user.setComment(userParts[4]);
		user.setHome(userParts[5]);
		user.setShell(userParts[6]);
		return user;
	}

	private void validateUserEntry(String[] userParts) {
		if(userParts.length != 7)
			throw new MalformedFileException("A user entry does not have all components; Please check file");
		if(!StringUtils.isNumeric(userParts[2]))
			throw new MalformedFileException("User Id for an entry is not a valid number; Please check file");
		if(!StringUtils.isNumeric(userParts[3]))
			throw new MalformedFileException("Group Id for an entry is not a valid number; Please check file");
		
	}
	
	public List<User> getUsers() throws NixFileNotFoundException,MalformedFileException{
		List<User> userList = new ArrayList<User>();
		try (Stream<String> stream = Files.lines(Paths.get(passwordFile))) {
			
			List<String> users = stream.collect(Collectors.toList());
			for(String u : users) {
				String[] userParts = u.split(":");
				validateUserEntry(userParts);
				User user = fillUser(userParts);
				userList.add(user);
			}
			stream.close();
			return userList;
		} catch (IOException e) {
			throw new NixFileNotFoundException("no pwd file present at the configured location");
		}

	}
	
	public List<User> filterUsers(List<User> users, String name, Integer uid, Integer gid,
			String comment, String home, String shell){
		Stream<User> stream = users.stream();
		if(name != null)
			stream = stream.filter(p -> p.getName().equals(name));
		if(uid != null)
			stream = stream.filter(p -> p.getUid().equals(uid));
		if(gid != null)
			stream = stream.filter(p -> p.getGid().equals(gid));
		if(comment != null)
			stream = stream.filter(p -> p.getComment().equals(comment));
		if(home != null)
			stream = stream.filter(p -> p.getHome().equals(home));
		if(shell != null)
			stream = stream.filter(p -> p.getShell().equals(shell));
		
		List<User> filteredUsers = stream.collect(Collectors.toList());
		stream.close();
		return filteredUsers;
	}

	public User getUserById(Integer uid) throws NixFileNotFoundException,RecordNotFoundException,MalformedFileException{
		try (Stream<String> stream = Files.lines(Paths.get(passwordFile))) {
			
			List<String> users = stream.collect(Collectors.toList());
			stream.close();
			for(String u : users) {
				String[] userParts = u.split(":");
				Integer currId = Integer.parseInt(userParts[2]);
				if(!uid.equals(currId))
					continue;
				validateUserEntry(userParts);
				User user = fillUser(userParts);
				return user;
			}
			throw new RecordNotFoundException("No User exists for the given ID");
		} catch(NumberFormatException nfe) {
			throw new MalformedFileException("UserId for an entry is not a valid number; Please check file");
		} catch(RecordNotFoundException re) {
			throw re;
		} catch (IOException e) {
			throw new NixFileNotFoundException("no pwd file present at the configured location");
		}
	}
	
	
}
