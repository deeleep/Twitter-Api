//package com.dlpmeh.service;
//
//import com.dlpmeh.config.model.User;
//import com.dlpmeh.exception.UserException; 
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    public void testRegisterUser() throws UserException { 
//        User user = new User();
//        user.setEmail("test11@example.com");
//        user.setPassword("password123");
//
//        User savedUser = userService.registerUser(user);
//
//        assertNotNull(savedUser.getId());
//        assertEquals("test11@example.com", savedUser.getEmail());
//    }
//}


package com.dlpmeh.service;

import com.dlpmeh.config.model.User;
import com.dlpmeh.exception.UserException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    static Long savedUserId;

    @Test
    @Order(1)
    public void testRegisterUser() throws UserException {
        User user = new User();
        user.setEmail("test11@example.com");
        user.setPassword("password123");
        user.setUsername("testUser");

        User savedUser = userService.registerUser(user);
        savedUserId = savedUser.getId();

        assertNotNull(savedUser.getId());
        assertEquals("test11@example.com", savedUser.getEmail());
    }

    @Test
    @Order(2)
    public void testFindUserById() throws UserException {
        User found = userService.findUserById(savedUserId);
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    @Order(3)
    public void testUpdateUser() throws UserException {
        User update = new User();
        update.setUsername("updatedUser");

        User updated = userService.updateUser(savedUserId, update);
        assertEquals("updatedUser", updated.getUsername());
    }

    @Test
    @Order(4)
    public void testFollowUser() throws UserException {
        // Create another user to follow
        User anotherUser = new User();
        anotherUser.setEmail("followme@example.com");
        anotherUser.setPassword("pass456");
        anotherUser.setUsername("followTarget");

        User targetUser = userService.registerUser(anotherUser);

        User result = userService.followUser(savedUserId, targetUser);
        assertEquals("followTarget", result.getUsername());
    }

    @Test
    @Order(5)
    public void testSearchUser() {
        List<User> results = userService.searchUser("updatedUser");
        assertTrue(results.size() > 0);
    }

    // If applicable
    @Test
    @Order(6)
    public void testFindUserProfileByJwt() throws UserException {
        // If your implementation expects a JWT, you'd need to log in and generate a valid token.
        // This is just a placeholder:
        String fakeToken = "Bearer some.jwt.token";
        assertThrows(UserException.class, () -> {
            userService.findUserProfileByJwt(fakeToken);
        });
    }
}
