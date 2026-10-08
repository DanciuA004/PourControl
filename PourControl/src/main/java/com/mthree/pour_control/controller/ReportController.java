package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.DailyCloseoutRequest;
import com.mthree.pour_control.service.AuditService;
import com.mthree.pour_control.service.ReportService;
import com.mthree.pour_control.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> getVarianceReport(
            @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        try {
            String report = reportService.generateVarianceReport(date);
            return ResponseEntity.ok(Map.of("report", report));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/reorder/{date}")
    @ResponseStatus(HttpStatus.OK)
    public String getReorder(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return reportService.calculateReorder(date);
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

