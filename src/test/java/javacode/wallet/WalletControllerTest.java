package javacode.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import javacode.error.InsufficientFundsException;
import javacode.error.NotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WalletService walletService;

    private WalletDto walletRequest;
    private WalletDto walletDto;

    @BeforeEach
    void before() {
        walletRequest = WalletDto.builder()
                .walledId(UUID.randomUUID())
                .amount(1000)
                .build();
        walletDto = WalletDto.builder()
                .walledId(UUID.randomUUID())
                .walletType(WalletType.DEPOSIT)
                .amount(1000)
                .build();
    }

    @SneakyThrows
    @Test
    void getById_whenInvoked_thenStatusOkAndReturnWallet() {
        when(walletService.getById(walletRequest.walledId())).thenReturn(walletRequest);

        String result = mockMvc.perform(get("/api/v1/wallets/{walletId}", walletDto.walledId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WalletDto actualWallet = objectMapper.readValue(result, WalletDto.class);

        assertEquals(walletRequest.walledId(), actualWallet.walledId());
        assertEquals(walletRequest.amount(), actualWallet.amount());
    }

    @SneakyThrows
    @Test
    void getById_whenNotFoundById_thenStatusNotFound() {
        when(walletService.getById(walletRequest.walledId()))
                .thenThrow(new NotFoundException(String.format("Wallet by id = %d not found.", anyLong())));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletDto.walledId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @SneakyThrows
    @Test
    void save_whenIsOk_thenStatusOkAndReturnWalletDto() {
        when(walletService.save(walletDto)).thenReturn(walletRequest);

        String result = mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WalletDto actualWallet = objectMapper.readValue(result, WalletDto.class);

        assertEquals(walletRequest.walledId(), actualWallet.walledId());
        assertEquals(walletRequest.amount(), actualWallet.amount());
    }

    @SneakyThrows
    @Test
    void save_whenNotFoundById_thenStatusNotFound() {
        when(walletService.save(walletDto))
                .thenThrow(new NotFoundException(String.format("Wallet by id = %d not found.", anyLong())));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @SneakyThrows
    @Test
    void save_whenInsufficientFunds_thenStatusConflict() {
        when(walletService.save(walletDto))
                .thenThrow(new InsufficientFundsException("Insufficient funds."));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(status().isConflict())
                .andReturn();
    }
}
