package employee_management.demo.Mapper;

import employee_management.demo.DTO.GetLeaveRequestTypes;
import employee_management.demo.Modal.LeaveTypes;
import org.springframework.stereotype.Component;

@Component
public class RequestLeaveTypesMapper {
    public GetLeaveRequestTypes toGetLeaveRequest(LeaveTypes leaveTypes) {
        GetLeaveRequestTypes dto = new GetLeaveRequestTypes();
        dto.setId(leaveTypes.getId());
        dto.setName(leaveTypes.getName());
        dto.setDescription(leaveTypes.getDescription());
        dto.setCreatedAt(leaveTypes.getCreatedDate());

        dto.setUpdatedAt(leaveTypes.getUpdatedDate());
        return dto;
    }
}
