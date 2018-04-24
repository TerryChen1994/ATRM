package atrm.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedGZIPInputStream;
import atrm.entity.FileHeader;
import atrm.entity.Record;
import atrm.parser.gzip.ContentParserGZIP;
import atrm.parser.gzip.FileHeaderParserGZIP;
import atrm.parser.gzip.RecordParserGZIP;
import atrm.parser.gzip.TestParserGZIP;

public class Test {

	private static String warcFileUri = "/users/chentairun/documents/workspace/ATRM/0000wb-00.warc.gz";
	private static String TargetFile = "/users/chentairun/documents/workspace/ATRM/TestOut.txt";

	public static void main(String[] args) throws Exception {
		File outFile = new File(TargetFile);

		try {
			ExtendedGZIPInputStream egzis = new ExtendedGZIPInputStream(new FileInputStream(warcFileUri));

			OutputStream out = new FileOutputStream(outFile);
			BufferedOutputStream bos = new BufferedOutputStream(out, 16384);

			parseFileHeader(egzis);

			processAnchor(egzis, bos);

			egzis.close();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 提取AnchorTag的静态方法
	public static void processAnchor(ExtendedGZIPInputStream gzis, BufferedOutputStream bos) throws Exception {
		RecordParserGZIP recordParser = new RecordParserGZIP();
		Record record;

		int records = 0;
		long sT = System.currentTimeMillis();
		TestParserGZIP testParser;
		label1: while ((record = recordParser.getNextRecord(gzis)) != null) {
			records++;
			System.out.println(records);
			testParser = new TestParserGZIP();
			
			testParser.extractAnchorText(record.getContent(),
					record.getRecordHeader().getWarcTargetUri());
			testParser.writeAnchorText(record.getRecordHeader().getWarcTargetUri(), bos);

		}
		long eT = System.currentTimeMillis();
		long costT = (eT - sT) / 1000;
		System.out.println("--------------");
		System.out.println("Records: " + records);
		System.out.println("Cost Time: " + costT);
	}

	

	// 解析WarcFile的总头文件
	public static void parseFileHeader(ExtendedGZIPInputStream gzis) throws IOException {
		// WarcFile的Header解析器，抽出WarcFile的Header
		FileHeaderParserGZIP fileHeaderParser = new FileHeaderParserGZIP();
		FileHeader fileHeader = fileHeaderParser.parseFileHeader(gzis);
		System.out.println("Sum of records: " + fileHeader.getWarcNumberOfDocuments());
		System.out.println("--------------");
	}

	
}
