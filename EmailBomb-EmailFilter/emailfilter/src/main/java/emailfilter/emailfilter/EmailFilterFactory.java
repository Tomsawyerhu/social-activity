package emailfilter.emailfilter;

import emailfilter.filterevidence.auto.AutoFilter;
import emailfilter.filterevidence.expression.ExpressionFilter;

public class EmailFilterFactory {
    public static EmailFilter emailFilter=new EmailFilter();
    public static EmailFilter getEmailFilter(ExpressionFilter filter){//批处理
        emailFilter.setFilter2(filter);
        return emailFilter;
    }
    public static EmailFilter getEmailFilter(AutoFilter filter){//垃圾邮件删除
        emailFilter.setFilter1(filter);
        return emailFilter;
    }
    public static EmailFilter getEmailFilter(ExpressionFilter filter1,AutoFilter filter2){//批处理+垃圾邮件删除
        emailFilter.setFilter1(filter2);
        emailFilter.setFilter2(filter1);
        return emailFilter;
    }
    public static EmailFilter getEmailFilter(AutoFilter filter1,ExpressionFilter filter2){//批处理+垃圾邮件删除
        emailFilter.setFilter1(filter1);
        emailFilter.setFilter2(filter2);
        return emailFilter;
    }

}
