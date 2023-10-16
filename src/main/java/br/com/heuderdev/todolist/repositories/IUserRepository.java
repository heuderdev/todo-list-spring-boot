package br.com.heuderdev.todolist.repositories;

import br.com.heuderdev.todolist.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.*;

public interface IUserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
}
