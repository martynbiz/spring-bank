package biz.rtyn.springbank.account;

import biz.rtyn.springbank.money.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    @Column(name = "currency", nullable = false, length = 3)
    private String currencyCode;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "opened_at", nullable = false)
    private Instant openedAt;

    protected Account() {
        // for JPA
    }

    private Account(String name, AccountType type, Currency currency) {
        this.name = name;
        this.type = type;
        this.currencyCode = currency.getCurrencyCode();
        this.balance = BigDecimal.ZERO;
        this.openedAt = Instant.now();
    }

    public static Account openCustomerAccount(String name) {
        return new Account(name, AccountType.LIABILITY, Money.GBP);
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public AccountType getType() {
        return type;
    }
    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }
    public Money getBalance() {
        return new Money(balance, getCurrency());
    }
    public Instant getOpenedAt() {
        return openedAt;
    }

}
