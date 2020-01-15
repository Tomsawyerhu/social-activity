import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

public class Test {
    public static void main(String[] args){
        System.out.println();
        int secDelta=5;
        JobDetail jobDetail= JobBuilder.newJob(SendMail.class).withIdentity("hhc","dypro").build();
        Trigger trigger=TriggerBuilder.newTrigger().withIdentity("trigger1","dypro").withSchedule(CronScheduleBuilder.cronSchedule("0/" + secDelta + " * * * * ?")).build();
        try {
            Scheduler scheduler=new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
