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

public class ParseWarcCompressed {

	private static String warcFileUri = "/users/chentairun/documents/workspace/MRTest/0000wb-00.warc.gz";
	private static String TargetFile = "/users/chentairun/documents/workspace/MRTest/GZIPout.txt";

	public static void main(String[] args) throws Exception {
		File outFile = new File(TargetFile);

		try {
			ExtendedGZIPInputStream egzis = new ExtendedGZIPInputStream(new FileInputStream(warcFileUri));

			FileOutputStream out = new FileOutputStream(outFile);
			BufferedOutputStream bos = new BufferedOutputStream(out);

			parseFileHeader(egzis);

			processAnchor(egzis, bos);

			egzis.close();
			bos.close();
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
		ContentParserGZIP contentParser;
		label1: while ((record = recordParser.getNextRecord(gzis)) != null) {
			records++;
			System.out.println(records);
			contentParser = new ContentParserGZIP();
			
			contentParser.extractAnchorText(record.getContent(),
					record.getRecordHeader().getWarcTargetUri());
			contentParser.writeAnchorText(record.getRecordHeader().getWarcTargetUri(), bos);
			if(records == 1){
				break label1;
			}

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

	// 计算AnchorTag的数量
	public static void calAnchorTag(ExtendedGZIPInputStream gzis) throws IOException {
		// WarcRecord的解析器，record包含Record头文件，Http头文件，Record内容
		RecordParserGZIP recordParser = new RecordParserGZIP();
		Record record;

		int records = 0;
		int startATag = 0;
		int endATag = 0;
		long sT = System.currentTimeMillis();

		ContentParserGZIP contentParser;
		label1: while ((record = recordParser.getNextRecord(gzis)) != null) {
			records++;
			// System.out.println(records + " " +
			// record.getRecordHeader().getWarcTrecId());
			System.out.println(records);
			contentParser = new ContentParserGZIP();

			AnchorTagCal atc = contentParser.calculateAnchorTag(record.getContent());
			startATag += atc.getStartATag();
			endATag += atc.getEndATag();

			if (records == 1) {
				break label1;
			}

		}

		long eT = System.currentTimeMillis();
		long costT = (eT - sT) / 1000;
		System.out.println("--------------");
		System.out.println("Records: " + records);
		System.out.println("startATag: " + startATag);
		System.out.println("endATag: " + endATag);
		System.out.println("Cost Time: " + costT);
	}
}
