package com.accenture.com.snoskov.adf.universityx.programs.rest;

import com.accenture.com.snoskov.adf.universityx.UniversityXAuthServiceApplication;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { UniversityXAuthServiceApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProgramControllerIntegrationTest {

    private static final int PORT = 8080;

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl91c2VyIiwicm9sZSI6IkFETUlOIiwiZXhwIjo1OTk3MTA0MzczLCJpYXQiOjE1ODIwNjQzNzN9.qDEdYizlPs4fp9yi-NYa-Yska_cyqXkSTnrxNafgyg0";

    private static final String BASE_URL = "/apis/programs";

    private static final String CONTENT_TYPE_JSON = "application/json";

    private Header adminRoleHeader;

    @BeforeClass
    public static void initialize() {
        RestAssured.port = PORT;
    }

    @Before
    public void setup() {
        adminRoleHeader = new Header("Authorization", "Bearer " + JWT_TOKEN);
    }

    @Test
    public void testRetrieveStudyPrograms() {
        Response response = RestAssured.given()
                .header(adminRoleHeader)
                .contentType(CONTENT_TYPE_JSON)
                .get(BASE_URL);

        String json = response.asString();
        assertTrue(json.contains("\"programId\":1"));
    }
}