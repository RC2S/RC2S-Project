package com.rc2s.dao;

import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author captp
 */
public interface IUserDAO extends UserDetailsService
{
	public User getUser(String login);
    public ArrayList<com.rc2s.common.vo.User> getUsers();
}
