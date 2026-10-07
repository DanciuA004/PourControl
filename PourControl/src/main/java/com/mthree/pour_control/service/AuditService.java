package com.mthree.pour_control.service;

import com.mthree.pour_control.dto.DailyCloseoutRequest;


public interface AuditService {
    void processCloseout(DailyCloseoutRequest request);
}
