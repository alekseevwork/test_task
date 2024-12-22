package javacode.wallet;

public interface WalletService {

    WalletDto save(WalletDto dto);

    WalletDto getById(long walletId);
}
