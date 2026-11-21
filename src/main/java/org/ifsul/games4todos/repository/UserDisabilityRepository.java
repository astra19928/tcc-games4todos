package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.UserDisability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDisabilityRepository extends JpaRepository<UserDisability, Integer> {
   void deleteById_IdUser(Integer id);
}
