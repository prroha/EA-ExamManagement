package org.proha.seed;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.proha.model.entity.User;
import org.proha.repository.UserRepository;
import org.proha.utils.PasswordConfig;

import java.util.Arrays;
import java.util.List;

@Singleton
@Startup
public class UserSeeder {

    @Inject
    private UserRepository userRepository;
    @Inject
    private PasswordConfig passwordConfig;

    @PostConstruct
    @Transactional
    public void seed() {
        if (userRepository.count() == 0) {
            String password1 = "prakash@123";
            String adminPassword = "admin@123";
            String salt1 = PasswordConfig.generateSalt();
            String salt2 = PasswordConfig.generateSalt();
            List<User> users = Arrays.asList(
                    new User("Prakash Dorsey", "prakash@study.xyzuni.edu", "prakash", PasswordConfig.hashPassword(password1, salt1), salt1),
                    new User("Admin", "admin@study.xyzuni.edu", "admin", PasswordConfig.hashPassword(adminPassword, salt2), salt2)
            );
            for (User user : users) {
                userRepository.save(user);
            }
        }
    }
}