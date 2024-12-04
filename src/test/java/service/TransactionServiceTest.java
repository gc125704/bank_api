package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.utfpr.bankapi.dto.TransferDTO;
import br.edu.utfpr.bankapi.dto.WithdrawDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.repository.TransactionRepository;
import br.edu.utfpr.bankapi.service.TransactionService;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    AvailableAccountValidation availableAccountValidation;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService service;

    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void deveriaSacar() throws NotFoundException {
        // ARRANGE
        double saldoInicial = 1500.00;
        WithdrawDTO withdrawDTO = new WithdrawDTO(12345, 500);
        Account sourceAccount = new Account("Gabriel Stabile", 12345, saldoInicial, 0);

        BDDMockito.given(availableAccountValidation.validate(withdrawDTO.sourceAccountNumber()))
                .willReturn(sourceAccount);

        // ACT
        service.withdraw(withdrawDTO);

        // ASSERT
        BDDMockito.then(transactionRepository).should().save(transactionCaptor.capture());
        Transaction transactionSalva = transactionCaptor.getValue();

        Assertions.assertEquals(sourceAccount, transactionSalva.getSourceAccount());
        Assertions.assertEquals(withdrawDTO.amount(), transactionSalva.getAmount());
        Assertions.assertEquals(TransactionType.WITHDRAW, transactionSalva.getType());
        Assertions.assertEquals(saldoInicial - withdrawDTO.amount(), transactionSalva.getSourceAccount().getBalance());
    }

    @Test
    void deveriaTransferir() throws NotFoundException {
        // ARRANGE
        double saldoInicialSource = 2000.00;
        double saldoInicialReceiver = 1000.00;
        TransferDTO transferDTO = new TransferDTO(12345, 67890, 500);
        Account sourceAccount = new Account("Gabriel Stabile", 12345, saldoInicialSource, 0);
        Account receiverAccount = new Account("Jane Doe", 67890, saldoInicialReceiver, 0);

        BDDMockito.given(availableAccountValidation.validate(transferDTO.sourceAccountNumber()))
                .willReturn(sourceAccount);
        BDDMockito.given(availableAccountValidation.validate(transferDTO.receiverAccountNumber()))
                .willReturn(receiverAccount);

        // ACT
        service.transfer(transferDTO);

        // ASSERT
        BDDMockito.then(transactionRepository).should().save(transactionCaptor.capture());
        Transaction transactionSalva = transactionCaptor.getValue();

        Assertions.assertEquals(sourceAccount, transactionSalva.getSourceAccount());
        Assertions.assertEquals(receiverAccount, transactionSalva.getReceiverAccount());
        Assertions.assertEquals(transferDTO.amount(), transactionSalva.getAmount());
        Assertions.assertEquals(TransactionType.TRANSFER, transactionSalva.getType());
        Assertions.assertEquals(saldoInicialSource - transferDTO.amount(), transactionSalva.getSourceAccount().getBalance());
        Assertions.assertEquals(saldoInicialReceiver + transferDTO.amount(), transactionSalva.getReceiverAccount().getBalance());
    }
}