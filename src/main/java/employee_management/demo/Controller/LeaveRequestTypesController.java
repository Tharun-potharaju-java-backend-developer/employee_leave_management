package employee_management.demo.Controller;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.GetLeaveRequestTypes;
import employee_management.demo.Service.LeaveRequestTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-request-types")
public class LeaveRequestTypesController {
    private final LeaveRequestTypes leaveRequestTypes;
    public LeaveRequestTypesController(LeaveRequestTypes leaveRequestTypes) {
        this.leaveRequestTypes = leaveRequestTypes;
    }
    @GetMapping
    public APIResponse<List<GetLeaveRequestTypes>> getLeaveRequestTypes() {
        return leaveRequestTypes.getLeaveRequestTypes();
    }

}
