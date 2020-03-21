package com.jiaxintec.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name:  QrCodeUtils
 * Author:      Jacky Zhang
 * Create Time: 2020-03-18 下午3:04
 * Description:
 */
@Slf4j
public class QrCodeUtils
{
    /**
     * 生成web版本二维码
     *
     * @param content  要生成二维码的路径
     * @param response response对象
     * @param width    二维码宽度
     * @param height   二维码高度
     * @throws IOException
     */
    public static void getTwoDimension(String content, HttpServletResponse response, int width, int height) throws IOException {
        log.error("qrcode : " + content);
        if (!StringUtils.isEmpty(content)) {
            ServletOutputStream stream = null;
            try {
                stream = response.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();

                Map<EncodeHintType, Object> props = new HashMap<>();
                props.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                props.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                props.put(EncodeHintType.MARGIN, 0);

                BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, height, width, props);
                BufferedImage img = MatrixToImageWriter.toBufferedImage(m);
                BufferedImage logo = ImageIO.read(QrCodeUtils.class.getClassLoader().getResourceAsStream("logo.png"));
                int w = img.getWidth();
                int h = img.getHeight();

                BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
                out.getGraphics().drawImage(img, 0, 0, w, h, null);
                out.getGraphics().drawImage(logo, w * 2/ 5, h*2/5, w*2/10, h*2/10, null);
                out.getGraphics().dispose();
                logo.flush();
                img.flush();
                out.flush();
                ImageIO.write(out, "png", stream);
            } catch (WriterException e) {
                log.error("image err :{}", e);
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }

}
