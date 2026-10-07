package com.mthree.pour_control.controller;

import com.mthree.pour_control.dto.DailyCloseoutRequest;
import com.mthree.pour_control.service.AuditService;
import com.mthree.pour_control.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReportController {

    private AuditService auditService;

    public ReportController(AuditService auditService) {
        this.auditService = auditService;

    }

    public void getVarianceReport() {

    }

    public void getRecorder() {

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

