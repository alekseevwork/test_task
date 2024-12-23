package javacode.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WalletServiceImplTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    private Wallet wallet;
    private WalletDto walletDto;
    private long walletId;

    @BeforeEach
    void before() {
        walletRepository.deleteAll();

        walletDto = WalletDto.builder()
                .walledId(UUID.randomUUID())
                .build();

        wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .amount(1000)
                .build();

        walletRepository.save(wallet);
    }

    @Test
    void save_whenOk_thenReturnWallet() {
        Wallet actualWallet = walletRepository.findById(wallet.getId()).get();

        assertEquals(actualWallet, wallet);
    }

}
