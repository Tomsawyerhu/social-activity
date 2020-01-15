package emailfilter;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.regex.Pattern;

/*
*对外接口
 */
public class FilterService {
    private String userName;
    private String pwd;
    public void initService(String userName,String password){
        this.pwd=password;
        this.userName=userName;
    }

    public void startService(int mode,String lastTime,int interval,String... s){//模式，作用时间，时间间隔（s），（批处理的语句,存储地址）
        assert isInitialized();
        assert Pattern.compile("([0-9]{2})-([0-5][0-9])-([0-5][0-9])").matcher(lastTime).matches();
        assert Pattern.compile("[0-5][0-9]").matcher(String.valueOf(interval)).matches();
        long lastTimeL=(Integer.parseInt(lastTime.substring(0,2))*60*60+Integer.parseInt(lastTime.substring(3,5))*60+Integer.parseInt(lastTime.substring(7)))*1000;
        Date endDate=new Date(new Date().getTime()+lastTimeL);
        JobDetail jobDetail= JobBuilder.newJob(FilterJob.class).withIdentity("hhc","dypro").build();
        jobDetail.getJobDataMap().put("USER",userName);
        jobDetail.getJobDataMap().put("MODE",mode);
        jobDetail.getJobDataMap().put("PASSWORD",pwd);
        if(mode!=1) {
            assert checkLegitimacy(s[0]);
            jobDetail.getJobDataMap().put("INPUT",s[0]);
            if(s.length>1){
                jobDetail.getJobDataMap().put("SAVEPATH",s[1]);
            }
        }
        Trigger trigger= TriggerBuilder.newTrigger().withIdentity("FilterJobTrigger","dypro").withSchedule(CronScheduleBuilder.cronSchedule("0/" + interval + " * * * * ?")).endAt(endDate).build();
        try {
            Scheduler scheduler=new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    private boolean isInitialized(){
        return userName!=null&&pwd!=null;
    }

    //检查批处理语句的合法性
    //是否符合语法,是否有具体含义，是否符合逻辑
    private boolean checkLegitimacy(String s){
        //TODO

        return true;
    }
}