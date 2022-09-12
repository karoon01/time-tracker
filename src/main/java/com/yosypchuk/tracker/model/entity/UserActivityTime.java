package com.yosypchuk.tracker.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "user_activity_time")
public class UserActivityTime {

    @EmbeddedId
    private UserActivityTimeKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("activityId")
    @JoinColumn(name = "activity_id")
    private Activity activity;

    private String duration;
}
