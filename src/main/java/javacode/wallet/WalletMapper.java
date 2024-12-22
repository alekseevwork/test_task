package javacode.wallet;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WalletMapper {

    public WalletDto toDto(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return WalletDto.builder()
                .walledId(wallet.getId())
                .amount(wallet.getAmount())
                .build();
    }
}
