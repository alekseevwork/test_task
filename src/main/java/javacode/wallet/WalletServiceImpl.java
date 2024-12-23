package javacode.wallet;

import javacode.error.InsufficientFundsException;
import javacode.error.NotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Data
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public WalletDto save(WalletDto dto) {
        Wallet wallet = walletRepository.findById(dto.walledId())
                .orElseThrow(() -> new NotFoundException("Wallet by id = " + dto.walledId() + " not found."));
        switch (dto.walletType()) {
            case DEPOSIT -> wallet.setAmount(wallet.getAmount() + dto.amount());
            case WITHDRAW -> wallet.setAmount(wallet.getAmount() - dto.amount());
        }
        if (wallet.getAmount() < 0) {
            throw new InsufficientFundsException("Insufficient funds.");
        }

        return WalletMapper.toDto(walletRepository.save(wallet));
    }

    @Override
    public WalletDto getById(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet by id = " + walletId + " not found."));
        return WalletMapper.toDto(wallet);
    }
}
