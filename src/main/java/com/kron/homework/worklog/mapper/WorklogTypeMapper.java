package com.kron.homework.worklog.mapper;

import com.kron.homework.worklog.dto.worklogtype.CreateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.UpdateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.WorklogTypeResponseDto;
import com.kron.homework.worklog.model.WorklogType;
import org.springframework.stereotype.Component;

@Component
public class WorklogTypeMapper {

    public WorklogType toEntity(CreateWorklogTypeDto dto) {
        WorklogType worklogType = new WorklogType();
        worklogType.setName(dto.getName());
        return worklogType;
    }

    public WorklogType updateEntityFromDto(UpdateWorklogTypeDto dto, WorklogType existingWorklogType) {
        if (dto.getName() != null) {
            existingWorklogType.setName(dto.getName());
        }
        return existingWorklogType;
    }

    public WorklogTypeResponseDto toResponseDto(WorklogType worklogType) {
        WorklogTypeResponseDto dto = new WorklogTypeResponseDto();
        dto.setId(worklogType.getId());
        dto.setName(worklogType.getName());
        return dto;
    }
}