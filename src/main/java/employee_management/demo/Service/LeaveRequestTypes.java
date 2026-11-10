package employee_management.demo.Service;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.GetLeaveRequestTypes;
import employee_management.demo.EmployeeRepository.LeaveRequestRepository;
import employee_management.demo.Mapper.RequestLeaveTypesMapper;
import employee_management.demo.Modal.LeaveTypes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestTypes {
    private final LeaveRequestRepository leaveRequestRepository;
    private final RequestLeaveTypesMapper requestLeaveTypesMapper;

    public LeaveRequestTypes(LeaveRequestRepository leaveRequestRepository, RequestLeaveTypesMapper requestLeaveTypesMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.requestLeaveTypesMapper = requestLeaveTypesMapper;
    }

    public APIResponse<List<GetLeaveRequestTypes>> getLeaveRequestTypes() {
        List<LeaveTypes> leaveRequestTypes = leaveRequestRepository.findAll();
        List<GetLeaveRequestTypes> response = leaveRequestTypes.stream()
                .map(requestLeaveTypesMapper::toGetLeaveRequest)
                .toList();
        return new APIResponse<>(
                "Leave request types fetched successfully",
                200,
                "SUCCESS",
                null,
                response);

    }
}
