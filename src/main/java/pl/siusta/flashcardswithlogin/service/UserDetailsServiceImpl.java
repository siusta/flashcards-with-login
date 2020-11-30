package pl.siusta.flashcardswithlogin.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.siusta.flashcardswithlogin.model.User;
import pl.siusta.flashcardswithlogin.repo.UserRepo;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            Optional<User> optionalUser = userRepo.findUserByUsername(s);
            return optionalUser.get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
