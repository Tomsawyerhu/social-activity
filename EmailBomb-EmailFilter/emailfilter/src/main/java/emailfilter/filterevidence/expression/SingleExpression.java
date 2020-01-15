package emailfilter.filterevidence.expression;

import emailfilter.filterevidence.expression.expression.Compare;
import emailfilter.filterevidence.expression.expression.Content;

/*
*单条批处理语句
* 格式 content+compare+comparison(e.g. DATE EQUAL 2019-01-01-10-00-00)
* */
public class SingleExpression {
    private static SingleExpression singleExpression=new SingleExpression();

    @Expression(Content = Content.DATE )
    private String content;

    @Expression(Compare = Compare.EQUAL )
    private String compare;

    @Expression(Comparison="yyyy-MM-dd-HH-mm-ss" )
    private String comparison;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public SingleExpression getInstance(){
        return singleExpression;
    }

    public static SingleExpression parse(String s){
        String[] m=s.split(" ");
        SingleExpression singleExpression=new SingleExpression();
        singleExpression.setContent(m[0]);
        singleExpression.setCompare(m[1]);
        singleExpression.setComparison(m[2]);
        return singleExpression;

    }

}
