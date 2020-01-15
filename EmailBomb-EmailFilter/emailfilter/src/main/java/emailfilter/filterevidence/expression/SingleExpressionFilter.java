package emailfilter.filterevidence.expression;

import emailfilter.filterevidence.Filter;
import javax.mail.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/*
*单条批处理语句过滤器
 */
public class SingleExpressionFilter implements Filter<Message> {
    private SingleExpression expression;
    public SingleExpressionFilter(SingleExpression expression){
        this.expression=expression;
    }
    public List<Message> filt(List<Message> list) {
        List<Message> list2=new ArrayList<Message>();
        for(Message message:list){
            if(isValid(message)) list2.add(message);
        }
        return list2;
    }

    public char getOperation() {
        return 0;
    }

    private boolean isValid(Message message) {//符合条件就是valid,要执行操作
        String title="",sender="",main="",attachment="";
        long date=0;
        String comparison=expression.getComparison();
        if(expression.getContent().equals("DATE")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date transformDate=null;
            try {
                transformDate=sdf.parse(comparison);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long date2=transformDate.getTime();//过滤条件时间
            try {
                date=message.getReceivedDate().getTime();//邮件接收时间
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if(expression.getCompare().equals("BEFORE")){
                return date < date2;
            }else if(expression.getCompare().equals("EQUAL")){
                return date==date2;
            }else{//AFTER
                return date>date2;
            }

        }else if(expression.getContent().equals("TITLE")){
            Pattern pattern=Pattern.compile(comparison);
            Pattern pattern1=Pattern.compile("^"+comparison+"$");
            try {
                title=message.getSubject();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if(expression.getCompare().equals("CONTAIN")){
                return pattern.matcher(title).find();
            }else if(expression.getCompare().equals("NCONTAIN")){
                return !pattern.matcher(title).find();
            }else if(expression.getCompare().equals("MATCH")){
                return pattern1.matcher(title).find();
            }else{//NMATCH
                return !pattern1.matcher(title).find();
            }

        }else if(expression.getContent().equals("SENDER")){
            Pattern pattern=Pattern.compile(comparison);
            Pattern pattern1=Pattern.compile("^"+comparison+"$");
            try {
                sender=message.getFrom()[0].toString();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if(expression.getCompare().equals("CONTAIN")){
                return pattern.matcher(sender).find();
            }else if(expression.getCompare().equals("NCONTAIN")){
                return !pattern.matcher(sender).find();
            }else if(expression.getCompare().equals("MATCH")){
                return pattern1.matcher(sender).find();
            }else{//NMATCH
                return !pattern1.matcher(sender).find();
            }

        }else if(expression.getContent().equals("MAIN")){
            Pattern pattern=Pattern.compile(comparison);
            Pattern pattern1=Pattern.compile("^"+comparison+"$");
            try {
                StringBuffer content = new StringBuffer(1000);
                getMailTextContent(message, content);
                main=content.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            if(expression.getCompare().equals("CONTAIN")){
                return pattern.matcher(main).find();
            }else if(expression.getCompare().equals("NCONTAIN")){
                return !pattern.matcher(main).find();
            }else if(expression.getCompare().equals("MATCH")){
                return pattern1.matcher(main).find();
            }else{//NMATCH
                return !pattern1.matcher(main).find();
            }

        }else {//ATTACHMENT
            //TODO
            StringBuffer buffer=new StringBuffer(1000);
            boolean isText=parseAttachment(buffer);
            if(expression.getCompare().equals("CONTAIN")){//内容

            }else if(expression.getCompare().equals("NCONTAIN")){

            }else if(expression.getCompare().equals("MATCH")){//格式

            }else{//NMATCH

            }

        }
        return false;
    }

    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {//获取正文内容
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part)part.getContent(),content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart,content);
            }
        }
    }

    /*
    *@return 是否为文本文件
    *@buffer 解析内容的容器
     */
    private boolean parseAttachment(StringBuffer buffer){
        //TODO：解析附件(文本类型)
        return true;
    }

}
