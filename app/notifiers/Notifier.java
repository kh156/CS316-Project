package notifiers;

import play.mvc.*;

import javax.mail.internet.*;

import models.*;

public class Notifier extends Mailer {

    public static boolean welcome(User user) throws Exception {
        setFrom(new InternetAddress("admin@cs316project.com", "Administrator"));
        setReplyTo(new InternetAddress("help@cs316project.com", "Help"));
        setSubject("Welcome %s", user.name);
        addRecipient(new InternetAddress(user.email, user.name), new InternetAddress("new-users@cs316project.com", "New users notice"));
        return sendAndWait(user);
    }
    
}

