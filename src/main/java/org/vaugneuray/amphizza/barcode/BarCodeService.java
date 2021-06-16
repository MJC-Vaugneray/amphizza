package org.vaugneuray.amphizza.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class BarCodeService {

    public BufferedImage barcodeImage(String text) throws WriterException {
//        EAN13Bean barcodeGenerator = new EAN13Bean();
//        BitmapCanvasProvider canvas =
//                new BitmapCanvasProvider(300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
//
//        barcodeGenerator.generateBarcode(canvas, String.format("%012d", Integer.parseInt(text)));
//        return canvas.getBufferedImage();

        var barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix =
                barcodeWriter.encode(String.format("%012d", Integer.parseInt(text)), BarcodeFormat.EAN_13, 400, 100);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public BufferedImage generateQRCodeImage(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 260, 260);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
