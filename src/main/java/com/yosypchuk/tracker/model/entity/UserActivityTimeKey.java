package com.yosypchuk.tracker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor @AllArgsConstructor
@Embeddable
public class UserActivityTimeKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "activity_id")
    private Long activityId;
}
