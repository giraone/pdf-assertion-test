package com.giraone.pdf;

import com.pdfunit.AssertThat;
import com.pdfunit.FormatUnit;
import com.pdfunit.filter.page.PagesFromTo;
import com.pdfunit.filter.region.PageRegion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

import static com.pdfunit.Constants.FIRST_PAGE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PdfAssertionsApplication.class)
public class PdfUnitTest {

    @BeforeAll
    public static void setupHeadlessMode() {
        System.setProperty("java.awt.headless", "false");
    }

    @Test
    void test_with_generated_pdf() {

        File pdf = new File("src/test/resources/pdfs/events-tiny-2-pages.pdf");
        // 15 mm = 2.54 * 0.60 ==> 10% Delta = 13mm
        // 5 mm Höhe mit Toleranz 10 mm
        PageRegion regionWithoutHeaderAndFooter = new PageRegion(13, 13, 195, 10, FormatUnit.MILLIMETERS);

        AssertThat.document(pdf)
            .restrictedTo(FIRST_PAGE)
            .restrictedTo(regionWithoutHeaderAndFooter)
            .hasText()
            .startingWith("Ereignisse")
            .containing("Mustermann-Wagner Annegret")
            .containing("08.12.1978");
    }

    @Test
    void test_with_large_text() {

        File pdf = new File("src/test/resources/pdfs/two-pages-text-only.pdf");

        Assertions.assertAll(
            () -> AssertThat.document(pdf)
                .restrictedTo(FIRST_PAGE)
                .restrictedTo(new PageRegion(23, 27, 187, 10)) // 25 mm = 0.98 inch * 2.54; 27 mm = 1.08 inch * 2,54
                .hasText()
                .containing("Titel"),
            () -> AssertThat.document(pdf)
                .restrictedTo(FIRST_PAGE)
                .restrictedTo(new PageRegion(23, 42, 187, 6)) // 25 mm = 0.98 inch * 2.54; 2742 mm = 1.66 inch * 2,54
                .hasText()
                .containing("Header"),
            () -> AssertThat.document(pdf)
                .restrictedTo(FIRST_PAGE)
                .restrictedTo(new PageRegion(23, 206, 187, 8))
                .hasText()
                .containing("tobefound"),
            () -> AssertThat.document(pdf)
                .restrictedTo(new PagesFromTo(2))
                .restrictedTo(new PageRegion(23, 175, 187, 8))
                .hasText()
                .containing("ToBeFound")
        );
    }
}
