package pl.siusta.flashcardswithlogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.siusta.flashcardswithlogin.model.User;
import pl.siusta.flashcardswithlogin.repo.UserRepo;

import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void saveUser(User u){
        userRepo.save(u);
    }

    public User getUserByUsername(String username){
        Optional<User> optionalUser = userRepo.findUserByUsername(username);
        return  optionalUser.get();
    }
}
