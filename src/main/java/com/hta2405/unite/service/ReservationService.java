package com.hta2405.unite.service;

import com.hta2405.unite.domain.Resource;

import java.util.List;

public interface ReservationService {
    public List<Resource> resourceSelectChange(String resourceType);
}
