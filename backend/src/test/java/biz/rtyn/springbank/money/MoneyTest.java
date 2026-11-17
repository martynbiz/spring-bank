package biz.rtyn.springbank.money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void normalisesToTwoDecimalPlaces() {
        assertThat(Money.gbp("10").amount()).isEqualTo(new BigDecimal("10.00"));
        assertThat(Money.gbp("10.5")).isEqualTo(Money.gbp("10.50"));
    }

    @Test
    void bigDecimalEqualsComparesScaleButMoneyDoesNot() {
        assertThat(new BigDecimal("2.0")).isNotEqualTo(new BigDecimal("2.00"));
        assertThat(new BigDecimal("2.0").compareTo(new BigDecimal("2.00"))).isZero();
        assertThat(Money.gbp("2.0")).isEqualTo(Money.gbp("2.00"));
    }

    @Test
    void rejectsFractionsOfAPenny() {
        assertThatThrownBy(() -> Money.gbp("0.001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("too many decimal places");
    }

    @Test
    void doubleCannotEvenAddTenPenceAndTwentyPence() {
        assertThat(0.1 + 0.2).isNotEqualTo(0.3);
    }

    @Test
    void refusesToMixCurrencies() {
        Money euros = Money.of("1.00", Currency.getInstance("EUR"));
        assertThatThrownBy(() -> Money.gbp("1.00").plus(euros))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Currency mismatch");
    }
}