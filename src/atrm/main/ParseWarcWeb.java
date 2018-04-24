package atrm.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedGZIPInputStream;
import atrm.entity.FileHeader;
import atrm.entity.Record;
import atrm.parser.gzip.ContentParserGZIP;
import atrm.parser.gzip.FileHeaderParserGZIP;
import atrm.parser.gzip.RecordParserGZIP;

public class ParseWarcWeb {

//	private static String warcFileUri = "/Volumes/EC-PHU3/0000wb-00.warc.gz";
	private static String TargetFile = "/users/chentairun/documents/workspace/ATRM/output.txt";
	private static String user = "s1721710";
	private static String host = "ibq0.slis.tsukuba.ac.jp";
	private static String password = "c113128";
	
	public static void main(String[] args) throws Exception {
		File outFile = new File(TargetFile);
		
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, 22);
		session.setPassword(password);
		
		java.util.Properties config = new java.util.Properties(); 
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
	    
		session.connect();
		
		ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
		sftp.connect();
		InputStream in = sftp.get("/work/ClueWeb12_Nodups/Disk1/ClueWeb12_00/0000wb/0000wb-00.warc.gz");

		try {
			ExtendedGZIPInputStream egzis = new ExtendedGZIPInputStream(in);

			OutputStream out = new FileOutputStream(outFile);
			BufferedOutputStream bos = new BufferedOutputStream(out, 16384);

			parseFileHeader(egzis);

			processAnchor(egzis, bos);

			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			sftp.disconnect();
			session.disconnect();
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
			if(records == 41355){
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
