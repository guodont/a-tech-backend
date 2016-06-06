package model;

import models.User;
import models.enums.UserType;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static play.test.Helpers.*;

/**
 * Created by guodont on 16/4/20.
 */
public class UserTest {

    @Test
    public void saveUser() {
        running(fakeApplication(fakeGlobal()), () -> {

//            User user = new User();
//            user.realName = "realName01";
//            user.name = "testUser01";
//            user.address = "sxau";
//            user.avatar = "avatar.png";
//            user.email = "testUser01@workerhub.com";
//            user.lastIp = "127.0.0.1";
//            user.phone = "18404968725";
//            user.setPassword("12345678");
//            user.userType = UserType.PUBLIC;
//            user.save();

//            Assertions.assertThat(user.getId()).isNotNull();
        });
    }

}
