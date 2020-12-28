package com.giraone.pdf;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfTextLocatorTest {

    @BeforeEach
    void init() {

        // Switch off font logging.
        Logger log = (Logger) LoggerFactory.getLogger("org.apache.fontbox");
        log.setLevel(Level.INFO);
    }

    @Test
    void assertThat_retrieveLocations_worksBasically() throws IOException {

        // arrange
        byte[] pdfBytes = FileUtil.readBytesFromResource("/pdfs/two-pages-text-only.pdf");
        assertThat(pdfBytes).isNotNull();

        // act
        PointAdaption pointAdaption = new PointAdaption(0.0f,0.0f);
        ScaleAdaption scaleAdaption = new ScaleAdaption(1.0f,1.0f);
        List<TextLocation> result;
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PdfTextLocator pdfTextLocator = new PdfTextLocator(document, pointAdaption, scaleAdaption);
            result = pdfTextLocator.retrieveLocations();
        }

        for (int i = 0; i < result.size(); i++) {
            TextLocation textLocation = result.get(i);
            System.out.printf("%3d %s %s\n", i, textLocation.printPos(), textLocation.text);
        }

        // assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(80);

        Percentage allowedDelta = Percentage.withPercentage(10);

        TextLocation title = result.get(0);
        assertThat(title.text).contains("Titel One Two Three");
        assertThat(title.page).isEqualTo(1);
        assertThat(title.x).isCloseTo(25.0f, allowedDelta);
        assertThat(title.y).isCloseTo(35.0f, allowedDelta);
        assertThat(title.width).isCloseTo(80.0f, allowedDelta);
        assertThat(title.height).isCloseTo(5.0f, allowedDelta);  // large font

        TextLocation header = result.get(1);
        assertThat(header.text).contains("Header Four Five Six");
        assertThat(header.page).isEqualTo(1);
        assertThat(header.x).isCloseTo(25.0f, allowedDelta);
        assertThat(header.y).isCloseTo(45.0f, allowedDelta);
        assertThat(header.width).isCloseTo(47.0f, allowedDelta);
        assertThat(header.height).isCloseTo(3.0f, allowedDelta);

        TextLocation textToBeFound1 = result.get(31);
        assertThat(textToBeFound1.text).contains("tobefound");
        assertThat(textToBeFound1.page).isEqualTo(1);
        assertThat(textToBeFound1.x).isCloseTo(25.0f, allowedDelta);
        assertThat(textToBeFound1.y).isCloseTo(210.0f, allowedDelta);
        assertThat(textToBeFound1.width).isCloseTo(150.0f, allowedDelta);
        assertThat(header.height).isCloseTo(3.0f, allowedDelta);

        TextLocation textToBeFound2 = result.get(67);
        assertThat(textToBeFound2.text).contains("ToBeFound");
        assertThat(textToBeFound2.page).isEqualTo(2);
        assertThat(textToBeFound2.x).isCloseTo(25.0f, allowedDelta);
        assertThat(textToBeFound2.y).isCloseTo(175.0f, allowedDelta);
        assertThat(textToBeFound2.width).isCloseTo(150.0f, allowedDelta);
        assertThat(header.height).isCloseTo(3.0f, allowedDelta);
    }
}