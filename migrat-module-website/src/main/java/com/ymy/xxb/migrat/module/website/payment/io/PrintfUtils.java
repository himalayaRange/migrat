package com.ymy.xxb.migrat.module.website.payment.io;

import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ymy.xxb.migrat.module.website.payment.ext.ZxingKit;

public class PrintfUtils {
  
	public static void writeQrCodeToFile(HttpServletResponse response , String qrCodeUrl) {
		ZxingKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,	"D://pay.png");
	}
	
	public static void WriteQrCodeToOutPutSteam(OutputStream outputStream , String contents) {
		ZxingKit.encodeOutPutSteam(outputStream, contents, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200);
	}
	
}
