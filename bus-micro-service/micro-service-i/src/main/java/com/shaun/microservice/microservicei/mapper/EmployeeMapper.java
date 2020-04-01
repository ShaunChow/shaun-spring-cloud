package com.shaun.microservice.microservicei.mapper;

import com.shaun.microservice.microservicei.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    @Insert("insert into employee(name,pwd) values " +
            "(#{name},#{pwd})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(Employee student);

    @Select("select * from employee")
    List<Employee> selectAll();
}
