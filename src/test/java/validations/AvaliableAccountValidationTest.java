package validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.repository.AccountRepository;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AvailableAccountValidationTest {
    @Mock
    static AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

    static AvailableAccountValidation availableAccountValidation;

    @BeforeEach
    void before() {
        availableAccountValidation = new AvailableAccountValidation();

        ReflectionTestUtils.setField(availableAccountValidation, "accountRepository", accountRepository);
    }

    @Test
    void deveriaRetornarContaExistente() throws NotFoundException {
        // ARRANGE
        long accountNumber = 12345;
        Account account = new Account("Gabriel", accountNumber, 17500.00, 0);
        BDDMockito.given(accountRepository.getByNumber(accountNumber)).willReturn(Optional.of(account));

        // ACT
        Account result = availableAccountValidation.validate(accountNumber);

        // ASSERT
        Assertions.assertEquals(account, result);
    }

    @Test
    void deveriaLancarExcecaoParaContaInexistente() {
        // ARRANGE
        long accountNumber = 12345;
        BDDMockito.given(accountRepository.getByNumber(accountNumber)).willReturn(Optional.empty());

        // ACT & ASSERT
        Assertions.assertThrows(NotFoundException.class, () -> availableAccountValidation.validate(accountNumber));
    }
}