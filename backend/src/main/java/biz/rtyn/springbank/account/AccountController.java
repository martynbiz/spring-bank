package biz.rtyn.springbank.account;

import biz.rtyn.springbank.money.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Currency;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountResponse> list() {
        return accountService.list().stream()
                .map(AccountResponse::from)
                .toList();
    }

    public record OpenAccountRequest(@NotNull @Size(max = 200) String name) {
    }

    public record AccountResponse(
            long id,
            String name,
            AccountType type,
            Currency currency,
            Money balance,
            Instant opened_at
    ) {
        public static AccountResponse from(Account account) {
            return new AccountResponse(
                    account.getId(),
                    account.getName(),
                    account.getType(),
                    account.getCurrency(),
                    account.getBalance(),
                    account.getOpenedAt()
            );
        }
    }
}
