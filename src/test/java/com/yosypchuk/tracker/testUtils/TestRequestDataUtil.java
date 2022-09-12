package com.yosypchuk.tracker.testUtils;

import com.yosypchuk.tracker.model.DTO.RequestDTO;
import com.yosypchuk.tracker.model.entity.Activity;
import com.yosypchuk.tracker.model.entity.Request;
import com.yosypchuk.tracker.model.entity.Status;
import com.yosypchuk.tracker.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestRequestDataUtil {
    public static final Long MOCK_ID = 1L;
    public static final Date MOCK_REQUEST_DATE = new Date();
    public static final Status MOCK_STATUS = Status.PENDING;
    public static final Status MOCK_APPROVED_STATUS = Status.APPROVED;
    public static final Status MOCK_REJECTED_STATUS = Status.REJECTED;
    public static final User MOCK_USER = TestUserDataUtil.createUser();
    public static final Activity MOCK_ACTIVITY = TestActivityDataUtil.createActivity();

    public static Request createRequest() {
        return Request.builder()
                .id(MOCK_ID)
                .requestDate(MOCK_REQUEST_DATE)
                .status(MOCK_STATUS)
                .user(MOCK_USER)
                .activity(MOCK_ACTIVITY)
                .build();
    }

    public static RequestDTO createRequestDto() {
        return RequestDTO.builder()
                .id(MOCK_ID)
                .requestDate(MOCK_REQUEST_DATE)
                .status(MOCK_STATUS)
                .user(MOCK_USER)
                .activity(MOCK_ACTIVITY)
                .build();
    }
}