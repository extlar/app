package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;

/**
 * Created by fdflores on 25/10/18.
 */

public class MailSender extends javax.mail.Authenticator {
    Context context;
    private String mailhost = "smtp.office365.com";
    private String port = "587";
    private String user;
    private String password;
    private Session session;

    /*----------------pdf---------------------------------*/
    private final String TAG = getClass().getSimpleName();
    final String TEXT = "";
    private int LETTER_SIZE_WIDTH = 1530;
    private int LETTER_SIZE_HIGH = 1980;
    //private ImageView iv_screenShot;
    //private Button b_topdf;
    PdfDocument document;

    public MailSender(String user, String password, Context context) {
        this.user = user;
        this.password = password;
        this.context = context;

        Properties props = new Properties();
        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String recipients, String choro) throws Exception {
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            //int paraSacarElAut = choro.lastIndexOf("Aut. ");;
            //String sacandoElAut = choro.substring(paraSacarElAut);
            message.setSubject("Comprobante de pago Yast\u00E1s ");

            //message.setContent(createPDF(createBitmap(choro)), "pdf");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            //mimeBodyPart.setContent(createBitmap(choro), null);
            Bitmap image = createBitmap(choro);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            mimeBodyPart.setDataHandler(new DataHandler(
                    new ByteArrayDataSource(byteArray, "image/jpeg")));

            // "XXX" below matches "XXX" above in html code
            mimeBodyPart.setContentID("<receipt>");
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    Bitmap createBitmap(String choro) {

        try {
            View screenShot = LayoutInflater.from(context).inflate(R.layout.pdf_layout, null);
            TextView body = screenShot.findViewById(R.id.tv_pdftexto);
            body.setText(choro);

            screenShot.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            screenShot.layout(0, 0, screenShot.getMeasuredWidth(),screenShot.getMeasuredHeight());

            final Bitmap bitmap = Bitmap.createBitmap(screenShot.getMeasuredWidth(),
                    screenShot.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            screenShot.draw(canvas);

            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "createView: " + e.toString());
        }
        return null;
    }
}
