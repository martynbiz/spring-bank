package biz.rtyn.springbank.account;

import biz.rtyn.springbank.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class AccountControllerTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void opensAnAccountWithAZeroGbpBalance() {
        var response = mvc.post().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Alice"}
                        """);

        assertThat(response).hasStatus(HttpStatus.CREATED);
        assertThat(response).headers().containsHeader("Location");
        assertThat(response).bodyJson().extractingPath("$.type").isEqualTo("LIABILITY");
        assertThat(response).bodyJson().extractingPath("$.balance.amount").isEqualTo("0.00");
        assertThat(response).bodyJson().extractingPath("$.balance.currency").isEqualTo("GBP");
        assertThat(response).bodyJson().doesNotHavePath("$.balance.zero");
    }

    @Test
    void listAccountsReturnsAccounts() {
        var response = mvc.get().uri("/accounts");

        assertThat(response).hasStatusOk();
        assertThat(response).hasContentType(MediaType.APPLICATION_JSON);
        assertThat(response).bodyJson().extractingPath("$[0].name").isEqualTo("John");
        assertThat(response).bodyJson().extractingPath("$[1].name").isEqualTo("Mary");
    }

    @Test
    void getAccountWithValidIdReturnsAccount() {
        var response = mvc.get().uri("/accounts/1");

        assertThat(response).hasStatusOk();
        assertThat(response).hasContentType(MediaType.APPLICATION_JSON);
        assertThat(response).bodyJson().extractingPath("$.name").isEqualTo("John");
        assertThat(response).bodyJson().extractingPath("$.type").isEqualTo("LIABILITY");
        assertThat(response).bodyJson().extractingPath("$.balance.amount").isEqualTo("0.00");
        assertThat(response).bodyJson().extractingPath("$.balance.currency").isEqualTo("GBP");
        assertThat(response).bodyJson().doesNotHavePath("$.balance.zero");
    }

    @Test
    void getAccountWithInvalidIdReturnsErrorResponse() {
        var response = mvc.get().uri("/accounts/999");

        assertThat(response).hasStatus4xxClientError();
        assertThat(response).hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response).bodyJson().isLenientlyEqualTo("""
                {
                  "title": "Not Found",
                  "status": 404,
                  "detail": "Account 999 was not found",
                  "instance": "/accounts/999"
                }
                """);
    }
}
