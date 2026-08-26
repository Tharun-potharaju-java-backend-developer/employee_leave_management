package employee_management.demo.Mapper;

import employee_management.demo.DTO.AddHoliday;
import employee_management.demo.DTO.EditHoliday;
import employee_management.demo.DTO.GetHoliday;
import employee_management.demo.Modal.Holiday;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class HolidayMapper {

    public Holiday toEntity(AddHoliday dto) {

        Holiday holiday = new Holiday();

        holiday.setHolidayName(dto.getHolidayName());
        holiday.setHolidayDate(LocalDate.parse(dto.getHolidayDate()));
        holiday.setHolidayDescription(dto.getHolidayDescription());

        return holiday;
    }

    public GetHoliday toGetHoliday(Holiday holiday) {

        GetHoliday dto = new GetHoliday();

        dto.setHolidayName(holiday.getHolidayName());
        dto.setHolidayDate(holiday.getHolidayDate());
        dto.setDescription(holiday.getHolidayDescription());

        return dto;
    }

    public void updateEntity(Holiday holiday, EditHoliday dto) {
        holiday.setHolidayName(dto.getHolidayName());
        holiday.setHolidayDate(LocalDate.parse(dto.getHolidayDate()));
        holiday.setHolidayDescription(dto.getHolidayDescription());

    }
}