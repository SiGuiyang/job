package quick.job.sql;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;
import quick.job.maintaince.model.JobModel;

public class JobSQL {

    public String selectAll(@Param("model") final JobModel model) {
        return new SQL() {
            {
                SELECT("*");
                FROM("t_job");
                if (!StringUtils.isEmpty(model.getJobName())) {
                    WHERE(" jobName = #{jobName}");
                }
                if (!StringUtils.isEmpty(model.getJobGroup())) {
                    WHERE(" jobGroup = #{jobGroup}");
                }
                if (!StringUtils.isEmpty(model.getJobStatus())) {
                    WHERE(" jobStatus = #{jobStatus}");
                }
                ORDER_BY("id DESC");
            }
        }.toString();
    }

    public String selectPrimaryKey(@Param("id") Long id) {
        return new SQL() {
            {
                SELECT("*");
                FROM("t_job");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    public String deleteJob(@Param("model") JobModel model) {
        return new SQL() {
            {
                UPDATE("t_job");
                SET("jobStatus = #{jobStatus}");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    public String update(JobModel model) {
        return new SQL() {
            {
                UPDATE("t_job");
                if (!StringUtils.isEmpty(model.getJobName())) {
                    SET("jobName = #{jobName}");
                }
                if (!StringUtils.isEmpty(model.getJobGroup())) {
                    SET("jobGroup = #{jobGroup}");
                }
                if (!StringUtils.isEmpty(model.getCron())) {
                    SET("cron = #{cron}");
                }
                if (!StringUtils.isEmpty(model.getMethodName())) {
                    SET("methodName = #{methodName}");
                }
                if (!StringUtils.isEmpty(model.getSpringId())) {
                    SET("springId = #{springId}");
                }
                if (!StringUtils.isEmpty(model.getClassName())) {
                    SET("className = #{className}");
                }
                if (!StringUtils.isEmpty(model.getJobStatus())) {
                    SET("jobStatus = #{jobStatus}");
                }
                if (!StringUtils.isEmpty(model.getDescription())) {
                    SET("description = #{description}");
                }

                WHERE("id=#{id}");
            }
        }.toString();
    }

    public String save(){
        return new SQL(){
            {
                INSERT_INTO("t_job");
                VALUES("createTime","#{createTime}");
                VALUES("jobName","#{jobName}");
                VALUES("jobGroup","#{jobGroup}");
                VALUES("jobStatus","#{jobStatus}");
                VALUES("cron","#{createTime}");
                VALUES("description","#{description}");
                VALUES("className","#{className}");
                VALUES("springId","#{springId}");
                VALUES("methodName","#{methodName}");
            }
        }.toString();
    }
}
