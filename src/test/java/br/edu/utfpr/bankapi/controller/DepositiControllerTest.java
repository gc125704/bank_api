package br.edu.utfpr.bankapi.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.edu.utfpr.bankapi.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
public class DepositiControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    EntityManager entityMannager;

    @Test
    void deveriaRetornar400ParaRequisicaoInvalida() throws Exception {
        // Arange

        var json = "{}"; // dados para a requisição

        // Act
        var res = mvc.perform(
                MockMvcRequestBuilders.post("/transaction/deposit").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // Assert
        org.junit.jupiter.api.Assertions.assertEquals(400, res.getStatus());
    }

    @Test
    void deveriaRetornar201ParaRequisicaoValida() throws Exception {
        // Arange
        Account Account = new Account("Teste",987654321,0,1000);

        entityMannager.persist(Account);

        var json = """
            {
                "receiverAccountNumber": 987654321,
                "amount": 1200
            }
            """; // dados para a requisição

        // Act
        var res = mvc.perform(
                MockMvcRequestBuilders.post("/transaction/deposit").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // Assert
        org.junit.jupiter.api.Assertions.assertEquals(201, res.getStatus());
    }

}
