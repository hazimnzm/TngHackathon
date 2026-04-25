package Hackathing.BackendTemplate.Service;

import Hackathing.BackendTemplate.DO.Merchant;
import Hackathing.BackendTemplate.Repository.MerchantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    public MerchantService(MerchantRepository merchantRepository, PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Merchant register(String email, String displayName, String rawPassword) {
        if (merchantRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        Merchant merchant = new Merchant();
        merchant.setEmail(email.toLowerCase().trim());
        merchant.setDisplayName(displayName.trim());
        merchant.setPasswordHash(passwordEncoder.encode(rawPassword));
        return merchantRepository.save(merchant);
    }

    public Merchant authenticate(String email, String rawPassword) {
        Merchant merchant = merchantRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, merchant.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return merchant;
    }
}

