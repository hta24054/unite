package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Cert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CertMapper {
    List<Cert> getCertByEmpId(String empId);

    void deleteAllCertByEmpId(String empId);

    void insertCert(@Param("list") List<Cert> certList);
}
