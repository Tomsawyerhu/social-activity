package emailfilter;

public class MyException extends Exception{
    private String alert;
    public MyException(String alert){
        super(alert);
        this.alert=alert;
    }

    @Override
    public String toString() {
        return "异常信息：" +
                 alert ;
    }
}
