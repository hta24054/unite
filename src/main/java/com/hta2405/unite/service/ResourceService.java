package com.hta2405.unite.service;

import com.hta2405.unite.domain.Resource;
import com.hta2405.unite.mybatis.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public List<Resource> getResourceList() {
        return resourceMapper.getAllResource();
    }

    public int addResource(Resource resource) {
        return resourceMapper.insertResource(resource);
    }

    public int updateResource(Resource resource) {
        return resourceMapper.updateResource(resource);
    }

    public int deleteResource(List<Long> list) {
        return resourceMapper.deleteResources(list);
    }
}
