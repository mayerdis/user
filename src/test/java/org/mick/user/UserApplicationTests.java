package org.mick.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mick.user.Dto.UserDto;
import org.mick.user.Entity.Phone;
import org.mick.user.Entity.User;
import org.mick.user.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ UserApplication.class })
class UserApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

	@ParameterizedTest
	@MethodSource("userJsonValueTest")
	void createUserTest(String input) throws Exception {
		Object user = new ObjectMapper().readValue(mockMvc.perform(post("/api/v1/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(input))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString(), User.class);
		assertThat(user).isInstanceOf(User.class);
	}

	@Test
	void searchUserTest() throws Exception {
		Object user = new ObjectMapper().readValue(mockMvc.perform(get("/api/v1/getUserByEmail").param("email","Michael_Ayerdis@maag.org")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), UserDto.class);
		assertThat(user).isInstanceOf(UserDto.class);
	}

	private static Stream<String> userJsonValueTest() throws JsonProcessingException {
		User userTest = new User();
		userTest.setName("Michael Ayerdis");
		userTest.setPassword("Hunt$?dfgd545er2");
		userTest.setEmail("michael_ayerdis@maag.org");
		Phone phone = new Phone();
		phone.setUserId(userTest);
		phone.setCitycode("01");
		phone.setContrycode("02");
		phone.setNumber("123456789");
		userTest.setPhones(new ArrayList<>() {{add(phone);}});
		return Stream.of(new ObjectMapper().writeValueAsString(userTest));
	}

}
