package edu.harvard.h2ms.common;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TestHelpers {
    static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	/**
	 * This can be used to read the JSON result of a compliance endpoint.
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Double> getDoubleMap(String json) {
		Map<String, Double> result;
		
		ObjectMapper objectMapper = new ObjectMapper();
		TypeFactory typeFactory = TypeFactory.defaultInstance();
		
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Double.class);
		try {
			return objectMapper.readValue(json, mapType);
		} catch(Exception e) {
			return null;
		}
	}
	
    public static String obtainAccessToken(MockMvc mvc, String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result =
                mvc.perform(
                        post("/oauth/token")
                                .params(params)
                                .with(httpBasic("h2ms", "secret"))
                                .accept("application/json"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(CONTENT_TYPE));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
    
}
