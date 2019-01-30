package quick.pager.job.mapper;


import java.util.List;
import quick.pager.job.maintaince.model.JobModel;

public interface JobMapper {

    // 查询所有job任务
    List<JobModel> selectAll(JobModel model);

    int insertSelective(JobModel record);

    JobModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JobModel record);
}