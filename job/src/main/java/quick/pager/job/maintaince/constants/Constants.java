package quick.pager.job.maintaince.constants;

public interface Constants {


    interface Operation {
        String all = "all";
        String info = "info";
        String delete = "delete";
        String save = "save";
        String update = "update";
        String execute = "execute";
        String pause = "pause";
        String resume = "resume";
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
