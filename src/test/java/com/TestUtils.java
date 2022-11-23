package com;

import io.restassured.path.json.JsonPath;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

public class TestUtils {

    public static JsonPath getPath(String filename, String context) throws FileNotFoundException {
        return new JsonPath(ResourceUtils.getFile(String.format("classpath:%s/%s", context, filename)));
    }
}
