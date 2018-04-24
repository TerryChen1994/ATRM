package atrm.reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import atrm.common.AnchorTagCal;
import atrm.common.ExtendedBufferedInputStream;
import atrm.entity.AnchorText;

public class ContentReader {

	public ContentReader() {
		init();
	}

	public void init() {
	}

	// 提取AnchorTag之间的全部内容,不进行处理
	// 被extractAnchorTextProcessed取代
	public ByteArrayOutputStream extractAnchorTextUnprocessed(ExtendedBufferedInputStream ebis) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 为了输出未能成功配对的AnchorTag的内容，用来计算该内容的长度的下标
		int indexOfErrTag = 0;

		//
		label1: while (true) {
			int c = ebis.read();
			if (isEnd(c)) {
				break label1;
			}
			if (c == 60) {
				c = ebis.read();
				if (isEnd(c)) {
					break label1;
				}
				if (c == 65 || c == 97) {
					c = ebis.read();
					if (isEnd(c)) {
						break label1;
					}
					if (c <= 32) {
						ebis.markTag(3);
						ebis.reset();

						//
						indexOfErrTag = 0;
						for (int i = 0; i < 3; i++) {
							c = ebis.read();
							baos.write(c);
							indexOfErrTag++;
						}
						label2: while (true) {

							c = ebis.read();
							if (isEnd(c)) {
								break label1;
							}
							if (skipC(c)) {
								indexOfErrTag++;
								continue;
							}
							if (c == 60) {
								c = ebis.read();
								if (isEnd(c)) {
									break label1;
								}
								if (skipC(c)) {
									indexOfErrTag++;
									continue;
								}
								if (c == 65 || c == 97) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										indexOfErrTag++;
										continue;
									}
									if (c <= 32) {
										ebis.markTag(3);
										ebis.reset();
										baos.write(13);

										// 显示未能成功配对的AnchorTag之间的内容
										// ByteArrayOutputStream baos2 = new
										// ByteArrayOutputStream();
										// ebis.markTag(indexOfErrTag);
										// ebis.reset();
										// for (int i = 0; i < indexOfErrTag +
										// 3; i++) {
										// c = ebis.read();
										// baos2.write(c);
										// }
										// System.out.println(baos2.toString());

										break label2;
									} else {
										baos.write(c);
										indexOfErrTag++;
									}
								} else if (c == 47) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										continue;
									}
									if (c == 65 || c == 97) {
										c = ebis.read();
										if (isEnd(c)) {
											break label1;
										}
										if (skipC(c)) {
											indexOfErrTag++;
											continue;
										}
										if (c == 62) {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
											}
											baos.write(13);
											break label2;
										} else {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
												indexOfErrTag++;
											}
										}
									} else {
										ebis.markTag(3);
										ebis.reset();
										for (int i = 0; i < 3; i++) {
											c = ebis.read();
											baos.write(c);
											indexOfErrTag++;
										}
									}
								} else {
									ebis.markTag(2);
									ebis.reset();
									for (int i = 0; i < 2; i++) {
										c = ebis.read();
										baos.write(c);
										indexOfErrTag++;
									}
								}
							} else {
								baos.write(c);
								indexOfErrTag++;
							}
						}

					}
				}
			}

		}
		return baos;
	}

	// 提取AnchorTag之间的全部内容，进行内容
	// 每读取到一条AnchorTag就重制输出流，将AnchorTag之间未处理的内容写入输出流
	public ByteArrayOutputStream extractAnchorTextProcessed(ExtendedBufferedInputStream ebis, String uri)
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayOutputStream anchorList = new ByteArrayOutputStream();

		// 为了输出未能成功配对的AnchorTag的内容，用来计算该内容的长度的下标
		int indexOfErrTag = 0;

		//
		label1: while (true) {
			int c = ebis.read();
			if (isEnd(c)) {
				break label1;
			}
			if (c == 60) {
				c = ebis.read();
				if (isEnd(c)) {
					break label1;
				}
				if (c == 65 || c == 97) {
					c = ebis.read();
					if (isEnd(c)) {
						break label1;
					}
					if (c <= 32) {
						ebis.markTag(3);
						ebis.reset();

						//
						baos = new ByteArrayOutputStream();
						indexOfErrTag = 0;
						for (int i = 0; i < 3; i++) {
							c = ebis.read();
							baos.write(c);
							indexOfErrTag++;
						}
						label2: while (true) {

							c = ebis.read();
							if (isEnd(c)) {
								break label1;
							}
							if (skipC(c)) {
								indexOfErrTag++;
								continue;
							}
							if (c == 60) {
								c = ebis.read();
								if (isEnd(c)) {
									break label1;
								}
								if (skipC(c)) {
									indexOfErrTag++;
									continue;
								}
								if (c == 65 || c == 97) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										indexOfErrTag++;
										continue;
									}
									if (c <= 32) {
										ebis.markTag(3);
										ebis.reset();
										baos.write(13);
										// extractAnchorText(baos, anchorList);
										extractAndProcessAnchorText(baos, anchorList, uri);

										// 显示未能成功配对的AnchorTag之间的内容
										// ByteArrayOutputStream baos2 = new
										// ByteArrayOutputStream();
										// ebis.markTag(indexOfErrTag);
										// ebis.reset();
										// for (int i = 0; i < indexOfErrTag +
										// 3; i++) {
										// c = ebis.read();
										// baos2.write(c);
										// }
										// System.out.println(baos2.toString());

										break label2;
									} else {
										baos.write(c);
										indexOfErrTag++;
									}
								} else if (c == 47) {
									c = ebis.read();
									if (isEnd(c)) {
										break label1;
									}
									if (skipC(c)) {
										continue;
									}
									if (c == 65 || c == 97) {
										c = ebis.read();
										if (isEnd(c)) {
											break label1;
										}
										if (skipC(c)) {
											indexOfErrTag++;
											continue;
										}
										if (c == 62) {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
											}
											baos.write(13);
											// extractAnchorText(baos,
											// anchorList);
											extractAndProcessAnchorText(baos, anchorList, uri);
											break label2;
										} else {
											ebis.markTag(4);
											ebis.reset();
											for (int i = 0; i < 4; i++) {
												c = ebis.read();
												baos.write(c);
												indexOfErrTag++;
											}
										}
									} else {
										ebis.markTag(3);
										ebis.reset();
										for (int i = 0; i < 3; i++) {
											c = ebis.read();
											baos.write(c);
											indexOfErrTag++;
										}
									}
								} else {
									ebis.markTag(2);
									ebis.reset();
									for (int i = 0; i < 2; i++) {
										c = ebis.read();
										baos.write(c);
										indexOfErrTag++;
									}
								}
							} else {
								baos.write(c);
								indexOfErrTag++;
							}
						}

					}
				}
			}

		}
		return anchorList;
	}

	// 处理每一条识别出来的AnchorTag之间的内容，抽取href内容和anchortext内容，'不'经过处理放入anchorList中
	// 被extractAndProcessAnchorText取代
	public void extractAnchorText(ByteArrayOutputStream baos, ByteArrayOutputStream anchorList) {
		byte[] bytes = baos.toByteArray();

		int index = 0;
		label1: while (true) {
			byte b1 = bytes[index++];

			if (b1 == 13) {
				anchorList.write(13);
				break label1;
			}

			if (b1 == 60) {
				label2: while (true) {
					byte b2 = bytes[index++];

					if (b2 == 13) {
						anchorList.write(13);
						break label1;
					}

					if (b2 == 72 || b2 == 104) {
						b2 = bytes[index++];

						if (b2 == 13) {
							anchorList.write(13);
							break label1;
						}

						if (b2 == 82 || b2 == 114) {
							b2 = bytes[index++];

							if (b2 == 13) {
								anchorList.write(13);
								break label1;
							}

							if (b2 == 69 || b2 == 101) {
								b2 = bytes[index++];

								if (b2 == 13) {
									anchorList.write(13);
									break label1;
								}

								if (b2 == 70 || b2 == 102) {
									label3: while (true) {
										byte b3 = bytes[index++];

										if (b3 == 13) {
											anchorList.write(13);
											break label1;
										}

										if (b3 == 34 || b3 == 39) {
											label4: while (true) {
												byte b4 = bytes[index++];

												if (b4 == 13) {
													anchorList.write(13);
													break label1;
												}

												if (b4 == 34 || b4 == 39) {
													anchorList.write(9);
													break label3;
												}
												anchorList.write(b4);
											}
										}
									}
								}

							}
						}
					}

					if (b2 == 62) {
						break label2;
					}
				}
			} else {
				anchorList.write(b1);
			}
		}
	}

	// 处理每一条识别出来的AnchorTag之间的内容，抽取href内容和anchortext内容，交给processAnchorText进行处理
	public void extractAndProcessAnchorText(ByteArrayOutputStream baos, ByteArrayOutputStream anchorList, String uri)
			throws Exception {
		byte[] bytes = baos.toByteArray();
		ByteArrayOutputStream anchorText = new ByteArrayOutputStream();
		ByteArrayOutputStream href = new ByteArrayOutputStream();

		int index = 0;
		label1: while (true) {
			byte b1 = bytes[index++];

			if (b1 == 13) {
				break label1;
			}

			if (b1 == 60) {
				label2: while (true) {
					byte b2 = bytes[index++];

					if (b2 == 13) {
						break label1;
					}

					if (b2 == 72 || b2 == 104) {
						b2 = bytes[index++];

						if (b2 == 13) {
							break label1;
						}

						if (b2 == 82 || b2 == 114) {
							b2 = bytes[index++];

							if (b2 == 13) {
								break label1;
							}

							if (b2 == 69 || b2 == 101) {
								b2 = bytes[index++];

								if (b2 == 13) {
									break label1;
								}

								if (b2 == 70 || b2 == 102) {
									label3: while (true) {
										byte b3 = bytes[index++];

										if (b3 == 13) {
											break label1;
										}

										if (b3 == 34 || b3 == 39) {
											label4: while (true) {
												byte b4 = bytes[index++];

												if (b4 == 13) {
													break label1;
												}

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

					if (b2 == 62) {
						break label2;
					}
				}
			} else {
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
				validHref = true;
			} catch (Exception e) {
				validHref = false;
			}
			if (validHref) {
				if (sHref.startsWith("http://") || sHref.startsWith("https://")) {
					outHref.write(sHref.getBytes());
					validHref = true;
				} else {
					validHref = false;
				}
			}

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
