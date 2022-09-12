package com.yosypchuk.tracker.model.DTO;

import com.yosypchuk.tracker.model.entity.Activity;
import com.yosypchuk.tracker.model.entity.Status;
import com.yosypchuk.tracker.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RequestDTO {
    private Long id;
    private Status status;
    @JsonFormat(pattern="yyyy/MM/dd")
    private Date requestDate;

    private Activity activity;
    private User user;
}
