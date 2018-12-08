package hx.ai.mapper;

import hx.ai.pojo.Faceku;
import hx.ai.pojo.FacekuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FacekuMapper {
    int countByExample(FacekuExample example);

    int deleteByExample(FacekuExample example);

    int insert(Faceku record);

    int insertSelective(Faceku record);

    List<Faceku> selectByExample(FacekuExample example);

    int updateByExampleSelective(@Param("record") Faceku record, @Param("example") FacekuExample example);

    int updateByExample(@Param("record") Faceku record, @Param("example") FacekuExample example);

    String selectBygiduid(String gid,String uid);
}