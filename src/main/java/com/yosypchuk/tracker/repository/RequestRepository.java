package com.yosypchuk.tracker.repository;

import com.yosypchuk.tracker.model.entity.Request;
import com.yosypchuk.tracker.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getAllRequestsByUserId(Long userId);

    @Modifying
    @Query(value ="UPDATE Request r SET r.status = ? WHERE r.id = ?",
    nativeQuery = true)
    void updateRequestStatusById(Status status, Long id);
}
