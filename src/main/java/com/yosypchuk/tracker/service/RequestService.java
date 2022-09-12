package com.yosypchuk.tracker.service;

import com.yosypchuk.tracker.model.DTO.RequestDTO;

import java.util.List;

public interface RequestService {
    RequestDTO createRequest(Long userId, Long activityId);

    List<RequestDTO> getAllRequests();

    List<RequestDTO> getAllUserRequests(Long id);

    void approveRequest(Long id);

    void rejectRequest(Long id);

    void deleteRequest(Long id);
}
