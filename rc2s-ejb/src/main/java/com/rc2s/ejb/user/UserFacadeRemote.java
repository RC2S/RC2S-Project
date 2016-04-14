package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface UserFacadeRemote
{
    public List<User> getAllUsers();
}
