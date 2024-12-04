package validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.utfpr.bankapi.dto.TransferDTO;
import br.edu.utfpr.bankapi.exception.WithoutBalanceException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.validations.AvailableBalanceValidation;

class AvailableBalanceValidationTest {

    AvailableBalanceValidation availableBalanceValidation = new AvailableBalanceValidation();

    @Test
    void deveriaLancarExcecaoParaSaldoInsuficiente() {
        // ARRANGE
        double saldoInicial = 1500.00;
        TransferDTO transferDTO = new TransferDTO(12345, 67890, 2000);
        Account sourceAccount = new Account("Gabriel", 12345, saldoInicial, 0);
        Transaction transaction = new Transaction(sourceAccount, null, transferDTO.amount(), TransactionType.TRANSFER);

        // ACT & ASSERT
        Assertions.assertThrows(WithoutBalanceException.class, () -> availableBalanceValidation.validate(transaction));
    }

    @Test
    void naoDeveriaLancarExcecaoParaSaldoSuficiente() {
        // ARRANGE
        double saldoInicial = 1500.00;
        TransferDTO transferDTO = new TransferDTO(12345, 67890, 1000);
        Account sourceAccount = new Account("Gabriel", 12345, saldoInicial, 0);
        Transaction transaction = new Transaction(sourceAccount, null, transferDTO.amount(), TransactionType.TRANSFER);

        // ACT & ASSERT
        Assertions.assertDoesNotThrow(() -> availableBalanceValidation.validate(transaction));
    }
}