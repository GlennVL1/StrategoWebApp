/*
* Software Development
* Karel de Grote-hogeschool
* 2013-2014
*/

import be.kdg.model.User;
import be.kdg.persistence.api.UserDbOperations;
import be.kdg.persistence.impl.UserDbOperationsImplementation;
import org.junit.After;
import org.junit.Before;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class Test {
    private User user = new User("username", "password", "email");
    private UserDbOperations operations = new UserDbOperationsImplementation();

    @Before
    public void insertBasicUser() {
        operations.insertNewUser(user);
    }

    @After
    public void removeBasicUser() {
        operations.removeUser(user);
    }

    @org.junit.Test
    public void testBasicDatabaseInsert() {
        User user = new User("username", "password", "email");
        UserDbOperations operations = new UserDbOperationsImplementation();
        operations.insertNewUser(user);
        assertEquals("users should be the same", user.toString(), operations.getUserById(user.getId()).toString());
    }

    @org.junit.Test
    public void testCorrectLogin() {
        assertTrue(operations.checkLogin("username", "password"));
    }

    @org.junit.Test
    public void testIncorrectLogin() {
        assertFalse(operations.checkLogin("username", "wrongPassword"));
    }
}
