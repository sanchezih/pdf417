package com.init.barcode.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.init.barcode.entity.Barcode;

@RestController
@RequestMapping("barcode")
public class BarcodeREST {

//	@Autowired
//	private BarcodeDAO BarcodeDAO;

	@RequestMapping(value = "pdf4172", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Barcode> decodedBarcode2(@RequestBody String encodedImage) throws Exception {

		Barcode bc = new Barcode();
		bc.setData(null);

		try {
			String decodedText = decodeQRCode(encodedImage);
			if (decodedText == null) {
				System.out.println("No QR Code found in the image");
			} else {
				System.out.println("Decoded text = " + decodedText);
				bc.setData(decodedText);
			}
		} catch (IOException e) {
			System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
		}

		return ResponseEntity.ok(bc);

	}

	private static String decodeQRCode(String data) throws IOException {

		String base64Image = data.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
		LuminanceSource source = new BufferedImageLuminanceSource(img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		try {
			Result result = new MultiFormatReader().decode(bitmap);
			return result.getText();
		} catch (NotFoundException e) {
			System.out.println("There is no QR code in the image");
			return null;
		}
	}

}
