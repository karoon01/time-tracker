package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.api.RequestApi;
import com.yosypchuk.tracker.model.DTO.RequestDTO;
import com.yosypchuk.tracker.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController implements RequestApi {

    private final RequestService requestService;

    @Override
    public List<RequestDTO> getAllRequests() {
        return requestService.getAllRequests();
    }

    @Override
    public List<RequestDTO> getAllUsersRequests(@PathVariable Long id) {
        return requestService.getAllUserRequests(id);
    }

    @Override
    public RequestDTO createRequest(@PathVariable Long userId, @PathVariable Long activityId) {
        return requestService.createRequest(userId, activityId);
    }

    @Override
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        requestService.approveRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        requestService.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }

}
