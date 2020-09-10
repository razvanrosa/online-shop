package org.fasttrackit.onlineshop.user;

import org.fasttrackit.onlineshop.domain.User;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.UserService;
import org.fasttrackit.onlineshop.transfer.SaveUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class UserServiceIntegrationTest {

    //field-injection
    @Autowired
    private UserService userService;

    @Test
    public void createUser_whenValidRequest_thenReturnSavedUser(){
        createUser();

    }

    @Test
    public void createUser_whenMissingFirstName_thenThrowException(){
        SaveUserRequest request = new SaveUserRequest();
        request.setFirstName(null);
        request.setLastName("Test Last Name");

        Exception exception = null;
        try {
            userService.createUser(request);
        }catch (Exception e ){
            exception = e;
        }
        assertThat(exception,notNullValue());
        assertThat("Unexpected exception type.",exception instanceof TransactionSystemException);
    }

    @Test
    public void getUser_whenExistingUser_thenReturnUser(){

        User createdUser = createUser();

        User userResponse = userService.getUser(createdUser.getId());

        assertThat(userResponse, notNullValue());
        assertThat(userResponse.getId(),is(createdUser.getId()));
        assertThat(userResponse.getFirstName(),is(createdUser.getFirstName()));
        assertThat(userResponse.getLastName(),is(createdUser.getLastName()));
    }

    @Test
    public void getUser_whenNonExistingUser_thenThrowResourceNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () ->userService.getUser(99999));

    }

    private User createUser() {
        SaveUserRequest request = new SaveUserRequest();

        request.setFirstName("Test First Name");
        request.setLastName("Test Last Name");

        User user = userService.createUser(request);

        assertThat(user, notNullValue());
        assertThat(user.getId(),greaterThan(0L));
        assertThat(user.getFirstName(), is(request.getFirstName()));
        assertThat(user.getLastName(), is(request.getLastName()));

        return user;
    }
}
