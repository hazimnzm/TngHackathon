package Hackathing.BackendTemplate.Security;

import Hackathing.BackendTemplate.Repository.MerchantRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserDetailsService implements UserDetailsService {

    private final MerchantRepository merchantRepository;

    public MerchantUserDetailsService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var merchant = merchantRepository.findByEmail(username.toLowerCase().trim())
                .orElseThrow(() -> new UsernameNotFoundException("Merchant not found"));

        return User.builder()
                .username(merchant.getEmail())
                .password(merchant.getPasswordHash())
                .roles("MERCHANT")
                .build();
    }
}

