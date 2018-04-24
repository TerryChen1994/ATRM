package atrm.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedBufferedInputStream;
import atrm.entity.FileHeader;
import atrm.entity.Record;
import atrm.parser.ContentParser;
import atrm.parser.FileHeaderParser;
import atrm.parser.RecordParser;

public class ParseWarc {

	private static String warcFileUri = "/Volumes/EC-PHU3/0000wb-00.warc";
	private static String TargetFile = "/Volumes/EC-PHU3/out.txt";

	public static void main(String[] args) throws Exception {
		File outFile = new File(TargetFile);

		try {
			InputStream in = new FileInputStream(warcFileUri);
			PushbackInputStream pbin = new PushbackInputStream(in);

			OutputStream out = new FileOutputStream(outFile);
			BufferedOutputStream bos = new BufferedOutputStream(out, 16384);

			parseFileHeader(pbin);

			processAnchor(pbin, bos);

			in.close();
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
	public static void processAnchor(PushbackInputStream pbin, BufferedOutputStream bos) throws Exception {
		RecordParser recordParser = new RecordParser();
		Record record;

		int records = 0;
		long sT = System.currentTimeMillis();
		ContentParser contentParser;
		label1: while ((record = recordParser.getNextRecord(pbin)) != null) {
			records++;
			// System.out.println(records + " " +
			// record.getRecordHeader().getWarcTrecId());
			System.out.println(records);
			contentParser = new ContentParser();
			contentParser.extractAnchorText(record.getContent(),
					record.getRecordHeader().getWarcTargetUri());
			contentParser.writeAnchorText(record.getRecordHeader().getWarcTargetUri(), bos);
			// if(records > 20)
			// break label1;

		}
		long eT = System.currentTimeMillis();
		long costT = (eT - sT) / 1000;
		System.out.println("--------------");
		System.out.println("Records: " + records);
		System.out.println("Cost Time: " + costT);
	}

	

	// 解析WarcFile的总头文件
	public static void parseFileHeader(PushbackInputStream pbin) throws IOException {
		// WarcFile的Header解析器，抽出WarcFile的Header
		FileHeaderParser fileHeaderParser = new FileHeaderParser();
		FileHeader fileHeader = fileHeaderParser.parseFileHeader(pbin);
		System.out.println("Sum of records: " + fileHeader.getWarcNumberOfDocuments());
		System.out.println("--------------");
	}

	// 计算AnchorTag的数量
	public static void calAnchorTag(PushbackInputStream pbin) throws IOException {
		// WarcRecord的解析器，record包含Record头文件，Http头文件，Record内容
		RecordParser recordParser = new RecordParser();
		Record record;

		int records = 0;
		int startATag = 0;
		int endATag = 0;
		long sT = System.currentTimeMillis();

		ContentParser contentParser;
		label1: while ((record = recordParser.getNextRecord(pbin)) != null) {
			records++;
			// System.out.println(records + " " +
			// record.getRecordHeader().getWarcTrecId());
			System.out.println(records);
			contentParser = new ContentParser();

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
