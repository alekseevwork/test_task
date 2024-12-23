package javacode.wallet;

import java.util.UUID;

public interface WalletService {

    WalletDto save(WalletDto dto);

    WalletDto getById(UUID walletId);
}
