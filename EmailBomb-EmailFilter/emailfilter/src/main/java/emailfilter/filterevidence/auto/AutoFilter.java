package emailfilter.filterevidence.auto;

import emailfilter.filterevidence.Filter;
import emailfilter.filterevidence.expression.SingleExpressionFilter;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/*
*垃圾邮件过滤器
*垃圾邮件的标准：过大?，内容重复，短时间内大量邮件，特定内容，动态黑名单
 */
public class AutoFilter implements Filter<Message> {
    //TODO
    public static int STDINTERVAL=5;
    public static int STDNUM=5;
    public List<Message> filt(List<Message> list) {
        if(list.size()==0) return new ArrayList<Message>();
        HashSet<Message> hashSet=new HashSet<Message>();
        //下面以模式1&&模式2为例
        autoMode1(list,hashSet,STDINTERVAL,STDNUM);
        autoMode2(list,hashSet);

        return new ArrayList<Message>(hashSet);
    }

    public char getOperation() {
        return 'D';
    }


    private long calculateTime(Message message){
        try {
            long date=message.getReceivedDate().getTime();//邮件接收时间
            return date;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
    *@hashSet 将筛除的邮件放入
     */

    /*
    *过滤模式1：
    *连续STDNUM封邮件之间间隔小于等于STDINTERVAL视为垃圾邮件
     */
    private void autoMode1(List<Message> list,HashSet<Message> hashSet,int STDINTERVAL,int STDNUM){
        List<Long> timeStampList=new ArrayList<Long>();
        for(Message message:list){
            timeStampList.add(calculateTime(message));
        }
        int size=list.size();
        if(size>=STDNUM){
            int start=0;
            while(start+STDNUM<=size){
                boolean flag=true;
                for(int k=0;k<STDNUM-1;k++){
                    if(!(calculateTime(list.get(start+k+1))-calculateTime(list.get(start+k))<=STDINTERVAL||-calculateTime(list.get(start+k+1))+calculateTime(list.get(start+k))<=STDINTERVAL)) flag=false;
                }
                if(flag) for(int k=0;k<STDNUM;k++){
                    hashSet.add(list.get(start+k));
                }
                start++;
            }
        }

    }

    /*
    *过滤模式2：
    *内容重复视为垃圾邮件
     */
    private void autoMode2(List<Message> list,HashSet<Message> hashSet){
        for(Message message:list){
            StringBuffer buffer=new StringBuffer(1000);
            try {
                SingleExpressionFilter.getMailTextContent(message,buffer);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int count=0;
            for(Message message1:list){
                StringBuffer buffer1=new StringBuffer(1000);
                try {
                    SingleExpressionFilter.getMailTextContent(message1,buffer1);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(buffer.toString().equals(buffer1.toString())) count++;
            }
            if(count>1)  hashSet.add(message);
        }
    }

    /*
    *过滤模式3：
    *包含特定内容
     */
    private void autoMode3(List<Message> list,HashSet<Message> hashSet){
        //TODO
    }


    /*
    *过滤模式4：
    *动态黑名单
     */
    private void autoMode4(List<Message> list,HashSet<Message> hashSet){
        //TODO
    }


    /*
     *解析黑名单
     */
    private String[] parseBlackList(String path){
        //TODO
        return null;
    }

}
