package com.library.library_system.repository;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    Optional<Worker> findByPerson(Person person);
}
