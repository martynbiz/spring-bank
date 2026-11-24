package biz.rtyn.springbank.account;

import biz.rtyn.springbank.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class AccountCollectionApiTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void returnsAPageWithPageMetadata() {
        var response = mvc.get().uri("/accounts?size=5");

        assertThat(response).hasStatusOk();
        assertThat(response).bodyJson().extractingPath("$.content.length()").isEqualTo(5);
        assertThat(response).bodyJson().extractingPath("$.page.size").isEqualTo(5);
        assertThat(response).bodyJson().extractingPath("$.page.number").isEqualTo(0);
        assertThat(response).bodyJson().extractingPath("$.page.totalElements").asNumber().satisfies(
                total -> assertThat(total.intValue()).isGreaterThanOrEqualTo(6));
    }

    @Test
    void theSecondPageHasTheRest() {
        var response = mvc.get().uri("/accounts?currencyCode=GBP&size=3&page=1");

        assertThat(response).bodyJson().isLenientlyEqualTo("""
                {
                  "content": [{"name": "Anne"}],
                  "page": {"size": 3, "number": 1, "totalElements": 4, "totalPages": 2}
                }
                """);
    }

    @Test
    void filtersByCurrencyCodeIgnoringCaseAndByType() {
        var response = mvc.get().uri("/accounts?currencyCode=eur&type=LIABILITY");

        assertThat(response).bodyJson().extractingPath("$.content[*].name")
                .isEqualTo(List.of("William", "Brenda"));
    }

    @Test
    void filtersByDateRange() {
        var response = mvc.get().uri("/accounts?from=2025-06-01T00:00:00Z&to=2026-05-31T00:00:00Z");

        assertThat(response).bodyJson().extractingPath("$.content[*].name")
                .isEqualTo(List.of("David", "Anne"));
    }

    @Test
    void sortsByOpenedAt() {
        var response = mvc.get().uri("/accounts?currencyCode=EUR&sort=openedAt,desc");

        assertThat(response).bodyJson().extractingPath("$.content[*].name").isEqualTo(List.of(
                "Brenda", "William"));
    }

    @Test
    void refusesToSortByAnythingElse() {
        var response = mvc.get().uri("/accounts?sort=name");

        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(response).bodyJson().extractingPath("$.errors.sort").isEqualTo("must be one of type, currency, balance, openedAt");
    }

    @Test
    void capsThePageSizeAt100() {
        var response = mvc.get().uri("/accounts?size=500");

        assertThat(response).hasStatusOk();
        assertThat(response).bodyJson().extractingPath("$.page.size").isEqualTo(100);
    }

    @Test
    void rejectsFiltersThatCannotBeConverted() {
        var response = mvc.get().uri("/accounts?type=GANGSTER&from=yesterday");

        assertThat(response).hasStatus(HttpStatus.BAD_REQUEST);
//        assertThat(response).bodyJson().isLenientlyEqualTo("""
//                {"errors": {"type": "has an invalid value", "from": "has an invalid value"}}
//                """);
    }
}
