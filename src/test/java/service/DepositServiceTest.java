package service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.utfpr.bankapi.dto.DepositDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.model.Transaction;
import br.edu.utfpr.bankapi.model.TransactionType;
import br.edu.utfpr.bankapi.repository.AccountRepository;
import br.edu.utfpr.bankapi.repository.TransactionRepository;
import br.edu.utfpr.bankapi.service.TransactionService;
import br.edu.utfpr.bankapi.validations.AvailableAccountValidation;


@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {
    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AvailableAccountValidation accountValidation;


    @InjectMocks
    TransactionService transactionService;

    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void deveriaDepositar() throws NotFoundException {
        //arrange
        double saldoInicial = 145.3;
        var depositDTO = new DepositDTO(12345,1000);
        var receiverAccount = new Account("John Smith",12345,saldoInicial,0);
        //BDDMockito.given(accountRepository.getByNumber(receiverAccount.getNumber())).willReturn(Optional.of(receiverAccount));
 
        BDDMockito.given(accountValidation.validate(receiverAccount.getNumber())).willReturn(receiverAccount);
      

        //act
        transactionService.deposit(depositDTO);

        //assert
        BDDMockito.then(transactionRepository).should().save(BDDMockito.any());

        BDDMockito.then(transactionRepository).should().save(transactionCaptor.capture());

        Transaction transacaoSalva = transactionCaptor.getValue();

        //analisando os valores que estão na transacao

        //verificando se a conta destinataria é a mesma que esta na transacao
        Assertions.assertEquals(receiverAccount,transacaoSalva.getReceiverAccount());

        //verificando se o valor da transacao é o mesmo que foi informado no depositoDTO
        Assertions.assertEquals(depositDTO.amount(), transacaoSalva.getAmount());

        //verificando se a operação definida na transação é deposit
        Assertions.assertEquals(TransactionType.DEPOSIT, transacaoSalva.getType());

        //verifica se o saldo foi alterado na conta de destino
        Assertions.assertEquals(saldoInicial+depositDTO.amount(), transacaoSalva.getReceiverAccount().getBalance());

    }
}
