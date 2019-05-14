package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.bbpos.bbdevice.BBDeviceController;
import com.bbpos.bbdevice.CAPK;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.R;
import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.activities.ProcesoDePago;

public class Recibos implements BBDeviceController.BBDeviceControllerListener {
	
	private static byte[] INIT = {0x1B,0x40};
	private static byte[] POWER_ON = {0x1B,0x3D,0x01};
	private static byte[] POWER_OFF = {0x1B,0x3D,0x02};
	private static byte[] NEW_LINE = {0x0A};
	private static byte[] ALIGN_LEFT = {0x1B,0x61,0x00};
	private static byte[] ALIGN_CENTER = {0x1B,0x61,0x01};
	private static byte[] ALIGN_RIGHT = {0x1B,0x61,0x02};
	private static byte[] EMPHASIZE_ON = {0x1B,0x45,0x01};
	private static byte[] EMPHASIZE_OFF = {0x1B,0x45,0x00};
	private static byte[] FONT_5X8 = {0x1B,0x4D,0x00};
	private static byte[] FONT_5X12 = {0x1B,0x4D,0x01};
	private static byte[] FONT_8X12 = {0x1B,0x4D,0x02};
	private static byte[] FONT_10X18 = {0x1B,0x4D,0x03};
	private static byte[] FONT_SIZE_0 = {0x1D,0x21,0x00};
	private static byte[] FONT_SIZE_1 = {0x1D,0x21,0x11};
	private static byte[] CHAR_SPACING_0 = {0x1B,0x20,0x00};
	private static byte[] CHAR_SPACING_1 = {0x1B,0x20,0x01};
	private static byte[] KANJI_FONT_24X24 = {0x1C,0x28,0x41,0x02,0x00,0x30,0x00};
	private static byte[] KANJI_FONT_16X16 = {0x1C,0x28,0x41,0x02,0x00,0x30,0x01};
	
	protected static String toHexString(byte[] b) {
		if(b == null) {
			return "null";
		}
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xFF ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	protected static byte[] hexToByteArray(String s) {
		if(s == null) {
			s = "";
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for(int i = 0; i < s.length() - 1; i += 2) {
			String data = s.substring(i, i + 2);
			bout.write(Integer.parseInt(data, 16));
		}
		return bout.toByteArray();
	}

	public static byte[] genReceiptPago(Context context) {
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

		int lineWidth = 384;
		int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
		String singleLine = "", lineaConcepto="", lineaDato="", cadenaVacia="";
		for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
			singleLine += "-";
		}
		String doubleLine = "";
		for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
			doubleLine += "=";
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(INIT);
			baos.write(POWER_ON);
			baos.write(NEW_LINE);
			baos.write(ALIGN_CENTER);

			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.colibri_ticket);
			int targetWidth = 384;
			int targetHeight = (int)Math.round((double)targetWidth / (double)bitmap.getWidth() * (double)bitmap.getHeight());
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
			byte[] imageCommand = BBDeviceController.getImageCommand(scaledBitmap, 150);

			baos.write(imageCommand, 0, imageCommand.length);

			baos.write(NEW_LINE);
			baos.write(CHAR_SPACING_0);

			baos.write(FONT_SIZE_0);
			baos.write(EMPHASIZE_ON);
			baos.write(FONT_5X12);
			baos.write("Insurgentes Sur 1458".getBytes());
			baos.write(NEW_LINE);
			baos.write("Col. Actipan Delegación Benito Juárez".getBytes());
			baos.write(NEW_LINE);
			baos.write("CDMX".getBytes());
			baos.write(NEW_LINE);


			baos.write(FONT_SIZE_1);
			baos.write(FONT_5X12);
			baos.write("llegas, pagas y\nYastas".getBytes());
			baos.write(NEW_LINE);

			baos.write(FONT_SIZE_0);
			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_10X18);
			baos.write("Transaccion No. 2524".getBytes());
			baos.write(NEW_LINE);

			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);

			baos.write(ALIGN_LEFT);

			baos.write(FONT_10X18);
			baos.write("ROR NO ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("ROR2014-000556-000029".getBytes());
			baos.write(NEW_LINE);
			baos.write(EMPHASIZE_OFF);
			baos.write("FECHA Y HORA:  ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write(currentDateTimeString.getBytes());
			baos.write(NEW_LINE);

			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);

			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);
			baos.write(NEW_LINE);
			baos.write("PAGO DE SERVICIO".getBytes());
			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);
			baos.write(NEW_LINE);
			/*s1 = "    SUBCHANGE";
			s2 = "500.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());*/
			int fontSize = 0;
			int fontWidth = 10 * (fontSize + 1) + (fontSize + 1);
			int numOfCharacterPerLine = lineWidth / fontWidth;
			lineaConcepto = "    Importe";
			lineaDato = ProcesoDePago.montoPago;
			cadenaVacia = lineaConcepto;

			for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
				cadenaVacia += " ";
			}
			cadenaVacia += lineaDato;
			baos.write(cadenaVacia.getBytes());
			baos.write(NEW_LINE);
			lineaConcepto = "    Comision";
			lineaDato = ProcesoDePago.comisionPago;
			cadenaVacia = lineaConcepto;
			for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
				cadenaVacia += " ";
			}
			cadenaVacia += lineaDato;
			baos.write(cadenaVacia.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			lineaConcepto = "    Total";
			lineaDato = ProcesoDePago.totalPago;
			cadenaVacia = lineaConcepto;
			for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
				cadenaVacia += " ";
			}
			cadenaVacia += lineaDato;
			baos.write(cadenaVacia.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(ALIGN_CENTER);

			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);
			baos.write("!DESCARGA BANCA MOVIL\nEN TU CELULAR¡".getBytes());
			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);

			baos.write(NEW_LINE);
			Bitmap bitmapII = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr_banca_movil);
			Bitmap scaledBitmapII = Bitmap.createScaledBitmap(bitmapII, targetWidth/4*3, targetWidth/4*3, false);
			byte[] imageCommandII = BBDeviceController.getImageCommand(scaledBitmapII, 150);
			baos.write(imageCommandII, 0, imageCommandII.length);
			baos.write(NEW_LINE);

			baos.write(ALIGN_CENTER);
			baos.write(NEW_LINE);
			baos.write(EMPHASIZE_ON);
			//baos.write("SUBIRLE SUELDO A TONA(repetir tres veces".getBytes());
			baos.write(NEW_LINE);

			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);
			baos.write("En caso de ser necesario el usuario podrá decidir agregar algún tipo de nota informativa para el cliente que recibirá éste comprobante.\n".getBytes());
			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);

			baos.write(FONT_8X12);
			baos.write(EMPHASIZE_OFF);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(POWER_OFF);

			return baos.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

    public static byte[] milagro() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        int lineWidth = 384;
        int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
        String singleLine = "", lineaConcepto="", lineaDato="", cadenaVacia="";
        for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine += "-";
        }
        String doubleLine = "";
        for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            doubleLine += "=";
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(INIT);
            baos.write(POWER_ON);
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);

           /* Bitmap bitmap = BitmapFactory.decodeResource( R.drawable.colibri_ticket);
            int targetWidth = 384;
            int targetHeight = (int)Math.round((double)targetWidth / (double)bitmap.getWidth() * (double)bitmap.getHeight());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
            byte[] imageCommand = BBDeviceController.getImageCommand(scaledBitmap, 150);

            baos.write(imageCommand, 0, imageCommand.length);*/

            baos.write(NEW_LINE);
            baos.write(CHAR_SPACING_0);

            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_ON);
            baos.write(FONT_5X12);
            baos.write("Insurgentes Sur 1458".getBytes());
            baos.write(NEW_LINE);
            baos.write("Col. Actipan Delegación Benito Juárez".getBytes());
            baos.write(NEW_LINE);
            baos.write("CDMX".getBytes());
            baos.write(NEW_LINE);


            baos.write(FONT_SIZE_1);
            baos.write(FONT_5X12);
            baos.write("llegas, pagas y\nYastas".getBytes());
            baos.write(NEW_LINE);

            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_OFF);
            baos.write(FONT_10X18);
            baos.write("Transaccion No. 2524".getBytes());
            baos.write(NEW_LINE);

            baos.write(FONT_8X12);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);

            baos.write(ALIGN_LEFT);

            baos.write(FONT_10X18);
            baos.write("ROR NO ".getBytes());
            baos.write(EMPHASIZE_ON);
            baos.write("ROR2014-000556-000029".getBytes());
            baos.write(NEW_LINE);
            baos.write(EMPHASIZE_OFF);
            baos.write("FECHA Y HORA:  ".getBytes());
            baos.write(EMPHASIZE_ON);
            baos.write(currentDateTimeString.getBytes());
            baos.write(NEW_LINE);

            baos.write(EMPHASIZE_OFF);
            baos.write(FONT_8X12);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);

            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);
            baos.write(NEW_LINE);
            baos.write("PAGO DE SERVICIO".getBytes());
            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);
            baos.write(NEW_LINE);
			/*s1 = "    SUBCHANGE";
			s2 = "500.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());*/
            int fontSize = 0;
            int fontWidth = 10 * (fontSize + 1) + (fontSize + 1);
            int numOfCharacterPerLine = lineWidth / fontWidth;
            lineaConcepto = "    Importe";
            lineaDato = ProcesoDePago.montoPago;
            cadenaVacia = lineaConcepto;

            for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
                cadenaVacia += " ";
            }
            cadenaVacia += lineaDato;
            baos.write(cadenaVacia.getBytes());
            baos.write(NEW_LINE);
            lineaConcepto = "    Comision";
            lineaDato = ProcesoDePago.comisionPago;
            cadenaVacia = lineaConcepto;
            for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
                cadenaVacia += " ";
            }
            cadenaVacia += lineaDato;
            baos.write(cadenaVacia.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            lineaConcepto = "    Total";
            lineaDato = ProcesoDePago.totalPago;
            cadenaVacia = lineaConcepto;
            for(int i = 0; i < numOfCharacterPerLine - lineaConcepto.length() - lineaDato.length(); ++i) {
                cadenaVacia += " ";
            }
            cadenaVacia += lineaDato;
            baos.write(cadenaVacia.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            baos.write(ALIGN_CENTER);

            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);
            baos.write("!DESCARGA BANCA MOVIL\nEN TU CELULAR¡".getBytes());
            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);
/*
            baos.write(NEW_LINE);
            Bitmap bitmapII = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr_banca_movil);
            Bitmap scaledBitmapII = Bitmap.createScaledBitmap(bitmapII, targetWidth/4*3, targetWidth/4*3, false);
            byte[] imageCommandII = BBDeviceController.getImageCommand(scaledBitmapII, 150);
            baos.write(imageCommandII, 0, imageCommandII.length);*/
            baos.write(NEW_LINE);

            baos.write(ALIGN_CENTER);
            baos.write(NEW_LINE);
            baos.write(EMPHASIZE_ON);
            //baos.write("SUBIRLE SUELDO A TONA(repetir tres veces".getBytes());
            baos.write(NEW_LINE);

            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);
            baos.write("En caso de ser necesario el usuario podrá decidir agregar algún tipo de nota informativa para el cliente que recibirá éste comprobante.\n".getBytes());
            baos.write(FONT_10X18);
            baos.write(EMPHASIZE_ON);

            baos.write(FONT_8X12);
            baos.write(EMPHASIZE_OFF);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            baos.write(POWER_OFF);

            return baos.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	
	public static byte[] genReceipt2(Context context) {
		int lineWidth = 384;
		int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
		String singleLine = "";
		for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
			singleLine += "-";
		}
		String doubleLine = "";
		for(int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
			doubleLine += "=";
		}
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(INIT);
			baos.write(POWER_ON);
			baos.write(NEW_LINE);
			baos.write(ALIGN_CENTER);
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.colibri_ticket);
			int targetWidth = 384;
			int targetHeight = (int)Math.round((double)targetWidth / (double)bitmap.getWidth() * (double)bitmap.getHeight());
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
			byte[] imageCommand = BBDeviceController.getImageCommand(scaledBitmap, 150);
			
			baos.write(imageCommand, 0, imageCommand.length);
			
			baos.write(NEW_LINE);
			baos.write(CHAR_SPACING_0);
			
			baos.write(FONT_SIZE_0);
			baos.write(EMPHASIZE_ON);
			baos.write(FONT_5X12);
			baos.write("Suite 1602, 16/F, Tower 2".getBytes());
			baos.write(NEW_LINE);
			baos.write("Nina Tower, No 8 Yeung Uk Road".getBytes());
			baos.write(NEW_LINE);
			baos.write("Tsuen Wan, N.T., Hong Kong".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_1);
			baos.write(KANJI_FONT_16X16);
			baos.write(BBDeviceController.getUnicodeCommand("�������u"));
			baos.write(FONT_5X12);
			baos.write(NEW_LINE);
			
			baos.write(FONT_SIZE_0);
			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_10X18);
			baos.write("Form No. 2524".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			
			baos.write(FONT_10X18);
			baos.write("ROR NO ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("ROR2014-000556-000029".getBytes());
			baos.write(NEW_LINE);
			baos.write(EMPHASIZE_OFF);
			baos.write("DATE/TIME ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("08/20/2014 10:42:46 AM".getBytes());
			baos.write(NEW_LINE);

			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_10X18);
			baos.write(EMPHASIZE_ON);
			baos.write("CHAN TAI MAN".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("BIR FORM NO : ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("0605".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("TYPE : ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("AP".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("PERIOD COVERED : ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("2014-8-20".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("ASSESSMENT NO : ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("885".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("DUE DATE : ".getBytes());
			baos.write(EMPHASIZE_ON);
			baos.write("2014-8-20".getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			int fontSize = 0;
			int fontWidth = 10 * (fontSize + 1) + (fontSize + 1);
			String s1 = "PARTICULARS";
			String s2 = "AMOUNT";
			String s = s1;
			int numOfCharacterPerLine = lineWidth / fontWidth;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			fontSize = 0;
			fontWidth = 10 * (fontSize + 1);
			
			s1 = "BASIC";
			s2 = "100.00";
			s = s1;
			numOfCharacterPerLine = lineWidth / fontWidth;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(EMPHASIZE_OFF);
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "    SUBCHANGE";
			s2 = "500.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "    INTEREST";
			s2 = "0.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "    COMPROMISE";
			s2 = "0.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "TOTAL";
			s2 = "500.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "TOTAL AMOUNT DUE";
			s2 = "600.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(FONT_10X18);
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_8X12);
			baos.write(doubleLine.getBytes());
			baos.write(NEW_LINE);
			
			s1 = "TOTAL AMOUNT PAID";
			s2 = "600.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(FONT_10X18);
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write("SIX HUNDRED DOLLARS ONLY".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(FONT_10X18);
			baos.write("MANNER OF PAYMENT".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write(" ACCOUNTS RECEIVABLE".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("TYPE OF PAYMENT".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write(" FULL".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write("MODE OF PAYMENT".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write("  CASH".getBytes());
			baos.write(NEW_LINE);
			
			s1 = "  AMOUNT";
			s2 = "600.00";
			s = s1;
			for(int i = 0; i < numOfCharacterPerLine - s1.length() - s2.length(); ++i) {
				s += " ";
			}
			s += s2;
			baos.write(EMPHASIZE_OFF);
			baos.write(s.getBytes());
			baos.write(NEW_LINE);
			
			baos.write("REMARKS".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write("TEST".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_OFF);
			baos.write(FONT_8X12);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			
			baos.write(FONT_SIZE_1);
			baos.write(EMPHASIZE_ON);
			baos.write(FONT_8X12);
			baos.write("CARDHOLDER'S COPY".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_LEFT);
			
			baos.write(FONT_SIZE_0);
			baos.write(EMPHASIZE_OFF);
			baos.write(singleLine.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(ALIGN_CENTER);
			baos.write(FONT_5X12);
			baos.write("This is to certify that the amount indicated herein has".getBytes());
			baos.write(NEW_LINE);
			baos.write("been received by the undersigned".getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			
			baos.write(EMPHASIZE_ON);
			baos.write(FONT_10X18);
			baos.write("CHAN SIU MING".getBytes());
			baos.write(NEW_LINE);
			
			String barcode = "B B P O S";
			Hashtable<String, String> barcodeData = new Hashtable<String, String>();
			barcodeData.put("barcodeDataString", barcode);
			barcodeData.put("barcodeHeight", "" + 50);
			barcodeData.put("barcodeType", "128");
			byte[] barcodeCommand = BBDeviceController.getBarcodeCommand(barcodeData);
			baos.write(barcodeCommand);
			
			baos.write(EMPHASIZE_ON);
			baos.write(FONT_10X18);
			baos.write(NEW_LINE);
			baos.write(barcode.getBytes());
			baos.write(NEW_LINE);
			
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);
			baos.write(NEW_LINE);

			baos.write(POWER_OFF);
			
			return baos.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void onWaitingForCard(BBDeviceController.CheckCardMode checkCardMode) {

	}

	@Override
	public void onWaitingReprintOrPrintNext() {

	}

	@Override
	public void onBTReturnScanResults(List<BluetoothDevice> list) {

	}

	@Override
	public void onBTScanTimeout() {

	}

	@Override
	public void onBTScanStopped() {

	}

	@Override
	public void onBTConnected(BluetoothDevice bluetoothDevice) {

	}

	@Override
	public void onBTDisconnected() {

	}

	@Override
	public void onUsbConnected() {

	}

	@Override
	public void onUsbDisconnected() {

	}

	@Override
	public void onSerialConnected() {

	}

	@Override
	public void onSerialDisconnected() {

	}

	@Override
	public void onReturnCheckCardResult(BBDeviceController.CheckCardResult checkCardResult, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnCancelCheckCardResult(boolean b) {

	}

	@Override
	public void onReturnDeviceInfo(Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnTransactionResult(BBDeviceController.TransactionResult transactionResult) {

	}

	@Override
	public void onReturnBatchData(String s) {

	}

	@Override
	public void onReturnReversalData(String s) {

	}

	@Override
	public void onReturnAmountConfirmResult(boolean b) {

	}

	@Override
	public void onReturnPinEntryResult(BBDeviceController.PinEntryResult pinEntryResult, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnPrintResult(BBDeviceController.PrintResult printResult) {

	}

	@Override
	public void onReturnAccountSelectionResult(BBDeviceController.AccountSelectionResult accountSelectionResult, int i) {

	}

	@Override
	public void onReturnAmount(Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnUpdateAIDResult(Hashtable<String, BBDeviceController.TerminalSettingStatus> hashtable) {

	}

	@Override
	public void onReturnUpdateGprsSettingsResult(boolean b, Hashtable<String, BBDeviceController.TerminalSettingStatus> hashtable) {

	}

	@Override
	public void onReturnUpdateTerminalSettingResult(BBDeviceController.TerminalSettingStatus terminalSettingStatus) {

	}

	@Override
	public void onReturnUpdateWiFiSettingsResult(boolean b, Hashtable<String, BBDeviceController.TerminalSettingStatus> hashtable) {

	}

	@Override
	public void onReturnReadAIDResult(Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onReturnReadGprsSettingsResult(boolean b, Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onReturnReadTerminalSettingResult(Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onReturnReadWiFiSettingsResult(boolean b, Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onReturnEnableAccountSelectionResult(boolean b) {

	}

	@Override
	public void onReturnEnableInputAmountResult(boolean b) {

	}

	@Override
	public void onReturnCAPKList(List<CAPK> list) {

	}

	@Override
	public void onReturnCAPKDetail(CAPK capk) {

	}

	@Override
	public void onReturnCAPKLocation(String s) {

	}

	@Override
	public void onReturnUpdateCAPKResult(boolean b) {

	}

	@Override
	public void onReturnRemoveCAPKResult(boolean b) {

	}

	@Override
	public void onReturnEmvReportList(Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnEmvReport(String s) {

	}

	@Override
	public void onReturnDisableAccountSelectionResult(boolean b) {

	}

	@Override
	public void onReturnDisableInputAmountResult(boolean b) {

	}

	@Override
	public void onReturnPhoneNumber(BBDeviceController.PhoneEntryResult phoneEntryResult, String s) {

	}

	@Override
	public void onReturnEmvCardDataResult(boolean b, String s) {

	}

	@Override
	public void onReturnEmvCardNumber(boolean b, String s) {

	}

	@Override
	public void onReturnEncryptPinResult(boolean b, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnEncryptDataResult(boolean b, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnInjectSessionKeyResult(boolean b, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnPowerOnIccResult(boolean b, String s, String s1, int i) {

	}

	@Override
	public void onReturnPowerOffIccResult(boolean b) {

	}

	@Override
	public void onReturnApduResult(boolean b, Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onRequestSelectApplication(ArrayList<String> arrayList) {

	}

	@Override
	public void onRequestSetAmount() {

	}

	@Override
	public void onRequestPinEntry(BBDeviceController.PinEntrySource pinEntrySource) {

	}

	@Override
	public void onRequestOnlineProcess(String s) {

	}

	@Override
	public void onRequestTerminalTime() {

	}

	@Override
	public void onRequestDisplayText(BBDeviceController.DisplayText displayText) {

	}

	@Override
	public void onRequestDisplayAsterisk(int i) {

	}

	@Override
	public void onRequestDisplayLEDIndicator(BBDeviceController.ContactlessStatus contactlessStatus) {

	}

	@Override
	public void onRequestProduceAudioTone(BBDeviceController.ContactlessStatusTone contactlessStatusTone) {

	}

	@Override
	public void onRequestClearDisplay() {

	}

	@Override
	public void onRequestFinalConfirm() {

	}

	@Override
	public void onRequestPrintData(int i, boolean b) {


	}

	@Override
	public void onPrintDataCancelled() {

	}

	@Override
	public void onPrintDataEnd() {

	}

	@Override
	public void onBatteryLow(BBDeviceController.BatteryStatus batteryStatus) {

	}

	@Override
	public void onAudioDevicePlugged() {

	}

	@Override
	public void onAudioDeviceUnplugged() {

	}

	@Override
	public void onError(BBDeviceController.Error error, String s) {

	}

	@Override
	public void onSessionInitialized() {

	}

	@Override
	public void onSessionError(BBDeviceController.SessionError sessionError, String s) {

	}

	@Override
	public void onAudioAutoConfigProgressUpdate(double v) {

	}

	@Override
	public void onAudioAutoConfigCompleted(boolean b, String s) {

	}

	@Override
	public void onAudioAutoConfigError(BBDeviceController.AudioAutoConfigError audioAutoConfigError) {

	}

	@Override
	public void onNoAudioDeviceDetected() {

	}

	@Override
	public void onDeviceHere(boolean b) {

	}

	@Override
	public void onPowerDown() {

	}

	@Override
	public void onPowerButtonPressed() {

	}

	@Override
	public void onDeviceReset() {

	}

	@Override
	public void onEnterStandbyMode() {

	}

	@Override
	public void onReturnNfcDataExchangeResult(boolean b, Hashtable<String, String> hashtable) {

	}

	@Override
	public void onReturnNfcDetectCardResult(BBDeviceController.NfcDetectCardResult nfcDetectCardResult, Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onReturnControlLEDResult(boolean b, String s) {

	}

	@Override
	public void onReturnVasResult(BBDeviceController.VASResult vasResult, Hashtable<String, Object> hashtable) {

	}

	@Override
	public void onRequestStartEmv() {

	}

	@Override
	public void onDeviceDisplayingPrompt() {

	}

	@Override
	public void onRequestKeypadResponse() {

	}

	@Override
	public void onReturnDisplayPromptResult(BBDeviceController.DisplayPromptResult displayPromptResult) {

	}

	@Override
	public void onBarcodeReaderConnected() {

	}

	@Override
	public void onBarcodeReaderDisconnected() {

	}

	@Override
	public void onReturnBarcode(String s) {

	}
}
