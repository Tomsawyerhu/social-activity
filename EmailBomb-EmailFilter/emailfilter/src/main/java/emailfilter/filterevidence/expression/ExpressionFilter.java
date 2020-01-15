package emailfilter.filterevidence.expression;

import emailfilter.emailfilter.EmailFilterFactory;
import emailfilter.filterevidence.AbstractFilter;
import emailfilter.filterevidence.Filter;
import emailfilter.filterevidence.expression.expression.Operation;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
*批处理过滤器
 */
public class ExpressionFilter extends AbstractFilter implements Filter<Message> {
    private static String AND="AND";
    private static String OR="OR";

    private String expression;//原始过滤规则
    private SingleExpression[] singleExpressions;//单条过滤规则集合
    private String[] conjections;//连词集合
    @Expression(Operation = Operation.D)
    private String operation;//操作

    public ExpressionFilter(String expression){
        this.expression=expression;
    }
    public ExpressionFilter(){}

    private void parse(){//将expression解析为SingleExpression和具体操作
        Pattern pattern=Pattern.compile("AND|OR");
        Matcher matcher=pattern.matcher(expression);
        int k=0;
        while(matcher.find()){ k++;}
        singleExpressions=new SingleExpression[k+1];
        conjections=new String[k];
        matcher.reset();
        if(matcher.find()){
            matcher.reset();
            int count=0;
            int start=0,end=0;
            boolean flag=true;

            while(matcher.find()){
                if(!flag){
                    start=end+3;
                }else {flag=false;}
                end=matcher.start();
                String single=expression.substring(start,end);
                if(matcher.group(count).equals(AND)) {conjections[count]=AND;}
                else {conjections[count]=OR;}
                start=matcher.end()+1;
                singleExpressions[count]=SingleExpression.parse(single);
                count++;
            }
            singleExpressions[count]=SingleExpression.parse(expression.substring(start,expression.length()-2));
        }else{
            //单条规则或无规则
            if(expression.length()!=1) singleExpressions[0]=SingleExpression.parse(expression.substring(0,expression.length()-2));
        }
        this.operation=expression.substring(expression.length()-1);
    }


    public List<Message> filt(List<Message> list) {
        List<Message> newList=null;
        SingleExpressionFilter singleExpressionFilter;
        this.parse();
        for(int i=0;i<singleExpressions.length;i++){
            singleExpressionFilter=new SingleExpressionFilter(singleExpressions[i]);
            if(i==0){
                newList=singleExpressionFilter.filt(list);
            }
            else{
                List<Message> newList2;
                if(conjections[i-1].equals(AND)){
                    newList2=singleExpressionFilter.filt(list);
                    newList=join(newList2,newList);
                }else{
                    newList2=singleExpressionFilter.filt(list);
                    newList=unite(newList,newList2);
                }
            }

        }
        return newList;
    }

    public char getOperation() {
        return this.operation.charAt(0);
    }


    private static List<Message> unite(List<Message>... lists){//求并,去重
        HashSet<Message> hashSet=new HashSet<Message>();
        for(List<Message> list:lists){
            hashSet.addAll(list);
        }
        return new ArrayList<Message>(hashSet);
    }

    private static List<Message> join(List<Message>... lists){//求交
        if (lists.length==0) return new ArrayList<Message>();
        HashSet<Message> hashSet=new HashSet<Message>();
        for(Message message:lists[0]){
            boolean flag=true;
            for(List<Message> list:lists){
                if(!new HashSet<Message>(list).contains(message))flag=false;
            }
            if(flag) hashSet.add(message);
        }
        return new ArrayList<Message>(hashSet);
    }

    public void filtAndClear() {
        EmailFilterFactory.getEmailFilter(this).filtAndClear();
    }
}
