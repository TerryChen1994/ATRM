package atrm.parser.gzip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedBufferedInputStream;
import atrm.reader.ContentReader;
import atrm.reader.gzip.ContentReaderGZIP;
import atrm.writer.AnchorTextWriter;

public class ContentParserGZIP {
	protected ContentReaderGZIP contentReaderGzip;
	protected AnchorTextWriter anchorTextWriter;
	protected ByteArrayOutputStream baos;

	public ContentParserGZIP() {
		init();
	}

	public void init() {
		contentReaderGzip = new ContentReaderGZIP();
		anchorTextWriter = new AnchorTextWriter();
		baos = new ByteArrayOutputStream();
	}

	public void extractAnchorText(ExtendedBufferedInputStream ebis, String uri) throws Exception {
		baos = contentReaderGzip.extractAnchorTextProcessed(ebis, uri);
	}

	public void writeAnchorText(String Uri, BufferedOutputStream bos) throws IOException {
		anchorTextWriter.writeAnchorText(Uri, bos, baos);

	}

	public AnchorTagCal calculateAnchorTag(ExtendedBufferedInputStream ebis) throws IOException {
		AnchorTagCal atc = contentReaderGzip.calculateAnchorTag(ebis);
		return atc;
	}
}
