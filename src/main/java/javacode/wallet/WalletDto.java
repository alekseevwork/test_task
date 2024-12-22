package javacode.wallet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record WalletDto(
        @NotNull
        Long walledId,

        @Enumerated(EnumType.STRING)
        @NotNull
        WalletType walletType,

        @NotNull
        int amount) {
}
