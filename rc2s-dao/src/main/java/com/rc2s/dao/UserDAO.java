package com.rc2s.dao;

import java.util.ArrayList;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends DaoAuthenticationProvider implements IUserDAO
{  
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
	
	@Override
	public User getUser(String login) {
        ArrayList<User> userList = new ArrayList<>();
		userList = (ArrayList<User>) sessionFactory.getCurrentSession()
				.createQuery("FROM User WHERE login = :login")
				.setParameter(":login", login)
				.list();
        if (userList.size() > 0)
            return userList.get(0);
        else
            return null;    
    }
    
    @Override
    public ArrayList<com.rc2s.common.vo.User> getUsers()
    {
		return (ArrayList<com.rc2s.common.vo.User>) sessionFactory.getCurrentSession().createQuery("FROM User").list();
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	private User buildUserFromUserEntity(com.rc2s.common.vo.User userEntity) {
		// convert model user to spring security user
		String username = userEntity.getUsername();
		String password = userEntity.getPassword();
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		ArrayList<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userEntity.getRole()));

		User springUser = new User(username, password, enabled,
			accountNonExpired, credentialsNonExpired, accountNonLocked,
			authorities);
		return springUser;
	}
}