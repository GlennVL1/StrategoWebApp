package be.kdg.beans;


import be.kdg.model.User;
import be.kdg.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by wouter on 6/02/14.
 */
@Component("userBean")
@Scope("session")
public class UserBean implements Serializable{
    private String username;
    private String password;
    private User user;
    @Autowired
    private UserService userService;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String checkCredentials()
    {
        if(userService.userIsValid(username,password))
        {
            return "index.xhtml";
        }
        return null;
    }
}