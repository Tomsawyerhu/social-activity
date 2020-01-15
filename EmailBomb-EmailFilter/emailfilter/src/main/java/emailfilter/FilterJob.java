package emailfilter;

import emailfilter.emailfilter.EmailFilter;
import emailfilter.emailfilter.EmailFilterFactory;
import emailfilter.filterevidence.auto.AutoFilter;
import emailfilter.filterevidence.expression.ExpressionFilter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class FilterJob implements Job {
    private Connection connection=new Connection();
    public static String savePath=null;
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String user=jobExecutionContext.getJobDetail().getJobDataMap().getString("USER");
        String pwd=jobExecutionContext.getJobDetail().getJobDataMap().getString("PASSWORD");
        int mode= jobExecutionContext.getJobDetail().getJobDataMap().getInt("MODE");
        connection.setConnection(user,pwd);
        EmailFilter emailFilter=null;
        if(mode==1){
            emailFilter= EmailFilterFactory.getEmailFilter(new AutoFilter());
        }else {
            String input=jobExecutionContext.getJobDetail().getJobDataMap().getString("INPUT");
            savePath=jobExecutionContext.getJobDetail().getJobDataMap().getString("SAVEPATH");
            if(mode==2) emailFilter= EmailFilterFactory.getEmailFilter(new ExpressionFilter(input));
            else if(mode==3) emailFilter=EmailFilterFactory.getEmailFilter(new ExpressionFilter(input),new AutoFilter());
            else try {
                    throw new MyException("模式不存在");
                } catch (MyException e) {
                    e.printStackTrace();
                }
        }
        assert emailFilter != null;
        if(!connection.isConnected()) emailFilter.connect(connection);
        emailFilter.filtAndClear();

    }
}
