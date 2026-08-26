package employee_management.demo.Controller;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.AddHoliday;
import employee_management.demo.DTO.EditHoliday;
import employee_management.demo.DTO.GetHoliday;
import employee_management.demo.Service.HolidayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(
            HolidayService holidayService
    ) {
        this.holidayService = holidayService;

    }

    @GetMapping()
    public APIResponse<List<GetHoliday>> getHolidayService() {
        return holidayService.getHolidayService();
    }

    @DeleteMapping("/{holidayId}")
    public APIResponse<?> deleteHoliday(String HolidayId) {
        return holidayService.deleteHolidayService(HolidayId);
    }

    @PostMapping()
    public APIResponse<GetHoliday> addHoliday(@RequestBody AddHoliday dto) {
        return holidayService.addHolidayService(dto);

    }

    @PutMapping("/{holidayId}")
    public APIResponse<GetHoliday> editHolidayService(@RequestBody EditHoliday dto) {
        return holidayService.editHolidayService(dto);

    }

}
