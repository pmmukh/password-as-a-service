package com.codesample.braincorp.controller;

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
import com.codesample.braincorp.service.GroupService;

@RestController
public class GroupController {

	@Autowired
	GroupService groupService;
	
    @RequestMapping("/groups")
    public List<Group> getGroups() {
    	try {
    		List<Group> groups = groupService.getGroups();
    		return groups;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }

    @RequestMapping("/groups/query")
    public List<Group> getFilteredGroups(@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "gid", required = false) Integer gid,@RequestParam(value = "member", required = false) List<String> members) {
    	try {
    		List<Group> groups = groupService.getGroups();
    		groups = groupService.filterGroups(groups, name, gid, members);
    		return groups;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }
    
    @RequestMapping("/groups/{gid}")
    public Group getGroupById(@PathVariable("gid") Integer gid) {
    	try {
    		Group group = groupService.getGroupById(gid);
    		return group;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(RecordNotFoundException re) {
			throw re;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }
    
    @RequestMapping("/users/{uid}/groups")
    public List<Group> getGroupsByUserId(@PathVariable("uid") Integer uid) {
    	try {
    		List<Group> groups = groupService.getGroupByUserId(uid);
    		return groups;
    	} catch(MalformedFileException mfe) {
    		throw mfe;
    	} catch(RecordNotFoundException re) {
			throw re;
    	} catch(NixFileNotFoundException ne) {
    		throw ne;
    	}
    }
}
