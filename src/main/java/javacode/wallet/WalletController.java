package javacode.wallet;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Data
@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto save(@Valid @RequestBody WalletDto dto) {
        log.info("POST pi/v1/wallet/ save - {}", dto);
        return walletService.save(dto);
    }

    @GetMapping("/wallets/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public WalletDto getById(@PathVariable Long walletId) {
        log.info("GET pi/v1/wallets/{} - getById", walletId);
        return walletService.getById(walletId);
    }
}