package atrm.parser.gzip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import atrm.common.ExtendedBufferedInputStream;
import atrm.reader.gzip.TestReaderGZIP;
import atrm.writer.TestWriter;

public class TestParserGZIP {
	protected TestReaderGZIP testReaderGzip;
	protected TestWriter testWriter;
	protected ByteArrayOutputStream baos;

	public TestParserGZIP() {
		init();
	}

	public void init() {
		testReaderGzip = new TestReaderGZIP();
		testWriter = new TestWriter();
		baos = new ByteArrayOutputStream();
	}

	public void extractAnchorText(ExtendedBufferedInputStream ebis, String uri) throws Exception {
		baos = testReaderGzip.extractAnchorTextProcessed(ebis, uri);

	}

	public void writeAnchorText(String Uri, BufferedOutputStream bos) throws IOException {

		testWriter.writeAnchorText(Uri, bos, baos);

	}

}
