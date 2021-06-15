package org.vaugneuray.amphizza;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vaugneuray.amphizza.barcode.BarCodeService;
import org.vaugneuray.amphizza.image.GraphicService;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class BarCodeController {

    private final GraphicService graphicService;
    private final BarCodeService barCodeService;
    private final OrderRepository orderRepository;

    public BarCodeController(GraphicService graphicService, BarCodeService barCodeService, OrderRepository orderRepository) {
        this.graphicService = graphicService;
        this.barCodeService = barCodeService;
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "/barcode", method = GET, produces = "image/png")
    public BufferedImage getBarCodeForTest(@RequestParam Long id) throws IOException {
        final var order = orderRepository.findById(id).orElseThrow();
        return graphicService.mergeImages(
                graphicService.getLogoImage(),
                barCodeService.barcodeImage(id.toString()),
                graphicService.textToBufferedImage(order.getPizzaType().toString()),
                graphicService.textToBufferedImage("N° de commande: " + order.getId()),
                graphicService.textToBufferedImage("Commandé à " + order.getCreatedAt().plusHours(2).format(DateTimeFormatter.ISO_TIME))
        );
    }
}
