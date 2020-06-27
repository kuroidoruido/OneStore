package integration;

import static integration.helpers.AuthHelper.connectUser;
import static integration.helpers.AuthHelper.DELETE;
import static integration.helpers.AuthHelper.GET;
import static integration.helpers.AuthHelper.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import integration.helpers.AuthHelper;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import onestore.rest.model.UserData;
import onestore.rest.model.UserInfo;

@MicronautTest
public class SimpleScenariiSpec {

    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGetUserInfos() {
        var authInfo = connectUser(client, "katsuo49",
                "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
        var getUserInfo = GET(authInfo, "/users/" + authInfo.username);
        var userInfo = client.toBlocking().retrieve(getUserInfo, UserInfo.class);

        assertNotNull(userInfo);
        assertEquals(userInfo.getUsername(), "katsuo49");
        assertEquals(userInfo.getFirstname(), "Anthony");
        assertEquals(userInfo.getLastname(), "Pena");
        assertEquals(userInfo.getBirthday(), "1993-02-28");
    }

    @Test
    void testCanReadAllMetadataOfOneUser() {
        var authInfo = connectUser(client, "katsuo49",
                "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
        var getAllMetadatas = GET(authInfo, "/metadata/" + authInfo.username);
        List<LinkedHashMap<String, Object>> userMetadatas = client.toBlocking().retrieve(getAllMetadatas, List.class);

        assertNotNull(userMetadatas);
        assertTrue(userMetadatas.size() > 0);
        var found = false;
        for (var metadata : userMetadatas) {
            if (metadata.get("dataContext").equals("Collektor-collections")) {
                found = true;
            }
        }
        if (!found) {
            fail("The user metadata list does not contains the metadata 'Collektor-collections'");
        }
    }

    @Test
    void testOwnerCanReadAllDataFromAContext() {
        var authInfo = connectUser(client, "katsuo49",
                "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");
        var getAllDatas = GET(authInfo, "/data/" + authInfo.username + "/Collektor-collections");
        List<UserData> userDatas = client.toBlocking().retrieve(getAllDatas, List.class);

        assertNotNull(userDatas);
        assertTrue(userDatas.size() > 0);
    }

    @Test
    void testAnonymousCanReadAllDataFromAContext() {
        var authInfo = AuthHelper.ANONYMOUS;
        var getAllDatas = GET(authInfo, "/data/katsuo49/Collektor-collections");
        List<UserData> userDatas = client.toBlocking().retrieve(getAllDatas, List.class);

        assertNotNull(userDatas);
        assertTrue(userDatas.size() > 0);
    }

    @Test
    void testInsertReadData() {
        var authInfo = connectUser(client, "katsuo49",
                "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");

        // insert one data
        var basicData = Map.of("hello", "world");
        var basicDataInsert = POST(authInfo, "/data/" + authInfo.username + "/Collektor-collections", basicData);
        var insertedData = client.toBlocking().retrieve(basicDataInsert, UserData.class);

        assertNotNull(insertedData);
        assertNotNull(insertedData.getId());
        assertNotNull(insertedData.getContent());
        assertEquals(insertedData.getContent(), basicData);

        // read that data
        var getOne = GET(authInfo, "/data/" + authInfo.username + "/Collektor-collections/" + insertedData.getId());
        var oneData = client.toBlocking().retrieve(getOne, UserData.class);

        assertNotNull(oneData);
        assertNotNull(oneData.getId());
        assertEquals(oneData.getId(), insertedData.getId());
        assertNotNull(oneData.getContent());

        // read all data with the new data
        var getAll = GET(authInfo, "/data/" + authInfo.username + "/Collektor-collections");
        List<LinkedHashMap<String, Object>> allData = client.toBlocking().retrieve(getAll, List.class);

        assertNotNull(allData);
        assertTrue(allData.size() > 0);
        var insertedDataFromAllData = allData.stream().filter(data -> insertedData.getId().equals(data.get("id")))
                .findAny();
        assertTrue(insertedDataFromAllData.isPresent());
        assertEquals(insertedDataFromAllData.get().get("id"), insertedData.getId());

        // delete inserted data
        var deleteOne = DELETE(authInfo,
                "/data/" + authInfo.username + "/Collektor-collections/" + insertedData.getId());
        var deletedData = client.toBlocking().retrieve(deleteOne, UserData.class);
        assertNotNull(deletedData);
        assertEquals(deletedData.getId(), insertedData.getId());

        // read all data without the new data
        allData = client.toBlocking().retrieve(getAll, List.class);

        assertNotNull(allData);
        assertTrue(allData.size() > 0);
        insertedDataFromAllData = allData.stream().filter(data -> insertedData.getId().equals(data.get("id")))
                .findAny();
        assertTrue(insertedDataFromAllData.isEmpty());
    }
}