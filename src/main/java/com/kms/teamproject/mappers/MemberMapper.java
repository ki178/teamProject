package com.kms.teamproject.mappers;

import com.kms.teamproject.entities.MemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    MemberEntity selectMemberById(@Param("memberId") String memberId);
}
