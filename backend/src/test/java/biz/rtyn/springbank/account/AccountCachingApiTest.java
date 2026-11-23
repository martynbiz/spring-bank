package biz.rtyn.springbank.account;

import biz.rtyn.springbank.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AccountCachingApiTest {

//    static final String NEW_EVENT = """
//            {
//              "title": "Test Gig",
//              "category": "MUSIC",
//              "venueId": 1,
//              "startsAt": "2030-09-01T19:30:00Z",
//              "description": "Created by a test"
//            }
//            """;
//
//    @Autowired
//    MockMvcTester mvc;
//
//    @Test
//    void anAccountHasAnETagAndMayBeCachedForAMinute() {
//        var response = mvc.get().uri("/accounts/5");
//
//        assertThat(response).hasStatusOk();
//        assertThat(response).headers().hasValue("ETag", "\"0\"");
//        assertThat(response).headers().hasValue("Cache-Control", "max-age=60, public");
//    }
//
//    @Test
//    void aMatchingIfNoneMatchIs304WithNoBody() {
//        var response = mvc.get().uri("/accounts/5").header("If-None-Match", "\"0\"");
//
//        assertThat(response).hasStatus(HttpStatus.NOT_MODIFIED);
//        assertThat(response).body().isEmpty();
//        assertThat(response).headers().hasValue("ETag", "\"0\"");
//    }
//
//    @Test
//    void changingTheAccountChangesTheETag() {
//        long id = createAccount();
//
//        var response = mvc.get().uri("/accounts/{id}", id);
//
//        assertThat(response).headers().hasValue("ETag", "\"0\"");
//
//        mvc.patch().uri("/accounts/{id}", id)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                        {"title": "Changed"}
//                        """)
//                .exchange();
//
//        response = mvc.get().uri("/accounts/{id}", id).header("If-None-Match", "\"0\"");;
//
//        assertThat(response).hasStatusOk();
//        assertThat(response).headers().hasValue("ETag", "\"1\"");
//    }
//
//    private long createAccount() {
//        var result = mvc.post().uri("/accounts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(NEW_EVENT)
//                .exchange();
//
//        assertThat(result).hasStatus(HttpStatus.CREATED);
//        String location = result.getResponse().getHeader("Location");
//        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
//    }
}
