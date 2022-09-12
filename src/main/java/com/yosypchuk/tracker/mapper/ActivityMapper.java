package com.yosypchuk.tracker.mapper;

import com.yosypchuk.tracker.model.DTO.ActivityDTO;
import com.yosypchuk.tracker.model.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityMapper {
    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    Activity mapActivity(ActivityDTO activityDTO);

    ActivityDTO mapActivityDto(Activity activity);
}
