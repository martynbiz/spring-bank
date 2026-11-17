package biz.rtyn.springbank.account;

import biz.rtyn.springbank.web.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public List<Account> list() {
        return accountRepository.findAll();
    }

    public Account get(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account", id));
    }
}
