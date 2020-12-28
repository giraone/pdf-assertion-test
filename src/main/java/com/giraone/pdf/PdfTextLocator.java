package com.giraone.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class PdfTextLocator extends PDFTextStripper {

    /** user space units per inch */
    private static final float POINTS_PER_INCH = 72;

    /** user space units per millimeter */
    private static final float MM_PER_POINT = 25.4f / POINTS_PER_INCH; // ~ 0.3527777

    private final List<TextLocation> locations;
    private final PointAdaption pointAdaption;
    private final ScaleAdaption scaleAdaption;

    public PdfTextLocator(PDDocument document, PointAdaption pointAdaption, ScaleAdaption scaleAdaption) throws IOException {
        super.setSortByPosition(true);
        this.document = document;
        this.pointAdaption = pointAdaption;
        this.scaleAdaption = scaleAdaption;
        this.locations = new ArrayList<>();
        // Dummy NOOP writer is needed
        this.output = getDummyOutputWriter();
    }

    public List<TextLocation> retrieveLocations() throws IOException {

        processPages(document.getDocumentCatalog().getPages());
        return locations;
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {

        super.writeString(text);

        // textPositions are all the single characters and their data, position, ...
        // we use only the first and last to identify a "location"
        TextPosition posFirst = textPositions.get(0);
        TextPosition posLast = textPositions.get(textPositions.size() - 1);

        final TextLocation location = new TextLocation(
            text.trim(), getCurrentPageNo(),
            pointAdaption.x + posFirst.getXDirAdj() * MM_PER_POINT * scaleAdaption.xScale,
            pointAdaption.y + posFirst.getYDirAdj() * MM_PER_POINT * scaleAdaption.yScale,
            (posLast.getXDirAdj() + posLast.getWidthDirAdj() - posFirst.getXDirAdj()) * MM_PER_POINT * scaleAdaption.xScale,
            // we document only the height of the first character!
            posFirst.getHeight() * MM_PER_POINT * scaleAdaption.yScale
        );
        locations.add(location);
    }

    //------------------------------------------------------------------------------------------------------------------

    private Writer getDummyOutputWriter() {
        return new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) {
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
    }
}
