package javacode.wallet;

import javacode.error.InsufficientFundsException;
import javacode.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WalletServiceImplTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    private Wallet wallet;
    private UUID walletId;

    @BeforeEach
    void before() {
        walletRepository.deleteAll();

        wallet = walletRepository.save(Wallet.builder().amount(1000).build());

        walletId = wallet.getId();
    }

    @Test
    void save_whenOk_thenReturnWallet() {
        Wallet actualWallet = walletRepository.findById(walletId).get();

        assertEquals(actualWallet, wallet);
    }

    @Test
    void save_whenInvalidId_thenThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> walletService.getById(UUID.randomUUID()));
    }

    @Test
    void save_whenDepositWalletType_thenWalletAmountBecomePlus100() {
        WalletDto walletDto = WalletDto.builder()
                .walletType(WalletType.DEPOSIT)
                .walledId(walletId)
                .amount(100)
                .build();
        walletService.save(walletDto);
        Wallet actualWallet = walletRepository.findById(walletId).get();

        assertEquals(actualWallet.getAmount(), 1100);
    }

    @Test
    void save_whenWithdrawWalletType_thenWalletAmountBecomeMinus100() {
        WalletDto walletDto = WalletDto.builder()
                .walletType(WalletType.WITHDRAW)
                .walledId(walletId)
                .amount(100)
                .build();
        walletService.save(walletDto);
        Wallet actualWallet = walletRepository.findById(walletId).get();

        assertEquals(actualWallet.getAmount(), 900);
    }

    @Test
    void save_whenWithdrawMoreAmount_thenThrowsInsufficientFundsException() {
        WalletDto walletDto = WalletDto.builder()
                .walletType(WalletType.WITHDRAW)
                .walledId(walletId)
                .amount(1100)
                .build();

        assertThrows(InsufficientFundsException.class, () -> walletService.save(walletDto));
    }
}
