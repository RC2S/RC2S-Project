package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import javax.ejb.Stateless;
import com.rc2s.application.services.user.IUserService;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

@Stateless(mappedName = "UserEJB")
@DeclareRoles({"admin"})
public class UserFacadeBean  implements UserFacadeRemote
{	
    @Inject
    private IUserService userService;
    
    @Override
    @RolesAllowed({"admin"})
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
