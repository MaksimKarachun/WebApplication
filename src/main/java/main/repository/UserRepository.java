package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByEmail(String email);

  User findByCode(String code);
}
