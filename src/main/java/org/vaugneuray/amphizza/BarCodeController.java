package org.vaugneuray.amphizza;

import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class BarCodeController {

    @RequestMapping(value = "/barcode", method = GET)
    public BufferedImage getBarCodeForTest(@RequestParam String text) throws IOException {
        EAN13Bean barcodeGenerator = new EAN13Bean();
        BitmapCanvasProvider canvas =
                new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        barcodeGenerator.generateBarcode(canvas, String.format("%012d", Integer.parseInt(text)));


        return canvas.getBufferedImage();
    }
}
