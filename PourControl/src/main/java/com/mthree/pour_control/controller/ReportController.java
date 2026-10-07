package com.mthree.pour_control.controller;

import com.mthree.pour_control.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/{date}")
    public String getVarianceReport(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return reportService.generateVarianceReport(date);
    }

    public void getReorder() {

    }
}
