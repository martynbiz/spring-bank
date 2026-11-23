package biz.rtyn.springbank.account;

import biz.rtyn.springbank.money.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
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
    public PagedModel<AccountResponse> list(AccountFilter filter, @PageableDefault(size = 20, sort = "openedAt") Pageable pageable) {
        return new PagedModel<>(accountService.search(filter, pageable).map(AccountResponse::from));
    }

    @GetMapping("/{id}")
    public AccountResponse getById(@PathVariable long id) {
        return AccountResponse.from(accountService.get(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> open(@Valid @RequestBody OpenAccountRequest request) {
        Account account = accountService.open(request.name());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return ResponseEntity.created(location).body(AccountResponse.from(account));
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
