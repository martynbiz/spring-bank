package biz.rtyn.springbank.account;

import biz.rtyn.springbank.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class AccountControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void listAccounts() {
        var response = mvc.get().uri("/accounts");

        assertThat(response).hasStatusOk();
        assertThat(response).hasContentType(MediaType.APPLICATION_JSON);
        assertThat(response).bodyJson().extractingPath("$[0].name").isEqualTo("John");
        assertThat(response).bodyJson().extractingPath("$[1].name").isEqualTo("Mary");
    }
}
