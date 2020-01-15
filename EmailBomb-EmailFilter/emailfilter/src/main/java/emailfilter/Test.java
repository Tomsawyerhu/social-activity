package emailfilter;

import emailfilter.emailfilter.EmailFilter;
import emailfilter.emailfilter.EmailFilterFactory;
import emailfilter.filterevidence.auto.AutoFilter;

public class Test {
    public static void main(String[] args) {
       FilterService service=new FilterService();
       service.initService("1095549886@qq.com","yfhrjghyiybahcee");
       service.startService(2,"00-05-00",5,"MAIN CONTAIN hello D");
    }
}
