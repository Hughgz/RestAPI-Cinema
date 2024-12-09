package com.example.API_Cinema.repository;

import com.example.API_Cinema.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Integer> {

    @Query("SELECT branch FROM Branch branch WHERE branch.name LIKE %:name%")
    List<Branch> findByName(@Param("name") String name);

    @Query("SELECT branch FROM Branch branch WHERE branch.address LIKE %:address%")
    List<Branch> findByAddress(@Param("address") String address);

    @Query("SELECT branch FROM Branch branch WHERE branch.phone LIKE %:phone%")
    List<Branch> findByPhone(@Param("phone") String phone);

    @Query("SELECT b FROM Branch b where b.id in " +
            "(SELECT s.branch.id FROM Schedule s JOIN s.movie m WHERE s.movie.id = :movieId )")
    List<Branch> getBranchThatShowTheMovie(@Param("movieId") Integer movieId);
    Boolean existsByPhone(String phone);
    Boolean existsByName(String name);
    Boolean existsByAddress(String address);

}
