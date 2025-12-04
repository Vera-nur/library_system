package com.library.library_system.repository;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.User; // Senin Entity ismin 'User' ise burayı 'User' yap!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPerson(Person person);

    // İçine bir şey yazmana gerek yok.
    // JpaRepository sayesinde findAll(), findById() gibi metodlar otomatik geliyor.
}