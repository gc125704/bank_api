package service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveriaRetornarContaPorNumero() throws Exception {
        // ARRANGE
        long accountNumber = 12345;

        // ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/account/" + accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", Matchers.is((int) accountNumber)));
    }

    @Test
    void deveriaRetornarTodasAsContas() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void deveriaSalvarConta() throws Exception {
        // ARRANGE
        String json = "{ \"name\": \"Gabriel Stabile\", \"number\": 12345, \"balance\": 0.0, \"specialLimit\": 0.0 }";

        // ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("Gabriel Stabile")))
                .andExpect(jsonPath("$.number", Matchers.is((int) 12345)))
                .andExpect(jsonPath("$.balance", Matchers.is(0)))
                .andExpect(jsonPath("$.specialLimit", Matchers.is(0.0)));
    }

    @Test
    void deveriaAtualizarConta() throws Exception {
        // ARRANGE
        long accountId = 1L;
        String json = "{ \"name\": \"Gabriel Stabile\", \"number\": 12345, \"balance\": 1500.0, \"specialLimit\": 0.0 }";

        // ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.put("/account/" + accountId)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Gabriel Stabile")))
                .andExpect(jsonPath("$.number", Matchers.is((int) 12345)))
                .andExpect(jsonPath("$.balance", Matchers.is(1500.0)))
                .andExpect(jsonPath("$.specialLimit", Matchers.is(0.0)));
    }
}