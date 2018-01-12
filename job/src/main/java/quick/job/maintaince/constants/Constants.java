package quick.job.maintaince.constants;

public interface Constants {

    enum Operation {
        all, info, delete, save, update, execute, pause, resume
    }

    enum JobStatus {
        pause("0", "暂停"),
        delete("1", "删除"),
        running("2", "正常");
        public String status;

        public String name;


        JobStatus(String status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
