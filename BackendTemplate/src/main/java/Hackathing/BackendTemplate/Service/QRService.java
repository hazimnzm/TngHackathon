package Hackathing.BackendTemplate.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRService {

    public String generateQrBase64FromInventoryId(long inventoryId) {
        String link = generateFetchLink(inventoryId);
        return convertLinkToQrBase64(link);
    }
    
    public String generateFetchLink(long inventoryId) {
        return "/my/items/" + inventoryId;
    }

    public String convertLinkToQrBase64(String link) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    link == null ? "" : link,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException("Failed to generate QR code", ex);
        }
    }
}
