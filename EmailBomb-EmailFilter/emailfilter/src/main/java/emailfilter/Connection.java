package emailfilter;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.security.GeneralSecurityException;
import java.util.Properties;

/*
*服务器连接类
 */
public class Connection {
    private boolean isConnected=false;
    private ConnectionConfig config=new ConnectionConfig();

    public void setConnection(String USER,String PASSWORD){
        config.setPASSWORD(PASSWORD);
        config.setUSER(USER);
    }
    public Store connect(){
        Properties props=new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.qq.com");
        props.setProperty("mail.imap.port", "143");
        // 1、创建session
        Session session = Session.getInstance(props);
        // 2、通过session得到Store对象
        // 3、连上邮件服务器
        Store store = null;
        try {
            store =  session.getStore("imap");
            store.connect(config.getUSER(),config.getPASSWORD());
        } catch (NoSuchProviderException e) {
            this.isConnected=false;
            e.printStackTrace();
            return null;
        } catch (MessagingException e) {
            this.isConnected=false;
            e.printStackTrace();
            return null;
        }

        this.isConnected=true;
        System.out.println("Connection success");
        return store;
    }

    public boolean isConnected(){
        return isConnected;
    }

    private class ConnectionConfig{
        String USER = "robot"; // 用户名
        String PASSWORD = "password520"; // 密码
        String MAIL_SERVER_HOST = "imap.qq.com"; // 邮箱服务器,默认qq邮箱
        String TYPE_HTML = "text/html;charset=UTF-8"; // 文本内容类型
        String MAIL_FROM = "[email protected]"; // 发件人
        String MAIL_TO = "[email protected]"; // 收件人
        String MAIL_CC = "[email protected]"; // 抄送人
        String MAIL_BCC = "[email protected]"; // 密送人

        public String getUSER() {
            return USER;
        }

        public void setUSER(String USER) {
            this.USER = USER;
        }

        public String getPASSWORD() {
            return PASSWORD;
        }

        public void setPASSWORD(String PASSWORD) {
            this.PASSWORD = PASSWORD;
        }

        public String getMAIL_SERVER_HOST() {
            return MAIL_SERVER_HOST;
        }

        public void setMAIL_SERVER_HOST(String MAIL_SERVER_HOST) {
            this.MAIL_SERVER_HOST = MAIL_SERVER_HOST;
        }

        public String getTYPE_HTML() {
            return TYPE_HTML;
        }

        public void setTYPE_HTML(String TYPE_HTML) {
            this.TYPE_HTML = TYPE_HTML;
        }

        public String getMAIL_FROM() {
            return MAIL_FROM;
        }

        public void setMAIL_FROM(String MAIL_FROM) {
            this.MAIL_FROM = MAIL_FROM;
        }

        public String getMAIL_TO() {
            return MAIL_TO;
        }

        public void setMAIL_TO(String MAIL_TO) {
            this.MAIL_TO = MAIL_TO;
        }

        public String getMAIL_CC() {
            return MAIL_CC;
        }

        public void setMAIL_CC(String MAIL_CC) {
            this.MAIL_CC = MAIL_CC;
        }

        public String getMAIL_BCC() {
            return MAIL_BCC;
        }

        public void setMAIL_BCC(String MAIL_BCC) {
            this.MAIL_BCC = MAIL_BCC;
        }
    }
}
