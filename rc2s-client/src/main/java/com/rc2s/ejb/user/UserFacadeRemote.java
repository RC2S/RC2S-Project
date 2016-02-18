package com.rc2s.ejb.user;

import com.rc2s.common.vo.User;
import java.util.ArrayList;

public interface UserFacadeRemote
{
    public ArrayList<User> getAllUsers();
}
