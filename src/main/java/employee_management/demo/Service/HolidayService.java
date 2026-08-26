package employee_management.demo.Service;

import employee_management.demo.DTO.APIResponse;
import employee_management.demo.DTO.AddHoliday;
import employee_management.demo.DTO.EditHoliday;
import employee_management.demo.DTO.GetHoliday;
import employee_management.demo.EmployeeRepository.HolidayRepository;
import employee_management.demo.Mapper.HolidayMapper;
import employee_management.demo.Modal.Holiday;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;

    public HolidayService(
            HolidayRepository holidayRepository,
            HolidayMapper holidayMapper
    ) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
    }
    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<List<GetHoliday>> getHolidayService() {

        List<GetHoliday> holidays = holidayRepository.findAll()
                .stream()
                .map(holidayMapper::toGetHoliday)
                .toList();

        return new APIResponse<>(
                "Holidays retrieved successfully",
                200,
                "SUCCESS",
                null,
                holidays
        );
    }
    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<?> deleteHolidayService(String holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId).orElseThrow(() -> new NoSuchElementException("Holiday not found"));

        holidayRepository.deleteById(holidayId);
        return new APIResponse<>(
                "Holiday deleted successfully",
                200,
                "SUCCESS",
                null,
                holiday
        );

    }
    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<GetHoliday> addHolidayService(AddHoliday dto) {

        Holiday holiday = holidayMapper.toEntity(dto);

        Holiday savedHoliday = holidayRepository.save(holiday);

        GetHoliday response = holidayMapper.toGetHoliday(savedHoliday);

        return new APIResponse<>(
                "Holiday created successfully",
                201,
                "SUCCESS",
                null,
                response
        );
    }
    @Transactional(transactionManager = "employeeTransactionManager")
    public APIResponse<GetHoliday> editHolidayService(EditHoliday dto) {

        Holiday holiday = holidayRepository.findById(dto.getHolidayId())
                .orElseThrow(() ->
                        new NoSuchElementException("Invalid holiday id")
                );

        holidayMapper.updateEntity(holiday, dto);

        Holiday savedHoliday = holidayRepository.save(holiday);

        GetHoliday response = holidayMapper.toGetHoliday(savedHoliday);

        return new APIResponse<>(
                "Holiday edited successfully",
                200,
                "SUCCESS",
                null,
                response
        );
    }
}