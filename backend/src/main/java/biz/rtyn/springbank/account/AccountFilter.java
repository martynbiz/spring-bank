package biz.rtyn.springbank.account;

import java.time.Instant;

public record AccountFilter(AccountType type, String currencyCode, Instant from, Instant to) {
}
