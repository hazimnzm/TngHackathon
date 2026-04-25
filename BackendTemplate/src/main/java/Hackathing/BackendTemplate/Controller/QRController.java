package Hackathing.BackendTemplate.Controller;

import Hackathing.BackendTemplate.Service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/qr")
public class QRController {

    @Autowired
    private QRService qrService;

    @GetMapping("/{inventoryId}")
    public Map<String, Object> generateInventoryQr(@PathVariable long inventoryId) {
        String link = qrService.generateFetchLink(inventoryId);
        String qrBase64 = qrService.convertLinkToQrBase64(link);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("inventoryId", inventoryId);
        response.put("link", link);
        response.put("qrBase64", qrBase64);
        return response;
    }
}
