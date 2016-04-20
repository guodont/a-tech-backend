package controller;

import controllers.Application;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;
import utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
 * Created by guodont on 16/4/20.
 */
public class ApplicationTest {

    private Map userFormMap;

    @Test
    public void testSum() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void testString() {
        String str = "Hello world";
        assertFalse(str.isEmpty());
    }


    @Test
    public void testIndex() {
        Result result = Application.index();
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/plain");
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("Welcome");
    }

    @Before
    public void setup() {
        userFormMap = new HashMap<>();
        userFormMap.put("password","12345678");
        userFormMap.put("phone","18000009999");
        userFormMap.put("userName","testUser01");
    }

    @Test
    public void singUpStepOne() {
        running(fakeApplication(), () -> {

            Map<String,String> data = new HashMap<String,String>();
            data.put("password","12345678");
            data.put("phone","18000009998");
            data.put("userName","testUser01");

            Application.SignUpStepOne ss = new Application.SignUpStepOne();
            ss.userName = "testUser02";
            ss.phone = "18000009998";
            ss.password = "12345678";

            // Arrange
            FakeRequest request = new FakeRequest("POST", "/api/v1/signup/one");
            request.withHeader("Content-Type","application/json;charset=UTF-8");
//            request.withFormUrlEncodedBody(data);
            request.withJsonBody(JsonUtils.object2Node(ss));

            // Act
            Result result = route(request);

            System.out.print(contentAsString(result));
            System.out.print(request.getWrappedRequest());
            // Assert
            assertThat(status(result)).isEqualTo(CREATED);
            assertThat(contentType(result)).isEqualTo("application/json");
            assertThat(contentAsString(result)).contains("User created successfully");
        });
    }
}
