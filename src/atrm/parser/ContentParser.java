package atrm.parser;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedBufferedInputStream;
import atrm.reader.ContentReader;
import atrm.writer.AnchorTextWriter;

public class ContentParser {
	protected ContentReader contentReader;
	protected AnchorTextWriter anchorTextWriter;
	protected ByteArrayOutputStream baos;

	public ContentParser() {
		init();
	}

	public void init() {
		contentReader = new ContentReader();
		anchorTextWriter = new AnchorTextWriter();
		baos = new ByteArrayOutputStream();
	}

	public void extractAnchorText(ExtendedBufferedInputStream ebis, String uri) throws Exception {
		baos = contentReader.extractAnchorTextProcessed(ebis, uri);
		// baos = contentReader.extractAnchorUnprocessed(ebis);
		// System.out.println(baos.toString());

	}

	public void writeAnchorText(String Uri, BufferedOutputStream bos) throws IOException {

		anchorTextWriter.writeAnchorText(Uri, bos, baos);

	}

	public AnchorTagCal calculateAnchorTag(ExtendedBufferedInputStream ebis) throws IOException {
		AnchorTagCal atc = contentReader.calculateAnchorTag(ebis);
		return atc;
	}
}
