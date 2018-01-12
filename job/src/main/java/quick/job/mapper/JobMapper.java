package quick.job.mapper;

import org.apache.ibatis.annotations.*;
import quick.job.maintaince.model.JobModel;
import quick.job.sql.JobSQL;

import java.util.List;

@Mapper
public interface JobMapper {

    @SelectProvider(method = "selectAll", type = JobSQL.class)
    List<JobModel> selectAll(@Param("model") JobModel model);

    @SelectProvider(method = "selectPrimaryKey", type = JobSQL.class)
    JobModel selectPrimaryKey(@Param("id") Long id);

    @UpdateProvider(method = "deleteJob", type = JobSQL.class)
    @Options(flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true)
    void deleteJob(@Param("model") JobModel model);

    @UpdateProvider(method = "update", type = JobSQL.class)
    @Options(flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true)
    int update(JobModel model);

    @InsertProvider(method = "save", type = JobSQL.class)
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void save(JobModel model);
}
