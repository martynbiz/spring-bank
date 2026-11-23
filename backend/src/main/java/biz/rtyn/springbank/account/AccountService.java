package biz.rtyn.springbank.account;

import biz.rtyn.springbank.web.InvalidFieldException;
import biz.rtyn.springbank.web.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    static final List<String> SORTABLE = List.of("type", "currency", "balance", "openedAt");

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public List<Account> list() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account get(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account", id));
    }

    public Page<Account> search(AccountFilter filter, Pageable pageable) {
        for (Sort.Order order: pageable.getSort()) {
            if (!SORTABLE.contains(order.getProperty())) {
                throw new InvalidFieldException("sort", "must be one of " + String.join(", ", SORTABLE));
            }
        }
        Pageable stable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by("id")));
        return accountRepository.findAll(AccountSpecification.matching(filter), stable);
    }

    @Transactional
    public Account open(String name) {
        return accountRepository.save(Account.openCustomerAccount(name));
    }
}
