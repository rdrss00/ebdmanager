package ebdmanager.controller;

import ebdmanager.config.SecurityConfig;
import ebdmanager.dto.AttendanceDateReportDTO;
import ebdmanager.dto.GeneralReportDTO;
import ebdmanager.dto.QuarterlyReportDTO;
import ebdmanager.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@Import(SecurityConfig.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    @DisplayName("Deve retornar relatório por data")
    void getReportByDate_Success() throws Exception {
        LocalDate date = LocalDate.of(2026, 10, 18);
        AttendanceDateReportDTO dto = new AttendanceDateReportDTO(
                date,
                1,
                10,
                2,
                83.33,
                9,
                8,
                50.0,
                List.of()
        );

        when(reportService.getReportByDate(eq(date))).thenReturn(dto);

        mockMvc.perform(get("/api/reports/by-date")
                        .with(httpBasic("user", "user123"))
                        .param("date", "2026-10-18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-10-18"))
                .andExpect(jsonPath("$.totalPresent").value(10))
                .andExpect(jsonPath("$.totalAbsent").value(2))
                .andExpect(jsonPath("$.overallAttendanceRate").value(83.33));
    }

    @Test
    @DisplayName("Deve retornar relatório geral quantitativo")
    void getGeneralReport_Success() throws Exception {
        GeneralReportDTO dto = new GeneralReportDTO(3L, 30L, 10.0, List.of());
        when(reportService.getGeneralReport()).thenReturn(dto);

        mockMvc.perform(get("/api/reports/general")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClasses").value(3L))
                .andExpect(jsonPath("$.totalStudents").value(30L))
                .andExpect(jsonPath("$.averageStudentsPerClass").value(10.0));
    }

    @Test
    @DisplayName("Deve retornar relatório por trimestre")
    void getQuarterlyReport_Success() throws Exception {
        QuarterlyReportDTO dto = new QuarterlyReportDTO(
                2026,
                1,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31),
                3L,
                30L,
                10.0,
                12,
                100,
                15,
                86.96,
                90,
                85,
                550.0,
                List.of()
        );

        when(reportService.getQuarterlyReport(2026, 1)).thenReturn(dto);

        mockMvc.perform(get("/api/reports/quarterly")
                        .with(httpBasic("user", "user123"))
                        .param("year", "2026")
                        .param("quarter", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2026))
                .andExpect(jsonPath("$.quarter").value(1))
                .andExpect(jsonPath("$.totalPresent").value(100));
    }
}
