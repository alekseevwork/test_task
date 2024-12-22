package javacode.wallet;

import jakarta.validation.ValidationException;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javacode.error.InsufficientFundsException;
import javacode.error.NotFoundException;

@Service
@Data
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public WalletDto save(WalletDto dto) {
        if (dto.walledId() == null) {
            throw new ValidationException("Wallet id is null.");
        }
        Wallet wallet = walletRepository.findById(dto.walledId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Wallet by id = %d not found.", dto.walledId()))
                );
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
    public WalletDto getById(long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Wallet by id = %d not found.", walletId))
                );
        return WalletMapper.toDto(wallet);
    }
}
