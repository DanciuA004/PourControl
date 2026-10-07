package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.DailyCloseoutRequest;
import com.mthree.pour_control.service.AuditService;
import com.mthree.pour_control.service.ReportService;
import com.mthree.pour_control.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ReportController {

    private AuditService auditService;
    private ReportService reportService;

    public ReportController(AuditService auditService, ReportService reportService) {
        this.auditService = auditService;
        this.reportService = reportService;

    }


    @GetMapping("/report/{date}")
    public String getVarianceReport(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return reportService.generateVarianceReport(date);
    }

    public void getReorder() {

    }


    /**
     * Returns
     *
     *
     * Expects request body like
     *{
     *   "cocktailCloseout": {
     *     "10": 5,  //cocktail id: new volume
     *     "12": 3
     *   },
     *   "stockCloseout": {
     *     "1000": 750,  //ingredient id: sale qty
     *     "2005": 1000
     *   }
     * }
     *
     */

    @PostMapping("/closeout")
    @ResponseStatus(HttpStatus.OK)
    public void processDailySale(@RequestBody DailyCloseoutRequest request){
        auditService.processCloseout(request);
    }
}

