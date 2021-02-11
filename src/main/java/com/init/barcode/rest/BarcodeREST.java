package com.init.barcode.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.init.barcode.entity.DecodedPDF417;

@RestController
@RequestMapping("barcode")
public class BarcodeREST {

//	@Autowired
//	private BarcodeDAO BarcodeDAO;

	@RequestMapping(value = "pdf417", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DecodedPDF417> decodedBarcode(@RequestBody String encodedImage) throws Exception {

		DecodedPDF417 bc = new DecodedPDF417();

		try {
			String decodedText = decodeCode(encodedImage);
			if (decodedText == null) {
				System.out.println("No PDF417 Code found in the image");
				return ResponseEntity.badRequest().build();
			} else {
				System.out.println("Decoded text = " + decodedText);
				bc.setData(decodedText);
			}
		} catch (IOException e) {
			System.out.println("Could not decode PDF417 Code, IOException :: " + e.getMessage());
		}
		return ResponseEntity.ok(bc);
	}

	private static String decodeCode(String data) throws IOException {

		String base64Image = data.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
		LuminanceSource source = new BufferedImageLuminanceSource(img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		try {
			Result result = new MultiFormatReader().decode(bitmap);
			return result.getText();
		} catch (NotFoundException e) {
			System.out.println("There is no PDF417 code in the image");
			return null;
		}
	}

}
