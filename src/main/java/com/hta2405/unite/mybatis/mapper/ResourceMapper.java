package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {
    List<Resource> getAllResource();

    int insertResource(Resource resource);

    int deleteResources(@Param("list") List<Long> list);

    int updateResource(Resource resource);
}
