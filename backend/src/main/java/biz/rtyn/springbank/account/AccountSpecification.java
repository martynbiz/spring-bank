package biz.rtyn.springbank.account;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    private AccountSpecification() {}

    static Specification<Account> matching(AccountFilter filter) {
        List<Specification<Account>> conditions = new ArrayList<>();
        if (filter.type() != null) {
            conditions.add((account, query, cb) ->
                    cb.equal(cb.lower(account.get("type")), filter.type()));
        }
        if (filter.currencyCode() != null) {
            conditions.add((account, query, cb) -> cb.equal(account.get("currencyCode"), filter.currencyCode()));
        }
        if (filter.from() != null) {
            conditions.add((account, query, cb) -> cb.greaterThanOrEqualTo(account.get("openedAt"), filter.from()));
        }
        if (filter.to() != null) {
            conditions.add((account, query, cb) -> cb.equal(account.get("openedAt"), filter.to()));
        }
        return Specification.allOf(conditions);
    }
}
