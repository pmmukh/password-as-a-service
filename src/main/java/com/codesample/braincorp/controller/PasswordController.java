package com.codesample.braincorp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codesample.braincorp.exception.MalformedFileException;
import com.codesample.braincorp.exception.NixFileNotFoundException;
import com.codesample.braincorp.exception.RecordNotFoundException;
import com.codesample.braincorp.model.Group;
import com.codesample.braincorp.model.User;
import com.codesample.braincorp.service.GroupService;
import com.codesample.braincorp.service.PasswordService;

@RestController
public class PasswordController {

	@Autowired
	PasswordService passwordService;

    @RequestMapping("/users")
    public List<User> getUsers() {
    	try {
    		List<User> users = passwordService.getUsers();
    		return users;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }

    @RequestMapping("/users/query")
    public List<User> getFilteredUsers(@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "uid", required = false) Integer uid,@RequestParam(value = "gid", required = false) Integer gid,
    		@RequestParam(value = "comment", required = false) String comment,@RequestParam(value = "home", required = false) String home,
    		@RequestParam(value = "shell", required = false) String shell) {
    	try {
    		List<User> users = passwordService.getUsers();
    		users = passwordService.filterUsers(users, name, uid, gid, comment, home, shell);
    		return users;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }
    
    @RequestMapping("/users/{uid}")
    public User getUserById(@PathVariable("uid") Integer uid) {
    	try {
    		User user = passwordService.getUserById(uid);
    		return user;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(RecordNotFoundException re) {
			throw re;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }
    
}
