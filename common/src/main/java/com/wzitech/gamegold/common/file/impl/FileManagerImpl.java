/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		FileManagerImpl
 *	包	名：		com.wzitech.gamegold.filemgmt.business.impl
 *	项目名称：	gamegold-filemgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午2:05:42
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.file.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.common.file.IFileManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author HeJian
 *
 */
@Component("fileManagerImpl")
public class FileManagerImpl extends AbstractBusinessObject implements IFileManager {
	/**
	 * 日志记录器
	 */
	private final static Logger logger = LoggerFactory.getLogger(FileManagerImpl.class);
	
	/**
	 * 商品图片存放根路径
	 */
//	@Value("${image.goods.rootdir}")
	private String imageGoodsRootDir="/srv/7Bao/bankimg";
	
	@Override
	public String saveAvatar(byte[] image, String bankName) {
		imageGoodsRootDir = FilenameUtils.normalize(imageGoodsRootDir);
		// 原始图片全路径
		String imageFullUrl = FilenameUtils.normalize(imageGoodsRootDir + System.getProperty("file.separator")
				  +System.currentTimeMillis()+ ".jpg");
		// 生成路径(包含原图片路径及缩略图路径)
		logger.debug("生成完整的文件名:{}", imageFullUrl);

		OutputStream outputStream = null;
		try {
			File imagePreFix = new File(FilenameUtils.getFullPathNoEndSeparator(imageFullUrl));
			FileUtils.forceMkdir(imagePreFix);// 生成目录如果文件目录不存在
			logger.info("存储的路径：{}",imagePreFix.getCanonicalPath());

			outputStream = new FileOutputStream(new File(imageFullUrl));
			outputStream.write(image);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.debug("文件{}没有找到{}",new Object[]{imageFullUrl,e1});
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("生成文件{}出错{}",new Object[]{imageFullUrl,e});
		}
		return imageFullUrl;
	}

}
