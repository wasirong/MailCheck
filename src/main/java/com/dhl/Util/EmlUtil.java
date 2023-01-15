package com.dhl.Util;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 使用Java的mail包解析 标准的 .eml格式的邮件文件
 *
 * @author
 * @date 2019/08/07
 */
public class EmlUtil {
    // 定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
    private String displayName;

    private String from;

    private List<String> to=new ArrayList<>();

    private String cc;

    private String bcc;

    private String server;

    private String subject;

    private String content;

    private String parsingPath;

    public String getDisplayName() {
        return displayName;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public String getServer() {
        return server;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getParsingPath() {
        return parsingPath;
    }

    public void setParsingPath(String parsingPath) {
        this.parsingPath = parsingPath;
    }

    public Vector<String[]> getAttachList() {
        return attachList;
    }

    public void setAttachList(Vector<String[]> attachList) {
        this.attachList = attachList;
    }

    public static Map<String, Object> getResult() {
        return result;
    }

    public static void setResult(Map<String, Object> result) {
        EmlUtil.result = result;
    }

    /**
     * 用于保存发送附件的文件名的集合（<code>new String[]{文件名,显示名称}</code>）
     */
    public void addFile(String[] filename) {
        attachList.add(filename);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 设置SMTP服务器地址
     */
    public void setServer(String smtpServer) {
        this.server = smtpServer;
    }

    /**
     * 设置发件人的地址
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 设置显示的名称
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 设置接收者
     */
    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * 设置主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 设置主体内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 用于保存发送附件的文件名的集合（<code>new String[]{文件名,显示名称}</code>）
     */
    private Vector<String[]> attachList = new Vector<String[]>();

    private String contentType = "text/html";

    private String charset = "utf-8";

    private int port = 25;

    private String sentDate;

    static Map<String, Object> result = new HashMap<>();

    public Map<String, Object> getEmlContent(String file) throws Exception {
        result = new HashMap<>();
        parserFile(file);
        return result;
    }

    public Map<String, Object> getEmlContent(File file) throws Exception {
        result = new HashMap<>();
        parserFile(file.getAbsolutePath());
        return result;
    }

    /**
     * 解析文件
     *
     * @param emlPath 文件路径
     */
    public Map<Object, Object> parserFile(String emlPath) throws Exception {
        Map<Object, Object> map;
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        InputStream inMsg;
        inMsg = new FileInputStream(emlPath);
        Message msg = new MimeMessage(session, inMsg);
        map = parseEml(msg);
        return map;
    }

    private Map<Object, Object> parseEml(Message msg) throws Exception {
        Map<Object, Object> map = new HashMap<>(10);
        // 发件人信息
        Address[] froms = msg.getFrom();
        if (froms != null) {
            InternetAddress addr = (InternetAddress) froms[0];
//            System.out.println("发件人地址:" + addr.getAddress());
//            System.out.println("发件人显示名:" + addr.getPersonal());
            result.put("sender", addr.getAddress());
        }
        //收件人信息
        Address[] tos = msg.getAllRecipients();
        String sjrAddressList = "";
        for (Address a : tos) {
            InternetAddress addr = (InternetAddress) a;
//            System.out.println("====>收件人地址：" + addr.getAddress());
            if (addr.getAddress() != null && addr.getAddress().length() >= 0) {
                sjrAddressList += addr.getAddress() + ",";
            }

        }
        sjrAddressList = sjrAddressList.substring(0, sjrAddressList.length() - 1);
        result.put("receiver", sjrAddressList);
//        System.out.println("邮件主题:" + msg.getSubject());

        result.put("senddate", msg.getSentDate());
        result.put("subject", msg.getSubject());
        result.put("receivedate", msg.getSentDate());
        Object o = msg.getContent();
//        if (o instanceof Multipart) {
//            Multipart multipart = (Multipart) o;
//            reMultipart(multipart);
//        } else if (o instanceof Part) {
//            Part part = (Part) o;
//            rePart(part);
//        } else
        {
//            System.out.println("类型" + msg.getContentType());

            map.put("subject", new StringUtil().GetIntFromStr(msg.getSubject()));
        }

        return map;
    }

    /**
     * 解析内容
     *
     * @param part
     * @throws Exception
     */
    private Map<String, Object> rePart(Part part) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (part.getDisposition() != null) {
            String strFileNmae = part.getFileName();
            if (strFileNmae != null) {
                // MimeUtility.decodeText解决附件名乱码问题
                strFileNmae = MimeUtility.decodeText(strFileNmae);
                System.out.println("发现附件: " + strFileNmae);

                // 打开附件的输入流
                InputStream in = part.getInputStream();
                File dir = new File(parsingPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String strFile = parsingPath/*"C:\\Users\\23328\\Downloads\\test\\"*/ + strFileNmae;
                map.put("attachname", strFileNmae);
                String type = strFileNmae.substring(strFileNmae.lastIndexOf(".") + 1);
                map.put("type", type);
                map.put("attachdownloadurl", strFile);
                FileOutputStream out = new FileOutputStream(strFile);
                byte[] bytes = new byte[1024];
                while (in.read(bytes, 0, 1024) != -1) {
                    out.write(bytes);
                }

                in.close();
                out.close();

            }
            System.out.println("内容类型: " + MimeUtility.decodeText(part.getContentType()));
            if (result.get("content").toString() != null && result.get("htmlcontent").toString() != null) {
                if (part.getContentType().startsWith("text/plain")) {
                    System.out.println("文本内容：" + part.getContent());
                    result.put("content", part.getContent());
                } else if (part.getContentType().startsWith("text/html")) {
                    System.out.println("HTML内容：" + part.getContent());
                    result.put("htmlcontent", part.getContent().toString());

                }
            }
            return map;

        } else {

            if (part.getContentType().startsWith("text/plain")) {
                System.out.println("文本内容：" + part.getContent());
                result.put("content", part.getContent());
            } else if (part.getContentType().startsWith("text/html")) {
//                System.out.println(part.getContentType());
                System.out.println("HTML内容：" + part.getContent());
                result.put("htmlcontent", part.getContent().toString());

            }
        }
        return null;
    }

    /**
     * 接卸包裹（含所有邮件内容(包裹+正文+附件)）
     *
     * @param multipart
     * @throws Exception
     */
    private void reMultipart(Multipart multipart) throws Exception {
        // System.out.println("邮件共有" + multipart.getCount() + "部分组成");
        // 依次处理各个部分
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            // System.out.println("处理第" + j + "部分");
            Part part = multipart.getBodyPart(j);// 解包, 取出 MultiPart的各个部分,
            // 每部分可能是邮件内容,
            // 也可能是另一个小包裹(MultipPart)
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
            if (part.getContent() instanceof Multipart) {
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                // 递归迭代
                reMultipart(p);
            } else {
                Map<String, Object> rePart = rePart(part);
                mapList.add(rePart);
                result.put("Attachment", mapList);
            }
        }
    }

    /**
     * 将email写至文件
     *
     * @throws IOException
     * @throws FileNotFoundException
     * @throws MessagingException
     */
    public void writeTo(String filename) throws FileNotFoundException,
            IOException, MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "false");

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        Message msg = new MimeMessage(session);
        Address from_address = new InternetAddress(from, displayName);
        msg.setFrom(from_address);

        InternetAddress[] addressTo =  new InternetAddress[to.size()];
        for (int i = 0; i < to.size(); i++) {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(to.get(i));
            addressTo[i]=internetAddress;
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        if (StringUtils.hasLength(cc)) {
            InternetAddress[] addressCc = {new InternetAddress(cc)};
            try {
                msg.setRecipients(Message.RecipientType.CC, addressCc);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.hasLength(bcc)) {
            InternetAddress[] addressBcc = {new InternetAddress(bcc)};
            msg.setRecipients(Message.RecipientType.BCC, addressBcc);
        }

        msg.setSubject(subject);
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(content.toString(), getContentType() + "; charset="
                + getCharset());
        mp.addBodyPart(mbp);
        if (!attachList.isEmpty()) {// 有附件
            for (String[] file : attachList) {
                mbp = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file[0]); // 得到数据源
                mbp.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
                String dspName = file.length < 2 ? fds.getName() : file[1];
                mbp.setFileName(MimeUtility.encodeText(dspName, getCharset(),
                        "B")); // 得到文件名同样至入BodyPart
                mp.addBodyPart(mbp);
            }
            attachList.removeAllElements();
        }
        msg.setContent(mp); // Multipart加入到信件
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            msg.setSentDate(dateFormat.parse(getSentDate())); // 设置信件头的发送日期

        } catch (ParseException e) {
            e.printStackTrace();
        }


        msg.saveChanges();
        // 写至文件
        msg.writeTo(new FileOutputStream(new File(filename)));
    }

//    //写入测试
//    public static void main(String[] args) throws Exception {
//        EmlUtil emlUtil = new EmlUtil();
//        emlUtil.setSubject("test");
//        emlUtil.setContent("test");
//        emlUtil.setDisplayName("test");
//        emlUtil.setFrom("test");
//        List<String> strings = new ArrayList<>();
//        strings.add("test");
//        emlUtil.setTo(strings);
//        emlUtil.setSentDate("2021-04-14 12:20:00");
//        emlUtil.writeTo("D:/test.eml");
//    }
}
