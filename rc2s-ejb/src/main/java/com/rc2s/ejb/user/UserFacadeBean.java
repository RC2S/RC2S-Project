package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import java.util.List;
import javax.ejb.EJB;

@Stateless(mappedName = "UserEJB")
public class UserFacadeBean implements UserFacadeRemote
{	
    @EJB
    private IUserService userService;
    
    @Override
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
