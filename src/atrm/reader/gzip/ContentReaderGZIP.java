package atrm.reader.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedBufferedInputStream;

public class ContentReaderGZIP {

	public ContentReaderGZIP() {
		init();
	}

	public void init() {
	}

	/**
	 * 提取AnchorTag之间的全部内容，进行处理 每读取到一条AnchorTag就重制输出流，将AnchorTag之间未处理的内容写入输出流
	 */
	public ByteArrayOutputStream extractAnchorTextProcessed(ExtendedBufferedInputStream ebis, String uri)
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayOutputStream anchorList = new ByteArrayOutputStream();

		// 循环读取字节
		label1: while (true) {
			int c = ebis.read();
			if (isEnd(c)) {
				break label1;
			}

			// 识别字符 '<'
			if (c == 60) {
				c = ebis.read();
				if (isEnd(c)) {
					break label1;
				}

				// 识别字符 'a' 或 'A'
				if (c == 65 || c == 97) {
					c = ebis.read();
					if (isEnd(c)) {
						break label1;
					}

					// 识别'空格'或'换行符'
					if (c <= 32) {
						ebis.markTag(3);
						ebis.reset();

						// 识别"<a "成功，回撤3个字符，将"<a "写入输出流
						baos = new ByteArrayOutputStream();
						for (int i = 0; i < 3; i++) {
							c = ebis.read();
							baos.write(c);
						}

						// 循环读取字符，写入输出流，直到a标签结束
						label2: while (true) {

							c = ebis.read();
							if (isEnd(c)) {
								break label1;
							}
							if (skipC(c)) {
								continue;
							}

							// 搜到 '<' 标签头
							if (c == 60) {
								c = ebis.read();
								if (isEnd(c)) {
									break label1;
								}
								if (skipC(c)) {
									continue;
								}

								// 搜到 "<a" 可能为新的a标签
								if (c == 65 || c == 97) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										continue;
									}
									// 搜到 "<a "
									// 确定为新的a标签，上一个a标签强行结束，回撤3个字符，写入输出流，并提交
									if (c <= 32) {
										ebis.markTag(3);
										ebis.reset();
										extractAndProcessAnchorText(baos, anchorList, uri);

										break label2;
									}

									// 搜到的不是 "<a " 而是其他如 "<ab..."，跳过这个非a标签，写入输出流
									else {
										baos.write(c);
									}
								}

								// 搜到 '</' 可能为</a>标签结尾
								else if (c == 47) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										continue;
									}
									// 搜到 "</a" 很可能为</a>标签结尾
									if (c == 65 || c == 97) {
										c = ebis.read();
										if (isEnd(c)) {
											break label1;
										}
										if (skipC(c)) {
											continue;
										}

										// 搜到 "</a>"
										// 确定为</a>标签结尾，上一个a标签结束，回撤4个字符，写入输出流，并提交
										if (c == 62) {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
											}
											extractAndProcessAnchorText(baos, anchorList, uri);
											break label2;
										}

										// 搜到的不是 "</a>" 而是其他如
										// "</ab.."，跳过这个非a标签，回撤4个字符，写入输出流
										else {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
											}
										}
									}

									// 搜到的不是 "</a" 而是其他如
									// "</b"，跳过这个非a标签，回撤3个字符，写入输出流
									else {
										ebis.markTag(3);
										ebis.reset();
										for (int i = 0; i < 3; i++) {
											c = ebis.read();
											baos.write(c);
										}
									}
								}

								// 搜到的不是 "</" 与 "<a" 而是其他如
								// "<exp"，跳过这个非a标签，回撤2个字符，写入输出流
								else {
									ebis.markTag(2);
									ebis.reset();
									for (int i = 0; i < 2; i++) {
										c = ebis.read();
										baos.write(c);
									}
								}
							} else {
								baos.write(c);
							}
						}

					}
				}
			}

		}
		return anchorList;
	}

	/**
	 * 处理每一条识别出来的AnchorTag之间的内容，抽取href内容和anchortext内容，交给processAnchorText进行处理
	 */
	public void extractAndProcessAnchorText(ByteArrayOutputStream baos, ByteArrayOutputStream anchorList, String uri)
			throws Exception {

		// baos 为以"<a"开头，并截取的a标签内的全部生内容
		byte[] bytes = baos.toByteArray();
		// 锚文本输出流
		ByteArrayOutputStream anchorText = new ByteArrayOutputStream();
		// 超链接输出流
		ByteArrayOutputStream href = new ByteArrayOutputStream();

		for (int i = 0; i < bytes.length;) {
			byte b1 = bytes[i++];
			
			if(skipC(b1)) continue;
			
			// 识别'<',进入标签
			if (b1 == 60) {

				// 循环读取内容，直到读取到"href"为止
				label2: while (true) {
					byte b2 = bytes[i++];
					if(skipC(b2)) continue;;

					// 读取到'h'
					if (b2 == 72 || b2 == 104) {
						b2 = bytes[i++];

						if(skipC(b2)) continue;;

						// 读取到'r'
						if (b2 == 82 || b2 == 114) {
							b2 = bytes[i++];

							if(skipC(b2)) continue;;

							// 读取到'e'
							if (b2 == 69 || b2 == 101) {
								b2 = bytes[i++];

								if(skipC(b2)) continue;;

								// 读取到'f'
								if (b2 == 70 || b2 == 102) {
									label3: while (true) {
										byte b3 = bytes[i++];

										if(skipC(b2)) continue;;

										if (b3 == 34 || b3 == 39) {
											label4: while (true) {
												byte b4 = bytes[i++];

												if(skipC(b2)) continue;;

												if (b4 == 34 || b4 == 39) {
													break label3;
												}

												if (b4 != 32) {
													href.write(b4);
												}
											}
										}
									}
								}

							}
						}
					}

					// 读取到'>'，表示
					if (b2 == 62) {
						
						break label2;
					}
				}
			}
			
			// 标签外内容为锚文本内容
			else {
				anchorText.write(b1);
			}
		}

		processHrefAndAnchorText(anchorText, href, anchorList, uri);
	}

	// 处理anchortext和href内容，放进anchorList中
	public void processHrefAndAnchorText(ByteArrayOutputStream anchorText, ByteArrayOutputStream href,
			ByteArrayOutputStream anchorList, String uri) throws Exception {
		boolean validHref = false;
		boolean validAnchorText = false;
		ByteArrayOutputStream outHref = new ByteArrayOutputStream();
		ByteArrayOutputStream outAnchorText = new ByteArrayOutputStream();

		/* uri处理模块 */
		String sHref = new String(href.toByteArray());
		sHref = replaceCR(sHref);
		if (sHref.length() > 0) {
			try {
				URL url = new URL(new URL(uri), sHref);
				sHref = url.toString();
				url.getAuthority();
				validHref = true;
				outHref.write(sHref.getBytes());
			} catch (MalformedURLException e) {
				validHref = false;
			}
//			if (validHref) {
//				if (sHref.startsWith("http://") || sHref.startsWith("https://")) {
//					outHref.write(sHref.getBytes());
//					validHref = true;
//				} else {
//					validHref = false;
//				}
//			}

		} else {
			validHref = false;
		}

		if (validHref = true) {
			/* 锚文本处理模块 */
			String sAnchorText = new String(anchorText.toByteArray());
			sAnchorText = sAnchorText.replaceAll("&nbsp;", " ");
			byte bAnchorText[] = sAnchorText.getBytes();

			if (bAnchorText.length > 0) {
				int current = 0;
				byte b = bAnchorText[current];
				if (isUsefulChar(b)) {
					boolean hasPre = false;
					for (; current < bAnchorText.length; current++) {
						b = bAnchorText[current];
						if (isUsefulChar(b)) {
							if (hasPre) {
								outAnchorText.write(32);
								outAnchorText.write(bAnchorText[current]);
								hasPre = false;
							} else
								outAnchorText.write(bAnchorText[current]);
						} else {
							hasPre = true;
						}
					}
					validAnchorText = true;
				} else {
					while (!isUsefulChar(b)) {
						if (current < bAnchorText.length - 1)
							current++;
						else {
							validAnchorText = false;
							break;
						}
						b = bAnchorText[current];
					}
					if (isUsefulChar(b)) {
						boolean hasPre = false;
						for (; current < bAnchorText.length; current++) {
							b = bAnchorText[current];
							if (isUsefulChar(b)) {
								if (hasPre) {
									outAnchorText.write(32);
									outAnchorText.write(bAnchorText[current]);
									hasPre = false;
								} else
									outAnchorText.write(bAnchorText[current]);
							} else {
								hasPre = true;
							}
						}
						validAnchorText = true;
					}
				}
			} else {
				validAnchorText = false;
			}
		}
		if (validHref && validAnchorText) {
			anchorList.write(outHref.toByteArray());
			anchorList.write(9);
			anchorList.write(outAnchorText.toByteArray());
			anchorList.write(13);
		}

	}

	public String replaceCR(String s) {
		// s = s.replaceAll("&nbsp;", " ");
		s = s.replaceAll("&amp;", "&");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&gt;", ">");
		s = s.replaceAll("&quot;", "\"");
		return s;
	}

	public boolean isUsefulChar(byte b) {
		if (b >= 48 && b <= 57)
			return true;
		if (b >= 65 && b <= 90)
			return true;
		if (b >= 97 && b <= 122)
			return true;
		return false;
	}

	public String parseDomain(String uri) throws Exception {
		String domain;
		if (uri.startsWith("http://") || uri.startsWith("https://")) {
			domain = uri.substring(0, uri.indexOf("/", 8));
		} else {
			System.out.println(uri);
			throw new Exception("URI format error");
		}
		return domain;
	}

	// 计算AnchorTag的数量
	public AnchorTagCal calculateAnchorTag(ExtendedBufferedInputStream ebis) throws IOException {
		AnchorTagCal atc = new AnchorTagCal();
		int startATag = 0;
		int endATag = 0;

		label1: while (true) {
			int c = ebis.read();
			if (c == -1) {
				break label1;
			}

			if (c == 60) {
				c = ebis.read();
				if (isEnd(c)) {
					break label1;
				}
				if (c == 65 || c == 97) {
					c = ebis.read();
					if (c <= 32) {
						startATag++;
					}
				} else if (c == 47) {
					c = ebis.read();
					if (isEnd(c)) {
						break label1;
					}
					if (c == 65 || c == 97) {
						c = ebis.read();
						if (isEnd(c)) {
							break label1;
						}
						if (c == 62) {
							endATag++;
						}
					}
				}
			}

		}
		atc.setStartATag(startATag);
		atc.setEndATag(endATag);
		return atc;
	}

	public boolean isEnd(int c) {
		if (c == -1)
			return true;
		return false;
	}

	public boolean skipC(int c) {
		if (c == 13 || c == 10)
			return true;
		return false;
	}

}
