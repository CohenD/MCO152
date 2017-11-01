import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.TimerTask;

class EmailMessage {
    String from, to, subject, message,  dateTimeRecieved;
}

interface IReadMail
{
    EmailMessage[] readMail();
}

class ReadMail implements IReadMail{

    final String userName = "emailTester6000@gmail.com";
    final String password = "Test6000";

    Properties props = new Properties();

    EmailMessage[] em;

    Session session = Session.getInstance(props);

    public ReadMail(){
        props.put("mail.pop3.host", "pop.gmail.com");
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.store.protocol", "pop3");
    }


    @Override
    public EmailMessage[] readMail() {

        try {
            Store mailStore = session.getStore("pop3s");
            mailStore.connect("pop.gmail.com", userName, password);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] emailMessages = folder.getMessages();

            em = new EmailMessage[emailMessages.length];

            //Iterate the messages
            for (int i = 0; i < emailMessages.length; i++) {

                em[i].from = emailMessages[i].getFrom()[0].toString();
                em[i].to = userName;
                em[i].subject = emailMessages[i].getSubject().toString();
                em[i].message = emailMessages[i].getContent().toString();
                em[i].dateTimeRecieved = emailMessages[i].getReceivedDate().toString();
            }
            folder.close(false);
            mailStore.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in receiving email.");
        }

        return em;
    }
}

interface IClock
{
    LocalDateTime now();
}

class Clock implements IClock{
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();//inject .of(LocalDate.now(), LocalTime.MAX.minusSeconds(3));
    }
}

interface IMailer
{
    void sendMail(String to, String subject, String message);
}

class Mailer implements IMailer{

    @Override
    public void sendMail(String to, String subject, String message) {
        String from = "emailtester6000@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("emailtester6000", "Test6000");
                    }
                });

        try {
            Message email = new MimeMessage(session);

            email.setFrom(new InternetAddress(from));
            email.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            email.setSubject(subject);

            email.setText(message);

            Transport.send(email);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

class ClockMailer {
    private IClock clock;
    private IMailer mailer;

    public ClockMailer(IClock iClock, IMailer iMailer) {
        clock = iClock;
        mailer = iMailer;
    }

    public void start(){
        //My opionion one of the most effecient ways which
        // maintians the structure of the specs and still allows for dependencey injection
        LocalDateTime now = clock.now();

        java.util.Timer timer = new java.util.Timer();

        long delay = now.toLocalTime().compareTo(LocalTime.MIDNIGHT) == 0 ? 0 :
                ChronoUnit.MILLIS.between(now, LocalDateTime.of(now.plusDays(1).toLocalDate(), LocalTime.MIDNIGHT));

        System.out.println(delay);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("inRun");
                mailer.sendMail(
                        "dcoh18@gmail.com",
                        "Hello I am a rich Nigerian Prince",
                        "Please call me Dr. Robinson");
            }
        }, delay);
    }
}


///////////////////////////////////////////////////////////////////////Boiler Plate
interface ElapsedTimeClockI{
    long currentTimeMillis();
}
class ElapsedTimeClock implements ElapsedTimeClockI{
    public long currentTimeMillis(){
        return System.currentTimeMillis();
    }
}
class ElapsedTimeClock2 implements ElapsedTimeClockI{
    public long currentTimeMillis(){
        return System.nanoTime() / 1_000_000;
    }
}
class Timer {

    public Timer(ElapsedTimeClockI etc){ // dependency injection Timer is dependent on ElapsedTimeClockI
        clock = etc;
    }
    private long startMs;
    private long stopMs;
    private boolean isStopped;
    private ElapsedTimeClockI clock;

    public void start() {

        isStopped = true;
        startMs = clock.currentTimeMillis();
    }

    public void stop() {
        isStopped = true;
        stopMs = clock.currentTimeMillis();
    }

    public void reset() {
        startMs = clock.currentTimeMillis();
    }

    public long getElapsedTime() {
        return (isStopped ? stopMs : clock.currentTimeMillis()) - startMs;
    }
}
///////////////////////////////////////////////////////////////////////Boiler Plate

public class Main {

    public static void main(String[] args) throws ParseException {

//        ClockMailer cm = new ClockMailer(new Clock(), new Mailer());
//
//        cm.start();

        ReadMail r = new ReadMail();

        r.readMail();

    }
}